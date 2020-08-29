package com.example.yourwaytotheroad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class ChangeTheme extends BaseActivity {

    final String KEY_CURRENT_THEME = "current_theme";
    final String DEFAULT_THEME = "default";
    final String LILAC_THEME = "lilac";
    final String MINT_THEME = "mint";
    final String SEA_BLUE_THEME = "sea_blue";
    final String GREY_THEME = "grey";

    Toolbar toolbar;
    TextView mTitle;
    ImageButton backBtn;

    RadioButton defaultThemeRB, lilacThemeRB, mintThemeRB, seaBlueThemeRB, greyThemeRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_theme);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Change Theme", mTitle);

        backBtn = toolbar.findViewById(R.id.actionBtn);
        backBtn.setImageResource(R.drawable.ic_arrow_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMainMenu = new Intent(ChangeTheme.this, MainActivity.class);
                finish();
                startActivity(gotoMainMenu);
            }
        });

        final SharedPreferences sharedPref = getSharedPreferences(KEY_CURRENT_THEME, Context.MODE_PRIVATE);
        String currentTheme = sharedPref.getString(KEY_CURRENT_THEME, DEFAULT_THEME);

        defaultThemeRB = findViewById(R.id.defaultThemeRB);
        lilacThemeRB = findViewById(R.id.lilacThemeRB);
        mintThemeRB = findViewById(R.id.mintThemeRB);
        seaBlueThemeRB = findViewById(R.id.seaBlueThemeRB);
        greyThemeRB = findViewById(R.id.greyThemeRB);

        defaultThemeRB.setChecked(currentTheme.equals(DEFAULT_THEME));
        lilacThemeRB.setChecked(currentTheme.equals(LILAC_THEME));
        mintThemeRB.setChecked(currentTheme.equals(MINT_THEME));
        seaBlueThemeRB.setChecked(currentTheme.equals(SEA_BLUE_THEME));
        greyThemeRB.setChecked(currentTheme.equals(GREY_THEME));

        defaultThemeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString(KEY_CURRENT_THEME, DEFAULT_THEME).apply();
                recreate();
            }
        });

        lilacThemeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString(KEY_CURRENT_THEME, LILAC_THEME).apply();
                recreate();
            }
        });

        mintThemeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString(KEY_CURRENT_THEME, MINT_THEME).apply();
                recreate();
            }
        });

        seaBlueThemeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString(KEY_CURRENT_THEME, SEA_BLUE_THEME).apply();
                recreate();
            }
        });

        greyThemeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString(KEY_CURRENT_THEME, GREY_THEME).apply();
                recreate();
            }
        });



    }

    public void setToolbar(Toolbar toolbar, String title, TextView titleTextView)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleTextView.setText(title);

    }
}
