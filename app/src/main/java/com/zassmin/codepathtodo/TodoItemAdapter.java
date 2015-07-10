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
    public static class ViewHolder {
        TextView itemName;
    }

    public TodoItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);

        // Check if the existing view is being reused, otherwise, inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.tvItemName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the data to the template using the data object
        viewHolder.itemName.setText(todoItem.getItemName());
        // return the completed view to render on screen
        return convertView;
    }
}
