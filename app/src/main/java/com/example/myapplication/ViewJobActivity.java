package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Cv.ActivityCV;
import com.example.myapplication.Cv.Cv;
import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.JobsClass.Favorite;
import com.example.myapplication.JobsClass.MyOrder;
import com.example.myapplication.JobsClass.OrderJob;
import com.example.myapplication.JobsClass.ProfileCompanyActivity;
import com.example.myapplication.JobsClass.Report;
import com.example.myapplication.Notification.APIService;
import com.example.myapplication.Notification.Client;
import com.example.myapplication.Notification.Data;
import com.example.myapplication.Notification.MyResponse;
import com.example.myapplication.Notification.NotificationSender;
import com.example.myapplication.Notification.Token;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.MainActivity.JOBs_KEY;
import static com.example.myapplication.MainActivity.JOBs_KEY1;
import static com.example.myapplication.Request_view.JOBs_KEY2;

public class ViewJobActivity extends AppCompatActivity {
    private ImageView iv;
    private Favorite f;
    private TextView tv_title, tv_details, tv_time, tv_link, vtitleview;
    private DatabaseReference reference, reference1, referencereport, reference2, referenceCv, referenceOr, referenceMyOrder;
    private FirebaseDatabase database;
    private APIService apiService;
    public static final String CHAT_KEY = "chat";
    public static final String COMPANY_KEY = "company";
    private String cardId, companyId, userUid;
    private Button btn, btnChat, btnComp;
    private FirebaseAuth auth;
    private String pathCompany;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private Advertisement jobs;
    private Cv xx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewjob_activity);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        iv = findViewById(R.id.viewJob_tv_image);
        tv_time = findViewById(R.id.viewJob_tv_timeAccept);
        tv_title = findViewById(R.id.viewJob_tv_title);
        tv_link = findViewById(R.id.viewJob_tv_link);
        tv_details = findViewById(R.id.viewJob_tv_fullDetails);
        vtitleview = findViewById(R.id.viewJob_tv_videoname);
        btn = findViewById(R.id.viewJob_btn_sendCv);
        btnChat = findViewById(R.id.viewJob_btn_Chat);
        btnComp = findViewById(R.id.viewJob_btn_comp);


        simpleExoPlayerView = findViewById(R.id.viewjob_Vv);

        auth = FirebaseAuth.getInstance();

        Intent startingIntent = getIntent();
        cardId = startingIntent.getStringExtra(JOBs_KEY);
        String cardI = startingIntent.getStringExtra(JOBs_KEY2);
        if (cardI != null && cardId == null)
            cardId = cardI;
        companyId = startingIntent.getStringExtra(JOBs_KEY1);

        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userUid = user.getUid();

        reference2 = database.getReference("ClientApplication").child("Favorite").child(user.getUid());
        reference1 = database.getReference("ClientApplication").child("Favorite").child(user.getUid());
        referenceCv = database.getReference("CV-user").child(user.getUid());
        referenceMyOrder = database.getReference("ClientApplication").child("MyOrder").child(user.getUid());
        referenceOr = database.getReference("OrderJob").child(cardId);

        reference = database.getReference().child("Main");
        storage = FirebaseStorage.getInstance();

        findById();

        checkButton();

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                intent.putExtra(CHAT_KEY, cardId);
                startActivity(intent);

            }
        });
        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobs.getIdCompany() != null) {
                    Intent intent = new Intent(getBaseContext(), ProfileCompanyActivity.class);
                    intent.putExtra(COMPANY_KEY, jobs.getIdCompany());
                    startActivity(intent);
                }

            }
        });
        try {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    referenceCv.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                //    referenceOrder.push().setValue(new OrderJob(user.getUid(), xx, cardId, jobs.getIdCompany()));

                                xx = snapshot.getValue(Cv.class);
                                if (xx != null) {


                                    referenceOr.push().setValue(new OrderJob(user.getUid(),
                                            xx.getsFirstName(), xx.getsLastname(), xx.getsHomeAddress()
                                            , xx.getsEmailAddress(), xx.getsPhoneNumber(), xx.getSsCountry(), xx.getSsWorkCity()
                                            , xx.getSsCity(), xx.getSsNationality()
                                            , xx.getSsExperience(), xx.getSsEducationLevel(), xx.getSsMilitaryServes(),
                                            xx.getSsExperienceSalary(), xx.getSsCurrentJobState(), xx.getSsJobType(), xx.getDate(), xx.getGender()
                                            , xx.getImageUriCv()));
                                    notification();
                                    referenceMyOrder.push().setValue(new MyOrder(cardId));
                                    Toast.makeText(getBaseContext(), R.string.wait_rquest, Toast.LENGTH_LONG).show();
                                    btn.setVisibility(View.GONE);


                                }
                            } else {
                                AlertDialog.Builder logoutA = new AlertDialog.Builder(v.getContext());
                                logoutA.setMessage("Enter your Cv please then send order?");
                                logoutA.setTitle("Create Cv ");
                                logoutA.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getBaseContext(), ActivityCV.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                logoutA.create().show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Log.d("cancel", error.getDetails());
                        }
                    });


//
                }
            });
        } catch (Exception e) {

        }

    }

    private void fillCarToField(Advertisement c) {
        if (c.getImageURL() != null && !c.getImageURL().equals(""))
            Glide.with(getBaseContext()).
                    load(c.getImageURL()).

                    placeholder(R.drawable.ic_launcher_foreground).
                    into(iv);
        tv_title.setText(c.getTitle());
        tv_time.setText(formatLongToTimeStr(c.getTimeAccpt()));
        tv_details.setText(c.getFullDetail());
        tv_link.setText("لللراغبين في التقديم و التواصل يمكنكم الاستفسار وارسال السيرة الذاتية من خلال الرابط التالي" + c.getLnk());

        if (c.getVideoUrl() != null && !c.getVideoUrl().equals(""))
            prepareexoplayer(c.getNameVidio(), c.getVideoUrl());
        else
            simpleExoPlayerView.setVisibility(View.GONE);
        pathCompany = c.getIdCompany();


    }


    public void findById() {
        Query sQuery = reference.orderByChild("id").equalTo(cardId);
        sQuery.limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            jobs = snapshot.getValue(Advertisement.class);
                            fillCarToField(jobs);

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

    MenuItem cancelling;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);
        MenuItem main_favorite = menu.findItem(R.id.main_favorite);
        MenuItem cancelling = menu.findItem(R.id.main_cancelling);
        MenuItem report = menu.findItem(R.id.main_report);


        ifFavoriteSelected(main_favorite);
        ifOrderSelected(cancelling);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_favorite:
                reference1 = database.getReference("ClientApplication").child("Favorite").child(user.getUid());
                favoriteSelected(item);
                return true;
            case R.id.main_report:

                getReport();
                return true;
            case R.id.main_cancelling:
                referenceMyOrder = database.getReference("ClientApplication").child("MyOrder").child(user.getUid());
                referenceOr = database.getReference("OrderJob").child(cardId);
                deleteOrder();
                deleteMyorder();
                return true;
        }

        return false;
    }


    public void favoriteSelected(MenuItem item) {
//////////////////////////////////////////////////////////////////////////////////////
        Query sQuery = reference1.orderByChild("id").equalTo(cardId);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    if (jobs != null)
                        //   reference1.push().setValue(jobs);
                        reference1.push().setValue(new Favorite(jobs.getId()));
                    item.setIcon(R.drawable.ic_favorite_yes);
                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    for (DataSnapshot child : snapshot.getChildren()) {

                        child.getRef().removeValue();
                        Log.d("key", "onDataChange: Removed " + child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void ifFavoriteSelected(MenuItem item) {
        Query sQuery = reference2.orderByChild("id").equalTo(cardId);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    item.setIcon(R.drawable.ic_favorite);
                } else {
                    item.setIcon(R.drawable.ic_favorite_yes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ifOrderSelected(MenuItem item) {
        Query sQuery = referenceOr.orderByChild("userUid").equalTo(userUid);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    item.setVisible(false);
                } else {
                    item.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void prepareexoplayer(String videotitle, String videourl) {
        try {
            vtitleview.setText(videotitle);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);

            Uri videoURI = Uri.parse(videourl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);


        } catch (Exception ex) {
            Log.d("Explayer Creshed", ex.getMessage().toString());
        }
    }


    private void notification() {
        if (jobs.getIdCompany() != null)

            FirebaseDatabase.getInstance().getReference().child("Company").child("Token").child(jobs.getIdCompany()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Token usertoken = dataSnapshot.getValue(Token.class);
                        sendNotifications(usertoken.getToken(), getString(R.string.notif), xx.getsFirstName() + "  " + xx.getsLastname());
                        Log.d("keynmb", usertoken.getToken() + "onDataChange: Removed ");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message, cardId, userUid, Data.getNewApplicantNotification());
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ViewJobActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }


    private void checkButton() {

        Query sQuery = referenceOr.orderByChild("userUid").equalTo(user.getUid());
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    btn.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void checkOrder() {

        Query sQuery = referenceOr.orderByChild("userUid").equalTo(user.getUid());
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //  snapshot.getRef().removeValue();
                } else {
                    referenceOr.push().setValue(new OrderJob(user.getUid(),
                            xx.getsFirstName(), xx.getsLastname(), xx.getsHomeAddress()
                            , xx.getsEmailAddress(), xx.getsPhoneNumber(), xx.getSsCountry(), xx.getSsWorkCity()
                            , xx.getSsCity(), xx.getSsNationality()
                            , xx.getSsExperience(), xx.getSsEducationLevel(), xx.getSsMilitaryServes(),
                            xx.getSsExperienceSalary(), xx.getSsCurrentJobState(), xx.getSsJobType(), xx.getDate(), xx.getGender()
                            , xx.getImageUriCv()));
                    notification();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void deleteOrder() {

        Query sQuery = referenceOr.orderByChild("userUid").equalTo(user.getUid());
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {

                        child.getRef().removeValue();
                        Log.d("key", "onDataChange: Removed " + child.getKey());
                    }
                    btn.setVisibility(View.VISIBLE);
                } else {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void deleteMyorder() {

        Query sQuery = referenceMyOrder.orderByChild("id").equalTo(cardId);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot child : snapshot.getChildren()) {

                        child.getRef().removeValue();
                        Log.d("key", "onDataChange: Removed " + child.getKey());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public static String formatLongToTimeStr(Long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy", Locale.US);
        String format = sdf.format(l);

        return format;
    }


    private void getReport() {

        referencereport = database.getReference("ClientApplication").child("report");
        EditText restMail = new EditText(getBaseContext());
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(ViewJobActivity.this);
        passwordResetDialog.setMessage("Enter your report ");
        passwordResetDialog.setTitle("What is the complaint?");
        passwordResetDialog.setView(restMail);
        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String report = restMail.getText().toString();

                if (!report.equals("")) {

                    referencereport.push().setValue(new Report(userUid,  cardId,  companyId,  report));
//
//                    referencereport.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                int count = 0;
//                                for (DataSnapshot d : snapshot.getChildren()) {
//                                    Report report1 = snapshot.getValue(Report.class);
//                                    if (report1.getCardId()==cardId && report1.getUserUid()==userUid) {
//                                        count++;
//                                        Toast.makeText(ViewJobActivity.this, "Your are send notification , waiting for a reply", Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                }
//                                if (count==0)
//                                {
//                                    Toast.makeText(ViewJobActivity.this, "Your notification has arrived , waiting for a reply", Toast.LENGTH_LONG).show();
//
//                                    referencereport.push().setValue(new Report(userUid,  cardId,  companyId,  report));
//                                }
//                            }
//                            else {
//                                Toast.makeText(ViewJobActivity.this, "Your notification has arrived , waiting for a reply", Toast.LENGTH_LONG).show();
//
//                                referencereport.push().setValue(new Report(userUid,  cardId,  companyId,  report));
//
//                            }


//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
//
//                        }
//                    });

                }

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.create().show();
    }


}






