/**
 * Class: SubViewScreen
 *
 * Date: February 4, 2018
 *
 * Copyright (c) 2018 - CMPUT 301 All Rights Reserved
 */

package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;


/**
 * Created by Jacob Bakker
 *
 * This class implements the screen for viewing, editing, and deleting Subscriptions.
 * <p>
 *     Rather than directly edit the Subscriptions in the userSubscriptions arraylist in MainActivty,
 *     this class will instead return a modified Subscription object meant to replace the one
 *     provided or a flag indicating that MainActivity should delete the provided Subscription.
 * </p>
 */
public class SubViewScreen extends AppCompatActivity {
    private Subscription displayedSub;
    private int subIndex; //Index of displayedSub in MainActivity's userSubscriptions arraylist

    /**
     * This method initializes both the Subscription being displayed and its index to the values
     * provided by the parent activity, then updates the screen with the attributes of that
     * Subscription object.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_view_screen);

        Intent intent = getIntent();
        Bundle subBundle = intent.getExtras();
        displayedSub = (Subscription) subBundle.getSerializable("VIEW");
        subIndex = subBundle.getInt("INDEX");

        displaySubAttributes();
    }

    private void displaySubAttributes() {
        if (displayedSub != null) {
            TextView textView = (TextView) findViewById(R.id.text_display_name);
            textView.setText(displayedSub.getName());
            textView = (TextView) findViewById(R.id.text_display_date);
            textView.setText(displayedSub.dateToString());
            textView = (TextView) findViewById(R.id.text_display_cost);
            textView.setText(displayedSub.costToString());
            textView = (TextView) findViewById(R.id.text_display_comment);
            textView.setText(displayedSub.getComment());
        }
    }

    /**
     * This method is called when the DONE button is pressed. The Subscription object being viewed
     * and its index are returned to MainActivity to allow the userSubscriptions arraylist to be
     * updated with any changes made.
     *
     * @param view
     */
    public void doneButton(View view) {
        Intent intent = new Intent();
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("RESULT_SUB", this.displayedSub);
        intent.putExtras(subBundle);
        intent.putExtra("INDEX", subIndex);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * This method starts the SubEntryScreen to allow the user to edit the Subscription being
     * viewed. Strings denoting the fact that the subscription is being edited are provided for
     * both the screen title and text for the DONE button in SubEntryScreen.
     *
     * @param view
     */
    public void editButton (View view) {
        Intent intent = new Intent(this, SubEntryScreen.class);
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("EDIT", displayedSub);
        intent.putExtras(subBundle);
        intent.putExtra("SCREEN_TITLE", getString(R.string.subscription_entry_title_edit));
        intent.putExtra("DONE_BUTTON", getString(R.string.subscription_entry_save));
        startActivityForResult(intent, 1);
    }

    /**
     * This method ends this activity and returns both the Subscription's index and a flag
     * indicating that this Subscription is to be deleted from the userSubscriptions arraylist
     * in MainActivity.
     *
     * @param view
     * @see MainActivity
     */
    public void deleteButton (View view) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", subIndex);
        intent.putExtra("DELETE", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * This method handles the results from editing the Subscription being viewed in the
     * SubEntryScreen activity. If the SubEntryScreen wasn't cancelled, the returned Subscription
     * object with the modified values will replace the current one being viewed. The method
     * displaySubAttributes() is called to update the screen with any and all changes made.
     *
     * @param requestCode
     * @param resultCode
     * @param data If resultCode=RESULT_OK then data should contain a serialized Subscription
     *             object meant to replace displayedSub.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle subBundle = data.getExtras();
            Subscription returnedSub = (Subscription) subBundle.getSerializable("RESULT_SUB");
            if (returnedSub != null) {
                displayedSub = returnedSub;
                displaySubAttributes();
            }
        }
    }


}
