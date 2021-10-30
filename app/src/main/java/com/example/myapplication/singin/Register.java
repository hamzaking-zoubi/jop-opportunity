package com.example.myapplication.singin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Cv.ActivityCV;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText fullName, password, email;
    private Button btn;
    private TextView view;
    private FirebaseAuth fAuth;
    private ProgressBar bar;
    private FirebaseFirestore db;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String userID;
    String FullName;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullName = findViewById(R.id.re_ed_name);
        email = findViewById(R.id.login_ed_email);
        password = findViewById(R.id.login_ed_password);
        btn = findViewById(R.id.login_bt_regstier);
        view = findViewById(R.id.login_tv_Already);
        bar = findViewById(R.id.login_progressBar);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("ClientApplication").child("SaveUserAccount");

        if (fAuth.getCurrentUser() != null) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                FullName = fullName.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is Required.");
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Password Must be more than 6 Characters ");
                    return;
                }
                bar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("user").document(userID);
                            Map<String, Object> user = new HashMap<>();

                            user.put("fName", FullName);
                            user.put("email", Email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("new  user  create", userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("new  user no create", e.getMessage());

                                }
                            });
                            reference = database.getReference("ClientApplication").child("SaveUserAccount");

                            reference.push().setValue(new User(FullName,fAuth.getCurrentUser().getUid(),Password,Email)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("new  user  create", userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("new  user no create", e.getMessage());

                                }
                            });


                            startActivity(new Intent(Register.this, ActivityCV.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "Error! try again", Toast.LENGTH_LONG).show();
                            bar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });
    }

//    private void verify() {
//        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
////                userID = fAuth.getCurrentUser().getUid();
////                DocumentReference documentReference = db.collection("user").document(userID);
////                Map<String, Object> user = new HashMap<>();
////                user.put("fName", FullName);
////                user.put("email", Email);
////                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
////                    @Override
////                    public void onSuccess(Void aVoid) {
////                        Log.d("new  user  create", userID);
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Log.d("new  user no create", e.getMessage());
////
////                    }
////                });
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("onfailur Email not Send", e.getMessage());
//            }
//        });
//    }

}
