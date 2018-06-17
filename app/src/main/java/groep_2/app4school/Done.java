package groep_2.app4school;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
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

import groep_2.app4school.utils.SharedPrefManager;
import groep_2.app4school.utils.Util;

import static groep_2.app4school.MainActivity.todo_deadline;
import static groep_2.app4school.MainActivity.todo_description;
import static groep_2.app4school.MainActivity.todo_id;
import static groep_2.app4school.MainActivity.todo_status;
import static groep_2.app4school.MainActivity.todo_title;

public class Done extends Fragment{

    List<todo> todolist;
    ListView listViewDone;

    public static final String todo_title = "todo_title";
    public static final String todo_id = "todo_id";
    public static final String todo_description = "todo_description";
    public static final String todo_status = "todo_status";
    public static final String todo_deadline = "todo_deadline";
    public static final String todo_priority = "todo_priority";
    SharedPrefManager sharedPrefManager;
    private String  mEmail;
    public SwipeRefreshLayout swipeRefreshLayout;



    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todolist = new ArrayList<>();
        mEmail = new SharedPrefManager(getContext()).getUserEmail();

        View view =  inflater.inflate(R.layout.done, container, false);
        listViewDone =view.findViewById(R.id.listViewDone);
        swipeRefreshLayout = view.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Refresh();
            }
        });
        return view;
    }

    public void Refresh() {
        swipeRefreshLayout.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                todolist = new ArrayList<>();


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("todos").child(Util.encodeEmail(mEmail)).orderByChild("todoPriority").equalTo("Done");
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
                            listViewDone.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                listViewDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                listViewDone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0){
                            todo todoList = todolist.get(position);
                            updateDialog(todoList.getTodoID(), todoList.getTodoTitle());
                        }

                        return false;
                    }
                });
                getFragmentManager().beginTransaction().add(R.id.fragment_container, new Done()).commit();

            }
        }, 1000);
    }

    public void onStart() {

        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        Query query = reference.child("todos").child(Util.encodeEmail(mEmail)).orderByChild("todoStatus").equalTo("Done");
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
                    listViewDone.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        listViewDone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                alertDialog.dismiss();
            }
        });

    }
    private boolean updatebtnhandler(String id, String title, String description, String deadline, String priority, String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail)).child(id);
        todo todo = new todo(id, title, description, deadline, priority, status);
        databaseReference.setValue(todo);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new Done()).commit();
        return true;
    }

    private void deleteTodo(String id) {
        DatabaseReference dfDelete = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail)).child(id);
        dfDelete.removeValue();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new Done()).commit();
    }
}
