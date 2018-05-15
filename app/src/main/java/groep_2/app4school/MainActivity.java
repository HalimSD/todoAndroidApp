package groep_2.app4school;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import groep_2.app4school.database.DataSource;
import groep_2.app4school.model.todoItem;
import groep_2.app4school.sample.SampleDataProvider;

public class MainActivity extends AppCompatActivity {
    TextView tvOut;
    List<todoItem> todoItemList = SampleDataProvider.dataItemList;
    List<String> todoTitles = new ArrayList<>();
    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        todoItem item = new todoItem(null, "todo title1", "first todo", "high");
//        tvOut = (TextView) findViewById(R.id.out);
//        tvOut.setText("");

        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.insertDatabase(todoItemList);
        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();


//        Collections.sort(todoItemList, new Comparator<todoItem>() {
//            @Override
//            public int compare(todoItem o1, todoItem o2) {
//                return o1.getTodoTitle().compareTo(o2.getTodoTitle());
//            }
//        });
//        for (todoItem item : todoItemList){
//            tvOut.append(item.getTodoTitle() + "\n");
//            todoTitles.add(item.getTodoTitle());
//        }
//        Collections.sort(todoTitles);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, todoTitles);

        List<todoItem> ListFromDB = mDataSource.getAllItems();

        DataItemAdapter adapter = new DataItemAdapter(this, ListFromDB);

        RecyclerView recyclerView = (RecyclerView) findViewById(android.R.id.list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }
}
