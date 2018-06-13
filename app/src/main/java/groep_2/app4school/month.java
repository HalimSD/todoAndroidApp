package groep_2.app4school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class month extends Fragment{

    List<todo> todolist;
    ListView listViewMonth;
    Calendar mCurrentDate;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todolist = new ArrayList<>();
        mCurrentDate = Calendar.getInstance();

        View view =  inflater.inflate(R.layout.month, container, false);
        listViewMonth =view.findViewById(R.id.listViewMonth);
        return view;
    }

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
    }

    public void onStart() {
long date = Calendar.getInstance().getTimeInMillis();
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("todos").orderByChild("todoDeadline").equalTo(Calendar.getInstance().get(Calendar.DAY_OF_MONTH + 3));
//        Log.v("test", Calendar.getInstance().get(mCurrentDate.get(Calendar.MONTH)));
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
                    listViewMonth.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
