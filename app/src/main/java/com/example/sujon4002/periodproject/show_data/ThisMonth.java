package com.example.sujon4002.periodproject.show_data;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujon4002.periodproject.R;
import com.example.sujon4002.periodproject.create_data.PeriodData;
import com.example.sujon4002.periodproject.helper_class.Calculator;
import com.example.sujon4002.periodproject.model.Config;
import com.example.sujon4002.periodproject.model.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class ThisMonth extends AppCompatActivity {

    long averageCycleTime;
    List<PeriodData> periodDataList = new ArrayList<>();
    Calculator calculator;
    private TextView textView1;
    private TextView textView2;

    private  String startDate;
    private  String endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_month);
        averageCycleTime = calculatingAverageCycleTime();

        textView1 = findViewById(R.id.text_view_1);
        textView2 = findViewById(R.id.text_view_2);

        getThisMonthDates();

        textView1.setText(startDate+"---"+endDate);
        textView2.setText("your average cycle time is: "+averageCycleTime);


    }
    void getThisMonthDates()
    {
        if(periodDataList==null)
        {
            startDate="No input";
            endDate = "No input";
            return;
        }
        int sizeOfList = periodDataList.size();
        if(sizeOfList == 0){
            startDate="No input";
            endDate = "No input";
            return;

        }
        startDate = periodDataList.get(sizeOfList-1).getStartDate();
        endDate = periodDataList.get(sizeOfList-1).getEndDate();
    }
    long calculatingAverageCycleTime()
    {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getApplicationContext());
        periodDataList = databaseQueryClass.getAllPeriodInformation();
        if(periodDataList==null)
        {
            String[] arrayOfString = Config.InitialInfo.split("-");
            return  Integer.parseInt(arrayOfString[1]);//cycle time
        }
        int sizeOfList = periodDataList.size();
        if(sizeOfList<=1){
            String[] arrayOfString = Config.InitialInfo.split("-");
            return Integer.parseInt(arrayOfString[1]);//cycle time

        }
        calculator = new Calculator();
        for(int i=1;i<periodDataList.size();i++)
        {
            String dateBefore = modifyDateForSubtraction( periodDataList.get(i-1).getStartDate());
            String dateAfter = modifyDateForSubtraction( periodDataList.get(i).getStartDate());
            calculator.setDates(dateBefore,dateAfter);
            averageCycleTime+=calculator.differenceBetweenTwoDates();
        }
        averageCycleTime/=(sizeOfList-1);
        return averageCycleTime;
    }
    String modifyDateForSubtraction(String date)
    {
        String[] array = date.split("-");
        String day = array[0];
        String month = String.valueOf(Integer.parseInt(array[1])+1 );
        String year = array[2];
        return (day+"-"+month+"-"+year);

    }

}
