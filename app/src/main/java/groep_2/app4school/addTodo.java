package groep_2.app4school;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addTodo extends Activity {

    Spinner todoPriority;
    Spinner todoStatus;
    EditText todoTitle, todoDescription, todoDeadline;
    CheckBox todoDone;
//    Switch todoReminder;
    Button addTodoBtn, todoReminder;
    ListView listViewTodo;
    List<todo> todolist;
    DatabaseReference databaseTodo;
//    FloatingActionButton dateBtn;
    Calendar mCurrentDate;
    int day, month, year;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        databaseTodo = FirebaseDatabase.getInstance().getReference("todos");

        todoPriority = findViewById(R.id.priority);
        todoStatus = findViewById(R.id.status);
        todoTitle = findViewById(R.id.title);
        todoDescription = findViewById(R.id.description);
        todoDeadline = findViewById(R.id.deadline);
        todoReminder = findViewById(R.id.reminder);
        addTodoBtn = findViewById(R.id.add);
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month= mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddTodo();

            }
        });

        todoDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String format = new SimpleDateFormat("dd MMM YYYY").format(Calendar.getInstance().getTime());
                        todoDeadline.setText(format);
                    }
                }, year,month,day);
                dateDialog.getDatePicker().setMinDate(new Date().getTime());
                dateDialog.show();
            }
        });

    }

    private void setAddTodo(){
        String title = todoTitle.getText().toString().trim();
        String description = todoDescription.getText().toString().trim();
        String deadline = todoDeadline.getText().toString().trim();
        String priority = todoPriority.getSelectedItem().toString();
        String status = todoStatus.getSelectedItem().toString();

        if (!TextUtils.isEmpty(title)){
           String id = databaseTodo.push().getKey();
           todo todo = new todo (id, title, description, deadline, priority, status);
           databaseTodo.child(id).setValue(todo);

           Toast.makeText(this, "A todo has been add", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Enter a todo", Toast.LENGTH_SHORT).show();
        }
    }
}
