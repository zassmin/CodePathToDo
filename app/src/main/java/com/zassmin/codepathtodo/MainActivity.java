package com.zassmin.codepathtodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.zassmin.codepathtodo.data.TodoItem;
import com.zassmin.codepathtodo.data.TodoItemDbHelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity { // how come blank activity doesn't extend `Activity`?

    private final int REQUEST_CODE = 20;

    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    TodoItemDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load db and data
        db = new TodoItemDbHelper(this);
        readItems();

        // set custom adaptor
        itemsAdapter = new TodoItemAdapter(this, items);

        // set adapter
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        // call listeners
        setupListViewListener();
        setupListViewItemListener(); // does order matter?
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        TodoItem oldItem = items.remove(position);
                        db.deleteTodoItem(oldItem); // FIXME: figure out return here
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
    }

    private void setupListViewItemListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("item", items.get(position).getItemName());
                        i.putExtra("position", position);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    private void readItems() {
        items = new ArrayList<TodoItem>(db.getAllTodoItems());
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) (findViewById(R.id.etNewItem));
        String itemText = etNewItem.getText().toString();
        int position = itemsAdapter.getCount();
        TodoItem item = new TodoItem(itemText, position);
        db.addTodoItem(item);
        itemsAdapter.add(item);
        etNewItem.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemText = data.getStringExtra("item");
            int position = data.getIntExtra("position", 0);
            TodoItem item = items.remove(position);
            item.setPriority(position);
            item.setItemName(itemText);
            db.updateTodoItem(item);
            items.add(position, item); // TodoItem now updated
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
