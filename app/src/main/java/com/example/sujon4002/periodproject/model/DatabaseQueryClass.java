package com.example.sujon4002.periodproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;


import com.example.sujon4002.periodproject.create_data.PeriodData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        //Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertPeriodInformation(PeriodData data){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PERIOD_START_DATE,data.getStartDate());
        contentValues.put(Config.COLUMN_PERIOD_END_DATE, data.getEndDate());
        contentValues.put(Config.COLUMN_PERIOD_DESCRIPTION, data.getDescription());

        try{
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_PERIOD_INFORMATION, null, contentValues);
        } catch (SQLiteException e){
            //Logger.d("Exception: " + e.getMessage());
            //Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }
        return id;
    }
    public long updatePeriodInformation(long id,PeriodData data){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PERIOD_START_DATE,data.getStartDate());
        contentValues.put(Config.COLUMN_PERIOD_END_DATE, data.getEndDate());
        contentValues.put(Config.COLUMN_PERIOD_DESCRIPTION, data.getDescription());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_PERIOD_INFORMATION, contentValues,
                    Config.COLUMN_PERIOD_ID + " = ? ",
                    new String[] {String.valueOf(id)});
        } catch (SQLiteException e){
            //Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage()+22, Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }
    public List<PeriodData> getAllPeriodInformation(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PERIOD_INFORMATION, null, null, null, null, null, null, null);


            //If you want to execute raw query then uncomment below 2 lines. And comment out above line.

            //String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
            //cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);


            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<PeriodData> periodDataList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERIOD_ID));
                        String startDate = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERIOD_START_DATE));
                        String endDate = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERIOD_END_DATE));
                        String description = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PERIOD_DESCRIPTION));

                        periodDataList.add(new PeriodData(id, startDate,endDate, description) );
                    }   while (cursor.moveToNext());
                    //Toast.makeText(context, "is empty: "+importantDataList.size(), Toast.LENGTH_SHORT).show();

                    return periodDataList;
                }

        } catch (Exception e){
            //Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();

            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public long getIdByStartDate(String date){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        long id = -1;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PERIOD_INFORMATION, null,
                    Config.COLUMN_PERIOD_START_DATE + " = ? ", new String[]{date},
                    null, null, null);


             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

             //String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_IMPORTANT_INFORMATION, Config.COLUMN_NAME, String.valueOf(Name));
             //cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);


            if(cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PERIOD_ID));
            }
        } catch (Exception e){
            //Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return id;
    }


    /*public long deleteStudentByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_STUDENT,
                    Config.COLUMN_STUDENT_REGISTRATION + " = ? ",
                    new String[]{ String.valueOf(registrationNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }*/

    /*public boolean deleteAllStudents(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_IMPORTANT_INFORMATION, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_IMPORTANT_INFORMATION);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }*/

}
