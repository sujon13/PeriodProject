package com.example.sujon4002.periodproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.sujon4002.periodproject.create_data.InitialInfoCreateListener;
import com.example.sujon4002.periodproject.create_data.InitialInfoFragment;
import com.example.sujon4002.periodproject.create_data.PeriodData;
import com.example.sujon4002.periodproject.model.Config;
import com.example.sujon4002.periodproject.model.DatabaseQueryClass;
import com.example.sujon4002.periodproject.show_data.ThisMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InitialInfoCreateListener {

    private Button startButton;
    private Button endButton;
    private Button thisMonthButton;
    private Button allMonthButton;
    private String startDate;
    private String endDate;
    private String description;


    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    long relativeTimeOfStartDate=-1;
    long relativeTimeOfEndDate=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isInitialDialogNeeded();


        startButton = findViewById(R.id.start_button_id);
        endButton = findViewById(R.id.end_button_id);
        thisMonthButton = findViewById(R.id.this_month_button_id);
        allMonthButton = findViewById(R.id.all_month_button_id);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final long relativeTimeOfCurrentDate = year*365+month*30+dayOfMonth;

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if(relativeTimeOfStartDate == -1)
                                {

                                    relativeTimeOfStartDate = year*365+month*30+day;
                                    long relativeTimeOfLastEndDate = findingRelativeTimeOfLastEndDate();
                                    if(relativeTimeOfStartDate > relativeTimeOfCurrentDate)
                                    {
                                        relativeTimeOfStartDate = -1;
                                        String message = "Future date can not be selected as start date";
                                        showAlertDialog(message);
                                    }
                                    else if(relativeTimeOfLastEndDate < relativeTimeOfStartDate)
                                    {
                                        startDate = String.valueOf(day)+"-"+String.valueOf(month)+
                                                "-"+String.valueOf(year);
                                        writingStartDateIntoDatabase();
                                    }
                                    else{
                                        relativeTimeOfStartDate = -1;
                                        String message = "start date must be selected after last time's end date";
                                        showAlertDialog(message);
                                    }
                                }
                                else {
                                    String message = "start date is already selected";
                                    showAlertDialog(message);
                                }
                            }
                        },year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final long relativeTimeOfCurrentDate = year*365+month*30+dayOfMonth;

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                relativeTimeOfEndDate = year*365+month*30+day;
                                if(relativeTimeOfEndDate > relativeTimeOfCurrentDate)
                                {
                                    relativeTimeOfEndDate = -1;
                                    String message = "Future date can not be selected as end date";
                                    showAlertDialog(message);
                                }
                                else if(relativeTimeOfStartDate == -1)
                                {
                                    relativeTimeOfEndDate = -1;
                                    String message = "start date is not selected";
                                    showAlertDialog(message);
                                }
                                else if( relativeTimeOfEndDate > relativeTimeOfStartDate) {
                                    Toast.makeText(MainActivity.this, "a " + relativeTimeOfStartDate, Toast.LENGTH_LONG).show();
                                    endDate = String.valueOf(day) + "-" + String.valueOf(month) +
                                            "-" + String.valueOf(year);
                                    writingEndDateIntoDatabase();
                                }
                                else{
                                    showAlertDialog();
                                }
                            }
                        },year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        thisMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThisMonth.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", Config.InitialInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }
    public void writingStartDateIntoDatabase()
    {

        endDate = "0-0-0";
        //if(endDate=="")endDate=null;
        //registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
        //description = descriptionEditText.getText().toString();
        description="";

        PeriodData periodData = new PeriodData(-1, startDate, endDate, description);

        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getApplicationContext());

        long id = databaseQueryClass.insertPeriodInformation(periodData);

        if(id>0){
            periodData.setId(id);
            //periodDataCreateListener.onPeriodDataCreated(periodData);
            //getDialog().dismiss();
        }
        //else showAlertDialog();
    }
    public void writingEndDateIntoDatabase()
    {

        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getApplicationContext());
        long id = databaseQueryClass.getIdByStartDate(startDate);


        PeriodData periodData = new PeriodData(-1, startDate, endDate, description);


        long howManyRowAffected = databaseQueryClass.updatePeriodInformation(id,periodData);

        if(howManyRowAffected>0){
            //periodData.setId(id);
            //periodDataCreateListener.onPeriodDataCreated(periodData);
            //getDialog().dismiss();
        }
        //else showAlertDialog();
        relativeTimeOfStartDate=-1;
        relativeTimeOfEndDate=-1;
    }
    public void showAlertDialog()
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("WARNING!!");
        alertDialog.setMessage("You have chosen wrong date\nEnd date must follows start date");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void showAlertDialog(String message)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("WARNING!!");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void onInitialInfoCreated(String info)
    {
        Config.InitialInfo = info;
    }
    public void showInitialDialog()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        InitialInfoFragment initialInfoFragment = InitialInfoFragment.newInstance("Some Title", MainActivity.this);
        initialInfoFragment.show(fragmentManager, "fragment_initial_info");
    }
    public void isInitialDialogNeeded()
    {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getApplicationContext());
        List<PeriodData>periodDataList = new ArrayList<>();
        periodDataList = databaseQueryClass.getAllPeriodInformation();
        if(periodDataList.isEmpty() && Config.InitialInfo.equals("oh no"))showInitialDialog();
        else{
            Toast.makeText(this, "111", Toast.LENGTH_LONG).show();
        }
    }
    long findingRelativeTimeOfLastEndDate()
    {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getApplicationContext());
        List<PeriodData>periodDataList = new ArrayList<>();
        periodDataList = databaseQueryClass.getAllPeriodInformation();
        if(periodDataList==null)
        {

            Toast.makeText(this, "reza", Toast.LENGTH_LONG).show();
            return -1;
        }
        int sizeOfList = periodDataList.size();
        if(sizeOfList==0)return -1;

        String lastEndDate = periodDataList.get(sizeOfList-1).getEndDate();
        Toast.makeText(this, lastEndDate+222, Toast.LENGTH_LONG).show();
        String[] arrayOfLastEndDate = lastEndDate.split("-");
        int day = Integer.parseInt(arrayOfLastEndDate[0]);
        int month = Integer.parseInt(arrayOfLastEndDate[1]);
        int year =Integer.parseInt(arrayOfLastEndDate[2]);
        return (year*365+month*30+day);



    }

}
