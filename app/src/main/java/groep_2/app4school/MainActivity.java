package groep_2.app4school;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import groep_2.app4school.database.DataSource;
import groep_2.app4school.model.todoItem;
import groep_2.app4school.sample.SampleDataProvider;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView tvOut;
    List<todoItem> todoItemList = SampleDataProvider.dataItemList;
    List<String> todoTitles = new ArrayList<>();
    DataSource mDataSource;

    @SuppressLint("ResourceType")
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

        List<todoItem> ListFromDB = mDataSource.getAllItems();

        DataItemAdapter adapter = new DataItemAdapter(this, ListFromDB);

        RecyclerView recyclerView = (RecyclerView) findViewById(android.R.id.list);
        recyclerView.setAdapter(adapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
//                    new Done()).commit();
//            navigationView.setCheckedItem(R.id.nav_done);
//        }
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_highPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                    new High_priority()).commit();
                break;
            case R.id.nav_noPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new No_priority()).commit();
                break;
            case R.id.nav_lowPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new Low_priority()).commit();
                break;
            case R.id.nav_3days:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new days()).commit();
                break;
            case R.id.nav_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new week()).commit();
                break;
            case R.id.nav_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new month()).commit();
                break;
            case R.id.nav_done:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new Done()).commit();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,
                        new Account()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


