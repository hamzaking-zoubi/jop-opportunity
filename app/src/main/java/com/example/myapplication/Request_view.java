package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Cv.Cv;
import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.JobsClass.CompanyProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEY;
import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEYCOMPANY;

public class Request_view extends AppCompatActivity {

    Button buttonDelete, buttonChat;
    private String Id, uid;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ImageView company, cvimg;
    Advertisement s;
    String keyval = "";
    String chatKey = "0";
    public static final String CHAT_REQUEST = "car key1";
    public static final String CHAT_REQUEST_IDCARD = "car hfdkey1";
    public static final String JOBs_KEY2 = "car key134561";

    private FirebaseAuth.AuthStateListener stateListener;
    private String companyId;
    DatabaseReference referenceM, referenceCv, referenceProfile, referenceMyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_view);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        company = findViewById(R.id.img_companyg);
        cvimg = findViewById(R.id.img_client123);

        buttonDelete = findViewById(R.id.delet_request);
        buttonChat = findViewById(R.id.Chat);
        referenceM = database.getReference("Main");
        //   buttonChat.setEnabled(false);

        referenceMyOrder = database.getReference("MyOrder").child(user.getUid());
        referenceCv = database.getReference("CV-user").child(user.getUid());
        Intent startingIntent = getIntent();
        Id = startingIntent.getStringExtra(REQUEST_KEY);
        companyId = startingIntent.getStringExtra(REQUEST_KEYCOMPANY);
        chatKey = startingIntent.getStringExtra(REQUEST_KEYCOMPANY);
        //findByIdMain();
//        if (chatKey.equals("1"))
//            buttonChat.setEnabled(true);
//        else
//            buttonChat.setEnabled(false);
//

        Log.d("companyIdreq", companyId);
        Log.d("companyIdreq1", Id);


        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                intent.putExtra(CHAT_REQUEST, companyId);
                intent.putExtra(CHAT_REQUEST_IDCARD, Id);
                startActivity(intent);
            }
        });

        findByIdCv();
        referenceProfile = database.getReference("Company").child("CompanyProfile").child(companyId);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Request_view.this, ViewJobActivity.class);
                intent.putExtra(JOBs_KEY2, Id);
                startActivity(intent);
            }
        });
        findByIdCompanyProfile();

    }


    private void findByIdMain() {

        if (Id != null) {
            Query sQuery = referenceM.orderByChild("id").equalTo(Id);
            sQuery.limitToFirst(1)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.exists()) {
                                Advertisement jobs = snapshot.getValue(Advertisement.class);
                                companyId = jobs.getIdCompany();
                            } else {
                                //////////////////////////////////////////
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }

    }

    private void findByIdCv() {


        referenceCv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(getBaseContext()).
                            load(snapshot.getValue(Cv.class).getImageUriCv()).
//                    centerCrop().
        placeholder(R.drawable.ic_launcher_foreground).
                            into(cvimg);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void findByIdCompanyProfile() {


        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Glide.with(getBaseContext()).
                            load(snapshot.getValue(CompanyProfile.class).getLogoImg()).

                            placeholder(R.drawable.ic_launcher_foreground).
                            into(company);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void Check() {


    }


}