package com.codepath.taskhelper;

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

    public static AddTaskDialogFragment newInstance(@Nullable Task task, int pos) {
        // If editing pass Task to be edited and its position, otherwise pass null and any int

        AddTaskDialogFragment frag = new AddTaskDialogFragment();
        Bundle args = null;
        if (task != null) {
            args = new Bundle();
            args.putString("body", task.body);
            args.putInt("priority", task.priority);
            args.putLong("date", task.date);
            args.putInt("pos", pos);
        }

        frag.setArguments(args);
        return frag;
    }

    public void onAddItem(View v, boolean editing, int pos) {
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

        listener.onTaskAdded(newTask, editing, pos);
        dismiss();
    }

    private long getUnixTimeFromDatePicker(DatePicker dp) {
        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year = dp.getYear();

        Calendar calendar = Calendar.getInstance();

        // Allow user until end of day
        calendar.set(year, month, day, 23, 59, 59);

        long unix = calendar.getTimeInMillis() / 1000;

        return unix;
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

        Bundle args = getArguments();
        if (args != null) {
            getDialog().setTitle(R.string.fragment_edit_task);
            btnAddTask.setText(R.string.btn_edit_task);

            String body = args.getString("body");
            etBody.setText(body);

            int priority = args.getInt("priority");
            if (priority == 0) {
                radioGroupPriority.check(R.id.radio_low);
            } else if (priority == 1) {
                radioGroupPriority.check(R.id.radio_medium);
            } else {
                radioGroupPriority.check(R.id.radio_high);
            }

            long date = args.getLong("date") * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            dpDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            final int pos = args.getInt("pos");

            btnAddTask.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onAddItem(v, true, pos);
                }
            });
        } else {
            getDialog().setTitle(R.string.fragment_add_task);
            btnAddTask.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onAddItem(v, false, -1);
                }
            });
        }

        etBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public interface OnTaskAddedListener {
        public void onTaskAdded(Task task, boolean editing, int pos);
    }
}