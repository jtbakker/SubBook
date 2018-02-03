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
    private static final int maxNameLength = 20;
    private static final int maxCommentLength = 30;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__entry_screen);

        // Initialize error dialog for invalid user input.
        this.errorDialog = new AlertDialog.Builder(SubEntryScreen.this).create();
        this.errorDialog.setTitle(getString(R.string.error_dialog_title));
        String errorButtonText = getString(R.string.error_dialog_button);
        this.errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, errorButtonText, new DialogInterface.OnClickListener() {
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
            EditText entryView = (EditText) findViewById(R.id.user_entry_name);
            entryView.setText(entrySub.getName());
            entryView = (EditText) findViewById(R.id.user_entry_date);
            entryView.setText(entrySub.dateToString());
            entryView = (EditText) findViewById(R.id.user_entry_cost);
            entryView.setText(entrySub.costToString());
            entryView = (EditText) findViewById(R.id.user_entry_comment);
            entryView.setText(entrySub.getComment());
        }
    }

    public void done(View view) {
        // Check that inputted name is valid.
        EditText view_NameEntry = (EditText) findViewById(R.id.user_entry_name);
        String string_NameEntry = view_NameEntry.getText().toString();
        // Name must have less than 20 characters.
        if (string_NameEntry.length() > SubEntryScreen.maxNameLength) {
            this.showErrorDialog(getString(R.string.error_message_name_long));
            return;
        // Name cannot be empty.
        } else if (string_NameEntry.length() == 0) {
            this.showErrorDialog(getString(R.string.error_message_name_empty));
            return;
        }
        // Check that inputted date is valid.
        EditText view_DateEntry = (EditText) findViewById(R.id.user_entry_date);
        String string_DateEntry = view_DateEntry.getText().toString();
        // Date cannot be empty.
        if (string_DateEntry.length() == 0) {
            this.showErrorDialog(getString(R.string.error_message_date_empty));
            return;
        }
        Date date_DateEntry;
        // Date must conform to format yyyy-mm-dd
        try {
            date_DateEntry = SubEntryScreen.dateFormat.parse(string_DateEntry);
        } catch (ParseException e) {
            this.showErrorDialog(getString(R.string.error_message_date_invalid));
            return;
        }
        // Check that inputted cost is valid.
        EditText view_CostEntry = (EditText) findViewById(R.id.user_entry_cost);
        String string_CostEntry = view_CostEntry.getText().toString();
        // Cost cannot be empty.
        if (string_CostEntry.length() == 0) {
            this.showErrorDialog(getString(R.string.error_message_cost_empty));
            return;
        }
        double double_CostEntry;
        // Cost must be a decimal value.
        try {
            double_CostEntry = Double.parseDouble(string_CostEntry);
        } catch (NumberFormatException e) {
            this.showErrorDialog(getString(R.string.error_message_cost_invalid));
            return;
        }
        // Cost cannot be a negative decimal value.
        if (double_CostEntry < 0) {
            this.showErrorDialog(getString(R.string.error_message_cost_negative));
            return;
        }
        // Check that inputted comment is valid.
        EditText view_CommentEntry = (EditText) findViewById(R.id.user_entry_comment);
        String string_CommentEntry = view_CommentEntry.getText().toString();
        // Comment must be less than 30 characters.
        if (string_CommentEntry.length() > SubEntryScreen.maxCommentLength) {
            this.showErrorDialog(getString(R.string.error_message_comment_long));
            return;
        }
        // If all values are valid, enter them into the provided Subscription
        // or create a new Subscription with those values.
        Intent returnIntent = new Intent();
        if (this.entrySub != null) {
            this.entrySub.setName(string_NameEntry);
            this.entrySub.setDate(date_DateEntry);
            this.entrySub.setCost(double_CostEntry);
            this.entrySub.setComment(string_CommentEntry);
        } else {
            this.entrySub = new Subscription(string_NameEntry, date_DateEntry, double_CostEntry, string_CommentEntry);
        }
        // Pass subscription back to parent activity.
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("RESULT_SUB", this.entrySub);
        returnIntent.putExtras(subBundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // If cancel button is pressed, go back to previous activity.
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    // If some user input was invalid, display relevant error message
    // as pop-up dialog.
    public void showErrorDialog(String errorMessage) {
        this.errorDialog.setMessage(errorMessage);
        this.errorDialog.show();
        return;
    }
}