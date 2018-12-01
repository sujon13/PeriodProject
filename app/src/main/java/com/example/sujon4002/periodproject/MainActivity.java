package com.example.sujon4002.periodproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.sujon4002.periodproject.create_data.PeriodData;
import com.example.sujon4002.periodproject.model.DatabaseQueryClass;
import com.example.sujon4002.periodproject.show_data.ThisMonth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                relativeTimeOfStartDate = year*365+month*30+day;
                                startDate = String.valueOf(day)+"-"+String.valueOf(month)+
                                        "-"+String.valueOf(year);
                                writingStartDateIntoDatabase();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                relativeTimeOfEndDate = year*365+month*30+day;
                                if( (relativeTimeOfEndDate > relativeTimeOfStartDate)&& relativeTimeOfStartDate>-1)
                                {
                                    endDate = String.valueOf(day)+"-"+String.valueOf(month)+
                                            "-"+String.valueOf(year);
                                    writingEndDateIntoDatabase();
                                }
                                else if(relativeTimeOfStartDate == -1)
                                {
                                    String message = "start date is not selected";
                                    showAlertDialog(message);
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
                startActivity(intent);
            }
        });



    }
    public void writingStartDateIntoDatabase()
    {

        endDate = "input is not given yet";
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

}
