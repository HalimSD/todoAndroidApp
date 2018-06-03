package groep_2.app4school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.SignInButton;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import groep_2.app4school.database.DataSource;
import groep_2.app4school.model.todoItem;
import groep_2.app4school.sample.SampleDataProvider;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private DrawerLayout drawer;
//    TextView tvOut;
    List<todoItem> todoItemList = SampleDataProvider.dataItemList;
//    List<String> todoTitles = new ArrayList<>();
    DataSource mDataSource;
    public Button mButton, addButton;
    public FirebaseAuth mAuth;
    public Button accbtn;
    ListView listViewTodo;
    DatabaseReference databaseTodo;
    List<todo> todolist;




    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_main);
        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.insertDatabase(todoItemList);
        listViewTodo = findViewById(R.id.listViewTodo);
        todolist = new ArrayList<>();

        databaseTodo = FirebaseDatabase.getInstance().getReference("todos");

        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();

//        List<todoItem> ListFromDB = mDataSource.getAllItems();

//        DataItemAdapter adapter = new DataItemAdapter(this, ListFromDB);

//        RecyclerView recyclerView = findViewById(android.R.id.list);
//        recyclerView.setAdapter(adapter);


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
    //            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
    //                    new Done()).commit();
    //            navigationView.setCheckedItem(R.id.fragment_container);
    //        }

        mButton = findViewById(R.id.callApi);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCallApiActivity();
            }
        });
        addButton = findViewById(R.id.gotoadd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });
    }

    public void openCallApiActivity() {
        Intent intent = new Intent(this, callApi.class);
        startActivity(intent);
        finish();
    }
    public void openAdd() {
        Intent intent = new Intent(this, addTodo.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            sendToLogin();
        }
// else {
//            Toast.makeText(this, "you are signed in", Toast.LENGTH_SHORT).show();
//        }

        databaseTodo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todolist.clear();
                for(DataSnapshot todoSnapshot : dataSnapshot.getChildren()){
                    todo todo = todoSnapshot.getValue(todo.class);
                    todolist.add(todo);
                }
                todoList adapter = new todoList(MainActivity.this, todolist);
                listViewTodo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(this , Account.class);
       startActivity(loginIntent);
        finish();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new High_priority()).addToBackStack(null).commit();
                break;
            case R.id.nav_noPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new No_priority()).addToBackStack(null).commit();
                break;
            case R.id.nav_lowPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Low_priority()).addToBackStack(null).commit();
                break;
            case R.id.nav_3days:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new days()).addToBackStack(null).commit();
                break;
            case R.id.nav_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new week()).addToBackStack(null).commit();
                break;
            case R.id.nav_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new month()).addToBackStack(null).commit();
                break;
            case R.id.nav_done:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Done()).addToBackStack(null).commit();
                break;
//            case R.id.nav_account:
//                Intent newActo = new Intent(this, Account.class);
//                startActivity(newActo);
//                finish();
//                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    static class ViewHolder {
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(android.R.id.list)
        RecyclerView list;
//        @BindView(R.id.googleBtn)
//        SignInButton googleBtn;
        @BindView(R.id.callApi)
        Button callApi;
        @BindView(R.id.nav_view)
        NavigationView navView;
        @BindView(R.id.drawer_layout)
        DrawerLayout drawerLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


