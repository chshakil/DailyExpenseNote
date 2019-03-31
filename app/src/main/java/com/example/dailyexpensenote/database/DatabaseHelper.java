package com.example.dailyexpensenote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "expensedb";
    private static String TABLE_NAME = "expense";
    private static int VERSION = 1;

    public static String COL_ID = "Id";
    public static String COL_TYPE = "ExpType";
    public static String COL_DATE = "ExpDate";
    public static String COL_TIME = "ExpTime";
    public static String COL_IMG = "ExpImage";
    public static String COL_AMOUNT = "ExpAmount";

    private String createSQl = "CREATE TABLE " + TABLE_NAME + " (Id integer primary key, ExpType TEXT, ExpDate Real, ExpTime Real, ExpImage TEXT, ExpAmount Real )";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSQl);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertData(String expTyep, double expAmount, long expDate, long expTime, String expImage)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE,expTyep);
        contentValues.put(COL_AMOUNT,expAmount);
        contentValues.put(COL_DATE,expDate);
        contentValues.put(COL_TIME,expTime);
        contentValues.put(COL_IMG,expImage);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public Cursor showData(){
        return getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME,null);
    }

    public Cursor showData(String expTyepInput,long fromDate, long toDate){
        String sql1 = "Select * from "+TABLE_NAME+" where ExpType = '"+expTyepInput+"' AND ExpDate between '"+fromDate+"' AND '"+toDate+"' ";
        return getReadableDatabase().rawQuery(sql1,null);
    }

    //-------------show 'date' and 'time' from database-------------------
    public Cursor showData(long fromDate, long toDate){
        String sql1 = "Select * from "+TABLE_NAME+" where ExpDate between '"+fromDate+"' AND '"+toDate+"' ";
        return getReadableDatabase().rawQuery(sql1,null);
    }

    //-------------show expTypeInput from database-------------------
    public Cursor showData(String expTypeInput){
        Log.d("Check", "showData: "+expTypeInput);
        String sql1 = "Select * from "+TABLE_NAME+" Where ExpType = '"+expTypeInput+"'";
        return getReadableDatabase().rawQuery(sql1,null);
    }

    //----------------Query from database--------------------
    public  Cursor getAmount(String expTypeInput,long fromDate, long toDate) {
        String sql1;
        if (expTypeInput.equals("")) {
            sql1= "Select SUM(ExpAmount) AS sumAmount from " + TABLE_NAME + " where  ExpDate between '" + fromDate + "' AND '" + toDate + "' ";
        } else {
            sql1 = "Select SUM(ExpAmount) AS sumAmount from " + TABLE_NAME + " where ExpType = '" + expTypeInput + "' AND ExpDate between '" + fromDate + "' AND '" + toDate + "' ";
        }


        return getReadableDatabase().rawQuery(sql1, null);
    }


    //----------------Delete Method------------------
    public  int  deleteData(int Id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int id =  sqLiteDatabase.delete(TABLE_NAME,"Id=?",new String[]{String.valueOf(Id)});
        sqLiteDatabase.close();
        return id;
    }


    //-----------------Update method--------------------
    public  int  updateData(int id, String expTyep, double expAmount, long expDate, long expTime, String expImage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE,expTyep);
        contentValues.put(COL_AMOUNT,expAmount);
        contentValues.put(COL_DATE,expDate);
        contentValues.put(COL_TIME,expTime);
        contentValues.put(COL_IMG,expImage);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int upid = sqLiteDatabase.update(TABLE_NAME,contentValues,"Id=?",new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return  upid;
    }
}
