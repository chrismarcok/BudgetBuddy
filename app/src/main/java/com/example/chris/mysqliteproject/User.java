package com.example.chris.mysqliteproject;

public class User {

    private String firstName;
    private String lastName;
    private boolean saveMoney;
    private String timePeriod;
    private float budget;


    public User(String first, String last, boolean save, String time, float budget){
        firstName = first;
        lastName = last;
        saveMoney = save;
        timePeriod = time;
        this.budget = budget;
    }

    public User(){

    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isSaveMoney() {
        return saveMoney;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSaveMoney(boolean saveMoney) {
        this.saveMoney = saveMoney;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}
