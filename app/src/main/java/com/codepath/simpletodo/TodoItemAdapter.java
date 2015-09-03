package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A list item adapter for a TodoItem
 */
public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    // View lookup cache
    private static class TodoViewHolder {
        TextView todoTextView;
    }

    Context mContext;
    public TodoItemAdapter(Context context, ArrayList<TodoItem> users) {
        super(context, 0, users);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        TodoViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new TodoViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
            viewHolder.todoTextView = (TextView) convertView.findViewById(R.id.todoTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TodoViewHolder) convertView.getTag();
        }

        int textColor = position % 2 == 0 ? R.color.primary_text_disabled_material_light
                                          : R.color.primary_dark_material_light;

        // Populate the data into the template view using the data object
        viewHolder.todoTextView.setText(todoItem.text);
        viewHolder.todoTextView.setTextColor(mContext.getResources().getColor(textColor));
        // Return the completed view to render on screen
        return convertView;
    }
}