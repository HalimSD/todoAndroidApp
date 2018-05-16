package groep_2.app4school;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    private DrawerLayout drawer;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


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

