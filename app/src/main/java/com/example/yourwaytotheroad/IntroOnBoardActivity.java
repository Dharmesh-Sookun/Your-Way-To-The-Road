package com.example.yourwaytotheroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroOnBoardActivity extends AppCompatActivity {

    private ViewPager screenPager;
    OnboardScreenViewPagerAdapter onboardScreenViewPagerAdapter;
    TabLayout tabIndicators;
    Button buttonNext;
    int position = 0;
    Button buttonGetStarted;
    Button buttonPrevious;
    Button buttonSkip;
    Animation getStartedBtnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_intro_on_board);

        screenPager = findViewById(R.id.screenPager);
        tabIndicators = findViewById(R.id.tabIndicator);
        buttonNext = findViewById(R.id.nextBtn);
        buttonGetStarted = findViewById(R.id.getStartedBtn);
        getStartedBtnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.get_started_button_anim);
        buttonPrevious = findViewById(R.id.prevBtn);
        buttonSkip = findViewById(R.id.skipBtn);

        final List<OnboardScreenItem> itemsList = new ArrayList<>();

        itemsList.add(new OnboardScreenItem("Learn Road Signs", "Learn mauritian highway signs and rules before your oral test.", R.drawable.intro_screen_first_image_circle_small));
        itemsList.add(new OnboardScreenItem("Practise Questions", "Practise different multiple choice questions before your oral test.", R.drawable.intro_screen_second_image_small));
        itemsList.add(new OnboardScreenItem("Book your oral test", "Use the app to book your oral test now", R.drawable.intro_screen_third_image_circle_small));

        onboardScreenViewPagerAdapter = new OnboardScreenViewPagerAdapter(this, itemsList);
        screenPager.setAdapter(onboardScreenViewPagerAdapter);

        tabIndicators.setupWithViewPager(screenPager);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if(position < itemsList.size())
                {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position == itemsList.size() - 1)
                {
                    showLastScreen();
                }

                else
                {
                    showPreviousScreens();
                }


            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if(position > 0)
                {
                    position--;
                    screenPager.setCurrentItem(position);
                }

                if(position == 0)
                {
                    screenPager.setCurrentItem(position);
                }
            }
        });

        tabIndicators.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getPosition() == itemsList.size() - 1)
                {
                    showLastScreen();
                }

                else
                {
                    showPreviousScreens();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoMainActivity = new Intent(IntroOnBoardActivity.this, MainActivity.class);
                finish();
                startActivity(gotoMainActivity);

            }
        });

        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoMainActivity = new Intent(IntroOnBoardActivity.this, MainActivity.class);
                finish();
                startActivity(gotoMainActivity);

            }
        });
    }

    private void showLastScreen()
    {
        buttonNext.setVisibility(View.INVISIBLE);
        tabIndicators.setVisibility(View.INVISIBLE);
        buttonPrevious.setVisibility(View.INVISIBLE);
        buttonGetStarted.setVisibility(View.VISIBLE);
        buttonGetStarted.setAnimation(getStartedBtnAnim);
    }

    private void showPreviousScreens()
    {
        buttonGetStarted.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        tabIndicators.setVisibility(View.VISIBLE);
        buttonPrevious.setVisibility(View.VISIBLE);
    }
}
