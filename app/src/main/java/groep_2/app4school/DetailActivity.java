package groep_2.app4school;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

import groep_2.app4school.model.todoItem;
import groep_2.app4school.utils.SharedPrefManager;
import groep_2.app4school.utils.Util;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    TextView todo_title, todo_description, todo_status, todo_deadline, todo_priority;
    ListView listViewTodo;
    private String mEmail;
    SharedPrefManager sharedPrefManager;
    Context mContext = this;
    Button deleteDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        todo_title = findViewById(R.id.todo_title);
        todo_description = findViewById(R.id.todo_description);
        todo_deadline = findViewById(R.id.todo_deadline);
        todo_status = findViewById(R.id.todo_status);
        todo_priority = findViewById(R.id.todo_priority);
        FloatingActionButton deleteDetail = findViewById(R.id.deleteDetails);
        listViewTodo = findViewById(R.id.listViewTodo);

        Intent intent = getIntent();

        final String id = intent.getStringExtra(MainActivity.todo_id);
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
        sharedPrefManager = new SharedPrefManager(mContext);
        mEmail = sharedPrefManager.getUserEmail();


        deleteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(id);

            }
        });

    }
    private void deleteTodo(String id) {
        DatabaseReference dfDelete = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail)).child(id);
        dfDelete.removeValue();
        Toast.makeText(this, "The todo is deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}