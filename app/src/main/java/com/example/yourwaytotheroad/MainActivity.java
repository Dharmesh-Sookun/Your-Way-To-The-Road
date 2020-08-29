package com.example.yourwaytotheroad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    TextView mTitle;
    ImageButton quitBtn;
    Button learnNowBtn;
    Button takeTestBtn;
    Button bookTestBtn;
    ImageView logoImg;
    SharedPreferences sharedPrefs;
    FloatingActionButton speakFab;

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        learnNowBtn = findViewById(R.id.learnNowBtn);
        takeTestBtn = findViewById(R.id.takeTestBtn);
        bookTestBtn = findViewById(R.id.bookTestBtn);
        logoImg = findViewById(R.id.logoImgView);
        speakFab = findViewById(R.id.speakFab);

        sharedPrefs = getSharedPreferences("current_theme", Context.MODE_PRIVATE);
        String currentTheme = sharedPrefs.getString("current_theme", "default");

        if(currentTheme.equals("default"))
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo));
        }

        else if(currentTheme.equals("lilac"))
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo_lilac_theme));
        }

        else if(currentTheme.equals("mint"))
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo_mint_theme));
        }

        else if(currentTheme.equals("sea_blue"))
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo_sea_blue_theme));
        }

        else if(currentTheme.equals("grey"))
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo_grey_theme));
        }

        else
        {
            logoImg.setBackground(getResources().getDrawable(R.drawable.logo));
        }

        setToolbar(toolbar, "Main Menu", mTitle);

        quitBtn = toolbar.findViewById(R.id.actionBtn);
        quitBtn.setImageResource(R.drawable.ic_power_settings);

        presentShowcaseSequence();

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.exit(0);
                finish();
            }
        });

        learnNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoCategories = new Intent(MainActivity.this, CategoriesActivity.class);
                finish();
                startActivity(gotoCategories);
            }
        });

        takeTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeTest = new Intent(MainActivity.this, TakeTestActivity.class);
                finish();
                startActivity(takeTest);
            }
        });

        bookTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookTest = new Intent(MainActivity.this, bookTest.class);
                finish();
                startActivity(bookTest);
            }
        });

        speakFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToOpenMic();
            }
        });

    }

    private void btnToOpenMic()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi there Speak Now.....");

        try
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        }
        catch(ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case REQ_CODE_SPEECH_OUTPUT:
                if(resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> voiceAsText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    executeAction(voiceAsText.get(0));
                }

                break;
        }
    }

    private void executeAction(String s)
    {
        if(s.equalsIgnoreCase("learn now") || s.equalsIgnoreCase("go to learn now") || s.equalsIgnoreCase("categories") || s.equalsIgnoreCase("go to categories"))
        {
            Intent gotoCategories = new Intent(MainActivity.this, CategoriesActivity.class);
            finish();
            startActivity(gotoCategories);
        }

        else if(s.equalsIgnoreCase("take test") || s.equalsIgnoreCase("test") || s.equalsIgnoreCase("take mock test"))
        {
            Intent takeTest = new Intent(MainActivity.this, TakeTestActivity.class);
            finish();
            startActivity(takeTest);
        }

        else if(s.equalsIgnoreCase("book test"))
        {
            Intent bookTest = new Intent(MainActivity.this, bookTest.class);
            finish();
            startActivity(bookTest);
        }

        else if(s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("close"))
        {
            finish();
        }
    }

    public void setToolbar(Toolbar toolbar, String title, TextView titleTextView)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleTextView.setText(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.changeTheme)
        {
            startActivity(new Intent(MainActivity.this, ChangeTheme.class));
        }

        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase");
            presentShowcaseSequence();
        }

        return super.onOptionsItemSelected(item);
    }

    private void presentShowcaseSequence()
    {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "showcase");

        String htmlStringLearnNow = "<b>Learn Now</b><br/><br/>Click this button to learn about the different signs in Mauritius";

        ShowcaseTooltip tooltipLearNow = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringLearnNow);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(learnNowBtn)
                        .setToolTip(tooltipLearNow)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.mask_color))
                        .withRectangleShape()
                        .build()
        );

        String htmlStringTakeMockText = "<b>Take Mock Test</b><br/><br/>Test your knowledge on highway rules by taking a mock test";

        ShowcaseTooltip showcaseTooltipTakeTest = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringTakeMockText);

        sequence.addSequenceItem(
            new MaterialShowcaseView.Builder(this)
                    .setTarget(takeTestBtn)
                    .setToolTip(showcaseTooltipTakeTest)
                    .setDismissOnTouch(true)
                    .setMaskColour(getResources().getColor(R.color.mask_color))
                    .withRectangleShape()
                    .build()
        );

        String htmlStringBookTest = "<b>Book your test now!!</b><br/><br/>Already feeling ready for your test? Book your real test here!";

        ShowcaseTooltip showcaseTooltipBookTest = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringBookTest);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(bookTestBtn)
                        .setToolTip(showcaseTooltipBookTest)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.mask_color))
                        .withRectangleShape()
                        .build()
        );

        String htmlStringVoiceCommand = "<b>Voice Command Options</b><br/><br/>" +
                "Use the following commands<br/><br/>" +
                "&#8226; 'Learn now'/'Got to learn now'/'Categories'/'Go to categories': To go to the learn now/categories screen<br/><br/>" +
                "&#8226; 'Take test'/'Test'/'Take mock test': To take a test on the app<br/><br/>" +
                "&#8226; 'Book Test': To book an oral test on the app<br/><br/>" +
                "&#8226; 'Exit'/'Quit'/'Close': To quit the app";

        ShowcaseTooltip showcaseTooltipVoiceCommand = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringVoiceCommand);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(speakFab)
                        .setToolTip(showcaseTooltipVoiceCommand)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.mask_color))
                        .withCircleShape()
                        .build()
        );

        sequence.start();
    }
}
