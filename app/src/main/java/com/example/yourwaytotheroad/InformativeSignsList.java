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

public class InformativeSignsList extends BaseActivity implements RecyclerViewAdapter.onItemListener {

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
        setContentView(R.layout.activity_informative_signs_list);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Informative Signs", mTitle);

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
                createItem("Informative Signs")
        )));

        final String[] CATEGORIES_LIST ={"Priority Signs", "Warning signs", "Prohibitive signs", "Mandatory Signs", "Road Markings"};

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformativeSignsList.this);
                    builder.setTitle("Select Sign Category");
                    builder.setItems(CATEGORIES_LIST, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0:
                                    Intent gotoPrioritySigns = new Intent(InformativeSignsList.this, PrioritySignsList.class);
                                    finish();
                                    startActivity(gotoPrioritySigns);
                                    break;

                                case 1:
                                    Intent gotoWarningSigns = new Intent(InformativeSignsList.this, WarningSignsList.class);
                                    finish();
                                    startActivity(gotoWarningSigns);
                                    break;

                                case 2:
                                    Intent gotoProhibitiveSigns = new Intent(InformativeSignsList.this, ProhibitiveSignsList.class);
                                    finish();
                                    startActivity(gotoProhibitiveSigns);
                                    break;

                                case 3:
                                    Intent gotoMandatorySigns = new Intent(InformativeSignsList.this, MandatorySignsList.class);
                                    finish();
                                    startActivity(gotoMandatorySigns);
                                    break;

                                case 4:
                                    Intent gotoRoadMarkings = new Intent(InformativeSignsList.this, RoadMarkingsList.class);
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
                    Intent gotoMainMenu = new Intent(InformativeSignsList.this, MainActivity.class);
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
                Intent gotoCategories = new Intent(InformativeSignsList.this, CategoriesActivity.class);
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
            Intent goToCategories = new Intent(InformativeSignsList.this, CategoriesActivity.class);
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
        ImageID.add(R.drawable.information_pedestrian_crossing);
        ImageDesc.add("Position of humped pedestrian crossing");

        ImageID.add(R.drawable.information_sign_dual_carriageway);
        ImageDesc.add("Start of dual carriageway");

        ImageID.add(R.drawable.information_sign_end_of_motorway);
        ImageDesc.add("End of motorway");

        ImageID.add(R.drawable.information_sign_hospital);
        ImageDesc.add("Hospital sign");

        ImageID.add(R.drawable.information_sign_in);
        ImageDesc.add("In sign");

        ImageID.add(R.drawable.information_sign_motorway);
        ImageDesc.add("Start of motorway");

        ImageID.add(R.drawable.information_sign_no_entry);
        ImageDesc.add("No entry sign");

        ImageID.add(R.drawable.information_sign_no_exit);
        ImageDesc.add("No exit sign");

        ImageID.add(R.drawable.information_sign_no_through_road);
        ImageDesc.add("No through road sign");

        ImageID.add(R.drawable.information_sign_no_through_road_to_the_left);
        ImageDesc.add("No through road to the left");

        ImageID.add(R.drawable.information_sign_no_through_road_to_the_right);
        ImageDesc.add("No through road to the right");

        ImageID.add(R.drawable.information_sign_one_way_traffic);
        ImageDesc.add("One way traffic");

        ImageID.add(R.drawable.information_sign_out);
        ImageDesc.add("Out sign");

        ImageID.add(R.drawable.information_sign_parking_zone);
        ImageDesc.add("Parking zone sign");

        ImageID.add(R.drawable.information_sign_pedestrian_crossing);
        ImageDesc.add("Position of pedestrian crossing");

        ImageID.add(R.drawable.information_sign_speed_camera);
        ImageDesc.add("Speed camera ahead. Limit 60 km/h");

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
            startActivity(new Intent(InformativeSignsList.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_commands_informative");
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
                .singleUse("showcase_voice_commands_informative")
                .show();
    }
}
