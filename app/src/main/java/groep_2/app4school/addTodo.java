package groep_2.app4school;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addTodo extends Activity {

    Spinner todoPriority;
    EditText todoTitle, todoDescription, todoDeadline;
    CheckBox todoDone;
    Switch todoReminder;
    Button addTodoBtn;

    DatabaseReference databaseTodo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        databaseTodo = FirebaseDatabase.getInstance().getReference("todos");

        todoPriority = findViewById(R.id.priority);
        todoTitle = findViewById(R.id.title);
        todoDescription = findViewById(R.id.description);
        todoDeadline = findViewById(R.id.deadline);
        todoDone = findViewById(R.id.done);
        todoReminder = findViewById(R.id.reminder);
        addTodoBtn = findViewById(R.id.add);


        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddTodo();

            }
        });

    }

    private void setAddTodo(){
        String title = todoTitle.getText().toString().trim();
        String description = todoDescription.getText().toString().trim();
        String deadline = todoDeadline.getText().toString().trim();
        String priority = todoPriority.getSelectedItem().toString();

        if (!TextUtils.isEmpty(title)){
           String id = databaseTodo.push().getKey();
           todo todo = new todo (id, title, description, deadline, priority);
           databaseTodo.child(id).setValue(todo);

           Toast.makeText(this, "A todo has been add", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Enter a todo", Toast.LENGTH_SHORT).show();
        }
    }
}
