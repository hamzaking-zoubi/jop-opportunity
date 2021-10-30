package com.example.myapplication.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;


public class Fragment_connect_whith_us extends Fragment {

    ImageView facebook, whatsapp, telegram;

    public Fragment_connect_whith_us() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_connect_whith_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        facebook = view.findViewById(R.id.profile_facebook);
        whatsapp = view.findViewById(R.id.profile_whatsapp);
        telegram = view.findViewById(R.id.profile_telegram);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWhatsApp();
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTelegram();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/profile.php?id=100004239645488"));
                startActivity(browserIntent);
            }
        });


    }

    void getTelegram() {

        try {
            Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
            telegramIntent.setData(Uri.parse("https://telegram.me/Hamza_zy_2023"));
            startActivity(telegramIntent);
        } catch (Exception e) {
            // show error message
        }
    }

    void getWhatsApp() {
        try {

            String url = "https://api.whatsapp.com/send?phone=+963945675744";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {

        }
    }

}