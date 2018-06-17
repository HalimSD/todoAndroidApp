package groep_2.app4school;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

import groep_2.app4school.model.User;
import groep_2.app4school.utils.Constant;
import groep_2.app4school.utils.SharedPrefManager;
import groep_2.app4school.utils.Util;

public class Account extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Main_Activity";
    public final static int RC_SIGN_IN = 9001;
    //    SignInButton button;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    SignInButton signInButton;
    private final Context mContext = this;
    private String name, email;
    private String photo;
    private Uri photoUri;
    private String idToken;
    private DrawerLayout drawer;
    NavigationView navView;
    public SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        mAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        signInButton = findViewById(R.id.googleBtn);
        signInButton.setOnClickListener(this);
        configureSignIN();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    createUserInFirebase();
                    startActivity(new Intent(Account.this, MainActivity.class));
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }

            }
        };


    }

    private void createUserInFirebase() {
        if (email != null )
        {
            final String Email = Util.encodeEmail(email.toLowerCase());
        final Firebase location = new Firebase(Constant.FIREBASE_URL_USERS).child(Email);

        location.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constant.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                    User newUser = new User(name, photo, Email, timestampJoined);
                    location.setValue(newUser);
                    Toast.makeText(Account.this, "Account created!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.d(TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                } else {
                    Toast.makeText(Account.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        }

    }
    public void initNavigationDrawer() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_account:
                       signOut();
                        break;
                }
                return false;
            }
        });
    }
        public void configureSignIN() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Account.this.getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(Account.this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Account.this, "Auth went wrong.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, save Token and a state then authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                idToken = account.getIdToken();

                name = account.getDisplayName();
                email = account.getEmail();
                photoUri = account.getPhotoUrl();
                photo = photoUri.toString();

                // Save Data to SharedPreference
                sharedPrefManager = new SharedPrefManager(mContext);
                sharedPrefManager.saveIsLoggedIn(mContext, true);

                sharedPrefManager.saveEmail(mContext, email);
                sharedPrefManager.saveName(mContext, name);
                sharedPrefManager.savePhoto(mContext, photo);

                sharedPrefManager.saveToken(mContext, idToken);
                //sharedPrefManager.saveIsLoggedIn(mContext, true);

                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(credential);
            } else {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Login Unsuccessful. ");
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private void firebaseAuthWithGoogle(AuthCredential credential) {
        showProgressDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(Account.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            createUserInFirebase();
                            Toast.makeText(Account.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Account.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

//    private void firebaseAuthWithGoogle (GoogleSignInAccount account){
//            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//            mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithCredential:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithCredential:failure", task.getException());
//                                Snackbar.make(findViewById(R.id.prefs_content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
////                            updateUI(null);
//                            }
//
//                            // ...
//                        }
//                    });
//        }
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
//        case R.id.nav_account:
//            signOut();
//            break;
    }

    drawer.closeDrawer(GravityCompat.START);
    return true;
}



    private void signOut(){
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
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        mAuth.addAuthStateListener(mAuthListener);
//        mGoogleApiClient.connect();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        mAuth.addAuthStateListener(mAuthListener);
////        updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        Util utils = new Util(this);
        int id = v.getId();

        if (id == R.id.googleBtn) {
            if (utils.isNetworkAvailable()) {
                signIn();
            } else {
                Toast.makeText(Account.this, "Oops! no internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
