package com.example.todoapp.todoapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;


public class TodoActivity extends ActionBarActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private EditText edNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        items  = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,  	android.R.layout.simple_list_item_1, items);

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
        itemsAdapter.add(edNewItem.getText().toString());
        edNewItem.setText("");
        saveItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
                intent.putExtra(EditItemActivity.TODO_ITEM_KEY, position);
                intent.putExtra(EditItemActivity.TODO_ITEM_TEXT, items.get(position));
                startActivityForResult(intent, EditItemActivity.REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(Exception e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, items);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent response) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == EditItemActivity.REQUEST_CODE) {
            items.set(response.getIntExtra(EditItemActivity.TODO_ITEM_KEY, -1),
                      response.getStringExtra(EditItemActivity.TODO_ITEM_TEXT));
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }
}
