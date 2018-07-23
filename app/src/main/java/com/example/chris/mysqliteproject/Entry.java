package com.example.chris.mysqliteproject;

import java.util.Date;

public class Entry {
    private static int _numOfEntries = 0;
    private int _id;
    private String _details;
    private Date _date;
    private String _location;
    private float _value;


    public Entry(float value, Date date, String details, String location){
        this._value = value;
        this._date = date;
        if (details.equals(null)){
            this._details = "";
        }
        else {
            this._details = details;
        }
        if (location.equals(null)){
            this._location = "";
        }
        else {
            this._location = location;
        }
        this._id = _numOfEntries;
        _numOfEntries++;
    }

    public Entry(String e){

    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public String get_details() {
        return _details;
    }

    public Date get_date() {
        return _date;
    }

    public String get_location() {
        return _location;
    }

    public float get_value() {
        return _value;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public void set_value(float _value) {
        this._value = _value;
    }
}
