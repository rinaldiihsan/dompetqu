package com.example.dompetq.models;

public class Transaction {
    private long id;
    private String type;  // "INCOME" atau "EXPENSE"
    private double amount;
    private String category;
    private String note;
    private String date;

    // Constructor kosong
    public Transaction() {
    }

    // Getters and Setters
    public int getId() {
        return (int) id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}