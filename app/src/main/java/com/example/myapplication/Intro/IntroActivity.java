package com.example.myapplication.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.singin.Register;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager pager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabLayout;
    private Button btnNext, getStart;
    Animation btnAnim;
    private int position;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (restorePrefData()) {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();

        }



        setContentView(R.layout.activity_intro);

        btnNext = findViewById(R.id.intro_btn_nxt);
        getStart = findViewById(R.id.intro_btn_getstart);
        tabLayout = findViewById(R.id.tab);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem(getString(R.string.Job_opportunity_application),getString(R.string.tab1)
                , R.drawable.hamza));
        mList.add(new ScreenItem(getString(R.string.looking_for_job), getString(R.string.tab2)
               , R.drawable.image1));
        mList.add(new ScreenItem(getString(R.string.Fast_forward), getString(R.string.tab3), R.drawable.imagesjob));
        mList.add(new ScreenItem(getString(R.string.Interact_with_us), getString(R.string.tab4), R.drawable.tab1));

        pager = findViewById(R.id.pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        pager.setAdapter(introViewPagerAdapter);

        tabLayout.setupWithViewPager(pager);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pager.getCurrentItem();
                if (position <= mList.size()) {
                    position++;
                    pager.setCurrentItem(position);
                }
                if (position == mList.size() - 1) {
                    /////////////////////////////
                    laoddLastScreen();

                }

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (position == mList.size() - 1) {
                    laoddLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                savePrfData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        boolean v = pref.getBoolean("isIntroOpnend", false);
        return v;
    }

    private void savePrfData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();

    }

    private void laoddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        getStart.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        getStart.setAnimation(btnAnim);

    }
}