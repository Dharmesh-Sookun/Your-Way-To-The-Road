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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import moe.feng.common.view.breadcrumbs.BreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class MandatorySignsList extends BaseActivity implements RecyclerViewAdapter.onItemListener {

    Toolbar toolbar;
    TextView mTitle;
    ImageButton backBtn;
    FloatingActionButton speakFab;

    private ArrayList<String> ImageDesc = new ArrayList<>();
    private ArrayList<Integer> ImageID = new ArrayList<>();

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    BreadcrumbsView mBreadcrumbsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandatory_signs_list);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Mandatory Signs", mTitle);

        backBtn = toolbar.findViewById(R.id.actionBtn);
        backBtn.setImageResource(R.drawable.ic_arrow_back);

        speakFab = findViewById(R.id.speakFab);

        presentShowcaseSequence();

        String htmlCode = "Categories &#x25BC;"; //&#x25bd;
        String strJava = Html.fromHtml(htmlCode).toString();

        mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);
        mBreadcrumbsView.setItems(new ArrayList<>(Arrays.asList(
                BreadcrumbItem.createSimpleItem("Main Menu"),
                createItem(strJava),
                createItem("Mandatory Signs")
        )));

        final String[] CATEGORIES_LIST ={"Priority Signs", "Warning signs", "Prohibitive signs", "Informative Signs", "Road Markings"};

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MandatorySignsList.this);
                    builder.setTitle("Select Sign Category");
                    builder.setItems(CATEGORIES_LIST, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    Intent gotoPrioritySigns = new Intent(MandatorySignsList.this, PrioritySignsList.class);
                                    finish();
                                    startActivity(gotoPrioritySigns);
                                    break;

                                case 1:
                                    Intent gotoWarningSigns = new Intent(MandatorySignsList.this, WarningSignsList.class);
                                    finish();
                                    startActivity(gotoWarningSigns);
                                    break;

                                case 2:
                                    Intent gotoProhibitiveSigns = new Intent(MandatorySignsList.this, ProhibitiveSignsList.class);
                                    finish();
                                    startActivity(gotoProhibitiveSigns);
                                    break;

                                case 3:
                                    Intent gotoInformativeSigns = new Intent(MandatorySignsList.this, InformativeSignsList.class);
                                    finish();
                                    startActivity(gotoInformativeSigns);
                                    break;

                                case 4:
                                    Intent gotoRoadMarkings = new Intent(MandatorySignsList.this, RoadMarkingsList.class);
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
                    Intent gotoMainMenu = new Intent(MandatorySignsList.this, MainActivity.class);
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
                Intent gotoCategories = new Intent(MandatorySignsList.this, CategoriesActivity.class);
                finish();
                startActivity(gotoCategories);
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
        if(s.equalsIgnoreCase("back") || s.equalsIgnoreCase("categories"))
        {
            Intent goToCategories = new Intent(MandatorySignsList.this, CategoriesActivity.class);
            finish();
            startActivity(goToCategories);
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
        ImageID.add(R.drawable.mandatory_keep_left);
        ImageDesc.add("Keep Left");

        ImageID.add(R.drawable.mandatory_sign_left_turn_only);
        ImageDesc.add("Left Turn Only");

        ImageID.add(R.drawable.mandatory_sign_right_turn_only);
        ImageDesc.add("Right Turn Only");

        ImageID.add(R.drawable.mandatory_sign_ahead_only);
        ImageDesc.add("Ahead Only");

        ImageID.add(R.drawable.mandatory_sign_compulsory_footpath);
        ImageDesc.add("Compulsory Footpath");

        ImageID.add(R.drawable.mandatory_sign_compulsory_minimum_speed_30);
        ImageDesc.add("Compulsory minimum speed of 30 km/h");

        ImageID.add(R.drawable.mandatory_sign_compulsory_roundabout);
        ImageDesc.add("Compulsory Roundabout");

        ImageID.add(R.drawable.mandatory_sign_compulsory_route_for_bicycle);
        ImageDesc.add("Compulsory route for bicycles");

        ImageID.add(R.drawable.mandatory_sign_compulsory_route_for_buses);
        ImageDesc.add("Compulsory route for buses");

        ImageID.add(R.drawable.mandatory_sign_end_of_compulsory_minimum_speed_30);
        ImageDesc.add("End of compulsory minimum speed of 30 km/h");

        ImageID.add(R.drawable.mandatory_sign_keep_right);
        ImageDesc.add("Keep right");

        ImageID.add(R.drawable.mandatory_sign_left_turn_ahead_only);
        ImageDesc.add("Compulsory left turn ahead");

        ImageID.add(R.drawable.mandatory_sign_pass_either_side);
        ImageDesc.add("Pass either side");

        ImageID.add(R.drawable.mandatory_sign_right_turn_ahead_only);
        ImageDesc.add("Compulsory right turn ahead");

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
            startActivity(new Intent(MandatorySignsList.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_commands_mandatory");
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
        String htmlString = "<b>Voice command options</b><br/><br/>" +
                "Use the following commands<br/><br/>" +
                "&#8226; 'Back'/'Categories': To go to the learn now/categories screen<br/><br/>" +
                "&#8226; 'Exit'/'Quit'/'Close': To quit the app";

        ShowcaseTooltip tooltip = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlString);

        new MaterialShowcaseView.Builder(this)
                .setTarget(speakFab)
                .setToolTip(tooltip)
                .withCircleShape()
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .singleUse("showcase_voice_commands_mandatory")
                .show();
    }
}
