package com.codepath.taskhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MainActivity displays all existing tasks and allows users to create new ones.
 *
 * @author Rowan-James Tran
 */
public class MainActivity extends AppCompatActivity implements TaskDialogFragment.OnTaskAddedListener {
    List<Task> tasks = new ArrayList<>();
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupListViewListener();
        setupFABListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        sortTasksBasedOnPreferences(tasks);
        itemsAdapter.notifyDataSetChanged();
    }

    public void onTaskAdded(Task task, boolean editing, int pos) {
        if (editing) {
            tasks.set(pos, task);
        } else {
            tasks.add(task);
        }

        sortTasksBasedOnPreferences(tasks);

        itemsAdapter.notifyDataSetChanged();
        dbHelper.updateTasks(tasks);
    }

    private void sortTasksBasedOnPreferences(List<Task> tasks) {
        Collections.sort(tasks, new TasksComparator());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        if (preferences.getString("pref_sortingOrder", "descending").equals("descending")) {
            Collections.reverse(tasks);
        }
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

    private void setupFABListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskFragment(null, -1);
            }
        });
    }

    private void showAddTaskFragment(@Nullable Task task, int pos) {
        FragmentManager fm = getSupportFragmentManager();
        TaskDialogFragment addTaskDialogFragment = TaskDialogFragment.newInstance(task, pos);
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
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
