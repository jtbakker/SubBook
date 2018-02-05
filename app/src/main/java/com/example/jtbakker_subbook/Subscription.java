/**
 * ClassName: Subscription
 *
 * Date: February 4, 2018
 *
 * [INSERT COPYRIGHT]
 */

package com.example.jtbakker_subbook;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jacob Bakker
 *
 * This class implements a basic Subscription object containing its name, date created,
 * cost, and optional comment.
 *  <p>
 *      The Serializable interface is used to allow Subscription objects to be passed between
 *      activities through intents.
 *  </p> <p>
 *      Since Subscription attributes are frequently displayed as text, both the date and cost
 *      have methods that return formatted strings.
 *  </p>
 */
public class Subscription implements Serializable {
    private String name;
    private Date date;
    private double cost;
    private String comment;
    private static final int NAME_MAX_LENGTH = 20;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    private static final String COST_FORMAT = "%.02f";
    private static final int COMMENT_MAX_LENGTH = 30;

    /**
     * This constructor is intended to be used when creating Subscription objects in the
     * SubEntryScreen, where user-inputted strings are passed as-is from the text entry views
     * into a new Subscription object.
     * <p>
     *     Each attribute is checked for correctness using a series of validation methods which
     *     throw SubFieldExceptions containing appropriate error messages if an attribute is
     *     invalid. These validation methods also return the attribute in its correct datatype
     *     if no errors are encountered. If an exception occurs, the constructor will also
     *     throw that exception.
     * </p>
     * @param name
     * @param date
     * @param cost
     * @param comment
     * @throws SubFieldException
     */
    Subscription(String name, String date, String cost, String comment) throws SubFieldException {
        try {
            this.name = validateName(name);
            this.date = validateDate(date);
            this.cost = validateCostString(cost);
            this.comment = validateComment(comment);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }

    // Accessor functions for Subscription attributes.
    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public double getCost() {
        return cost;
    }
    public String getComment() {
        return comment;
    }

    // Returns string for date attribute formatted to yyyy-MM-dd
    public String dateToString() {
        return DATE_FORMAT.format(date);
    }
    // Returns string for cost up to 2 decimal places.
    public String costToString() {
        return String.format(Locale.CANADA, COST_FORMAT, cost);
    }

    /**
     * Each mutator function first validates the input attribute before modifying the object.
     * If a SubFieldException occurs, the mutators will also throw that exception.
     * <p>
     *     Even though cost and date are only stored as double and Date values respectively,
     *     strings can instead be entered as input. These strings will be validated for
     *     correctness before being converted to their appropriate datatype.
     * </p>
     */
    public void setName(String name) throws SubFieldException {
        try {
            this.name = validateName(name);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }
    public void setDate(Date date) throws SubFieldException {
        try {
            String stringDate = date.toString();
            this.date = validateDate(stringDate);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }
    public void setDateString(String date) throws SubFieldException {
        try {
            this.date = validateDate(date);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }
    public void setCostString(String cost) throws SubFieldException {
        try {
            this.cost = validateCostString(cost);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }
    public void setCostDouble(double cost) throws SubFieldException {
        try {
            this.cost = validateCostDouble(cost);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }
    public void setComment(String comment) throws SubFieldException {
        try {
            this.comment = validateComment(comment);
        } catch (SubFieldException sfe) {
            throw sfe;
        }
    }

    /**
     * Validation functions check whether an input attribute is legal before returning that
     * value in its correct datatype according to the Subscription object attributes
     * (e.g. cost is returned as a double regardless of whether the input was a double or string).
     * If an input attribute is illegal, a SubFieldException with an error message describing the
     * issue is thrown. This error message is intended to be displayed to the user when entering
     * new Subscription values.
     * Legal attribute values are defined as follows:
     *      - Names cannot be empty or longer than 20 characters.
     *      - Dates must be in the YYYY-MM-DD format.
     *      - Cost must be a non-negative decimal value.
     *      - Comments cannot be longer than 30 characters.
     */
    private String validateName(String name) throws SubFieldException {
        SubFieldException sfe = new SubFieldException();
        if (name.length() > NAME_MAX_LENGTH) {
            sfe.setMessage(String.format(Locale.ENGLISH, "Name cannot be longer than %d characters.", NAME_MAX_LENGTH));
            throw sfe;
        } else if (name.length() == 0) {
            sfe.setMessage("Name cannot be left empty.");
            throw sfe;
        }
        return name;
    }
    private Date validateDate(String date) throws SubFieldException {
        SubFieldException sfe = new SubFieldException();
        if (date.length() == 0) {
            sfe.setMessage("Date cannot be left empty.");
            throw sfe;
        }
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            sfe.setMessage("Date must be in YYYY-MM-DD format.");
            throw sfe;
        }
    }
    private double validateCostString(String cost) throws SubFieldException {
        SubFieldException sfe = new SubFieldException();
        if (cost.length() == 0) {
            sfe.setMessage("Monthly charge cannot be left empty.");
            throw sfe;
        }
        try {
            double formattedCost = Double.parseDouble(cost);
            validateCostDouble(formattedCost);
            return formattedCost;
        } catch (NumberFormatException e) {
            sfe.setMessage("Monthly charge must be a non-negative decimal value.");
            throw sfe;
        }
    }
    private double validateCostDouble(double cost) throws SubFieldException {
        if (cost < 0) {
            throw new SubFieldException("Monthly charge cannot be a negative value.");
        }
        return cost;
    }
    private String validateComment(String comment) throws SubFieldException {
        SubFieldException sfe = new SubFieldException();
        if (comment.length() > COMMENT_MAX_LENGTH) {
            sfe.setMessage(String.format(Locale.CANADA, "Comment cannot be longer than %d characters.", COMMENT_MAX_LENGTH));
            throw sfe;
        }
        return comment;
    }
}
