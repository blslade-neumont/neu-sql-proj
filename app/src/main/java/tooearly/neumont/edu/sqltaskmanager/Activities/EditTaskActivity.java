package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        init(taskId);
    }

    private void init(int taskId) {
        if (taskId == 0) task = new Task();
        else {
            TextView title = (TextView)findViewById(R.id.lbl_title);
            title.setText(R.string.title_edit_task);
            task = taskService.findById(taskId);
        }

        EditText taskName = (EditText)findViewById(R.id.taskName);
        taskName.setText(task.name);

        EditText taskDescription = (EditText)findViewById(R.id.taskDescription);
        taskDescription.setText(task.description);
    }

    private TaskService taskService;

    public Task task;

    public void submitClicked(View view) {
        EditText taskName = (EditText)findViewById(R.id.taskName);
        task.name = taskName.getText().toString();

        EditText taskDescription = (EditText)findViewById(R.id.taskDescription);
        task.description = taskDescription.getText().toString();

        taskService.update(task);
        finish();
    }
}
