package tooearly.neumont.edu.sqltaskmanager.Activities;

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
    ArrayList<Task> listItems=new ArrayList<>();
    TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskService = new TaskService(this);

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

        taskService.create(new Task("One"));
        taskService.create(new Task("Two"));
        taskService.create(new Task("Three"));

        taskService.create(new Task("Fish"));
        taskService.create(new Task("Dog"));
        taskService.create(new Task("Chicken"));
    }

    private TaskService taskService;

    protected void addTaskClicked(View view) {
        //TODO: navigate to the create activity
    }
    protected void completeTaskClicked(View view) {
        //TODO: update the selected task to mark it as complete/incomplete, then refreshFilter()
    }
    protected void deleteTaskClicked(View view) {
        //TODO: delete the selected task from the database, then refreshFilter()
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
