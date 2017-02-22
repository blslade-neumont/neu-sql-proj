package tooearly.neumont.edu.sqltaskmanager;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    ArrayList<String> listItems=new ArrayList<String>();
//    ArrayAdapter<String> adapter;
    int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.task_fragment, null);

        TextView textView = (TextView) v.findViewById(R.id.taskTitle);
        textView.setText("your text");

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.taskList);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    protected void addTask(View view) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.task_fragment, null);

        TextView textView = (TextView) v.findViewById(R.id.taskTitle);
        textView.setText("your text " + clickCounter);

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.taskList);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
