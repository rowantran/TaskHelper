package com.codepath.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        tvBody.setText(task.body);

        return convertView;
    }
}
