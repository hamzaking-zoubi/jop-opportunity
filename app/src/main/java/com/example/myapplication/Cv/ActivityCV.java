package com.example.myapplication.Cv;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;


public class ActivityCV extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    private String[] country = {"Syria", "India", "USA", "China", "Japan", "Other"};
    private String[] city = {"Damascus", "Homes", "Dara", "Tartarus", "Aleppo", "Other"};
    private String[] nationality = {"Syrian", "Palestinian", "Jordanian", "Iranians", "Japan", "Other"};
    private String[] experience = {"There is no", "1 years", "2 years", "3 years", "4 years", "5 years", "6 years", "More than 10 years"};
    private String[] educationLevel = {"There is no", "secondary certificate", "College degree", "Institute Certificate", "Diploma", "Master degree", "Doctor's certificate"};
    private String[] militaryServes = {"Exempt", "Finished", " in Service", "Postponed"};
    ProgressBar bar;
    String date;
    private String[] ExperienceSalary = {"from 25000 to 50000", "from 50000 to 75000", "from 75000 to 100000", "mor than million"};
    private String[] CurrentJobState = {"Unemployed", "Working"};
    private String[] jobType = {"Full time", "Contract", "part time", "Free lancer", "Work from home"};

    private RadioButton radioSexButton, rfemale, rmale;
    private RadioGroup radioSexGroup;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnSend;
    private String sGender = "female";
    private EditText firstname = null, lastname = null, homeAddress, emailAddress = null, phoneNumber;
    private String sFirstName, sLastname, sHomeAddress, sEmailAddress, sPhoneNumber;
    private String ssCountry, ssWorkCity, ssCity, ssNationality, ssExperience, ssEducationLevel, ssMilitaryServes, ssExperienceSalary, ssCurrentJobState, ssJobType;
    private DatabaseReference reference, referenceCv;
    private FirebaseDatabase database;
    private Cv xx;
    int Id;
    private String cardId;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Spinner spin_city;
    private Spinner spin_nationality;
    private Spinner spin_experience;
    private Spinner spin_educationLevel;
    private Spinner spin_military;
    private Spinner spin_salaryArea;
    private Spinner spin_CurrentJobState;
    private Spinner spin_jobType;
    private Spinner spin_workCity;
    private ImageView iv;
    private Uri imageUri = null;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv);

        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        bar = findViewById(R.id.et_detail_progressCv);
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = database.getReference("CV-user").child(user.getUid());
        referenceCv = database.getReference("CV-user").child(user.getUid());
        cardId = reference.getKey();

        radioSexGroup = findViewById(R.id.radiogro);

        rmale = findViewById(R.id.rb_cv_male);
        rfemale = findViewById(R.id.rb_cv_female);

        iv = findViewById(R.id.detailes_ivv);
        firstname = findViewById(R.id.et_cv_name);
        lastname = findViewById(R.id.et_cv_Nickname);
        homeAddress = findViewById(R.id.et_cv_homeaddress);
        emailAddress = findViewById(R.id.et_cv_email);
        phoneNumber = findViewById(R.id.et_cv_phone);
        btnSend = findViewById(R.id.btnSend);

        referenceCv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    xx = snapshot.getValue(Cv.class);
                    if (xx != null) {
                        fillCvToField(xx);
                    }
                } else {


                    ///////////////////////////////////
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // }


        spin_city = (Spinner) findViewById(R.id.custom_cv_spenar_city);
        spin_nationality = (Spinner) findViewById(R.id.custom_cv_spenar_nationlity);
        spin_experience = (Spinner) findViewById(R.id.custom_cv_spenar_experience);
        spin_educationLevel = (Spinner) findViewById(R.id.custom_cv_spenar_educationLevel);
        spin_military = (Spinner) findViewById(R.id.custom_cv_spenar_militaryServes);
        spin_salaryArea = (Spinner) findViewById(R.id.custom_cv_spenar_salaryِِِArea);
        spin_CurrentJobState = (Spinner) findViewById(R.id.custom_cv_spenar_CurrentJobState);
        spin_jobType = (Spinner) findViewById(R.id.custom_cv_spenar_jobTybe);
        spin_workCity = (Spinner) findViewById(R.id.custom_cv_spenar_workCities);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        spin_city.setOnItemSelectedListener(this);
        spin_nationality.setOnItemSelectedListener(this);
        spin_educationLevel.setOnItemSelectedListener(this);
        spin_military.setOnItemSelectedListener(this);
        spin_experience.setOnItemSelectedListener(this);
        spin_salaryArea.setOnItemSelectedListener(this);
        spin_CurrentJobState.setOnItemSelectedListener(this);
        spin_jobType.setOnItemSelectedListener(this);
        spin_workCity.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, city);
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nationality);
        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, educationLevel);
        ArrayAdapter aa4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, militaryServes);
        ArrayAdapter aa5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, experience);
        ArrayAdapter aa6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ExperienceSalary);

        ArrayAdapter aa7 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CurrentJobState);
        ArrayAdapter aa8 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, jobType);
        ArrayAdapter aa9 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, city);


        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin_city.setAdapter(aa);
        spin_nationality.setAdapter(aa2);
        spin_educationLevel.setAdapter(aa3);
        spin_military.setAdapter(aa4);
        spin_experience.setAdapter(aa5);
        spin_salaryArea.setAdapter(aa6);
        spin_CurrentJobState.setAdapter(aa7);
        spin_jobType.setAdapter(aa8);
        spin_workCity.setAdapter(aa9);

        bar.setVisibility(View.GONE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromField();


                if ((sFirstName.equals("") || imageUri == null || imageUri.equals(" ") || sFirstName == null) || (sLastname.equals("") || sLastname == null) || (sEmailAddress.equals("") || sEmailAddress == null)) {
                    Toast.makeText(getBaseContext(), R.string.fill_fields, Toast.LENGTH_LONG).show();
                    if (sFirstName.equals("") || sFirstName == null) {
                        firstname.setHint(R.string.enter_namecv);
                        firstname.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    if (sLastname.equals("") || sLastname == null) {
                        lastname.setHint(R.string.enter_lastname);
                        lastname.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    if (sEmailAddress.equals("") || sEmailAddress == null) {
                        emailAddress.setHint(R.string.fenter_emailcv);
                        emailAddress.setHintTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    if (imageUri == null || imageUri.equals(" ")) {
                        Toast.makeText(getBaseContext(), "Select image ", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (imageUri != null) {
                        bar.setVisibility(View.VISIBLE);
                        uploadFile(imageUri);
                    }

                }
//                } else {
//
//                    Cv cv = new Cv(sFirstName, sLastname, sHomeAddress, sEmailAddress,
//                            sPhoneNumber, ssCountry, ssWorkCity, ssCity, ssNationality, ssExperience, ssEducationLevel,
//                            ssMilitaryServes, ssExperienceSalary, ssCurrentJobState, ssJobType, date, sGender);
//                    reference.setValue(cv);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                }


            }


        });

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


    }


    private void fillCvToField(Cv v) {

        if (v.getsFirstName() != null && !v.getsFirstName().equals(""))
            firstname.setText(v.getsFirstName());
        if (v.getsLastname() != null && !v.getsLastname().equals(""))
            lastname.setText(v.getsLastname());
        if (v.getsHomeAddress() != null && !v.getsHomeAddress().equals(""))
            homeAddress.setText(v.getsHomeAddress());
        if (v.getsEmailAddress() != null && !v.getsEmailAddress().equals(""))
            emailAddress.setText(v.getsEmailAddress());
        if (v.getsPhoneNumber() != null && !v.getsPhoneNumber().equals(""))
            phoneNumber.setText(v.getsPhoneNumber());
        if (v.getDate() != null && !v.getDate().equals(""))
            dateButton.setText(v.getDate());

        spin_city.setSelection(((ArrayAdapter<String>) spin_city.getAdapter()).getPosition(v.getSsCity()));
        spin_nationality.setSelection(((ArrayAdapter<String>) spin_nationality.getAdapter()).getPosition(v.getSsNationality()));
        spin_experience.setSelection(((ArrayAdapter<String>) spin_experience.getAdapter()).getPosition(v.getSsExperience()));
        spin_educationLevel.setSelection(((ArrayAdapter<String>) spin_educationLevel.getAdapter()).getPosition(v.getSsEducationLevel()));
        spin_military.setSelection(((ArrayAdapter<String>) spin_military.getAdapter()).getPosition(v.getSsMilitaryServes()));
        spin_salaryArea.setSelection(((ArrayAdapter<String>) spin_salaryArea.getAdapter()).getPosition(v.getSsExperienceSalary()));
        spin_CurrentJobState.setSelection(((ArrayAdapter<String>) spin_CurrentJobState.getAdapter()).getPosition(v.getSsCurrentJobState()));
        spin_jobType.setSelection(((ArrayAdapter<String>) spin_jobType.getAdapter()).getPosition(v.getSsJobType()));
        spin_workCity.setSelection(((ArrayAdapter<String>) spin_workCity.getAdapter()).getPosition(v.getSsWorkCity()));
        if (v.getGender().equals("") && v.getGender() != null)
            if (v.getGender().equals("male")) {

                radioSexGroup.check(rmale.getId());
            } else {

                radioSexGroup.check(rfemale.getId());
            }

        if (v.getImageUriCv() != null && !v.getImageUriCv().equals(""))
            Glide.with(getBaseContext()).
                    load(v.getImageUriCv()).
                    centerCrop().
                    placeholder(R.drawable.ic_launcher_foreground).
                    into(iv);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
        //  String item = parent.getItemAtPosition(position).toString();
        Spinner spin_city = (Spinner) parent;
        Spinner spin_nationality = (Spinner) parent;
        Spinner spin_experience = (Spinner) parent;
        Spinner spin_educationLevel = (Spinner) parent;
        Spinner spin_military = (Spinner) parent;
        Spinner spin_salaryArea = (Spinner) parent;
        Spinner spin_CurrentJobState = (Spinner) parent;
        Spinner spin_jobType = (Spinner) parent;
        Spinner spin_workCity = (Spinner) parent;


        if (spin_city.getId() == R.id.custom_cv_spenar_city) {
            ssCity = city[position];
        }
        if (spin_CurrentJobState.getId() == R.id.custom_cv_spenar_CurrentJobState) {
            ssCurrentJobState = CurrentJobState[position];
        }
        if (spin_jobType.getId() == R.id.custom_cv_spenar_jobTybe) {
            ssJobType = jobType[position];
        }
        if (spin_workCity.getId() == R.id.custom_cv_spenar_workCities) {
            ssWorkCity = city[position];
        }

        if (spin_nationality.getId() == R.id.custom_cv_spenar_nationlity) {
            ssNationality = nationality[position];
        }
        if (spin_educationLevel.getId() == R.id.custom_cv_spenar_educationLevel) {
            ssEducationLevel = educationLevel[position];
        }
        if (spin_experience.getId() == R.id.custom_cv_spenar_experience) {
            ssExperience = experience[position];
        }
        if (spin_military.getId() == R.id.custom_cv_spenar_militaryServes) {
            ssMilitaryServes = militaryServes[position];
        }
        if (spin_salaryArea.getId() == R.id.custom_cv_spenar_salaryِِِArea) {
            ssExperienceSalary = ExperienceSalary[position];
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


    private void getDataFromField() {
        sFirstName = firstname.getText().toString();
        sLastname = lastname.getText().toString();
        sEmailAddress = emailAddress.getText().toString();
        sHomeAddress = homeAddress.getText().toString();
        sPhoneNumber = phoneNumber.getText().toString();
        int selectedId = radioSexGroup.getCheckedRadioButtonId();


        radioSexButton = (RadioButton) findViewById(selectedId);

        sGender = radioSexButton.getText().toString();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            iv.setImageURI(imageUri);
            ///  uploadFile(imageUri);


        }

    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    private void uploadFile(Uri uri) {
        if (uri != null) {

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                            Cv cv = new Cv(sFirstName, uri.toString(), sLastname, sHomeAddress, sEmailAddress,
                                    sPhoneNumber, ssCountry, ssWorkCity, ssCity, ssNationality, ssExperience, ssEducationLevel,
                                    ssMilitaryServes, ssExperienceSalary, ssCurrentJobState, ssJobType, date, sGender);
                            reference.setValue(cv);


                            Toast.makeText(ActivityCV.this, "upload successful", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityCV.this, "onFailure", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    if (progress == 100.0) {
                        bar.setProgress((int) progress);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }
            });
        } else {
            Toast.makeText(getBaseContext(), "no image selected", Toast.LENGTH_LONG).show();

        }


    }


}