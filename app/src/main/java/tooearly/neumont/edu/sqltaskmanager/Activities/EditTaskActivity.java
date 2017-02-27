package tooearly.neumont.edu.sqltaskmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

        Spinner colorSpinner = (Spinner)findViewById(R.id.colors_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        String[] choices = getResources().getStringArray(R.array.colors_array);
        colorSpinner.setSelection(getColorIndex(choices, task.color));
    }

    private int getColorIndex(String[] choices, int color) {
        for (int q = 0; q < choices.length; q++) {
            String choice = choices[q];
            if (choice.equalsIgnoreCase("White") && color == Color.WHITE) return q;
            else if (choice.equalsIgnoreCase("Blue") && color == Color.BLUE) return q;
            else if (choice.equalsIgnoreCase("Red") && color == Color.RED) return q;
            else if (choice.equalsIgnoreCase("Yellow") && color == Color.YELLOW) return q;
            else if (choice.equalsIgnoreCase("Green") && color == Color.GREEN) return q;
        }
        return 0;
    }
    private int getColor(String[] choices, int index) {
        String choice = choices[index];
        if (choice.equalsIgnoreCase("White")) return Color.WHITE;
        else if (choice.equalsIgnoreCase("Blue")) return Color.BLUE;
        else if (choice.equalsIgnoreCase("Red")) return Color.RED;
        else if (choice.equalsIgnoreCase("Yellow")) return Color.YELLOW;
        else if (choice.equalsIgnoreCase("Green")) return Color.GREEN;
        else return Color.WHITE;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leave Without Saving?")
                .setMessage("Do you want to save your changes?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        save();
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }}).show();
    }


    private TaskService taskService;

    public Task task;

    public void save() {
        EditText taskName = (EditText)findViewById(R.id.taskName);
        task.name = taskName.getText().toString();

        EditText taskDescription = (EditText)findViewById(R.id.taskDescription);
        task.description = taskDescription.getText().toString();

        Spinner spinner = (Spinner)findViewById(R.id.colors_spinner);
        String[] choices = getResources().getStringArray(R.array.colors_array);
        task.color = getColor(choices, spinner.getSelectedItemPosition());

        if (task.id == 0) taskService.create(task);
        else taskService.update(task);
        finish();
    }

    public void submitClicked(View view) {
        save();
    }
}
