package com.something.chris.mysqliteproject;

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
    public static final String COLUMN_REPEAT = "_repeat";
    public static final String COLUMN_HAS_REPEATED = "_repeated";
//    public static final String COLUMN_DETAILS = "_details";
//    public static final String COLUMN_LOCATION = "_location";
    public static final String COLUMN_TAG_ID = "_tag";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    public static final DateFormat DATE_FORMAT_NO_SEC = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    public static final DateFormat DATE_FORMAT_NO_TIME = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DATE_FORMAT_NO_TIME_SPACES = new SimpleDateFormat("yyyy MM dd");
    public static final DateFormat DATE_FORMAT_NO_TIME_SLASHES = new SimpleDateFormat("yyyy/MM/dd");
    public static final DateFormat DATE_FORMAT_CALENDAR = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
    public static final DateFormat DATE_FORMAT_LOGS = new SimpleDateFormat("E, MMM dd - hh:mm a");
    public static final DateFormat DATE_FORMAT_TIME = new SimpleDateFormat("hh:mm a");

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void checkAndUpdateTable(){
        SQLiteDatabase db = getWritableDatabase();
        if (!isFieldExist(db, "entries", "_repeat")){
            db.execSQL("ALTER TABLE " + TABLE_ENTRIES + " ADD COLUMN " + COLUMN_REPEAT + " TEXT;");
            db.execSQL("UPDATE " + TABLE_ENTRIES + " SET " + COLUMN_REPEAT + " = \"" + "Never" + "\";");
        }
        if (!isFieldExist(db, "entries", "_repeated")){
            db.execSQL("ALTER TABLE " + TABLE_ENTRIES + " ADD COLUMN " + COLUMN_HAS_REPEATED + " INTEGER;");
            db.execSQL("UPDATE " + TABLE_ENTRIES + " SET " + COLUMN_HAS_REPEATED + " = " + 0 + ";");
        }
        db.close();
    }

    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName)
    {
        boolean isExist = false;

        Cursor res = null;

        try {

            res = db.rawQuery("Select * from "+ tableName +" limit 1", null);

            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex!=-1){
                isExist = true;
            }

        } catch (Exception e) {
        } finally {
            try { if (res !=null){ res.close(); } } catch (Exception e1) {}
        }

        return isExist;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ENTRIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VALUE + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TAG_ID + " INTEGER, " +
                COLUMN_REPEAT + " TEXT, " +
                COLUMN_HAS_REPEATED + " INTEGER" +
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
        values.put(COLUMN_TAG_ID, entry.get_tag().getId());
        values.put(COLUMN_REPEAT, entry.get_repeat());
        values.put(COLUMN_HAS_REPEATED, entry.is_repeated()? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
    }

    public void deleteEntry(int entryId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_ENTRIES + " WHERE " + COLUMN_ID + " = \"" + entryId + "\";");
        db.close();
    }


    public void updateEntry(Entry e){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_ENTRIES + " SET " +
                COLUMN_VALUE + " = " + e.get_value() + ", " +
                COLUMN_REPEAT + " = \"" + e.get_repeat() + "\", " +
                COLUMN_HAS_REPEATED + " = " + (e.is_repeated()? 1 : 0) + ", " +
                COLUMN_DATE + " = \"" + DATE_FORMAT.format(e.get_date()) + "\", " +
                COLUMN_TAG_ID + " = \"" + e.get_tag().getId() + "\"" +
                " WHERE " + COLUMN_ID + " = " + e.get_id() +";");
        db.close();
    }


    public void fetchDatabaseEntries(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ENTRIES + ";";

        Cursor c = db.rawQuery(query, null);
        HomeActivity.entries.clear();
        c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_VALUE)) != null){

                String strDate =  c.getString(c.getColumnIndex(COLUMN_DATE));

                Date resultDate = new Date(1L);
                int tagId = c.getInt(c.getColumnIndex(COLUMN_TAG_ID));
                try {
                    resultDate = DATE_FORMAT.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Tag t = HomeActivity.tags.get(0);
                for (int i = 0; i < HomeActivity.tags.size(); i++){
                    if (tagId == HomeActivity.tags.get(i).getId()){
                        t = HomeActivity.tags.get(i);
                    }
                }

                Entry e = new Entry(c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getFloat(c.getColumnIndex(COLUMN_VALUE)), resultDate, t,
                        c.getString(c.getColumnIndex(COLUMN_REPEAT)),
                        (c.getInt(c.getColumnIndex(COLUMN_HAS_REPEATED)) == 1));

                HomeActivity.entries.add(e);

            }
            c.moveToNext();
        }
        c.close();
        db.close();
    }

}
