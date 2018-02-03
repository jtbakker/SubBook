package com.example.jtbakker_subbook;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jacob Bakker on 1/21/2018.
 */

public class Subscription implements Serializable{
    private String name = "";
    private Date date;
    private double cost; // Monthly charge
    private String comment = "";
    private static final DateFormat subDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
    private static final String subCostFormat = "%.02f";

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

    public String dateToString() {
        return subDateFormat.format(date);
    }

    public String costToString() {
        return String.format(Locale.CANADA, subCostFormat, cost);
    }
}
