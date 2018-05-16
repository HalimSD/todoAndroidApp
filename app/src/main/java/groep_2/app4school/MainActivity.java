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








//package groep_2.app4school;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import groep_2.app4school.database.DataSource;
//import groep_2.app4school.model.todoItem;
//import groep_2.app4school.sample.SampleDataProvider;
//import groep_2.app4school.sample.SigninActivity;
//
//public class MainActivity extends AppCompatActivity {
//    TextView tvOut;
//    List<todoItem> todoItemList = SampleDataProvider.dataItemList;
//    List<String> todoTitles = new ArrayList<>();
//    DataSource mDataSource;
//    private DrawerLayout drawer;
//    private Toolbar toolbar;
//    DrawerLayout mDrawerLayout;
//    ListView mDrawerList;
//    String[] mCategories;
//    RecyclerView mRecyclerView;
//    DataItemAdapter mItemAdapter;
//    private static final int SIGNIN_REQUEST = 1001;
//    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
//    private static final String TAG = "MainActivity";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        Collections.sort(todoItemList, new Comparator<todoItem>() {
//            @Override
//            public int compare(todoItem o1, todoItem o2) {
//                return o1.getTodoTitle().compareTo(o2.getTodoTitle());
//            }
//        });
//
//        DataItemAdapter adapter = new DataItemAdapter(this, todoItemList);
//
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);
//
//
//        mRecyclerView = (RecyclerView) findViewById(android.R.id.title);
//        if (grid) {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        }
//
//
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mCategories = getResources().getStringArray(todoItem);
//        mDrawerList = (ListView) findViewById(R.id.tvItemName);
//        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mCategories));
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String category = mCategories[position];
//                Toast.makeText(MainActivity.this, "You chose " + category,
//                        Toast.LENGTH_SHORT).show();
//                mDrawerLayout.closeDrawer(mDrawerList);
//            }
//        });
//
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
////                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.addDrawerListener(toggle);
////        toggle.syncState();
//
////        todoItem item = new todoItem(null, "todo title1", "first todo", "high");
////        tvOut = (TextView) findViewById(R.id.out);
////        tvOut.setText("");
//
//        mDataSource = new DataSource(this);
//        mDataSource.open();
//        mDataSource.seedDatabase(todoItemList);
////        mDataSource.insertDatabase(todoItemList);
////        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();
//
//
////        for (todoItem item : todoItemList) {
////            tvOut.append(item.getTodoTitle() + "\n");
////            todoTitles.add(item.getTodoTitle());
////        }
////        Collections.sort(todoTitles);
////        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
////                this, android.R.layout.simple_list_item_1, todoTitles);
//
////        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
////        boolean grid = settings.getBoolean("pref_display_grid", false);
//
//
////        if (grid) {
////            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
////        };
////        dIsplayDataItems();
//
//        //        List<todoItem> ListFromDB = mDataSource.getAllItems();
////
////
//        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
//        dIsplayDataItems();
////        recyclerView.setAdapter(adapter);
////    }
//    }
//
//    //    @Override
////    public View onCreateView(LayoutInflater inflater,
////                             ViewGroup container,
////                             Bundle savedInstanceState) {
////        View mDrawerListView = inflater.inflate(
////                R.layout.fragment_navigation_drawer, container, false);
////        mDrawerListView.setFitsSystemWindows(true);
////        return mDrawerListView;
////    }
////
//    private void dIsplayDataItems() {
//
//        todoItemList = mDataSource.getAllItems();
//        mItemAdapter = new DataItemAdapter(this, todoItemList);
//        mRecyclerView.setAdapter(mItemAdapter);
//    }
//
////    @Override
////    public void onBackPressed() {
////        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
////        } else {
////            super.onBackPressed();
////        }
////    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mDataSource.close();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mDataSource.open();
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_signin:
//                Intent intent = new Intent(this, SigninActivity.class);
//                startActivityForResult(intent, SIGNIN_REQUEST);
//                return true;
//            case R.id.action_settings:
//                // Show the settings screen
//                Intent settingsIntent = new Intent(this, PrefsActivity.class);
//                startActivity(settingsIntent);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
//            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
//            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();
//
//            SharedPreferences.Editor editor =
//                    getSharedPreferences("CCC", MODE_PRIVATE).edit();
//            editor.putString(SigninActivity.EMAIL_KEY, email);
//            editor.apply();
//
//        }
//
//    }
//}
