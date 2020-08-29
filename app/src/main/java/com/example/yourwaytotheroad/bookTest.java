package com.example.yourwaytotheroad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import moe.feng.common.view.breadcrumbs.BreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;

public class bookTest extends BaseActivity {

    Toolbar toolbar;
    TextView mTitle;
    ImageButton backBtn;

    EditText name,nic ,address,phone/*,day,month,year*/;
    Button submit;
    DatabaseReference reff;
    long maxid = 0;
    FloatingActionButton speakFab;

    TextView selectDateTextView;
    ImageButton selectDateImageButton;
    DatePickerDialog.OnDateSetListener dateSetListener;

    Dialog dialog;
    TextView errorMessageTextView, successMessageTextView;
    ImageView closePositivePopup, closeNegativePopup;
    Button popUpOkBtn;

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    BreadcrumbsView mBreadcrumbsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test);

        dialog = new Dialog(this);

        toolbar = findViewById(R.id.Toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        setToolbar(toolbar, "Book Test", mTitle);

        backBtn = toolbar.findViewById(R.id.actionBtn);
        backBtn.setImageResource(R.drawable.ic_arrow_back);
        speakFab = findViewById(R.id.speakFab);

        presentShowcaseSequence();

        mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);
        mBreadcrumbsView.setItems(new ArrayList<>(Arrays.asList(
                BreadcrumbItem.createSimpleItem("Main Menu"),
                createItem("Book Test")
        )));

        mBreadcrumbsView.setCallback(new BreadcrumbsCallback<BreadcrumbItem>() {
            @Override
            public void onItemClick(@NonNull BreadcrumbsView breadcrumbsView, int i)
            {
                if(i == 0)
                {
                    Intent gotoMainMenu = new Intent(bookTest.this, MainActivity.class);
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
                Intent gotoMainMenu = new Intent(bookTest.this, MainActivity.class);
                finish();
                startActivity(gotoMainMenu);
            }
        });

        name = findViewById(R.id.name);
        nic = findViewById(R.id.nic);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
      //  day = findViewById(R.id.year);
      //  month = findViewById(R.id.month);
      //  year = findViewById(R.id.year);
        submit = findViewById(R.id.submit);
        selectDateTextView = findViewById(R.id.selectDateTextView);
        selectDateImageButton = findViewById(R.id.selectDateImgButton);

        selectDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(bookTest.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month++;

                String newMonth = "";
                String newDay = "";

                if(dayOfMonth < 10)
                {
                    newDay = "0" + dayOfMonth;
                }
                else
                {
                    newDay = Integer.toString(dayOfMonth);
                }

                if(month < 10)
                {
                    newMonth = "0" + month;
                }

                else
                {
                    newMonth = Integer.toString(month);
                }

                selectDateTextView.setText(newDay + "/" + newMonth + "/" + year);

            }
        };

        nic.addTextChangedListener(new TextValidator(nic) {
            @Override
            public void validate(TextView textView, String text)
            {
                if(!text.isEmpty())
                {
                    if(text.length() != 14)
                    {
                        nic.setError("An NIC number must contain 14 characters.");
                    }

                    else if((Character.isLowerCase(text.charAt(0))) || !(Character.isLetter(text.charAt(0))))
                    {
                        nic.setError("An NIC number must start with an uppercase letter");
                    }

                    else
                    {
                        String dateFromNIC = text.substring(1, 3) + "/" + text.substring(3, 5) + "/" + text.substring(5, 7);

                        if(!isDateValid(dateFromNIC, "dd/MM/yyyy"))
                        {
                            nic.setError("Invalid date of birth in your NIC number");
                        }

                        else
                        {
                            String yearOfBirth = text.substring(5, 7);
                            if(Integer.parseInt(yearOfBirth) > 60)
                            {
                                yearOfBirth = "19" + yearOfBirth;
                            }

                            else
                            {
                                yearOfBirth = "20" + yearOfBirth;
                            }

                            Calendar cal = Calendar.getInstance();
                            int currentYear = cal.get(Calendar.YEAR);

                            if(currentYear - (Integer.parseInt(yearOfBirth)) < 18)
                            {
                                nic.setError("You must be at least 18 years old to apply for the test");
                            }
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().trim().equals("")||nic.getText().toString().trim().equals("")||
                        address.getText().toString().trim().equals("")||phone.getText().toString().trim().equals("") || selectDateTextView.getText().toString().equals("Please select a date"))
                {
                    dialog.setContentView(R.layout.custom_popup_negative);
                    closeNegativePopup = dialog.findViewById(R.id.closeBtn);
                    errorMessageTextView = dialog.findViewById(R.id.errorMessageTextView);
                    popUpOkBtn = dialog.findViewById(R.id.okBtn);

                    errorMessageTextView.setText("All fields are required. Please fill in all fields!");

                    closeNegativePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    popUpOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                else{

                    reff= FirebaseDatabase.getInstance().getReference().child("Member");
                    String name1=name.getText().toString().trim();
                    String Nic1=nic.getText().toString().trim();
                    String address1=address.getText().toString().trim();
                    String phone11=phone.getText().toString().trim();
                    String date= selectDateTextView.getText().toString().trim();
                    final member Member=new member();
                    Member.setAddress(address1);
                    Member.setDate(date);
                    Member.setNIC(Nic1);
                    Member.setName(name1);
                    Member.setPhone(phone11);


                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                                maxid = (dataSnapshot.getChildrenCount());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                    //	reff.child(String.valueOf(maxid +1)).setValue(member);
                    reff.child(String.valueOf(maxid +1)).child("name").setValue(Member.getName());

                    reff.child(String.valueOf(maxid +1)).child("nic").setValue(Member.getNIC());

                    reff.child(String.valueOf(maxid +1)).child("phone").setValue(Member.getPhone());

                    reff.child(String.valueOf(maxid +1)).child("date").setValue(Member.getDate());

                    reff.child(String.valueOf(maxid +1)).child("address").setValue(Member.getAddress());

                    //Form submitted goes down here
                    dialog.setContentView(R.layout.custom_popup_positive);
                    closePositivePopup = dialog.findViewById(R.id.closeBtn);
                    successMessageTextView = dialog.findViewById(R.id.successMessageTextView);
                    popUpOkBtn = dialog.findViewById(R.id.okBtn);

                    successMessageTextView.setText("Congratulations!! Your test has been booked...");

                    closePositivePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    popUpOkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }


        });

        speakFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToOpenMic();
            }
        });

    }

    public void setToolbar(Toolbar toolbar, String title, TextView titleTextView)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleTextView.setText(title);

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
            Intent gotoMainMenu = new Intent(bookTest.this, MainActivity.class);
            finish();
            startActivity(gotoMainMenu);
        }

        else if(s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("close"))
        {
            finish();
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
            startActivity(new Intent(bookTest.this, ChangeTheme.class));
        }
        else if(item.getItemId() == R.id.showHelp)
        {
            MaterialShowcaseView.resetSingleUse(this, "showcase_voice_command_book_test");
            presentShowcaseSequence();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isDateValid(String dateToValidate, String dateFormat)
    {
        if(dateToValidate == null)
        {
            return false;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(false);

        try
        {
            simpleDateFormat.parse(dateToValidate);
        }

        catch(Exception e)
        {
            return false;
        }

        return true;
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
                "&#8226; 'Back'/'Main menu': To go back to main menu<br/><br/>" +
                "&#8226; 'Exit'/'Quit'/'Close': To quit the app";

        ShowcaseTooltip showcaseTooltipVoiceCommand = ShowcaseTooltip.build(this)
                .corner(30)
                .textSize(1, 16)
                .textColor(getResources().getColor(R.color.content_text_color))
                .text(htmlStringVoiceCommand);

        new MaterialShowcaseView.Builder(this)
                .setTarget(speakFab)
                .setToolTip(showcaseTooltipVoiceCommand)
                .setDismissOnTouch(true)
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .singleUse("showcase_voice_command_book_test")
                .show();
    }
}
