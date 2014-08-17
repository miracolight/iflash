package com.example.todoapp.todoapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.todoapp.todoapplication.R;

public class EditItemActivity extends ActionBarActivity {

    public static final String  TODO_ITEM_KEY = "todo_item_key";
    public static final String  TODO_ITEM_TEXT = "todo_item_text";
    public static final int     REQUEST_CODE = 100;

    private EditText edEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        edEditItem = (EditText)findViewById(R.id.etEditItem);
        edEditItem.append(getIntent().getStringExtra(TODO_ITEM_TEXT));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
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

    public void saveTodoItem(View v) {
        // Prepare data intent
        Intent response = new Intent();
        // Pass relevant data back as a result
        response.putExtra(TODO_ITEM_KEY, getIntent().getIntExtra(TODO_ITEM_KEY, -1));
        response.putExtra(TODO_ITEM_TEXT, edEditItem.getText().toString());
        // Activity finished ok, return the data
        setResult(RESULT_OK, response); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}
