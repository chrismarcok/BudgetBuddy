package com.example.chris.mysqliteproject;

public class Entry {
    private int _id;
    private String _name;

    public Entry(String name){
        this._name = name;
    }

    public Entry(){
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }
}
