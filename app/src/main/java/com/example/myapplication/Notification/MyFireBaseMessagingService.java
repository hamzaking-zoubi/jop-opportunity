package com.example.myapplication.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Cv.Cv;
import com.example.myapplication.JobsClass.Advertisement;
import com.example.myapplication.R;
import com.example.myapplication.Request_view;
import com.example.myapplication.ViewJobActivity;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEY;
import static com.example.myapplication.Fragment.Fragment_requests.REQUEST_KEYCOMPANY;
import static com.example.myapplication.MainActivity.JOBs_KEY;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "channel_1";
    static Context ctx;
    private String TAG = "FirebaseService";

    public static final String NOTIFICATION_CHANNEL_ID = "nh-demo-channel-id";
    public static final String ENAPLE_CHAT = "nh-demo-channel-fggid";
    public static final String NOTIFICATION_CHANNEL_NAME = "Notification Hubs Demo Channel";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Notification Hubs Demo Channel";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private DatabaseReference reference, referenceSaveNotification, referenceM, referenceCv;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        ////////////////////////////////////
        String title, message, carId, CompanyId;
        int typeNonotification;
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        carId = remoteMessage.getData().get("advId");
        CompanyId = remoteMessage.getData().get("CompanyId");
        typeNonotification = Integer.parseInt(remoteMessage.getData().get("notificationType"));


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("TokenNotifications").child(user.getUid());
        referenceSaveNotification = database.getReference("ClientApplication").child("referenceSaveNotification").child(user.getUid());
        referenceCv = database.getReference("CV-user").child(user.getUid());
        referenceM = database.getReference("Main");

        findById(title, message, carId, CompanyId, typeNonotification);



        following(title, message, carId, CompanyId, typeNonotification);

        accept(title, message, carId, CompanyId, typeNonotification);

    }

    PendingIntent pi;

    private void setNotification(String title, String message, String carId, String CompanyId, int type) {


        String CHANNEL_ID = "1234";

        // Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.mysound);
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //For API 26+ you need to put some additional code like below:
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "regrege", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription("sgrgregerg");
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)

                    .build();


            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        if (type == 1) {
            Intent intent = new Intent(this, ViewJobActivity.class);
            intent.putExtra(JOBs_KEY, carId);
            pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        }
        if (type == Data.getNewMessageAcceptNotification()) {
            Intent intent = new Intent(this, Request_view.class);
            intent.putExtra(REQUEST_KEY, carId);
            intent.putExtra(REQUEST_KEYCOMPANY, CompanyId);
            intent.putExtra( ENAPLE_CHAT ,"1");
            pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        }



        NotificationCompat.Builder status = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        status.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pi)
                .setVibrate(new long[]{0, 500, 1000})
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .addAction(R.drawable.ic_baseline_notifications_paused_24, "action", pi);


        //   .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getApplicationContext().getPackageName()+"/"+R.raw.apple_ring))
        //    .setContentIntent(pendingIntent)
        //     .setContent(views);
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
        mNotificationManager.notify(notificationId, status.build());

    }

    private void filters(String title, String message, String carId, String CompanyId, int typeNonotification, Advertisement jobs) {

        referenceCv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cv cv = snapshot.getValue(Cv.class);
                    if (typeNonotification == Data.getNewAdvNotification()) {
                        Log.d("filter1",jobs.getTitle()+"");

                        if (check(cv.getSsJobType(), jobs.getWorkTime()) && check(cv.getSsExperienceSalary(), jobs.getSalary()) &&
                                check(cv.getSsEducationLevel(), jobs.getEducationLevel()) && check(cv.getSsExperience(), jobs.getExperienceYears()))
                            Log.d("filter2",jobs.getTitle()+"");
                            setNotification(title, message, carId, CompanyId, typeNonotification);

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
    public void findById(String title, String message, String carId, String CompanyId, int typeNonotification) {
        Query sQuery = referenceM.orderByChild("id").equalTo(carId);
        sQuery.limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            Advertisement jobs = snapshot.getValue(Advertisement.class);
                            Log.d("filter",jobs.getTitle()+"");
                            filters(title, message, carId, CompanyId, typeNonotification,jobs);
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
    private void following(String title, String message, String carId, String CompanyId, int typeNonotification) {

        Query sQuery = reference.orderByChild("myToken").equalTo(CompanyId);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (typeNonotification == Data.getNewAdvNotification()) {
                        setNotification(title, message, carId, CompanyId, typeNonotification);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });//following


    }

    private void accept(String title, String message, String carId, String CompanyId, int typeNonotification) {


        if (typeNonotification == Data.getNewMessageAcceptNotification()) {
            setNotification(title, message, carId, CompanyId, typeNonotification);//2
    //    referenceSaveNotification.

         //   referenceSaveNotification.push().setValue(new Data(title, message, carId, CompanyId));




//
        }

    }
    boolean check(String names, String x) {



        if ( names.equals(x)) {

            return true;

        }
        return false;
    }





}
