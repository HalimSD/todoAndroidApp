package groep_2.app4school;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import groep_2.app4school.database.DataSource;
import groep_2.app4school.utils.Constant;
import groep_2.app4school.utils.SharedPrefManager;
import groep_2.app4school.utils.Util;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private DrawerLayout drawer;
    //    TextView tvOut;
//    List<todoItem> todoItemList = SampleDataProvider.dataItemList;
    //    List<String> todoTitles = new ArrayList<>();
    DataSource mDataSource;
    public Button mButton;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public Button accbtn;
    DatabaseReference databaseTodo;
    ListView listViewTodo;
    List<todo> todolist;
    FloatingActionButton addButton;
    Button updateBtn;

    public static final String todo_title = "todo_title";
    public static final String todo_id = "todo_id";
    public static final String todo_description = "todo_description";
    public static final String todo_status = "todo_status";
    public static final String todo_deadline = "todo_deadline";
    public static final String todo_priority = "todo_priority";

    GoogleApiClient mGoogleApiClient;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    SharedPrefManager sharedPrefManager;
    Context mContext = this;
    private String mEmail, mName;
    private TextView userEmail, userName;
    private CircleImageView userIMG;
    //    private NavigationView navigationView;
    Calendar mCurrentDate;
    //    int day, month, year;
    private int todoYear, todoMonth, todoDay;
    private Button todoDatePiker;
    static final int DATE_DIALOG_ID = 0;
    private EditText editDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

//        View header = navigationView.getHeaderView(0);

        setContentView(R.layout.activity_main);
//        mDataSource = new DataSource(this);
//        mDataSource.open();
//        mDataSource.insertDatabase(todoItemList);
        listViewTodo = findViewById(R.id.listViewTodo);
        todolist = new ArrayList<>();
        Intent intent = getIntent();
        String idd = intent.getStringExtra(Constant.FIREBASE_LOCATION_USERS);
        sharedPrefManager = new SharedPrefManager(mContext);
        mEmail = sharedPrefManager.getUserEmail();
        mName = sharedPrefManager.getName();
        userName = findViewById(R.id.header_name);


//        if (userName == null){
//            mName = "";
//        } else{
//            userName.setText(mName);
//
//        }

//        userName.setText(mName);

//        if (Util.encodeEmail(mEmail) != null){
//
//        } else {
//            databaseTodo = FirebaseDatabase.getInstance().getReference("todos");
//
//        }
        if (mEmail == null) {
            Intent intentt = new Intent(this, Account.class);
            startActivity(intentt);

        } else {
            databaseTodo = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail));
            Query query = databaseTodo.orderByChild("todoPriority").equalTo("High");
        }

//        query.addListenerForSingleValueEvent(databaseTodo.valu);

        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();

//        List<todoItem> ListFromDB = mDataSource.getAllItems();

//        DataItemAdapter adapter = new DataItemAdapter(this, ListFromDB);

//        RecyclerView recyclerView = findViewById(android.R.id.list);
//        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.pref_header_notifications);
            String description = getString(R.string.pref_default_display_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_01", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View nameView = navigationView.getHeaderView(0);
        TextView useName = nameView.findViewById(R.id.header_name);
        useName.setText(mName);
        View emailView = navigationView.getHeaderView(0);
        TextView useEmail = emailView.findViewById(R.id.header_email);
        useEmail.setText(mEmail);
        View imageView = navigationView.getHeaderView(0);
        String uri = sharedPrefManager.getPhoto();
        ImageView profileIMG = imageView.findViewById(R.id.header_img);
        Picasso.with(mContext).load(uri).into(profileIMG);
        Picasso.with(mContext).load(uri).fit().centerCrop()
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon);
//                .into(userIMG);

//        Log.v(uri), "==========================================================================================================================================================================================");

//        Uri mPhotoUri = Uri.parse(uri);
//        Picasso.with(this)
//                .load(mPhotoUri)
//                .placeholder(android.R.drawable.sym_def_app_icon)
//                .error(android.R.drawable.sym_def_app_icon)
//                .into(userIMG);

//        userEmail = navigationView.findViewById(R.id.header_email);

//        userEmail.setText(mEmail);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(MainActivity.this.getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Auth went wrong.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

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


        listViewTodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                todo todo = todolist.get(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(todo_id, todo.getTodoID());
                intent.putExtra(todo_title, todo.getTodoTitle());
                intent.putExtra(todo_description, todo.getTodoDescription());
                intent.putExtra(todo_deadline, todo.getTodoDeadline());
                intent.putExtra(todo_status, todo.getTodoStatus());
                startActivity(intent);
            }
        });
        listViewTodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todo todoList = todolist.get(position);
                 updateDialog(todoList.getTodoID(), todoList.getTodoTitle());
                return false;
            }
        });
//        mButton = findViewById(R.id.callApi);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCallApiActivity();
//            }
//        });
        addButton = findViewById(R.id.gotoadd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });
    }

    private void updateDialog(final String todoID, String todoTitle) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
        final Button choose = dialogView.findViewById(R.id.todoDatePiker);



        final Calendar c = Calendar.getInstance();
        todoYear = c.get(Calendar.YEAR);
        todoMonth = c.get(Calendar.MONTH);
        todoDay = c.get(Calendar.DAY_OF_MONTH);
        editDeadline.setText(
                new StringBuilder()
                        .append(todoDay).append("-")
                        .append(todoMonth + 1).append("-")
                        .append(todoYear).append(" "));


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
                updatebtnhandler(todoID, title, description, deadline, priority, status);
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
        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        Date,
                        todoYear, todoMonth, todoDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener Date =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    todoYear = year;
                    todoMonth = monthOfYear;
                    todoDay = dayOfMonth;
                    updateDisplay();
                    Log.v(String.valueOf(Date), "==========================================================================================================================================================================================");

                }

                private void updateDisplay() {

                    editDeadline.setText(
                            new StringBuilder()
                                    .append(todoDay).append("-")
                                    .append(todoMonth + 1).append("-")
                                    .append(todoYear).append(" "));
                }

            };

    private boolean updatebtnhandler(String id, String title, String description, String deadline, String priority, String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail)).child(id);
        todo todo = new todo(id, title, description, deadline, priority, status);
        databaseReference.setValue(todo);
        Toast.makeText(this, "Todo updated", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void deleteTodo(String id) {
        DatabaseReference dfDelete = FirebaseDatabase.getInstance().getReference("todos").child(Util.encodeEmail(mEmail)).child(id);
        dfDelete.removeValue();
        Toast.makeText(this, "The todo is deleted!", Toast.LENGTH_SHORT).show();

    }

    public void openCallApiActivity() {
        Intent intent = new Intent(this, callApi.class);
        startActivity(intent);
        finish();
    }


    public void openAdd() {
        Intent intent = new Intent(this, addTodo.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
//            Toast.makeText(this, "you are signed in", Toast.LENGTH_SHORT).show();
//
            databaseTodo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    todolist.clear();
                    for (DataSnapshot todoSnapshot : dataSnapshot.getChildren()) {
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

    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(this, Account.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mDataSource.open();
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
                        new High_priority()).commit();
                break;
            case R.id.nav_noPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new No_priority()).commit();
                break;
            case R.id.nav_lowPriority:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Low_priority()).commit();
                break;
            case R.id.nav_googleAPI:
                openCallApiActivity();
                break;
//
            case R.id.nav_done:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Done()).commit();
                break;
            case R.id.nav_InProgress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InProgress()).commit();
                break;

            case R.id.nav_settings:
                Settings();
                break;

            case R.id.nav_home:
                home();
                break;
            case R.id.nav_account:
                accountt();
                break;
//            case R.id.nav_InProgress:
//                progress();
//                break;
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

    public void home() {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
    }

    public void accountt() {
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(getApplicationContext(), Account.class);
                        startActivity(intent);
                    }
                }
        );
//        Intent home = new Intent(this, Account.class);
//        startActivity(home);
//        finish();
    }
//    public void progress() {
//        Intent progress = new Intent(this, InProgress.class);
//        startActivity(progress);
//        finish();
//    }

    public void Settings() {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

    public void sendNotification(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Todo reminder!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel_01");

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.high);
        mBuilder.setContentTitle("Task has to be done");
        mBuilder.setContentText("Example of a high priority task");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(001, mBuilder.build());

    }

    static class ViewHolder {
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(android.R.id.list)
        RecyclerView list;
        //        @BindView(R.id.googleBtn)
//        SignInButton googleBtn;
//        @BindView(R.id.callApi)
//        Button callApi;
        @BindView(R.id.nav_view)
        NavigationView navView;
        @BindView(R.id.drawer_layout)
        DrawerLayout drawerLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


