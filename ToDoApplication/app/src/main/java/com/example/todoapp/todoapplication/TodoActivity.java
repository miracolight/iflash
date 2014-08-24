package com.example.todoapp.todoapplication;

import android.content.Intent;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ListView;


import com.example.todoapp.todoapplication.data.TodoEntry;


import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity implements EditItemDialogFragment.EditItemDialogListener {

    private FragmentManager         fm = getSupportFragmentManager();

    private ArrayList<TodoEntry>    items;
    private TodoAdapter             itemsAdapter;
    private ListView                lvItems;

    private EditText                edNewItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        items  = new ArrayList<TodoEntry>(TodoEntry.getAll());
        itemsAdapter = new TodoAdapter(this, R.layout.activity_todo_item, items);

        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        edNewItem = (EditText)findViewById(R.id.edNewItem);
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addTodoItem(View v) {
        edNewItem = (EditText)findViewById(R.id.edNewItem);
        TodoEntry item = new TodoEntry(edNewItem.getText().toString());
        item.save();
        items.add(item);
        edNewItem.setText("");
        itemsAdapter.notifyDataSetChanged();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                items.get(position).delete();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
                intent.putExtra(EditItemActivity.TODO_ITEM_KEY, position);
                intent.putExtra(EditItemActivity.TODO_ITEM_TEXT, items.get(position).description);
                startActivityForResult(intent, EditItemActivity.REQUEST_CODE);
                */
                EditItemDialogFragment editFragment = new EditItemDialogFragment();
                Bundle args = new Bundle();
                args.putParcelable(EditItemActivity.TODO_ITEM, items.get(position));
                editFragment.setArguments(args);
                // Show DialogFragment
                editFragment.show(fm, "Dialog Fragment");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent response) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == EditItemActivity.REQUEST_CODE) {
            int pos = response.getIntExtra(EditItemActivity.TODO_ITEM_KEY, -1);
            String todoText = response.getStringExtra(EditItemActivity.TODO_ITEM_TEXT);
            TodoEntry todoEntry = items.get(pos);
            todoEntry.description=todoText;
            todoEntry.save();
            itemsAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onFinishEditDialog() {
        itemsAdapter.notifyDataSetChanged();
    }
}
