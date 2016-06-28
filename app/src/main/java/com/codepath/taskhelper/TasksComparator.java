package com.codepath.taskhelper;

import java.util.Comparator;

/**
 * TasksComparator compares two Tasks.
 *
 * The Tasks are first sorted by their priority, going from lowest priority to highest.
 * In the event that two Tasks have the same priority, they are sorted from latest to earliest
 * due date.
 *
 * @Author Rowan-James Tran
 */
public class TasksComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        int priority1 = task1.priority;
        int priority2 = task2.priority;

        if (priority1 > priority2) {
            return 1;
        } else if (priority1 < priority2) {
            return -1;
        } else {
            if (task1.date < task2.date) {
                return 1;
            } else if (task1.date > task2.date) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
