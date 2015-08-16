package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private String mTodoText;
    private int mTodoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mTodoText = getIntent().getStringExtra(MainActivity.LIST_ITEM_TEXT_EXTRA);
        mTodoIndex = getIntent().getIntExtra(MainActivity.LIST_ITEM_INDEX_EXTRA, -1);

        EditText etTodoItem = (EditText) findViewById(R.id.todoEditField);
        etTodoItem.setText(mTodoText);
    }

    public void onSave(View v) {
        EditText etTodoItem = (EditText) findViewById(R.id.todoEditField);
        Intent data = new Intent();
        data.putExtra(MainActivity.LIST_ITEM_TEXT_EXTRA, etTodoItem.getText().toString());
        data.putExtra(MainActivity.LIST_ITEM_INDEX_EXTRA, mTodoIndex); // ints work too
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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
