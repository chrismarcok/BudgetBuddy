package com.something.chris.mysqliteproject;


import java.util.Date;

public class Entry {

    private int _id;
    private Date _date;
    private String _repeat;
    private float _value;
    private Tag _tag;
    public boolean _repeated;

    public Entry(int id, float value, Date date, Tag tag, String repeat, boolean repeated){
        this._id = id;
        this._value = value;
        this._date = date;
        this._tag = tag;
        this._repeat = repeat;
        this._repeated = repeated;
    }
    public Entry(float value, Date date, Tag tag, String repeat, boolean repeated){
        this._value = value;
        this._date = date;
        this._tag = tag;
        this._repeat = repeat;
        this._repeated = repeated;
    }

    public Entry(String e){

    }

    public boolean is_repeated() {
        return _repeated;
    }

    public void set_repeated(boolean _repeated) {
        this._repeated = _repeated;
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

    public String get_repeat() {
        return _repeat;
    }

    public void set_repeat(String _repeat) {
        this._repeat = _repeat;
    }

    public void set_value(float _value) {
        this._value = _value;
    }
}
