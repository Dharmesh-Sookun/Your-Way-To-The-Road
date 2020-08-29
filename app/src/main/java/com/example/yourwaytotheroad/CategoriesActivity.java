package com.example.yourwaytotheroad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import moe.feng.common.view.breadcrumbs.BreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;
import uk.co.deanwild.materialshowcaseview.shape.CircleShape;

public class CategoriesActivity extends BaseActivity implements RecyclerViewAdapter.onItemListener {

    Toolbar toolbar;
    TextView mTitle;
    ImageButton backBtn;
    TextToSpeech mTTS;
    FloatingActionButton speakFab;

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    private ArrayList<String> ImageDesc = new ArrayList<>();
    private ArrayList<Integer> ImageID = new ArrayList<>();

    BreadcrumbsView mBreadcrumbsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Categories", mTitle);

        backBtn = toolbar.findViewById(R.id.actionBtn);
        backBtn.setImageResource(R.drawable.ic_arrow_back);

        speakFab = findViewById(R.id.speakFab);
        presentShowcaseSequence();

        String htmlCode = "Categories &#x25BC;"; //&#x25bd;
        String strJava = Html.fromHtml(htmlCode).toString();

        mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);
        mBreadcrumbsView.setItems(new ArrayList<>(Arrays.asList(
                BreadcrumbItem.createSimpleItem("Main Menu"),
                createItem(strJava)
        )));

        final String[] CATEGORIES_LIST ={"Priority Signs", "Warning signs", "Prohibitive signs", "Mandatory Signs", "Informative Signs", "Road Markings"};

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
                    builder.setTitle("Select Sign Category");
                    builder.setItems(CATEGORIES_LIST, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    Intent gotoPrioritySigns = new Intent(CategoriesActivity.this, PrioritySignsList.class);
                                    finish();
                                    startActivity(gotoPrioritySigns);
                                    break;

                                case 1:
                                    Intent gotoWarningSigns = new Intent(CategoriesActivity.this, WarningSignsList.class);
                                    finish();
                                    startActivity(gotoWarningSigns);
                                    break;

                                case 2:
                                    Intent gotoProhibitiveSigns = new Intent(CategoriesActivity.this, ProhibitiveSignsList.class);
                                    finish();
                                    startActivity(gotoProhibitiveSigns);
                                    break;

                                case 3:
                                    Intent gotoMandatorySigns = new Intent(CategoriesActivity.this, MandatorySignsList.class);
                                    finish();
                                    startActivity(gotoMandatorySigns);
                                    break;

                                case 4:
                                    Intent gotoInformativeSigns = new Intent(CategoriesActivity.this, InformativeSignsList.class);
                                    finish();
                                    startActivity(gotoInformativeSigns);
                                    break;

                                case 5:
                                    Intent gotoRoadMarkings = new Intent(CategoriesActivity.this, RoadMarkingsList.class);
                                    finish();
                                    startActivity(gotoRoadMarkings);
                                    break;
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                else if(i == 0)
                {
                    Intent gotoMainMenu = new Intent(CategoriesActivity.this, MainActivity.class);
                    finish();
                    startActivity(gotoMainMenu);
                }
            }

            @Override
            public void onItemChange(@NonNull BreadcrumbsView breadcrumbsView, int i, @NonNull BreadcrumbItem breadcrumbItem) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMainMenu = new Intent(CategoriesActivity.this, MainActivity.class);
                finish();
                startActivity(gotoMainMenu);
            }
        });

        speakFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToOpenMic();
            }
        });

        initImages();
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
        if(s.equalsIgnoreCase("back") || s.equalsIgnoreCase("main menu"))
        {
            Intent goToMainMenu = new Intent(CategoriesActivity.this, MainActivity.class);
            finish();
            startActivity(goToMainMenu);
        }

        else if(s.equalsIgnoreCase("priority signs") || s.equalsIgnoreCase("priority sign") || s.equalsIgnoreCase("priority"))
        {
            Intent goToPrioritySigns = new Intent(CategoriesActivity.this, PrioritySignsList.class);
            finish();
            startActivity(goToPrioritySigns);
        }

        else if(s.equalsIgnoreCase("warning sign") || s.equalsIgnoreCase("warning signs") || s.equalsIgnoreCase("warning"))
        {
            Intent goToWarningSigns = new Intent(CategoriesActivity.this, WarningSignsList.class);
            finish();
            startActivity(goToWarningSigns);
        }

        else if(s.equalsIgnoreCase("prohibitive sign") || s.equalsIgnoreCase("prohibitive signs") || s.equalsIgnoreCase("prohibitive"))
        {
            Intent goToProhibitiveSigns = new Intent(CategoriesActivity.this, ProhibitiveSignsList.class);
            finish();
            startActivity(goToProhibitiveSigns);
        }

        else if(s.equalsIgnoreCase("mandatory sign") || s.equalsIgnoreCase("mandatory signs") || s.equalsIgnoreCase("mandatory"))
        {
            Intent goToMandatorySigns = new Intent(CategoriesActivity.this, MandatorySignsList.class);
            finish();
            startActivity(goToMandatorySigns);
        }

        else if(s.equalsIgnoreCase("informative sign") || s.equalsIgnoreCase("informative signs") || s.equalsIgnoreCase("informative"))
        {
            Intent goToInformativeSigns = new Intent(CategoriesActivity.this, InformativeSignsList.class);
            finish();
            startActivity(goToInformativeSigns);
        }

        else if(s.equalsIgnoreCase("road markings") || s.equalsIgnoreCase("road marking"))
        {
            Intent goToRoadMarkings = new Intent(CategoriesActivity.this, RoadMarkingsList.class);
            finish();
            startActivity(goToRoadMarkings);
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

    private void initImages()
    {
        ImageID.add(R.drawable.priority_give_way);
        ImageDesc.add("Priority Signs");

        ImageID.add(R.drawable.warning_pedestrian_crossing);
        ImageDesc.add("Warning Signs");

        ImageID.add(R.drawable.prohibition_no_left_turn);
        ImageDesc.add("Prohibitive Signs");

        ImageID.add(R.drawable.mandatory_keep_left);
        ImageDesc.add("Mandatory Signs");

        ImageID.add(R.drawable.information_pedestrian_crossing);
        ImageDesc.add("Informative Signs");

        ImageID.add(R.drawable.road_marking_warning_line);
        ImageDesc.add("Road Markings");

        initRecyclerView();

    }


    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ImageDesc, ImageID, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClicked(int position) {

        Intent intent;

        if(position == 0)
        {
            intent = new Intent(CategoriesActivity.this, PrioritySignsList.class);
            finish();
            startActivity(intent);
        }

        else if(position == 1)
        {
            intent = new Intent(CategoriesActivity.this, WarningSignsList.class);
            finish();
            startActivity(intent);
        }

        else if(position == 2)
        {
            intent = new Intent(CategoriesActivity.this, ProhibitiveSignsList.class);
            finish();
            startActivity(intent);
        }

        else if(position == 3)
        {
            intent = new Intent(CategoriesActivity.this, MandatorySignsList.class);
            finish();
            startActivity(intent);
        }

        else if(position == 4)
        {
            intent = new Intent(CategoriesActivity.this, InformativeSignsList.class);
            finish();
            startActivity(intent);
        }

        else if(position == 5)
        {
            intent = new Intent(CategoriesActivity.this, RoadMarkingsList.class);
            finish();
            startActivity(intent);
        }
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
            startActivity(new Intent(CategoriesActivity.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_commands_categories");
            presentShowcaseSequence();
        }
        return super.onOptionsItemSelected(item);
    }

    private static BreadcrumbItem createItem(String title) {
        List<String> list = new ArrayList<>();
        list.add(title);
        return new BreadcrumbItem(list);
    }

    private void presentShowcaseSequence()
    {
        String htmlStringVoiceCommand = "<b>Voice command options</b><br/><br/>" +
                "Use the following commands<br/><br/>" +
                "&#8226; 'Priority Signs'/'Priority Sign'/'Priority': To go to priority signs list<br/><br/>" +
                "&#8226; 'Warning Signs'/'Warning Sign'/'Warning': To go to warning signs list<br/><br/>" +
                "&#8226; 'Prohibitive Signs'/'Prohibitive Sign'/'Prohibitive': To go to prohibitive signs list<br/><br/>" +
                "&#8226; 'Mandatory Signs'/'Mandatory Sign'/'Mandatory': To go to mandatory signs list<br/><br/>" +
                "&#8226; 'Informative Signs'/'Informative Sign'/'Informative': To go to informative signs list<br/><br/>" +
                "&#8226; 'Road Markings'/'Road Marking': To go to road markings list<br/><br/>" +
                "&#8226; 'Back'/'Categories': To go to the learn now/categories screen<br/><br/>" +
                "&#8226; 'Exit'/'Quit'/'Close': To quit the app";

        ShowcaseTooltip showcaseTooltipVoiceCommand = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 15)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringVoiceCommand);

        new MaterialShowcaseView.Builder(this)
                .setTarget(speakFab)
                .setToolTip(showcaseTooltipVoiceCommand)
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .singleUse("showcase_voice_commands_categories")
                .show();


    }
}
