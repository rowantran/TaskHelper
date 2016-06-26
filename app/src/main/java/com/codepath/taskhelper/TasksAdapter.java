package com.codepath.taskhelper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {
    Context context;

    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        TextView tvBody = (TextView) convertView.findViewById(R.id.tv_body);
        tvBody.setText(task.body);

        ImageView ivPriority = (ImageView) convertView.findViewById(R.id.iv_priority);
        int drawableID;
        if (task.priority == 0) {
            drawableID = R.drawable.priority_low;
        } else if (task.priority == 1) {
            drawableID = R.drawable.priority_medium;
        } else {
            drawableID = R.drawable.priority_high;
        }
        ivPriority.setImageDrawable(ContextCompat.getDrawable(context, drawableID));

        return convertView;
    }
}
