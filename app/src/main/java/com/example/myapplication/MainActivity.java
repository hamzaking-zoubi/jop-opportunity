package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.myapplication.Cv.ActivityCV;
import com.example.myapplication.Cv.Cv;
import com.example.myapplication.Notification.APIService;
import com.example.myapplication.Notification.Client;
import com.example.myapplication.Notification.Token;
import com.example.myapplication.singin.Login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mFirebaseAuthLis;
    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;
    DatabaseReference reference;
    FirebaseDatabase database;
    String userId;
    FirebaseFirestore db;
    TextView name, mail;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    public static final int EDIT_CV_REQUSTCOD = 23;
    public static final String EDIT_CV_PUTEXTRA = "idcv";
    public static final int ADD_CAR_REQUSTCOD = 1;
    public static final String JOBs_KEY = "car key";
    public static final String JOBs_KEY1 = "car key123";
    private APIService apiService;
    TextView passs;
    TextView navUsername;
    ImageView smallIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        db = FirebaseFirestore.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
       // FloatingActionButton fab=findViewById(R.id.fab);

        View headerView = navigationView.getHeaderView(0);
        database = FirebaseDatabase.getInstance();
        navUsername = (TextView) headerView.findViewById(R.id.headar_name);
        passs = (TextView) headerView.findViewById(R.id.header_password);
        smallIm = (ImageView) headerView.findViewById(R.id.imageView_main);
        reference = database.getReference("CV-user").child(userId);
        fillDataNav();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                switch (menuItem.getItemId()) {
                    case R.id.nav_cv:
                        Intent intent = new Intent(getApplicationContext(), ActivityCV.class);
                        intent.putExtra(EDIT_CV_PUTEXTRA, 22);
                        startActivity(intent);
                        return true;
                    case R.id.nav_singout:
                        AlertDialog.Builder logoutA = new AlertDialog.Builder(MainActivity.this, R.style.InvitationDialog);
                        logoutA.setMessage("Are you sure to Log out of the account?");
                        logoutA.setTitle("Log out");
                        logoutA.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Logout();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        logoutA.create().show();
                        return true;


                }


//                if (id == R.id.nav_cv) {
//                    Intent intent = new Intent(getApplicationContext(), ActivityCV.class);
//                    intent.putExtra(EDIT_CV_PUTEXTRA, 22);
//                    startActivity(intent);
//                } else if (id == R.id.nav_singout) {
//                    AlertDialog.Builder logoutA = new AlertDialog.Builder(MainActivity.this, R.style.InvitationDialog);
//                    logoutA.setMessage("Are you sure to Log out of the account?");
//                    logoutA.setTitle("Log out");
//                    logoutA.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Logout();
//                        }
//                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    logoutA.create().show();
//                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem, navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //  MyFirebaseMessagingService x=new MyFirebaseMessagingService();
        UpdateToken();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(MainActivity.this, AddJobActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ///   MenuInflater inflater = getMenuInflater();
        //    inflater.inflate(R.menu.activity_main_drawer, menu);


        return false;
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("uiddd", firebaseUser.getUid());
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getBaseContext(), Login.class));


    }

    private void fillDataNav() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cv cv = snapshot.getValue(Cv.class);
                    if (cv.getImageUriCv() != null && !cv.getImageUriCv().equals(""))

                        Glide.with(getBaseContext()).
                                load(cv.getImageUriCv()).
                                placeholder(R.drawable.ic_launcher_foreground).
                                into(smallIm);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        DocumentReference documentReference = db.collection("user").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    if (value.exists()) {

                        passs.setText(value.getString("email"));
                        navUsername.setText(value.getString("fName"));
                    }
                } else {
                    passs.setText(getString(R.string.Job_opportunity_application));
                    navUsername.setText("*****************");
                }


            }
        });
    }
}