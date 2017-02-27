package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.Models.TaskListAdapter;
import tooearly.neumont.edu.sqltaskmanager.R;
import tooearly.neumont.edu.sqltaskmanager.Services.TaskService;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "tooearly.neumont.edu.sqltaskmanager.EXTRA_TASK_ID";

    ArrayList<Task> listItems=new ArrayList<>();
    TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskService = TaskService.getInstance(this);

        Spinner filter = (Spinner)findViewById(R.id.filter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.refreshFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MainActivity.this.refreshFilter();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);

        adapter=new TaskListAdapter(this, R.layout.task_fragment, listItems);
        listView.setAdapter(adapter);

        seedData();
    }

    private void seedData() {
        taskService.deleteAll();

        Task task = new Task("One");
        task.color = Color.RED;

        taskService.create(task);


        Task task2 = new Task("Two");
        task2.color = Color.BLUE;

        taskService.create(task2);
        taskService.create(new Task("Three"));

        taskService.create(new Task("Fish"));
        taskService.create(new Task("Dog"));
        taskService.create(new Task("Chicken"));
    }

    private TaskService taskService;

    public void addTaskClicked(View view) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, 0);
        startActivity(intent);
    }
    public void completeTaskClicked(View view) {
        Task task = (Task)((View)view.getParent()).getTag();
        task.completed = !task.completed;
        taskService.update(task);
        System.out.println(task.completed);
        refreshFilter();
    }
    public void deleteTaskClicked(View view) {
        Task task = (Task)((View)view.getParent()).getTag();
        taskService.delete(task.id);
        refreshFilter();
    }
    public void viewTaskClicked(View view) {
        Task task = (Task)view.getTag();
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra(EXTRA_TASK_ID, task.id);
        startActivity(intent);
    }

    protected void refreshFilter() {
        Spinner filter = (Spinner)findViewById(R.id.filter);
        String item = (String)filter.getSelectedItem();
        String filterStr = null;
        if (item == null || item.equals("All")) filterStr = null;
        else if (item.equals("Completed")) filterStr = "completed=1";
        else if (item.equals("To Do")) filterStr = "completed=0";
        Task[] tasks = taskService.find(filterStr);

        listItems.clear();
        Collections.addAll(listItems, tasks);
        adapter.notifyDataSetChanged();
    }
}
