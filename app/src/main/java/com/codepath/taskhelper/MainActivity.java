package com.codepath.taskhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialogFragment.OnTaskAddedListener {
    List<Task> tasks;
    TasksAdapter itemsAdapter;
    ListView lvItems;

    TasksDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = TasksDatabaseHelper.getInstance(getApplicationContext());
        tasks = dbHelper.getTasks();

        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new TasksAdapter(getApplicationContext(), tasks);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onTaskAdded(Task task, boolean editing, int pos) {
        if (editing) {
            tasks.set(pos, task);
            itemsAdapter.notifyDataSetChanged();
        } else {
            itemsAdapter.add(task);
        }

        dbHelper.updateTasks(tasks);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos,
                                                   long id) {
                        tasks.remove(pos);
                        itemsAdapter.notifyDataSetChanged();

                        dbHelper.updateTasks(tasks);

                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        showAddTaskFragment((Task) lvItems.getItemAtPosition(pos), pos);
                        itemsAdapter.notifyDataSetChanged();

                        dbHelper.updateTasks(tasks);
                    }
                }
        );
    }

    private void showAddTaskFragment(@Nullable Task task, int pos) {
        FragmentManager fm = getSupportFragmentManager();
        AddTaskDialogFragment addTaskDialogFragment = AddTaskDialogFragment.newInstance(task, pos);
        addTaskDialogFragment.show(fm, "fragment_add_task");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                showAddTaskFragment(null, -1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
