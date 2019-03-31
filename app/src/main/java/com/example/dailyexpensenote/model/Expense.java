package com.example.dailyexpensenote.model;

public class Expense {
    private int expenseId;
    private String expenseType;
    private long expnseDate;
    private long expenseTime;
    private double expenseAmount;
    private String expenseDocument;

    public Expense(String expenseType, long expnseDate, long expenseTime, double expenseAmount, String expenseDocument) {
        this.expenseType = expenseType;
        this.expnseDate = expnseDate;
        this.expenseTime = expenseTime;
        this.expenseAmount = expenseAmount;
        this.expenseDocument = expenseDocument;
    }

  //Setter Methods


    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public void setExpnseDate(long expnseDate) {
        this.expnseDate = expnseDate;
    }

    public void setExpenseTime(long expenseTime) {
        this.expenseTime = expenseTime;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public void setExpenseDocument(String expenseDocument) {
        this.expenseDocument = expenseDocument;
    }

    //Getter Methods


    public int getExpenseId() {
        return expenseId;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public long getExpnseDate() {
        return expnseDate;
    }

    public long getExpenseTime() {
        return expenseTime;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseDocument() {
        return expenseDocument;
    }
}
