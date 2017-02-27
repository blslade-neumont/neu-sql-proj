package tooearly.neumont.edu.sqltaskmanager.Models;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tooearly.neumont.edu.sqltaskmanager.R;

/**
 * Created by Kyle Kacprzynski on 2/23/2017.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {
    public TaskListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TaskListAdapter(Context context, int resource, List<Task> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.task_fragment, null);
        }

        Task task = getItem(position);
        v.setTag(task);

        if (task != null) {
            TextView taskTitle = (TextView) v.findViewById(R.id.taskTitle);
            TextView taskTime = (TextView) v.findViewById(R.id.taskTime);
            Button colorTag = (Button) v.findViewById(R.id.colorTag);
            colorTag.setBackgroundColor(task.color);

            if (taskTitle != null) {
                if(task.completed) {
                    taskTitle.setPaintFlags(taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    taskTitle.setPaintFlags(taskTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                taskTitle.setText(task.name);
            }

            if (taskTime != null) {
                taskTime.setText(task.time_spent + "");
            }
        }

        return v;
    }

}
