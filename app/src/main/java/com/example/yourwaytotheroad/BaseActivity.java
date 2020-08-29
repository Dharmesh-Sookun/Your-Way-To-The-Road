package com.example.yourwaytotheroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private String currentTheme;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences("current_theme", Context.MODE_PRIVATE);
        currentTheme = sharedPref.getString("current_theme", "default");
        setAppTheme(currentTheme);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selectedTheme = sharedPref.getString("current_theme", "default");
        if(currentTheme != selectedTheme)
        {
            recreate();
        }
    }

    private void setAppTheme(String currentTheme)
    {
        switch (currentTheme)
        {
            case "default":
                setTheme(R.style.Theme_App_Default);
                break;

            case "lilac":
                setTheme(R.style.Theme_App_Lilac);
                break;

            case "mint":
                setTheme(R.style.Theme_App_Mint);
                break;

            case "sea_blue":
                setTheme(R.style.Theme_App_SeaBlue);
                break;

            case "grey":
                setTheme(R.style.Theme_App_Pink);
                break;

            default:
                setTheme(R.style.Theme_App_Default);
        }
    }
}
