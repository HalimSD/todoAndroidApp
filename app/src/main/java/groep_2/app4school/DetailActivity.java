package groep_2.app4school;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

import groep_2.app4school.model.todoItem;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    TextView todo_title, todo_description, todo_status, todo_deadline, todo_priority;
    ListView listViewTodo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        todo_title = findViewById(R.id.todo_title);
        todo_description = findViewById(R.id.todo_description);
        todo_deadline = findViewById(R.id.todo_deadline);
        todo_status = findViewById(R.id.todo_status);
        todo_priority = findViewById(R.id.todo_priority);

        listViewTodo = findViewById(R.id.listViewTodo);

        Intent intent = getIntent();

        String id = intent.getStringExtra(MainActivity.todo_id);
        String title = intent.getStringExtra(MainActivity.todo_title);
        todo_title.setText(title);
        String description = intent.getStringExtra(MainActivity.todo_description);
        todo_description.setText(description);
        String deadline = intent.getStringExtra(MainActivity.todo_deadline);
        todo_deadline.setText(deadline);
        String status = intent.getStringExtra(MainActivity.todo_status);
        todo_status.setText(status);
        String priority = intent.getStringExtra(MainActivity.todo_priority);
        todo_priority.setText(priority);

    }
}