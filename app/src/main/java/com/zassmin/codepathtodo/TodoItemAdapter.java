package com.zassmin.codepathtodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zassmin.codepathtodo.data.TodoItem;

import java.util.ArrayList;

/**
 * Created by zassmin on 7/6/15.
 */
public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    public TodoItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        tvItemName.setText(todoItem.getItemName());

        return convertView;
    }
}
