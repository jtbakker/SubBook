/**
 * ClassName: SubEntryScreen
 *
 * Date: February 4, 2018
 *
 * Copyright (c) 2018 - CMPUT 301 All Rights Reserved
 */

package com.example.jtbakker_subbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubEntryScreen extends AppCompatActivity {
    private AlertDialog errorDialog;
    private Subscription entrySub;

    /**
     * Created by Jacob Bakker
     *
     * This method implements the errorDialog functionality intended for illegal inputs while
     * modifying display strings based on whether a Subscription is being created or edited.
     * <p>
     *     Both the screen title and done button text vary depending on whether this activity
     *     was started to either create or edit a Subscription. If a Subscription is being edited,
     *     a Subscription object will have been provided in the Intent from SubEntryScreen. In that
     *     case, its attributes will be fed into their respective EditText views using the
     *     fillEntryFields() method.
     * </p>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__entry_screen);

        // Initialize error dialog for invalid user input.
        errorDialog = new AlertDialog.Builder(SubEntryScreen.this).create();
        errorDialog.setTitle(getString(R.string.error_dialog_title));
        String errorButtonText = getString(R.string.error_dialog_button);
        errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, errorButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        // Set screen title and confirm button text.
        Intent intent = getIntent();
        // Set screen title to either "New Subscription" or "Edit Subscription" based
        // on intent value.
        String screenTitleString = intent.getStringExtra("SCREEN_TITLE");
        TextView screenTitleView = findViewById(R.id.text_sub_purpose);
        screenTitleView.setText(screenTitleString);
        // Set text for confirm button to either "SAVE" if editing existing subscription
        // or "CREATE" if creating new subscription.
        Button doneButton = (Button) findViewById(R.id.button_done);
        String doneButtonString = intent.getStringExtra("DONE_BUTTON");
        // If string was provided in intent, set button text to that string. Otherwise,
        // default string is "SAVE".
        if (doneButtonString != null) {
            doneButton.setText(doneButtonString);
        }
        // If Subscription object was provided, load its attributes into the entry
        // fields.
        Bundle subBundle = intent.getExtras();
        entrySub = (Subscription) subBundle.getSerializable("EDIT");
        if (entrySub != null) {
            fillEntryFields();
        }
    }

    /**
     * This method puts each attribute from the provided entrySub Subscription into its respective
     * EditText view. This only occurs when editing a Subscription and is intended to allow the
     * user to edit existing values rather than replace them with completely new ones.
     */
    private void fillEntryFields() {
        EditText entryView = (EditText) findViewById(R.id.user_entry_name);
        entryView.setText(entrySub.getName());
        entryView = (EditText) findViewById(R.id.user_entry_date);
        entryView.setText(entrySub.dateToString());
        entryView = (EditText) findViewById(R.id.user_entry_cost);
        entryView.setText(entrySub.costToString());
        entryView = (EditText) findViewById(R.id.user_entry_comment);
        entryView.setText(entrySub.getComment());
    }

    /**
     * This method pulls all user-inputted values from the EditText views and tries to create
     * a Subscription object with those values.
     * <p>
     *     Invalid values are reported by the Subscription class via a SubFieldException, causing
     *     the showErrorDialog method to be called to display the exception's error message and the
     *     immediate termination of this function. If the Subscription is successfully created, the
     *     SubEntryScreen terminates and returns the serialized Subscription object to the parent
     *     activity.
     * </p>
     * @param view
     * @see Subscription
     */
    public void done(View view) {
        // Get all user-inputted Subscription attributes.
        EditText entryView = (EditText) findViewById(R.id.user_entry_name);
        String nameEntry = entryView.getText().toString();
        entryView = (EditText) findViewById(R.id.user_entry_date);
        String dateEntry = entryView.getText().toString();
        entryView = (EditText) findViewById(R.id.user_entry_cost);
        String costEntry = entryView.getText().toString();
        entryView = (EditText) findViewById(R.id.user_entry_comment);
        String commentEntry = entryView.getText().toString();

        // Create new Subscription object. If any input value is invalid, display dialog
        // with error message and abandon creating a new Subscription.
        try {
            entrySub = new Subscription(nameEntry, dateEntry, costEntry, commentEntry);
        } catch (SubFieldException e) {
            showErrorDialog(e.getMessage());
            return;
        }
        // If Subscription creation was successful, add new Subscription to intent and return
        // it to the parent activity.
        Intent intent = new Intent();
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("RESULT_SUB", entrySub);
        intent.putExtras(subBundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * This method is called when the CANCEL button is used, causing the activity to terminate.
     * A return code reflecting the fact that no Subscriptions were created or changed is returned.
     * @param view
     */
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * This method creates a pop-up displaying the provided errorMessage.
     * @param errorMessage
     */
    public void showErrorDialog(String errorMessage) {
        errorDialog.setMessage(errorMessage);
        errorDialog.show();
    }
}