package com.namutomatvey.financialaccount.dto;

public class ViewCategory {
    private long category_id;
    private double amount;
    private String category;


    public ViewCategory(long id, String category, double amount) {
        this.category_id = id;
        this.amount = amount;
        this.category = category;
    }

    public long getId() {
        return category_id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

}
