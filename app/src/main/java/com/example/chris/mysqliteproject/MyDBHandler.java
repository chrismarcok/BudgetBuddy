package com.example.chris.mysqliteproject;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "entries.db";
    public static final String TABLE_ENTRIES = "entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VALUE = "_value";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_DETAILS = "_details";
    public static final String COLUMN_LOCATION = "_location";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ENTRIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_VALUE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_LOCATION + " TEXT" +
                ");";
        //CREATE TABLE entries (_id INTEGER PRIMARY KEY AUTOINCREMENT, _name TEXT);
        db.execSQL(query);

        //db.execSQL("CREATE TABLE entries (_id INTEGER PRIMARY KEY AUTOINCREMENT, _name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        //db.execSQL("DROP TABLE IF EXISTS entries;");
        onCreate(db);
    }

    public void addEntry(Entry entry){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entry.get_id());
        values.put(COLUMN_VALUE, entry.get_value());
        values.put(COLUMN_DATE, entry.get_date().toString());
        values.put(COLUMN_DETAILS, entry.get_details());
        values.put(COLUMN_LOCATION, entry.get_location());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ENTRIES, null, values);
        db.close();
//        SQLiteDatabase db = getWritableDatabase();
//        String name = entry.get_name();
//        db.execSQL("INSERT INTO entries (_name) VALUES (\"" + name + "\");");
    }

    public void deleteEntry(int entryId){
        SQLiteDatabase db = getWritableDatabase();
        //DELETE FROM entries WHERE _name = "insertnamehere";
        db.execSQL("DELETE FROM "+ TABLE_ENTRIES + " WHERE " + COLUMN_ID + " = \"" + entryId + "\";");
//        db.execSQL("DELETE FROM entries WHERE _name = " + "\"" + entryName + "\";");
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ENTRIES + ";";
        //String query = "SELECT * FROM entries;";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_VALUE)) != null){
                dbString += c.getString(c.getColumnIndex(COLUMN_ID)) + ". ";
                dbString += c.getString(c.getColumnIndex(COLUMN_VALUE)) + "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }
}
