package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Fragment_setting extends Fragment {

    public static final String MyPREFERENCES = "preferences";
    public static final String KEY_MODE = "isNightMode";
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase database;
    private FirebaseUser user;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Switch aSwitchDark;
    private TextView textView;
    private Button clear_btn;

    public Fragment_setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        aSwitchDark = view.findViewById(R.id.setting_swith_dark);
        clear_btn = view.findViewById(R.id.setting_btn_clear_favorite);
        clear_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                user = mAuth.getCurrentUser();
                reference = database.getReference("ClientApplication").child("Favorite").child(user.getUid());
                reference.removeValue();
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CheckNightModeActivity();
        aSwitchDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);

                    getActivity().recreate();

                } else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);

                    getActivity().recreate();

                }
            }
        });


    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_MODE, nightMode);
        editor.apply();


    }

    private void CheckNightModeActivity() {

        if (sharedPreferences.getBoolean(KEY_MODE, false)) {
            aSwitchDark.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            aSwitchDark.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }


    }
}