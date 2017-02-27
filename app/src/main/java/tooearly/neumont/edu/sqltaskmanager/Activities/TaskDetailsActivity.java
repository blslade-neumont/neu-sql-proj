package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;
import tooearly.neumont.edu.sqltaskmanager.R;
import tooearly.neumont.edu.sqltaskmanager.Services.TaskService;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskService = TaskService.getInstance(this);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK_ID, 0);

        init(taskId);
    }

    private void init(int taskId) {
        if (taskId == 0) throw new IllegalArgumentException("No task with ID " + taskId);
        else task = taskService.findById(taskId);

        updateCompletedCheckbox();

        TextView taskName = (TextView) findViewById(R.id.taskName);
        taskName.setText(task.name);

        if(task.completed) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskName.setPaintFlags(taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        TextView taskDescription = (TextView)findViewById(R.id.taskDescription);
        taskDescription.setText(task.description);
    }
    private void updateCompletedCheckbox() {

    }

    private TaskService taskService;

    private Task task;

    public void onToggleClicked(View view) {
        task.completed = !task.completed;
        taskService.update(task);
        updateCompletedCheckbox();
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
