package com.example.sujon4002.periodproject.helper_class;

import android.util.Log;
import android.widget.Toast;

import com.example.sujon4002.periodproject.MainActivity;
import com.example.sujon4002.periodproject.show_data.ThisMonth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Calculator {
    private String dateBefore;
    private String dateAfter;
    public Calculator()
    {

    }
    public void setDates(String dateBefore,String dateAfter)
    {
        this.dateAfter = dateAfter;
        this.dateBefore = dateBefore;
    }
    public long differenceBetweenTwoDates()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy");

        //Parsing the date
        long diff = 0;
        try{
            Date oldDate = dateFormat.parse(dateBefore);
            Date newDate = dateFormat.parse(dateAfter);

            //calculating number of days in between
            diff = newDate.getTime() - oldDate.getTime();
            diff = (diff)/(1000*3600*24);// in days
        }
        catch (ParseException e)
        {
            //Toast.makeText(ThisMonth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return diff;
    }
}
