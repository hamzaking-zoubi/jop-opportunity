package com.example.myapplication.JobsClass;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Notification.APIService;
import com.example.myapplication.Notification.Client;
import com.example.myapplication.Notification.Data;
import com.example.myapplication.Notification.MyResponse;
import com.example.myapplication.Notification.NotificationSender;
import com.example.myapplication.Notification.Token;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddJobActivity extends AppCompatActivity {
    private TextInputEditText et_titl, te_fewDetails, et_time, et_details, et_catgre, et_link;

    private ImageView iv;
    private Menu menu;
    private MenuItem save, Edit, delete, favorite;
private Advertisement advertisement;
    private Uri imageUrii = null, videouri = null;

    private FirebaseDatabase database;
    private DatabaseReference reference,referencetoken;
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RC_VIDEO_PICKER = 101;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    ProgressBar bar;
    private FirebaseAuth.AuthStateListener stateListener;
    String id;//id advertisement
    VideoView videoView;
    Button browse1, upload;
    MediaController mediaController;
    String title = "", fewdetails = "", image = null, time = "";
    String fullDetail = "", link = "", catagore = "";
    FirebaseUser user1;
    private String idCopmany;//uid company
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        videoView = (VideoView) findViewById(R.id.Add_job_video);
        upload = (Button) findViewById(R.id.add_job_upload_btn);
        browse1 = (Button) findViewById(R.id.add_job_browse_btn);
        te_fewDetails = findViewById(R.id.add_job_detail_fewDetails_et);
        et_titl = findViewById(R.id.add_job_title_et);
        et_details = findViewById(R.id.add_job_details_et);
        et_time = findViewById(R.id.add_job_timeAccpt_et);
        iv = findViewById(R.id.add_job_iv);
        et_link = findViewById(R.id.add_job_linke_et);
        et_catgre = findViewById(R.id.add_job_catagore_et);
        bar = findViewById(R.id.add_job_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //////////
        idCopmany = user.getUid();

        /////
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Main");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        id = reference.push().getKey();

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user1 = user;
                }

            }
        };
        // bar.setVisibility(View.GONE);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();
        browse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, RC_VIDEO_PICKER);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = et_titl.getText().toString();
                fewdetails = te_fewDetails.getText().toString();
                time = et_time.getText().toString();
                fullDetail = et_details.getText().toString();
                link = et_link.getText().toString();
                catagore = et_catgre.getText().toString();
             //   addJob();
                reference.child(id).setValue(new Advertisement(id, idCopmany, System.currentTimeMillis(), null
                        , title, fullDetail, 1, fewdetails, link, catagore));
                notification();

            }
        });
    }

    private void clearField() {
        et_titl.setText("");
        te_fewDetails.setText("");
        et_details.setText("");
        et_time.setText("");
        et_catgre.setText("");
        et_link.setText("");
    }

    private void fillJobToField(Advertisement c) {
        if (c.getImageURL() != null && !c.getImageURL().equals(""))
            iv.setImageURI(Uri.parse(c.getImageURL()));
        et_titl.setText(c.getTitle());
        te_fewDetails.setText(c.getFewDetail());
        et_time.setText(c.getTimeAccpt()+"");
        et_details.setText(c.getFullDetail());
        et_catgre.setText(c.getCategory());
    }

    private void disableField() {
        et_titl.setEnabled(false);
        te_fewDetails.setEnabled(false);
        et_details.setEnabled(false);
        et_time.setEnabled(false);
    }

    private void enableField() {
        et_titl.setEnabled(true);
        te_fewDetails.setEnabled(true);
        et_details.setEnabled(true);
        et_time.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_option, menu);
//        this.menu = menu;
//        MenuItem save = menu.findItem(R.id.save);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        save = menu.findItem(R.id.save);
//
//        switch (item.getItemId()) {
//            case R.id.save:


                // bar.setVisibility(View.VISIBLE);

                // reference.child(id).setValue(new Jobs(id, idCopmany, System.currentTimeMillis(), null
                ///       , title, fullDetail, time, fewdetails, link, catagore));

                //   addJob();

                // Toast.makeText(AddJobActivity.this, "uplod successfull", Toast.LENGTH_LONG).show();


//                return true;
//
//
//        }


        return false;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUrii = data.getData();
            iv.setImageURI(imageUrii);
            ///  uploadFile(imageUri);

        } else if (requestCode == RC_VIDEO_PICKER && resultCode == RESULT_OK) {
            videouri = data.getData();
            videoView.setVideoURI(videouri);


        }
    }


//    public void QueryEditInfo(String image, String title, String fullDetail, String time, String fewDetail, String lnk, String category) {
//        Query userQuery = reference.orderByChild("id").equalTo("");
//
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("lnk", lnk);
//                    map.put("fewDetail", fewDetail);
//                    map.put("time", time);
//                    map.put("fullDetail", fullDetail);
//                    map.put("image", image);
//                    map.put("title", title);
//                    map.put("category", category);
//
//                    ds.getRef().updateChildren(map);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("ttag", databaseError.getMessage()); //Don't ignore errors!
//            }
//        };
//        userQuery.addListenerForSingleValueEvent(valueEventListener);
//    }


    private void addJob() {

        getImageUri(new FirebaseCallback() {
            @Override
            public void onCallback(String imageUriSucss) {

                image = imageUriSucss;
                if (videouri != null)
                    processVideoUploading();
                else {
                    reference.child(id).setValue(new Advertisement(id, idCopmany, System.currentTimeMillis(), imageUriSucss
                            , title, fullDetail, 1, fewdetails, link, catagore));
                    Toast.makeText(AddJobActivity.this, "uplod successfull", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

//    private void uploadFile(Uri uri) {
//
//
//        if (uri != null) {
//
//            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    bar.setProgress(0);
//                                }
//                            }, 500);
//                            reference.child(id).setValue(new Jobs(id, idCopmany, System.currentTimeMillis(), uri.toString()
//                                    , title, fullDetail, time, fewdetails, link, catagore));
//                            Toast.makeText(AddJobActivity.this, "uplod successfull", Toast.LENGTH_LONG).show();
//
//
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(AddJobActivity.this, "onFailure", Toast.LENGTH_LONG).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                    if (progress == 100.0) {
//                        bar.setProgress((int) progress);
//                        finish();
//                    }
//
//
//                }
//            });
//        } else {
//            Toast.makeText(getBaseContext(), "no image selected", Toast.LENGTH_LONG).show();
//
//        }
//
//
//    }

    private void getImageUri(FirebaseCallback firebaseCallback) {


        if (imageUrii != null) {

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUrii));
            fileRef.putFile(imageUrii).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bar.setProgress(0);
                                }
                            }, 500);
//                                reference.child(id).setValue(new Jobs(id, idCopmany, System.currentTimeMillis(), uri.toString()
//                                        , title, fullDetail, time, fewdetails, link, catagore));
//                                Toast.makeText(AddJobActivity.this, "uplod successfull", Toast.LENGTH_LONG).show();

                            firebaseCallback.onCallback(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddJobActivity.this, "onFailure", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    if (progress == 100.0) {
                        bar.setProgress((int) progress);
                     //   finish();
                        bar.setVisibility(View.GONE);
                    }


                }
            });
        } else {
            Toast.makeText(getBaseContext(), "no image selected", Toast.LENGTH_LONG).show();

        }


    }

    private interface FirebaseCallback {
        void onCallback(String imageUriSucss);


    }

    private void processVideoUploading() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Media Uploader");
        pd.show();
        final StorageReference uploader = storageReference.child(System.currentTimeMillis() + "." + getExtension());
        uploader.putFile(videouri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                // reference.child(id).setValue(new Jobs(id, "hgyi", uri.toString(), System.currentTimeMillis(), null
                                //       , title, fullDetail, time, fewdetails, link, catagore))
                                reference.child(id).setValue(new Advertisement(id, idCopmany, System.currentTimeMillis(), image
                                        , title, fullDetail, 1, fewdetails, link, catagore, uri.toString()))

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), "Successfully uploaded", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float per = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :" + (int) per + "%");
                        if (per==100.0)
                        {
                            Toast.makeText(getApplicationContext(), " upload  sucses", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }
    private void notification() {


        Log.d("keynmb",  "onDataChange: Removed ");
                FirebaseDatabase.getInstance().getReference().child("Tokens").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot f:dataSnapshot.getChildren()){
                                Token usertoken = f.getValue(Token.class);
                                sendNotifications(usertoken.getToken(), getString(R.string.notif), "hamzaaaaaa");
                                Log.d("keynmb", usertoken.getToken() + "onDataChange: Removed ");
                            }

                        }
                        else
                        {
                            Log.d("keynmb",  "onDataChange: Removed ");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message,id,idCopmany,Data.getNewAdvNotification());
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(AddJobActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

    private String getExtension() {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }
}
