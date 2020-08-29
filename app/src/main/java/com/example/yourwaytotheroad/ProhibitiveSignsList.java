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

public class ProhibitiveSignsList extends BaseActivity implements RecyclerViewAdapter.onItemListener {

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
        setContentView(R.layout.activity_prohibitive_signs_list);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Prohibitive Signs", mTitle);

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
                createItem("Prohibitive Signs")
        )));

        final String[] CATEGORIES_LIST ={"Priority Signs", "Warning signs", "Mandatory Signs", "Informative Signs", "Road Markings"};

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProhibitiveSignsList.this);
                    builder.setTitle("Select Sign Category");
                    builder.setItems(CATEGORIES_LIST, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    Intent gotoPrioritySigns = new Intent(ProhibitiveSignsList.this, PrioritySignsList.class);
                                    finish();
                                    startActivity(gotoPrioritySigns);
                                    break;

                                case 1:
                                    Intent gotoWarningSigns = new Intent(ProhibitiveSignsList.this, WarningSignsList.class);
                                    finish();
                                    startActivity(gotoWarningSigns);
                                    break;

                                case 2:
                                    Intent gotoMandatorySigns = new Intent(ProhibitiveSignsList.this, MandatorySignsList.class);
                                    finish();
                                    startActivity(gotoMandatorySigns);
                                    break;

                                case 3:
                                    Intent gotoInformativeSigns = new Intent(ProhibitiveSignsList.this, InformativeSignsList.class);
                                    finish();
                                    startActivity(gotoInformativeSigns);
                                    break;

                                case 4:
                                    Intent gotoRoadMarkings = new Intent(ProhibitiveSignsList.this, RoadMarkingsList.class);
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
                    Intent gotoMainMenu = new Intent(ProhibitiveSignsList.this, MainActivity.class);
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
                Intent gotoCategories = new Intent(ProhibitiveSignsList.this, CategoriesActivity.class);
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
            Intent goToCategories = new Intent(ProhibitiveSignsList.this, CategoriesActivity.class);
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
        ImageID.add(R.drawable.prohibition_no_left_turn);
        ImageDesc.add("No Left Turn");

        ImageID.add(R.drawable.prohibition_no_u_turns);
        ImageDesc.add("No U turns");

        ImageID.add(R.drawable.prohibition_of_passing_without_stopping);
        ImageDesc.add("Prohibition of passing without stopping");

        ImageID.add(R.drawable.prohibitory_sign_prohibition_of_passing_without_stopping_police);
        ImageDesc.add("Prohibition of passing without stopping - Police");

        ImageID.add(R.drawable.prohibitory_sign_prohibition_of_passing_without_stopping_customs);
        ImageDesc.add("Prohibition of passing without stopping - Customs");

        ImageID.add(R.drawable.prohibitory_sign_prohibition_of_passing_without_stopping_children);
        ImageDesc.add("Prohibition of passing without stopping - Children");

        ImageID.add(R.drawable.prohibitory_sign_no_overtaking);
        ImageDesc.add("Prohibition of overtaking");

        ImageID.add(R.drawable.prohibitory_sign_overtaking_is_prohibited_only_for_goods_vehicles);
        ImageDesc.add("Overtaking is prohibited only for goods vehicles");

        ImageID.add(R.drawable.prohibitory_sign_no_vehicles);
        ImageDesc.add("Vehicular traffic prohibited in both directions");

        ImageID.add(R.drawable.prohibitory_sign_no_entry);
        ImageDesc.add("Prohibition of entry");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_pedestrians);
        ImageDesc.add("Prohibition of entry for pedestrians");

        ImageID.add(R.drawable.prohibitory_sign_no_cyclists);
        ImageDesc.add("Prohibition of entry for cyclists");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_handcarts);
        ImageDesc.add("Prohibition of entry for handcarts");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_motorcycles);
        ImageDesc.add("Prohibition of entry for motorcyclists");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_power_driven_vehicle_except_two_wheeled_motorcycles_without_side_car);
        ImageDesc.add("Prohibition of entry for power driven vehicle");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_power_driven_agricultural_vehicles);
        ImageDesc.add("Prohibition of entry for power-driven agricultural vehicles");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_buses_and_coaches);
        ImageDesc.add("Prohibition of entry for buses and coaches");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_goods_vehicles);
        ImageDesc.add("Prohibition of entry for goods vehicles");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_more_than_2_tons_vehicles);
        ImageDesc.add("Prohibition of entry for vehicles weighing more than 2 tons");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_vehicles_having_an_overall_height_exceeding_3_5_metres);
        ImageDesc.add("No entry for vehicles having an overall height exceeding 3.5 m");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_vehicles_more_than_2_meters_wide);
        ImageDesc.add("Prohibition of entry for vehicles more than 2 m wide");

        ImageID.add(R.drawable.prohibitory_sign_no_entry_for_vehicles_exceeding_10_meters_in_length);
        ImageDesc.add("Prohibition of entry for vehicles exceeding 10 meters in length");

        ImageID.add(R.drawable.prohibitory_sign_no_horning);
        ImageDesc.add("Prohibition of sounding horns");

        ImageID.add(R.drawable.prohibitory_sign_speed_limit_50);
        ImageDesc.add("Speed limit is 50 km/h");

        ImageID.add(R.drawable.prohibitory_sign_end_of_prohibitions);
        ImageDesc.add("End of all prohibitions");

        ImageID.add(R.drawable.prohibitory_sign_speed_limit_zone);
        ImageDesc.add("20 km/h zone");

        ImageID.add(R.drawable.prohibitory_sign_parking_prohibited);
        ImageDesc.add("Prohibition of parking");

        ImageID.add(R.drawable.prohibitory_sign_parking_and_standing_prohibited);
        ImageDesc.add("Prohibition of parking and standing");

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
            startActivity(new Intent(ProhibitiveSignsList.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_commands_prohibitive");
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
                .singleUse("showcase_voice_commands_prohibitive")
                .show();
    }
}

