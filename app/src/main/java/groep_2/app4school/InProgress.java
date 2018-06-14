package groep_2.app4school;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static groep_2.app4school.MainActivity.todo_deadline;
import static groep_2.app4school.MainActivity.todo_description;
import static groep_2.app4school.MainActivity.todo_id;
import static groep_2.app4school.MainActivity.todo_status;
import static groep_2.app4school.MainActivity.todo_title;

public class InProgress extends Fragment{

    List<todo> todolist;
    ListView listViewInProgress;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todolist = new ArrayList<>();

        View view =  inflater.inflate(R.layout.in_progress, container, false);
        listViewInProgress =view.findViewById(R.id.listViewInProgress);
        return view;
    }

    public void onStart() {

        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("todos").orderByChild("todoStatus").equalTo("In Progress");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Log.v(issue.toString(), "data");
                        todo todo = issue.getValue(todo.class);
                        todolist.add(todo);
                    }
                    todoList adapter = new todoList(getActivity(), todolist);
                    listViewInProgress.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewInProgress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                todo todo = todolist.get(position);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(todo_id, todo.getTodoID());
                intent.putExtra(todo_title, todo.getTodoTitle());
                intent.putExtra(todo_description, todo.getTodoDescription());
                intent.putExtra(todo_deadline, todo.getTodoDeadline());
                intent.putExtra(todo_status, todo.getTodoStatus());
                startActivity(intent);
            }
        });
        listViewInProgress.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todo todoList = todolist.get(position);
                updateDialog(todoList.getTodoID(), todoList.getTodoTitle());
                return false;
            }
        });
    }
    private void updateDialog(final String todoID, String todoTitle) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        final TextView editTitle = dialogView.findViewById(R.id.editTitle);
        final EditText editDescription = dialogView.findViewById(R.id.editDescription);
        final EditText editDeadline = dialogView.findViewById(R.id.editDeadline);
        final Spinner editPriority = dialogView.findViewById(R.id.editPriority);
        final Spinner editStatus = dialogView.findViewById(R.id.editStatus);
        final Button updateBtn = dialogView.findViewById(R.id.updateBtn);
        final Button deleteBtn = dialogView.findViewById(R.id.deleteBtn);

        dialogBuilder.setTitle("Updating a todo " + todoTitle);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                String deadline = editDeadline.getText().toString().trim();
                String priority = editPriority.getSelectedItem().toString();
                String status = editStatus.getSelectedItem().toString();

                if (TextUtils.isEmpty(title)) {
                    editTitle.setError("Title required!");
                    return;
                }
                updatebtnhandler(todoID, title, description, deadline, priority,  status);
                alertDialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(todoID);
            }
        });

    }
    private boolean updatebtnhandler(String id, String title, String description, String deadline, String priority, String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos").child(id);
        todo todo = new todo(id, title, description, deadline, priority, status);
        databaseReference.setValue(todo);

        return true;
    }

    private void deleteTodo(String id) {
        DatabaseReference dfDelete = FirebaseDatabase.getInstance().getReference("todos").child(id);
        dfDelete.removeValue();

    }
}