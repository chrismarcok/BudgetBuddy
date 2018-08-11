package com.example.chris.mysqliteproject;


import java.util.Date;

public class Entry {

    private int _id;

    private Date _date;

    private float _value;
    private Tag _tag;


    public Entry(int id, float value, Date date, Tag tag){
        this._id = id;
        this._value = value;
        this._date = date;
        this._tag = tag;
    }
    public Entry(float value, Date date, Tag tag){
        this._value = value;
        this._date = date;
        this._tag = tag;
    }

    public Entry(String e){

    }

    public Tag get_tag() {
        return _tag;
    }

    public void set_tag(Tag tag) {
        this._tag = tag;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public Date get_date() {
        return _date;
    }

    public float get_value() {
        return _value;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_value(float _value) {
        this._value = _value;
    }
}
