package com.example.myapplication.JobsClass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Notification.MyToken;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static com.example.myapplication.ViewJobActivity.COMPANY_KEY;

public class ProfileCompanyActivity extends AppCompatActivity {
    private TextView nameCompany, phoneCompany, PlaceCompany, typeJob, typeJob1, wib, email, wib1, email1;
    private ImageView instagram, twitter, facebook;
    private String companyId;
    private ImageView imageView;
    private DatabaseReference reference, referenceF;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button btn;
    private CompanyProfile p;
    private MyToken t;
    public static final String SHOW_ALL_KEY = "show all";

    private String facebooks, instagrams, twitters;

    public ProfileCompanyActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        nameCompany = findViewById(R.id.prof_companyname_tv);
        phoneCompany = findViewById(R.id.prof_companyphon_tv);
        typeJob = findViewById(R.id.prof_companyJop_tv);
        typeJob1 = findViewById(R.id.prof_about);
        PlaceCompany = findViewById(R.id.prof_companyplace_tv);
        email = findViewById(R.id.prof_companyemail_tv);
        wib = findViewById(R.id.prof_companywib_tv);
        btn = findViewById(R.id.folloing_btn_send);

        email1 = findViewById(R.id.prof_email);
        wib1 = findViewById(R.id.prof_wi);

        instagram = findViewById(R.id.profileCompany_instegram);
        twitter = findViewById(R.id.profileCompany_twittier);
        facebook = findViewById(R.id.profileCompany_facebook);
        imageView = findViewById(R.id.prof_companyLogo);

        Intent startingIntent = getIntent();
        companyId = startingIntent.getStringExtra(COMPANY_KEY);
        referenceF = database.getReference("ClientApplication").child("TokenNotifications").child(user.getUid());
        if (companyId != null && !companyId.equals("")) {
            reference = database.getReference("Company").child("CompanyProfile").child(companyId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        CompanyProfile companyProfile = snapshot.getValue(CompanyProfile.class);
                        fillProfile(companyProfile);

                    } else {


                        Toast.makeText(getBaseContext(), "no profile company", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query sQuery = referenceF.orderByChild("myToken").equalTo(companyId);
                sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            referenceF.push().setValue(new MyToken(companyId));
                            btn.setText(R.string.you_are_following_now);
                        } else {
                            Toast.makeText(getBaseContext(), "you are following already unfollow", Toast.LENGTH_LONG).show();
                            for (DataSnapshot child : snapshot.getChildren()) {

                                child.getRef().removeValue();
                                Log.d("key", "onDataChange: Removed " + child.getKey());
                            }
                            btn.setText(R.string.following);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebooks != null && !facebooks.equals("")) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebooks));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                    }


                }

            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twitters != null && !twitters.equals("")) {

                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitters));
                        startActivity(browserIntent);
                    } catch (Exception e) {

                    }


                }

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instagrams != null && !instagrams.equals("")) {

                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagrams));
                        startActivity(browserIntent);
                    } catch (Exception e) {

                    }


                }

            }
        });

        check();
    }

    private void fillProfile(CompanyProfile profile) {


        if (profile.getLogoImg() != null && !profile.getLogoImg().equals("")) {
            Glide.with(getBaseContext()).
                    load(profile.getLogoImg()).
                    placeholder(R.drawable.ic_launcher_foreground).
                    into(imageView);
        } else {

            imageView.setVisibility(View.GONE);
        }
        if (profile.getCompanyName() != null && !profile.getCompanyName().equals("")) {
            nameCompany.setText(profile.getCompanyName());

        } else {

            nameCompany.setVisibility(View.GONE);
        }
        if (profile.getCompanySite() != null && !profile.getCompanySite().equals("")) {
            PlaceCompany.setText(profile.getCompanySite());

        } else {

            PlaceCompany.setVisibility(View.GONE);
        }

        if (profile.getEmail() != null && !profile.getEmail().equals("")) {
            email.setText(profile.getEmail());

        } else {

            email.setVisibility(View.GONE);
            email1.setVisibility(View.GONE);
        }

        if (profile.getCompanyNumber() != null && !profile.getCompanyNumber().equals("")) {
            phoneCompany.setText(profile.getEmail());

        } else {

            phoneCompany.setVisibility(View.GONE);
        }

        if (profile.getElcSiteLink() != null && !profile.getElcSiteLink().equals("")) {
            wib.setText(profile.getElcSiteLink());

        } else {

            wib.setVisibility(View.GONE);
            wib1.setVisibility(View.GONE);
        }
        if (profile.getAboutCompany() != null && !profile.getAboutCompany().equals("")) {
            typeJob.setText(profile.getAboutCompany());

        } else {

            typeJob.setVisibility(View.GONE);
            typeJob1.setVisibility(View.GONE);

        }
        if (profile.getFacebookAccount() != null && !profile.getFacebookAccount().equals("")) {
            facebooks = profile.getFacebookAccount();


        } else {
            facebook.setVisibility(View.GONE);
        }


        if (profile.getTwitterAccount() != null && !profile.getTwitterAccount().equals("")) {
            twitters = profile.getTwitterAccount();


        } else {
            twitter.setVisibility(View.GONE);
        }
        if (profile.getInstagramAccount() != null && !profile.getInstagramAccount().equals("")) {
            instagrams = profile.getInstagramAccount();


        } else {
            instagram.setVisibility(View.GONE);
        }


    }

    private void check() {
        Query sQuery = referenceF.orderByChild("myToken").equalTo(companyId);
        sQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {

                } else {

                    btn.setText(R.string.you_are_following_now);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.showalljobs, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.SHOW_ALL:
                Intent intent = new Intent(getBaseContext(), ShowAllJobs.class);
                intent.putExtra(SHOW_ALL_KEY, companyId);
                startActivity(intent);

                return true;
        }
        return super.onContextItemSelected(item);
    }

}