package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.R;
import tooearly.neumont.edu.sqltaskmanager.Services.TaskService;

public class TaskDetailsActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    TextView timerText;
    Handler customerHandler = new Handler();
    long startTime=0L, timeInMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMilliseconds;
            int secs = (int)(updateTime/1000);
            int mins=secs/60;
            secs%=60;
            int milliseconds=(int)(updateTime%1000);
            timerText.setText(""+mins+":"+String.format("%02d", secs));// + ":" + String.format("%03d", milliseconds));
            customerHandler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

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
                task.time_spent = (int)(updateTime / 1000);
                taskService.update(task);
            }
        });

        taskService = TaskService.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK_ID, 0);

        init(taskId);
    }

    private void init(int taskId) {
        if (taskId == 0) throw new IllegalArgumentException("No task with ID " + taskId);
        else task = taskService.findById(taskId);

        timeSwapBuff = task.time_spent * 1000;
        int secs = (int)(timeSwapBuff/1000);
        int mins=secs/60;
        secs%=60;
        int milliseconds=(int)(timeSwapBuff%1000);
        timerText.setText(""+mins+":"+String.format("%02d", secs));// + ":" + String.format("%03d", milliseconds));

        TextView taskName = (TextView) findViewById(R.id.taskName);
        taskName.setText(task.name);

        Button colorTag = (Button) findViewById(R.id.colorTag);
        colorTag.setBackgroundColor(task.color);

        if(task.completed) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskName.setPaintFlags(taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        TextView taskDescription = (TextView)findViewById(R.id.taskDescription);
        taskDescription.setText(task.description);
    }

    private TaskService taskService;

    private Task task;

    public void onToggleClicked(View view) {
        task.completed = !task.completed;
        taskService.update(task);

        TextView taskName = (TextView) ((View) view.getParent()).findViewById(R.id.taskName);
        if(task.completed) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskName.setPaintFlags(taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
    public void editTaskClicked(View view) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(MainActivity.EXTRA_TASK_ID, task.id);
        startActivity(intent);
    }
    public void onDeleteClicked(View view) {
        taskService.delete(task.id);
        finish();
    }
}
