package com.namutomatvey.financialaccount.dto;

public class Currency {
    private long id;
    private String name;
    private String short_name;
    private double coefficient;


    public Currency(long id, String name, String short_name, double coefficient) {
        this.id = id;
        this.name = name;
        this.short_name = short_name;
        this.coefficient = coefficient;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return short_name;
    }

    public double getCoefficient() {
        return coefficient;
    }
}