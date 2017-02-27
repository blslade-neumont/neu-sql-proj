package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;


import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.Models.TaskListAdapter;
import tooearly.neumont.edu.sqltaskmanager.R;
import tooearly.neumont.edu.sqltaskmanager.Services.TaskService;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> listItems=new ArrayList<>();
    TaskListAdapter adapter;

    Button btnStart, btnStop;
    TextView timerText;
    Handler customerHandler = new Handler();
    long startTime=0L, timeInMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMilliseconds;
            int secs = (int)updateTime/1000;
            int mins=secs/60;
            secs%=60;
            int milliseconds=(int)(updateTime%1000);
            timerText.setText(""+mins+":"+String.format("%2d", secs) + ":" + String.format("%3d", milliseconds));
            customerHandler.postDelayed(this, 0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.startButton);
        btnStop = (Button)findViewById(R.id.stopButton);
        timerText = (TextView)findViewById(R.id.textViewTime);

        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startTime = SystemClock.uptimeMillis();
                customerHandler.postDelayed(updateTimerThread, 0);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                timeSwapBuff+=timeInMilliseconds;
                customerHandler.removeCallbacks(updateTimerThread);
            }
        });

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
