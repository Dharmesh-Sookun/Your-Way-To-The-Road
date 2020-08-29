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

public class WarningSignsList extends BaseActivity implements RecyclerViewAdapter.onItemListener {

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
        setContentView(R.layout.activity_warning_signs_list);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Warning Signs", mTitle);

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
                createItem("Warning Signs")
        )));

        final String[] CATEGORIES_LIST ={"Priority Signs", "Prohibitive Signs", "Mandatory Signs", "Informative Signs", "Road Markings"};

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WarningSignsList.this);
                    builder.setTitle("Select Sign Category");
                    builder.setItems(CATEGORIES_LIST, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    Intent gotoPrioritySigns = new Intent(WarningSignsList.this, PrioritySignsList.class);
                                    finish();
                                    startActivity(gotoPrioritySigns);
                                    break;

                                case 1:
                                    Intent gotoProhibitiveSigns = new Intent(WarningSignsList.this, ProhibitiveSignsList.class);
                                    finish();
                                    startActivity(gotoProhibitiveSigns);
                                    break;

                                case 2:
                                    Intent gotoMandatorySigns = new Intent(WarningSignsList.this, MandatorySignsList.class);
                                    finish();
                                    startActivity(gotoMandatorySigns);
                                    break;

                                case 3:
                                    Intent gotoInformativeSigns = new Intent(WarningSignsList.this, InformativeSignsList.class);
                                    finish();
                                    startActivity(gotoInformativeSigns);
                                    break;

                                case 4:
                                    Intent gotoRoadSigns = new Intent(WarningSignsList.this, RoadMarkingsList.class);
                                    finish();
                                    startActivity(gotoRoadSigns);
                                    break;

                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                else if(i == 0)
                {
                    Intent gotoMainMenu = new Intent(WarningSignsList.this, MainActivity.class);
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
                Intent gotoCategories = new Intent(WarningSignsList.this, CategoriesActivity.class);
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
            Intent goToCategories = new Intent(WarningSignsList.this, CategoriesActivity.class);
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
        ImageID.add(R.drawable.warning_pedestrian_crossing);
        ImageDesc.add("Pedestrian Crossing Ahead");

        ImageID.add(R.drawable.warning_children_crossing);
        ImageDesc.add("Children Crossing Ahead");

        ImageID.add(R.drawable.warning_road_works);
        ImageDesc.add("Road Works Ahead");

        ImageID.add(R.drawable.warning_cyclist_entering_or_crossing);
        ImageDesc.add("Cyclist entering or crossing");

        ImageID.add(R.drawable.warning_domestic_animals_crossing);
        ImageDesc.add("Domestic animals crossing");

        ImageID.add(R.drawable.warning_wild_animals_crossing);
        ImageDesc.add("Wild animals crossing");

        ImageID.add(R.drawable.warning_slippery_road);
        ImageDesc.add("Slippery Road");

        ImageID.add(R.drawable.warning_loose_gravels);
        ImageDesc.add("Loose Gravels");

        ImageID.add(R.drawable.warning_falling_rocks);
        ImageDesc.add("Falling Rocks");

        ImageID.add(R.drawable.warning_hump_bridge);
        ImageDesc.add("Hump Bridge");

        ImageID.add(R.drawable.warning_hump);
        ImageDesc.add("Hump");

        ImageID.add(R.drawable.warning_uneven_roads);
        ImageDesc.add("Uneven Roads");

        ImageID.add(R.drawable.warning_road_leads_on_to_quay_or_river_bank);
        ImageDesc.add("Road leads on to quay or river bank");

        ImageID.add(R.drawable.warning_steep_ascent);
        ImageDesc.add("Steep Ascent");

        ImageID.add(R.drawable.warning_dangerous_descent);
        ImageDesc.add("Dangerous Descent");

        ImageID.add(R.drawable.warning_airfield);
        ImageDesc.add("Airfield");

        ImageID.add(R.drawable.warning_crosswinds);
        ImageDesc.add("Crosswinds");

        ImageID.add(R.drawable.warning_level_crossing_without_gate_or_barrier);
        ImageDesc.add("Level crossing without gate or barrier");

        ImageID.add(R.drawable.warning_level_crossing_with_gate_or_barrier);
        ImageDesc.add("Level crossing with gate or barrier");

        ImageID.add(R.drawable.warning_other_dangers);
        ImageDesc.add("Other Dangers");

        ImageID.add(R.drawable.warning_traffic_light);
        ImageDesc.add("Traffic Signals Ahead");

        ImageID.add(R.drawable.warning_roundabout_ahead);
        ImageDesc.add("Roundabout Ahead");

        ImageID.add(R.drawable.warning_two_way_traffic_across_one_way_road);
        ImageDesc.add("Two way traffic across one way road");

        ImageID.add(R.drawable.warning_two_way_traffic);
        ImageDesc.add("Two Way Traffic");

        ImageID.add(R.drawable.warning_road_narrows_on_right);
        ImageDesc.add("Road narrows on the right");

        ImageID.add(R.drawable.warning_road_narrows_on_both_sides);
        ImageDesc.add("Road narrows on both sides");


        ImageID.add(R.drawable.warning_right_hand_lane_of_a_2_lane_1_way_road_closed);
        ImageDesc.add("Right-hand lane of a 2-lane 1-way road closed");

        ImageID.add(R.drawable.warning_traffic_merging_from_left_behind);
        ImageDesc.add("Traffic merging from left behind");

        ImageID.add(R.drawable.warning_crossroads);
        ImageDesc.add("Crossroads Ahead");

        ImageID.add(R.drawable.warning_side_road_on_the_right);
        ImageDesc.add("Side road on the right");

        ImageID.add(R.drawable.warning_staggered_junction);
        ImageDesc.add("Staggered Junction");

        ImageID.add(R.drawable.warning_bend_to_the_right);
        ImageDesc.add("Bend to the right");

        ImageID.add(R.drawable.warning_double_bend_first_to_right);
        ImageDesc.add("Double bends first to the right");

        ImageID.add(R.drawable.warning_sharp_deviation_of_route);
        ImageDesc.add("Sharp Deviation of the route");

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
            startActivity(new Intent(WarningSignsList.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_commands_warning");
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
                .singleUse("showcase_voice_commands_warning")
                .show();
    }
}
