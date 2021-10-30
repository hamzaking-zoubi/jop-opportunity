package com.example.myapplication.Chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.singin.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static com.example.myapplication.Request_view.CHAT_REQUEST;
import static com.example.myapplication.ViewJobActivity.CHAT_KEY;

public class ChatActivity extends AppCompatActivity {
    RecyclerView rv;
    ProgressBar bar;
    Button button;
    EditText tv;
    ImageButton ivb;

    public static final String ANONYMOUS = "anonymous";
    public static final String TAG = "MainActivity";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;
    public static final int RC_SIGN_IN = 1;
    //if request.auth != null
    ArrayList<Message> messages;
    private Uri selectedImageUri;
    private String mUsername;
    private FirebaseDatabase database;
    private DatabaseReference reference, referenceCv;
    private ChildEventListener eventListener;
    private Message friendlyMessage;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private RecyclerAdapterChat rva;
    private FirebaseAuth auth;
    FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    String userId;
    String chatRequest = null;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent stringIntent = getIntent();
        String cardId = stringIntent.getStringExtra(CHAT_KEY);

        String chatRequest = stringIntent.getStringExtra(CHAT_REQUEST);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        user = auth.getCurrentUser();
        storageReference = firebaseStorage.getReference().child("chat_photos");
        referenceCv = database.getReference("ClientApplication").child("SaveUserAccount");
        findNameById();


        if (chatRequest != null) {
            if (user != null) {
//                String idCard=stringIntent.getStringExtra( CHAT_REQUEST_IDCARD);
                String xx = chatRequest + user.getUid();

                reference = database.getReference().child("message").child(xx);
                stateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {

                            onSignedInInitialize(mUsername);

                            //   Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                        } else {

                        }

                    }
                };
            }


        } else {
            reference = database.getReference().child("message").child(cardId);
            stateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {

                        onSignedInInitialize(mUsername);

                        //   Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                    } else {

                    }

                }
            };


        }


        rv = findViewById(R.id.main_Rv);
        ivb = findViewById(R.id.main_photo);
        button = findViewById(R.id.main_button);
        tv = findViewById(R.id.main_text);
        bar = findViewById(R.id.main_pro);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        messages = new ArrayList<>();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
                ((LinearLayoutManager) lm).setReverseLayout(true);
                ((LinearLayoutManager) lm).setStackFromEnd(true);



     ///Collections.reverse(messages);
        rva = new RecyclerAdapterChat(messages);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        rv.setAdapter(rva);

        bar.setVisibility(ProgressBar.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = tv.getText().toString();
                friendlyMessage = new Message(d, mUsername, null);
                reference.push().setValue(friendlyMessage);
                tv.setText("");
            }
        });
        ivb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //  intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0)
                    button.setEnabled(true);
                else
                    button.setEnabled(false);


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//
//        DocumentReference documentReference = db.collection("user").document(userId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot value, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException error) {
//                if (value != null) {
//                    if (value.exists()) {
//                        mUsername= (String) value.get(value.getString("fName"));
//
//
//                    }
//                } else {
//                    mUsername=ANONYMOUS;
//
//                }
//
//
//            }
//        });


        //////////

    }

    private void onSignedOutClear() {
        mUsername = ANONYMOUS;
        rva.clear();
        detachDatabaseListener();
        rv.clearAnimation();
        rv.clearOnScrollListeners();
        rv.clearFocus();
    }

    private void detachDatabaseListener() {
        if (eventListener != null) {
            reference.removeEventListener(eventListener);
            eventListener = null;

        }
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if (eventListener == null) {
            eventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    friendlyMessage = snapshot.getValue(Message.class);
                  messages.add(friendlyMessage);
                   // rva.setMessages();
                   // rva.addMessages(friendlyMessage);
                    rv.setAdapter(rva);

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
            };
            reference.addChildEventListener(eventListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(stateListener);
        detachDatabaseListener();
        rva.clear();
        rv.clearAnimation();
        rv.clearOnScrollListeners();
        rv.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (requestCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "sing in", Toast.LENGTH_LONG).show();
            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), "sing in canceled", Toast.LENGTH_LONG).show();
                finish();
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            System.out.println("1");
            Uri selected_photo = data.getData();
            final StorageReference photoRef = storageReference.child(selected_photo.getLastPathSegment());

            photoRef.putFile(selected_photo).addOnSuccessListener(
                    this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //     bar.setVisibility(ProgressBar.INVISIBLE);

                                    Message current_message = new Message(null, mUsername, uri.toString());
                                    reference.push().setValue(current_message);

                                }
                            });


                        }
                    }
            );

        }


    }


    public void findNameById() {
        Query sQuery = referenceCv.orderByChild("id").equalTo(user.getUid());
        sQuery.limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            mUsername = user.getName();
                            Log.d("nameeeee", user.getName() + "d");

                        } else {
                            mUsername = ANONYMOUS;
                            Log.d("nameeeee", "d2");
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


    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(stateListener);
    }
}