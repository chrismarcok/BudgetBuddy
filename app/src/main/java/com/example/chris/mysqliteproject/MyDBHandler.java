package com.example.chris.mysqliteproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "entries.db";
    public static final String TABLE_ENTRIES = "entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VALUE = "_value";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_DETAILS = "_details";
    public static final String COLUMN_LOCATION = "_location";
    public static final String COLUMN_TAG_ID = "_tag";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    public static final DateFormat DATE_FORMAT_NO_SEC = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    public static final DateFormat DATE_FORMAT_NO_TIME = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DATE_FORMAT_NO_TIME_SPACES = new SimpleDateFormat("yyyy MM dd");
    public static final DateFormat DATE_FORMAT_NO_TIME_SLASHES = new SimpleDateFormat("yyyy/MM/dd");
    public static final DateFormat DATE_FORMAT_CALENDAR = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ENTRIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VALUE + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_TAG_ID + " INTEGER" +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    public void addEntry(Entry entry){

        ContentValues values = new ContentValues();
        values.put(COLUMN_VALUE, entry.get_value());
        values.put(COLUMN_DATE, DATE_FORMAT.format(entry.get_date()));
        values.put(COLUMN_DETAILS, entry.get_details());
        values.put(COLUMN_LOCATION, entry.get_location());
        values.put(COLUMN_TAG_ID, entry.get_tag().getId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
    }

    public void deleteEntry(int entryId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_ENTRIES + " WHERE " + COLUMN_ID + " = \"" + entryId + "\";");
        db.close();
    }

    public void dequeue(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_ENTRIES + " WHERE " + COLUMN_ID + " = (SELECT min(" + COLUMN_ID +  ") FROM " + TABLE_ENTRIES + ");");
        db.close();

    }

    public void updateEntry(Entry e){
        SQLiteDatabase db = getWritableDatabase();
        //UPDATE entries SET _id = 123 WHERE _id = 0;
        db.execSQL("UPDATE " + TABLE_ENTRIES + " SET " +
                COLUMN_VALUE + " = " + e.get_value() + ", " +
                COLUMN_DATE + " = \"" + DATE_FORMAT.format(e.get_date()) + "\", " +
                COLUMN_LOCATION + " = \"" + e.get_location() + "\", " +
                COLUMN_DETAILS + " = \"" + e.get_details() + "\", " +
                COLUMN_TAG_ID + " = \"" + e.get_tag().getId() + "\"" +
                " WHERE " + COLUMN_ID + " = " + e.get_id() +";");
        db.close();
    }


    public String databaseToString(){

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ENTRIES + ";";


        Cursor c = db.rawQuery(query, null);
        HomeActivity.entries.clear();
        c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_VALUE)) != null){

                String strDate =  c.getString(c.getColumnIndex(COLUMN_DATE));

                Date resultDate = new Date(1L);

                try {
                    resultDate = DATE_FORMAT.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Entry e = new Entry(c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getFloat(c.getColumnIndex(COLUMN_VALUE)), resultDate,
                        c.getString(c.getColumnIndex(COLUMN_LOCATION)), c.getString(c.getColumnIndex(COLUMN_DETAILS)));

                dbString += e.get_id() + ". ";
                dbString += DATE_FORMAT.format(e.get_date()) + ", ";
                dbString += "$" + String.format("%.2f", e.get_value()) + "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public void fetchDatabaseEntries(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ENTRIES + ";";

        Cursor c = db.rawQuery(query, null);
        HomeActivity.entries.clear();
        c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_VALUE)) != null){

                String strDate =  c.getString(c.getColumnIndex(COLUMN_DATE));

                Date resultDate = new Date(1L);

                try {
                    resultDate = DATE_FORMAT.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Entry e = new Entry(c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getFloat(c.getColumnIndex(COLUMN_VALUE)), resultDate,
                        c.getString(c.getColumnIndex(COLUMN_LOCATION)), c.getString(c.getColumnIndex(COLUMN_DETAILS)));

                HomeActivity.entries.add(e);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
    }

}
