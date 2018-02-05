/**
 * ClassName: MainActivity
 *
 * Date Created: January 29, 2018
 *
 * Copyright (c) 2018 - CMPUT 301 All Rights Reserved
 */

package com.example.jtbakker_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *  Created by Jacob Bakker
 *
 *  This class implements the functionality for the user Subscription container and the main
 *  screen for the app.
 */
public class MainActivity extends AppCompatActivity {
    private ListView sub_ListView;
    private ArrayList<Subscription> userSubscriptions;
    private SubscriptionAdapter subAdapter;
    private static final String FILENAME = "subscriptionList.sav";


    /**
     * This method initializes the ListView for userSubscriptions and a the method for handling
     * row selection.
     * <p>
     *     If a row is tapped, the SubEntryScreen activity is started and is passed both the
     *     Subscription object and index for the selected row. The Subscription is required for both
     *     display and editing purposes while the index is intended to be passed back by
     *     SubViewScreen on completion, allowing the tapped Subscription to either be updated or
     *     deleted based on the activity result.
     * </p>
     * <param> savedInstanceState
     * <see> SubViewScreen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub_ListView = (ListView) findViewById(R.id.list_sub);
        /**
         * This method for responding to row clicks was adapted from the "Android ListView Tutorial"
         * by Odie Edo-Osagie created on May 4, 2016.
         * Link at Link at "https://www.raywenderlich.com/124438/android-listview-tutorial"
         */
        sub_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), SubViewScreen.class);
                Bundle clickedSubBundle = new Bundle();

                Subscription clickedSub = userSubscriptions.get(position);
                clickedSubBundle.putSerializable("VIEW", clickedSub);
                intent.putExtras(clickedSubBundle);
                intent.putExtra("INDEX", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * This method starts the SubEntryScreen so that user can create a new Subscription.
     * <p>
     *     SubEntryScreen displays a screen title denoting the purpose of the entry i.e. whether
     *     the user is editing or creating a Subscription. This title must be supplied as a
     *     key-value pair where key="SCREEN TITLE" in the intent.
     * </p>
     *
     * @param view
     * @see SubEntryScreen
     */
    public void addNewSub(View view) {
        Intent intent = new Intent(this, SubEntryScreen.class);
        String subEntryTitle = getString(R.string.subscription_create);
        intent.putExtra("SCREEN_TITLE", subEntryTitle);
        startActivityForResult(intent, 1);
    }

    /**
     * This method updates the list of user Subscriptions both on screen and on file according
     * to the results of either the SubEntryScreen or the SubViewScreen.
     * <p>
     *     If the user did not cancel out of either screen (i.e. resultCode=RESULT_OK), then the
     *     intent is checked for a Subscription object and an index value. There are 3 cases to
     *     for these values:
     * </p><p>
     *     Case 1: A Subscription object is found and the index is >=0. These return values are the
     *     result of the user editing a Subscription object at the given index value, so update
     *     the userSubscriptions list with this modified Subscription.
     * </p><p>
     *     Case 2: A Subscription object is found and the index is -1. The -1 is intended to denote
     *     a new Subscription object, so insert the new Subscription at the end of the list.
     * </p><p>
     *     Case 3: No Subscription object is found and the index is >=0. This is intended to mean that
     *     the user selected "DELETE" for the Subscription at the index while on the SubViewScreen since doing
     *     so returns only the index. However, in the event that an error occured in SubViewScreen
     *     caused no Subscription to be given in data, a third key-value pair in data is checked before
     *     deleting the Subscription. If the value for key="DELETE" is 'false' then the Subscription
     *     at that index is deleted. This means that the delete operation must be explicitly
     *     requested in order to avoid accidental deletions due to errors.
     * </p><p>
     *     The userSubscriptions list is modified in any case, so the method updates the listView
     *     by notifying subAdapter of changes, recalculates the total cost by calling displayTotalCost,
     *     and saves the changed list to a file.
     * </p>
     * @param requestCode
     * @param resultCode
     * @param data Subscription objects created either in SubViewScreen or SubEntryScreen are passed
     *             back to MainActivity via this Intent object.
     * @see SubEntryScreen
     * @see SubViewScreen
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle subBundle = data.getExtras();
            Subscription newSub = (Subscription) subBundle.getSerializable("RESULT_SUB");
            if (newSub != null) {
                int subIndex = data.getIntExtra("INDEX", -1);
                if (subIndex != -1) {
                    userSubscriptions.set(subIndex, newSub); // Case 1.
                } else {
                    userSubscriptions.add(newSub); // Case 2.
                }
            // Case 3. Check if delete operation was requested.
            } else if (subBundle.getBoolean("DELETE", false)) {
                int deleteIndex = subBundle.getInt("INDEX", -1);
                if (deleteIndex >= 0) {
                    userSubscriptions.remove(deleteIndex);
                }
            }
            subAdapter.notifyDataSetChanged(); // Update the ListView for mySubscriptions
            displayTotalCost(); // Update the total cost TextView.
            saveInFile(); // Save changes to file.
        }
    }

    /**
     * This method recalculates the total cost of all Subscriptions created so far and updates
     * the screen with the new value.
     */
    public void displayTotalCost() {
        TextView view_TotalCost = (TextView) findViewById(R.id.text_total_cost);
        double double_TotalCost = 0;
        if (userSubscriptions != null) {
            int size = userSubscriptions.size();
            for (int i = 0; i < size; i = i + 1) {
                double_TotalCost += userSubscriptions.get(i).getCost();
            }
        }
        // Format total cost to 2 decimal places.
        String string_TotalCost = String.format(Locale.CANADA, "%,.2f", double_TotalCost);
        // Insert total cost into display string.
        string_TotalCost = getString(R.string.main_total_cost, string_TotalCost);
        // Update screen with modified total cost string.
        view_TotalCost.setText(string_TotalCost);
    }

    /**
     * Initializes both the Subscription dataset using loadFromFile() and the adapter for
     * the Subscription list. Once the dataset is initialized, update the displayed total
     * cost of all subscriptions.
     *
     * Taken from lonelyTwitter project at https://github.com/vingk/lonelyTwitter/tree/w18wlab3
     * See "lonelyTwitter" activity for method of same name.
     */
    protected void onStart() {
        super.onStart();
        loadFromFile();
        subAdapter = new SubscriptionAdapter(this, userSubscriptions);
        sub_ListView.setAdapter(subAdapter);
        displayTotalCost();
    }

    /**
     * Loads userSubscription arraylist from memory. If none exists, initialize userSubscriptions
     * to empty arrayList.
     *
     * Taken from lonelyTwitter project at https://github.com/vingk/lonelyTwitter/tree/w18wlab3
     * See "lonelyTwitter" activity for method of same name.
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-24
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            userSubscriptions = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            userSubscriptions = new ArrayList<Subscription>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the userSubscriptions list to file.
     *
     * Taken from lonelyTwitter project at https://github.com/vingk/lonelyTwitter/tree/w18wlab3
     * See "lonelyTwitter" activity for method of same name.
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson  = new Gson();
            gson.toJson(userSubscriptions, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Taken from lonelyTwitter project at https://github.com/vingk/lonelyTwitter/tree/w18wlab3
     * See "lonelyTwitter" activity for method of same name.
     */
    protected void onDestroy() {
        super.onDestroy();
        Log.i("In destroy method", "The app is closing");
    }
}
