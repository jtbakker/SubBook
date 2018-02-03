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


public class MainActivity extends AppCompatActivity {
    private ListView sub_ListView;
    private ArrayList<Subscription> userSubscriptions;
    private SubscriptionAdapter subAdapter;
    private static final String FILENAME = "subscriptionList.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub_ListView = (ListView) findViewById(R.id.list_sub);
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
        this.displayTotalCost();
    }

    // The method "addNewSub" starts the SubEntryScreen activity in the "create" mode,
    // allowing the user to initialize and add a new subscription to their list.
    // The intent requires the title for the screen and an index for the subscription in
    // the arraylist to be modified. Since a new subscription is being created, -1 will be
    // supplied in indicate that a new subscription is being created.
    public void addNewSub(View view) {
        Intent intent = new Intent(this, SubEntryScreen.class);
        String subEntryTitle = getString(R.string.subscription_create);
        int subIndex = -1;
        intent.putExtra("SCREEN_TITLE", "New Subscription");
        intent.putExtra("SUBSCRIPTION_INDEX", subIndex);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle subBundle = data.getExtras();
            Subscription newSub = (Subscription) subBundle.getSerializable("RESULT_SUB");
            if (newSub != null) {
                int subIndex = data.getIntExtra("INDEX", -1);
                if (subIndex != -1) {
                    userSubscriptions.set(subIndex, newSub);
                } else {
                    userSubscriptions.add(newSub);
                }
            } else if (subBundle.getBoolean("DELETE", false)) {
                int deleteIndex = subBundle.getInt("INDEX", -1);
                if (deleteIndex >= 0) {
                    userSubscriptions.remove(deleteIndex);
                }
            }
            subAdapter.notifyDataSetChanged();
            displayTotalCost();
            saveInFile();
        }
    }
    // Recalculate total monthly charge and display to screen.
    public void displayTotalCost() {
        TextView view_TotalCost = (TextView) findViewById(R.id.text_total_cost);
        double double_TotalCost = 0;
        if (userSubscriptions != null) {
            int size = userSubscriptions.size();
            for (int i = 0; i < size; i = i + 1) {
                double_TotalCost += userSubscriptions.get(i).getCost();
            }
        }
        String string_TotalCost = String.format(Locale.CANADA, "%,.2f", double_TotalCost);
        string_TotalCost = getString(R.string.main_total_cost, string_TotalCost);
        view_TotalCost.setText(string_TotalCost);
    }

    protected void onStart() {
        super.onStart();
        loadFromFile();
        subAdapter = new SubscriptionAdapter(this, userSubscriptions);
        sub_ListView.setAdapter(subAdapter);
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();

            userSubscriptions = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            userSubscriptions = new ArrayList<Subscription>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    protected void onDestroy() {
        super.onDestroy();
        Log.i("In destrog method", "The app is closing");
    }
}
