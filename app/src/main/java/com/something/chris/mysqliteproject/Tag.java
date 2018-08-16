package com.something.chris.mysqliteproject;

import android.graphics.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tag {

    private int _id;
    private String _col;
    private String _text;
    private ArrayList<String> _aliases = new ArrayList<>();

    public Tag(int id, String col, String text){
        _id = id;
        _text = text;
        _col = col;
    }
    public Tag(String col, String text){
        _text = text;
        _col = col;
    }

    public Tag(){

    }

    public String getCol() {
        return _col;
    }

    public void setCol(String col) {
        this._col = col;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
}
