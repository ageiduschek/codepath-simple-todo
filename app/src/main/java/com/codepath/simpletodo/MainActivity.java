package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final int EDIT_REQUEST_CODE = 31415;

    public static final String LIST_ITEM_TEXT_EXTRA = "list_item_text";
    public static final String LIST_ITEM_INDEX_EXTRA = "list_item_index";


    private ArrayList<TodoItem> items;
    private ArrayAdapter<TodoItem> itemsAdapter;
    private ListView lvItems;
    private TodoItemDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = TodoItemDatabaseHelper.getInstance(this);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = db.getAllTodoItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListeners();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        TodoItem item = db.addTodoItem(itemText);
        itemsAdapter.add(item);
        etNewItem.setText("");
    }

    private void setupListViewListeners() {
        // Set click listener for edits
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
                launchEditorView(pos);
            }
        });

        // Set click listener for deletes
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id) {
                TodoItem todoItem = items.remove(pos);
                db.deleteTodoItem(todoItem);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String newText = data.getExtras().getString(LIST_ITEM_TEXT_EXTRA);
            int index = data.getExtras().getInt(LIST_ITEM_INDEX_EXTRA, -1);
            TodoItem updatedItem = db.editTodoItem(items.get(index), newText);
            items.set(index, updatedItem);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    public void launchEditorView(int itemIndex) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra(LIST_ITEM_TEXT_EXTRA, items.get(itemIndex).text);
        i.putExtra(LIST_ITEM_INDEX_EXTRA, itemIndex);
        startActivityForResult(i, EDIT_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
