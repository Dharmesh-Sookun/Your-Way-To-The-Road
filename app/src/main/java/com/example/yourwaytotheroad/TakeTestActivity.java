package com.example.yourwaytotheroad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import moe.feng.common.view.breadcrumbs.BreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class TakeTestActivity extends BaseActivity {

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    SharedPreferences sharedPrefs;
    String currentTheme;

    Toolbar toolbar;
    TextView mTitle;
    ImageButton backBtn;
    FloatingActionButton speakFab;
    BreadcrumbsView mBreadcrumbsView;

    private TextToSpeech mTTS;
    ImageView image;
    Button one,two,three, sound, speak;
    TextView question,wrong1,correct1,/*option1,option2,option3*/result, test_description;
    EditText no_question;
    int correct=0;
    int wrong=0;
    String answer=new String();
    int id=0;
    int number = 40,counter=0;
    boolean click=false, mute = true;

    ArrayList<ImageQuestion>content=new ArrayList<ImageQuestion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);

        sharedPrefs = getSharedPreferences("current_theme", Context.MODE_PRIVATE);
        currentTheme = sharedPrefs.getString("current_theme", "default");

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Mock Test", mTitle);

        backBtn = toolbar.findViewById(R.id.actionBtn);
        backBtn.setImageResource(R.drawable.ic_arrow_back);

        speakFab = findViewById(R.id.speakFab);

        mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);
        mBreadcrumbsView.setItems(new ArrayList<>(Arrays.asList(
                BreadcrumbItem.createSimpleItem("Main Menu"),
                createItem("Mock Test")
        )));

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 0)
                {
                    Intent gotoMainMenu = new Intent(TakeTestActivity.this, MainActivity.class);
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
                Intent gotoMainMenu = new Intent(TakeTestActivity.this, MainActivity.class);
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

        //no_question=(EditText)findViewById(R.id.no_question);
        image = findViewById(R.id.image);
        question = findViewById(R.id.question);
        //option1=(TextView)findViewById(R.id.option1);
        // option2=(TextView)findViewById(R.id.option2);
        // option3=(TextView)findViewById(R.id.option3);
        wrong1 = findViewById(R.id.wrong);
        correct1 = findViewById(R.id.correct);
        result = findViewById(R.id.result);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        sound = findViewById(R.id.mute);
        speak = findViewById(R.id.speak);

        mute = false;

        content.add(new ImageQuestion(R.drawable.warning_right_hand_lane_of_a_2_lane_1_way_road_closed,"1"));
        content.add(new ImageQuestion(R.drawable.mandatory_sign_right_turn_only,"2"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_prohibition_of_passing_without_stopping_police,"3"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_goods_vehicles,"4"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_vehicles,"5"));
        content.add(new ImageQuestion(R.drawable.warning_loose_gravels,"6"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_more_than_2_tons_vehicles,"7"));
        content.add(new ImageQuestion(R.drawable.information_sign_one_way_traffic,"8"));
        content.add(new ImageQuestion(R.drawable.warning_double_bend_first_to_right,"9"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_power_driven_agricultural_vehicles,"10"));
        content.add(new ImageQuestion(R.drawable.warning_falling_rocks,"11"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_buses_and_coaches,"12"));
        content.add(new ImageQuestion(R.drawable.warning_road_narrows_on_right,"13"));
        content.add(new ImageQuestion(R.drawable.information_sign_pedestrian_crossing,"14"));
        content.add(new ImageQuestion(R.drawable.warning_dangerous_descent,"15"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_cyclists,"16"));
        content.add(new ImageQuestion(R.drawable.prohibition_no_left_turn,"17"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_parking_and_standing_prohibited,"18"));
        content.add(new ImageQuestion(R.drawable.warning_crosswinds,"19"));
        content.add(new ImageQuestion(R.drawable.temporary_prohibition_of_passing_without_stopping,"20")); //Temporary Stop
        content.add(new ImageQuestion(R.drawable.information_sign_out,"21"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_motorcycles,"22"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_parking_prohibited,"23"));
        content.add(new ImageQuestion(R.drawable.warning_road_works,"24"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_speed_limit_50,"25"));
        content.add(new ImageQuestion(R.drawable.warning_two_way_traffic_across_one_way_road,"26"));
        content.add(new ImageQuestion(R.drawable.mandatory_sign_compulsory_roundabout,"27"));
        content.add(new ImageQuestion(R.drawable.priority_give_way,"28"));
        content.add(new ImageQuestion(R.drawable.warning_traffic_light,"29"));
        content.add(new ImageQuestion(R.drawable.mandatory_sign_compulsory_minimum_speed_30,"30"));
        content.add(new ImageQuestion(R.drawable.warning_road_leads_on_to_quay_or_river_bank,"31"));
        content.add(new ImageQuestion(R.drawable.prohibitory_sign_no_entry_for_power_driven_vehicle_except_two_wheeled_motorcycles_without_side_car,"32"));
        content.add(new ImageQuestion(R.drawable.warning_two_way_traffic,"33"));
        content.add(new ImageQuestion(R.drawable.mandatory_sign_compulsory_route_for_buses,"34"));
        content.add(new ImageQuestion(R.drawable.warning_staggered_junction,"35"));
        content.add(new ImageQuestion(R.drawable.warning_level_crossing_with_gate_or_barrier,"36"));
        content.add(new ImageQuestion(R.drawable.warning_crossroads,"37"));
        content.add(new ImageQuestion(R.drawable.warning_cyclist_entering_or_crossing,"38"));
        content.add(new ImageQuestion(R.drawable.warning_wild_animals_crossing,"39"));
        content.add(new ImageQuestion(R.drawable.warning_side_road_on_the_right,"40"));
        content.add(new ImageQuestion(R.drawable.warning_hump_bridge,"41"));

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                    else {
                        Log.e("TTS", "Initialization failed");
                    }

                }
            }
        });

        get_question();

        presentShowcaseSequence();

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mute==true)
                {
                    mute=false;
                    sound.setBackgroundResource(R.drawable.ic_volume_up); //R.drawable.ic_volume_off
                    speak.setText("Speak");
                    speak.setCompoundDrawables(getDrawable(R.drawable.ic_speaker_phone_24dp), null, null, null);
                }
                else
                {
                    speaknull();
                    mute=true;
                    sound.setBackgroundResource(R.drawable.ic_volume_off); //R.drawable.ic_volume_up
                    speak.setText("Speak");
                    speak.setCompoundDrawables(getDrawable(R.drawable.ic_speaker_phone_24dp), null, null, null);

                }
            }
        });

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mute==true||speak.getText().toString().trim()=="Stop")
                {
                    if(mute==true)
                    {
                        Toast toast = null;
                        toast.makeText(TakeTestActivity.this,"Mute mode",Toast.LENGTH_SHORT).show();
                    //    speak.setText("Speak");
                     //   speak.setCompoundDrawables(getDrawable(R.drawable.ic_speaker_phone_24dp), null, null, null);
                    }
                    else{
                        speaknull();
                        speak.setText("Speak");
                        speak.setCompoundDrawables(getDrawable(R.drawable.ic_speaker_phone_24dp), null, null, null);
                    }
                }
                else
                {
                    String[] screen={question.getText().toString().trim(),one.getText().toString().trim(),two.getText().toString().trim(),three.getText().toString().trim()};
                    speak(screen);
                    speak.setText("Stop");
                    speak.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_stop_24dp), null, null, null);

                }

            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaknull();
                String response=one.getText().toString().trim();
                if(counter==number)
                {  validation(response,"one");
                    image.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    //  option1.setVisibility(View.INVISIBLE);
                    // option2.setVisibility(View.INVISIBLE);
                    // option3.setVisibility(View.INVISIBLE);
                    wrong1.setVisibility(View.INVISIBLE);
                    correct1.setVisibility(View.INVISIBLE);
                    one.setVisibility(View.INVISIBLE);
                    two.setVisibility(View.INVISIBLE);
                    three.setVisibility(View.INVISIBLE);
                    String w= String.valueOf(wrong);
                    String c= String.valueOf(correct);
                    result.setText("Correct : "+c+" Wrong : "+w);

                }
                else{
                    if(click==false)
                    {click=true;
                        validation(response,"one");
                        delay_quest();
                    }
                    else
                    {
                        Toast toast = null;
                        toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });



        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaknull();
                String response=two.getText().toString().trim();
                if(counter==number)
                {  validation(response,"two");
                    image.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    //	option1.setVisibility(View.INVISIBLE);
                    //option2.setVisibility(View.INVISIBLE);
                    //	option3.setVisibility(View.INVISIBLE);
                    wrong1.setVisibility(View.INVISIBLE);
                    correct1.setVisibility(View.INVISIBLE);
                    one.setVisibility(View.INVISIBLE);
                    two.setVisibility(View.INVISIBLE);
                    three.setVisibility(View.INVISIBLE);
                    String w= String.valueOf(wrong);
                    String c= String.valueOf(correct);
                    result.setText("Correct : "+c+" Wrong : "+w);

                }
                else{

                    if(click==false)
                    {click=true;
                        validation(response,"two");
                        delay_quest();
                    }
                    else
                    {
                        Toast toast = null;
                        toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });




        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaknull();
                String response=three.getText().toString().trim();
                if(counter==number)
                {  validation(response,"three");
                    image.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    //	option1.setVisibility(View.INVISIBLE);
                    //	option2.setVisibility(View.INVISIBLE);
                    //	option3.setVisibility(View.INVISIBLE);
                    wrong1.setVisibility(View.INVISIBLE);
                    correct1.setVisibility(View.INVISIBLE);
                    one.setVisibility(View.INVISIBLE);
                    two.setVisibility(View.INVISIBLE);
                    three.setVisibility(View.INVISIBLE);
                    String w= String.valueOf(wrong);
                    String c= String.valueOf(correct);
                    result.setText("Correct : "+c+" Wrong : "+w);

                }
                else{
                    if(click==false)
                    {click=true;
                        validation(response,"three");
                        delay_quest();
                    }
                    else
                    {
                        Toast toast = null;
                        toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    public void setToolbar(Toolbar toolbar, String title, TextView titleTextView)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleTextView.setText(title);

    }

    public void delay_quest()
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                one.setTextColor(Color.parseColor("#FFFFFF"));
                two.setTextColor(Color.parseColor("#FFFFFF"));
                three.setTextColor(Color.parseColor("#FFFFFF"));
                get_question();
            }
        }, 3000);
    }

    public void validation(String ans,String press)
    {
        if(ans.equals(answer))
        {
            correct++;
            if(one.getText().toString().trim()==answer)
            {
               // one.setBackgroundColor(Color.parseColor("#52de97"));
               // one.setTextColor(Color.parseColor("#52de97"));//green
                one.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }
            else if(two.getText().toString().trim()==answer)
            {
               // two.setBackgroundColor(Color.parseColor("#52de97"));
               // two.setTextColor(Color.parseColor("#52de97"));//green
                two.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }
            else if(three.getText().toString().trim()==answer)
            {
               // three.setBackgroundColor(Color.parseColor("#52de97"));
               // three.setTextColor(Color.parseColor("#52de97"));//green
                three.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }

            if(mute==false) {
                speaknull();
                String[] con = {"correct"};
                speak(con);
            }
        }
        else
        {
            wrong++;
            if(one.getText().toString().trim()==answer)
            {
               // one.setBackgroundColor(Color.parseColor("#52de97"));
               // one.setTextColor(Color.parseColor("#52de97"));//green
                one.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }
            else if(two.getText().toString().trim()==answer)
            {
               // two.setBackgroundColor(Color.parseColor("#52de97"));
              //  two.setTextColor(Color.parseColor("#52de97"));//green
                two.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }
            else if(three.getText().toString().trim()==answer)
            {
               // three.setBackgroundColor(Color.parseColor("#52de97"));
               // three.setTextColor(Color.parseColor("#52de97"));//green
                three.setBackground(getDrawable(R.drawable.custom_round_button_green));
            }

            if(press.equals("one"))
            {
               // one.setBackgroundColor(Color.parseColor("#b22222"));
              //  one.setTextColor(Color.parseColor("#b22222"));//red
                one.setBackground(getDrawable(R.drawable.custom_round_button_red));
            }
            if(press.equals("two"))
            {
               // two.setBackgroundColor(Color.parseColor("#b22222"));
               // two.setTextColor(Color.parseColor("#b22222"));//red
                two.setBackground(getDrawable(R.drawable.custom_round_button_red));
            }
            if(press.equals("three"))
            {
               // three.setBackgroundColor(Color.parseColor("#b22222"));
              //  three.setTextColor(Color.parseColor("#b22222"));//red
                three.setBackground(getDrawable(R.drawable.custom_round_button_red));
            }

            if(mute==false)
            {
                speaknull();
                String[] con={"wrong"};
                speak(con);
            }

        }

        String w= String.valueOf(wrong);
        String c= String.valueOf(correct);
        wrong1.setText("Wrong : "+w);
        correct1.setText("Correct : "+c);
//	delay();
    }


    public  void get_question()
    {
        counter++;
        speak.setText("Speak");
        speaknull();

        final Random random = new Random();
        id = random.nextInt(content.size()) ;
        result.setText("");

        DatabaseReference reff;

        reff = FirebaseDatabase.getInstance().getReference().child("Question").child(content.get(id).getQuestion_id());

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String quest = dataSnapshot.child("Quest").getValue().toString().trim();
                String opt1 = dataSnapshot.child("opt1").getValue().toString().trim();
                String opt2 = dataSnapshot.child("opt2").getValue().toString().trim();
                String ans = dataSnapshot.child("ans").getValue().toString().trim();


                answer = ans;
                question.setText(counter+"/"+number+"  "+quest);
                int position=random.nextInt(3);
                if (position== 0) {
                /*    one.setBackgroundColor(Color.parseColor("#6200EE"));
                    two.setBackgroundColor(Color.parseColor("#6200EE"));
                    three.setBackgroundColor(Color.parseColor("#6200EE")); */;

                    one.setBackground(getDrawable(R.drawable.custom_round_button));
                    two.setBackground(getDrawable(R.drawable.custom_round_button));
                    three.setBackground(getDrawable(R.drawable.custom_round_button));

           /*         if(currentTheme.equals("default"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.buttonNormal));


                    }

                    else if(currentTheme.equals("lilac"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                    }

                    else if(currentTheme.equals("mint"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                    }

                    else if(currentTheme.equals("sea_blue"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                    }

                    else if(currentTheme.equals("grey"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                    } */

                    one.setText(opt2);
                    two.setText(ans);
                    three.setText(opt1);
                }
                if (position== 1) {
                 /*   one.setBackgroundColor(Color.parseColor("#6200EE"));
                    two.setBackgroundColor(Color.parseColor("#6200EE"));
                    three.setBackgroundColor(Color.parseColor("#6200EE")); */

                    one.setBackground(getDrawable(R.drawable.custom_round_button));
                    two.setBackground(getDrawable(R.drawable.custom_round_button));
                    three.setBackground(getDrawable(R.drawable.custom_round_button));

             /*       if(currentTheme.equals("default"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.buttonNormal));

                    }

                    else if(currentTheme.equals("lilac"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                    }

                    else if(currentTheme.equals("mint"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                    }

                    else if(currentTheme.equals("sea_blue"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                    }

                    else if(currentTheme.equals("grey"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                    } */

                    one.setText(ans);
                    two.setText(opt1);
                    three.setText(opt2);

                }
                if (position== 2) {
                 /*   one.setBackgroundColor(Color.parseColor("#6200EE"));
                    two.setBackgroundColor(Color.parseColor("#6200EE"));
                    three.setBackgroundColor(Color.parseColor("#6200EE")); */

                    one.setBackground(getDrawable(R.drawable.custom_round_button));
                    two.setBackground(getDrawable(R.drawable.custom_round_button));
                    three.setBackground(getDrawable(R.drawable.custom_round_button));

           /*         if(currentTheme.equals("default"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.buttonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.buttonNormal));

                    }

                    else if(currentTheme.equals("lilac"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.lilacButtonNormal));
                    }

                    else if(currentTheme.equals("mint"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.mintButtonNormal));
                    }

                    else if(currentTheme.equals("sea_blue"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.seaBlueButtonNormal));
                    }

                    else if(currentTheme.equals("grey"))
                    {
                        one.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        two.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                        three.setBackgroundColor(getResources().getColor(R.color.greyButtonNormal));
                    } */

                    one.setText(opt2);
                    two.setText(opt1);
                    three.setText(ans);
                }

                image.setImageResource(content.get(id).getImage());
                click=false;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("error");
                result.setText("No internet connection");

            }
        });
    }

    private void speak(String[] show) {

        mTTS.setPitch(1f);
        mTTS.setSpeechRate(1f);
        for(int i=0;i<show.length;i++)
        {
            mTTS.speak(show[i], TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    private void speaknull() {

        mTTS.setPitch(1f);
        mTTS.setSpeechRate(1f);

        mTTS.speak("", TextToSpeech.QUEUE_FLUSH, null, null);

    }

    private void btnToOpenMic() {

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
                    highlightOptionsButton(voiceAsText.get(0));
                }

                break;
        }
    }

    private void highlightOptionsButton(String s) {

        if(s.equalsIgnoreCase("option 1") || s.equalsIgnoreCase("option one") || s.equalsIgnoreCase("one") || s.equalsIgnoreCase("1"))
        {
            speaknull();
            String response=one.getText().toString().trim();
            if(counter==number)
            {  validation(response,"one");
                image.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                //  option1.setVisibility(View.INVISIBLE);
                // option2.setVisibility(View.INVISIBLE);
                // option3.setVisibility(View.INVISIBLE);
                wrong1.setVisibility(View.INVISIBLE);
                correct1.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                String w= String.valueOf(wrong);
                String c= String.valueOf(correct);
                result.setText("Correct : "+c+" Wrong : "+w);

            }
            else{
                if(click==false)
                {click=true;
                    validation(response,"one");
                    delay_quest();
                }
                else
                {
                    Toast toast = null;
                    toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                }

            }
        }

        else if(s.equalsIgnoreCase("option 2") || s.equalsIgnoreCase("option two") || s.equalsIgnoreCase("two") || s.equalsIgnoreCase("2"))
        {
            speaknull();
            String response=two.getText().toString().trim();
            if(counter==number)
            {  validation(response,"two");
                image.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                //	option1.setVisibility(View.INVISIBLE);
                //option2.setVisibility(View.INVISIBLE);
                //	option3.setVisibility(View.INVISIBLE);
                wrong1.setVisibility(View.INVISIBLE);
                correct1.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                String w= String.valueOf(wrong);
                String c= String.valueOf(correct);
                result.setText("Correct : "+c+" Wrong : "+w);

            }
            else{

                if(click==false)
                {click=true;
                    validation(response,"two");
                    delay_quest();
                }
                else
                {
                    Toast toast = null;
                    toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                }
            }

        }

        else if(s.equalsIgnoreCase("option 3") || s.equalsIgnoreCase("option three") || s.equalsIgnoreCase("three") || s.equalsIgnoreCase("3"))
        {
            speaknull();
            String response=three.getText().toString().trim();
            if(counter==number)
            {  validation(response,"three");
                image.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                //	option1.setVisibility(View.INVISIBLE);
                //	option2.setVisibility(View.INVISIBLE);
                //	option3.setVisibility(View.INVISIBLE);
                wrong1.setVisibility(View.INVISIBLE);
                correct1.setVisibility(View.INVISIBLE);
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                String w= String.valueOf(wrong);
                String c= String.valueOf(correct);
                result.setText("Correct : "+c+" Wrong : "+w);

            }
            else{
                if(click==false)
                {click=true;
                    validation(response,"three");
                    delay_quest();
                }
                else
                {
                    Toast toast = null;
                    toast.makeText(TakeTestActivity.this,"Wait",Toast.LENGTH_SHORT).show();

                }
            }
        }
        else if(s.equalsIgnoreCase("main menu") || s.equalsIgnoreCase("back"))
        {
            Intent gotoMainMenu = new Intent(TakeTestActivity.this, MainActivity.class);
            finish();
            startActivity(gotoMainMenu);
        }

        else if(s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("close"))
        {
            finish();
        }

        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT);
            toast.show();
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
            startActivity(new Intent(TakeTestActivity.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_takeTest");
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
        String htmlStringMute = "<b>Mute/Unmute button</b><br><br/>Use this button to mute or unmute question narrator";

        ShowcaseTooltip showcaseTooltipMute = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringMute);

        String htmlStringVoiceCommand = "<b>Voice command options</b><br/><br/>Use the following commands<br/><br/>" +
                "&#8226; 'Option 1'/'One': To select the first option<br/><br/>" +
                "&#8226; 'Option 2'/'Two': To select the second option<br/><br/>" +
                "&#8226; 'Option 3'/'Three': To select the third option<br/><br/>" +
                "&#8226; 'Back'/'Main menu': To go back to main menu<br/><br/>" +
                "&#8226; 'Exit'/'Quit'/'Close': To quit the app";

        ShowcaseTooltip showcaseTooltipVoiceCommand = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringVoiceCommand);

        String htmlStringSpeak = "<b>Speak button</b><br/><br/>Click this button to read the question aloud";

        ShowcaseTooltip showcaseTooltipSpeak = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringSpeak);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "showcase_takeTest");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                .setTarget(sound)
                .setToolTip(showcaseTooltipMute)
                .withCircleShape()
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .build()

        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                .setTarget(speakFab)
                .setToolTip(showcaseTooltipVoiceCommand)
                .withCircleShape()
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                .setTarget(speak)
                .setToolTip(showcaseTooltipSpeak)
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .build()
        );

        sequence.start();
    }
}
