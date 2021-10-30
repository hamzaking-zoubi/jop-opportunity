package com.example.myapplication.singin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText password, email;
    Button btn;
    TextView view, viewForgot;
    FirebaseAuth fAuth;
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_ed_email);
        password = findViewById(R.id.login_ed_password);
        btn = findViewById(R.id.login_bt_regstier);
        bar = findViewById(R.id.login_progressBar);
        view = findViewById(R.id.login_tv_Already);
        fAuth = FirebaseAuth.getInstance();
        viewForgot = findViewById(R.id.login_fogot_pass);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is Required.");
                    return;
                }
                if (Password.length() < 6) {

                    password.setError("Password Must be>= 6 Characters ");
                    return;
                }
                bar.setVisibility(View.VISIBLE);

                ////
                fAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // verifiyEmailAddress();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            Toast.makeText(getApplicationContext(), "Logged in successfully ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error ! Try Again", Toast.LENGTH_LONG).show();
                            bar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }

        });
        viewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText restMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setMessage("Enter your Email To Received Reset Link");
                passwordResetDialog.setTitle("Rest Password");
                passwordResetDialog.setView(restMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = restMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Reset Link send To Your Email.", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error ! Reset Link is Not send" + e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //  Toast.makeText(getApplicationContext(), "OnBackPressed", Toast.LENGTH_LONG).show();

    }

    private void verifiyEmailAddress() {

        FirebaseUser user = fAuth.getCurrentUser();
        if (user.isEmailVerified()) {
        } else {
            Toast.makeText(getApplicationContext(), "Please verify your account..", Toast.LENGTH_LONG).show();


        }
    }


}