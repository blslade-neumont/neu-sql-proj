package tooearly.neumont.edu.sqltaskmanager;


import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.Models.TaskListAdapter;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> listItems=new ArrayList<>();
    TaskListAdapter adapter;
    int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        adapter=new TaskListAdapter(this, R.layout.task_fragment, listItems);
        listView.setAdapter(adapter);
    }

    protected void addTask(View view) {
        Task t = new Task();
        t.name = "name: " + clickCounter;
        t.time_spent = 42;
        clickCounter++;

        listItems.add(t);
        adapter.notifyDataSetChanged();
    }

    protected void deleteTask(View view) {
//        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.taskList);
//        insertPoint.removeView(view);
    }
}
