package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.R;
import tooearly.neumont.edu.sqltaskmanager.Services.TaskService;

public class EditTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskService = TaskService.getInstance(this);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK_ID, 0);
        if (taskId == 0) task = new Task();
        else task = taskService.findById(taskId);

        init();
    }

    private void init() {

    }

    private TaskService taskService;

    public Task task;
}
