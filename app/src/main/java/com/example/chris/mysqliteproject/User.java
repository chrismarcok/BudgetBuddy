package com.example.chris.mysqliteproject;

import java.util.Date;

public class User {

    private String firstName;
    private String lastName;
    private boolean saveMoney;
    private String timePeriod;
    private float budget;
    private Date appSetupDate;
    private Date currentBudgetStartDate;
    private Date nextBudgetStartDate;


    public User(String first, String last, boolean save, String time, float budget, Date appSetup, Date currentBudgetDate, Date nextBudgetDate){
        firstName = first;
        lastName = last;
        saveMoney = save;
        timePeriod = time;
        this.budget = budget;
        appSetupDate = appSetup;
        currentBudgetStartDate = currentBudgetDate;
        nextBudgetStartDate = nextBudgetDate;
    }

    public User(){

    }

    public Date getAppSetupDate() {
        return appSetupDate;
    }

    public void setAppSetupDate(Date appSetupDate) {
        this.appSetupDate = appSetupDate;
    }

    public Date getCurrentBudgetStartDate() {
        return currentBudgetStartDate;
    }

    public void setCurrentBudgetStartDate(Date currentBudgetStartDate) {
        this.currentBudgetStartDate = currentBudgetStartDate;
    }

    public Date getNextBudgetStartDate() {
        return nextBudgetStartDate;
    }

    public void setNextBudgetStartDate(Date nextBudgetStartDate) {
        this.nextBudgetStartDate = nextBudgetStartDate;
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
