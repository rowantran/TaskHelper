package com.codepath.taskhelper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {
    static final int SECONDS_PER_DAY = 86400;
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

        long currentTime = new Date().getTime() / 1000;
        long timeDifference = task.date - currentTime;
        String dateMessage;
        if (timeDifference < 0) {
            dateMessage = "Past due date!";
        } else {
            if (timeDifference / SECONDS_PER_DAY <= 1) {
                dateMessage = "Due today!";
            } else {
                double daysDue = timeDifference / SECONDS_PER_DAY;
                dateMessage = "Due in " + String.valueOf((int) daysDue) + " days";
            }
        }

        TextView tvDue = (TextView) convertView.findViewById(R.id.tv_due);
        tvDue.setText(dateMessage);

        return convertView;
    }
}
