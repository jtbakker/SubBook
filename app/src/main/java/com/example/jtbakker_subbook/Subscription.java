package com.example.jtbakker_subbook;

import java.util.Date;

/**
 * Created by Jacob Bakker on 1/21/2018.
 */

public class Subscription {
    private String name;
    private Date date;
    private double cost; // Monthly charge
    private String comment;

    Subscription(String name, Date date, double cost, String comment) {
        this.name = name; // Limited to 20 characters.
        this.date = date; // yyyy-mm-dd
        this.cost = cost; // Monthly non-negative charge in CAD.
        this.comment = comment; // Can be null, indicating no comment.
        // Limited to 30 characters.
    }

    // Accessor functions for subscriptions.
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public double getCost() {
        return cost;
    }
    public String getComment() {return comment; }

    // Mutator functions for subscriptions.
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public void setComment(String comment) {this.comment = comment;}
}
