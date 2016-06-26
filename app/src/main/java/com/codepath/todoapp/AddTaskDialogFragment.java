package com.codepath.todoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Calendar;

public class AddTaskDialogFragment extends DialogFragment {
    private EditText etBody;
    private Button btnAddTask;
    private RadioGroup radioGroupPriority;
    private DatePicker dpDate;

    OnTaskAddedListener listener;

    public AddTaskDialogFragment() {}

    public static AddTaskDialogFragment newInstance(String title) {
        AddTaskDialogFragment frag = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);

        frag.setArguments(args);
        return frag;
    }

    public void onAddItem(View v) {
        String taskBody = etBody.getText().toString();
        int taskPriorityID = radioGroupPriority.getCheckedRadioButtonId();

        Task newTask = new Task();
        newTask.body = taskBody;

        if (taskPriorityID == R.id.radio_low) {
            newTask.priority = 0;
        } else if (taskPriorityID == R.id.radio_medium) {
            newTask.priority = 1;
        } else {
            newTask.priority = 2;
        }

        newTask.date = getUnixTimeFromDatePicker(dpDate);

        listener.onTaskAdded(newTask);
        dismiss();
    }

    private long getUnixTimeFromDatePicker(DatePicker dp) {
        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year = dp.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Convert getTimeInMillis() to Unix time
        return calendar.getTimeInMillis() / 1000;
    }

    @Override
    public void onAttach(Context context) {
        Activity activity;

        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                listener = (OnTaskAddedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnTaskAddedListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etBody = (EditText) view.findViewById(R.id.et_body);
        btnAddTask = (Button) view.findViewById(R.id.btn_add_task);
        radioGroupPriority = (RadioGroup) view.findViewById(R.id.radiogroup_priority);
        dpDate = (DatePicker) view.findViewById(R.id.dp_date);

        String title = getArguments().getString("title", "Add Task");
        getDialog().setTitle(title);

        etBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onAddItem(v);
            }
        });
    }

    public interface OnTaskAddedListener {
        public void onTaskAdded(Task task);
    }
}