package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView sub_ListView;
    private ArrayList<Subscription> userSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub_ListView = (ListView) findViewById(R.id.list_sub);
        userSubscriptions = new ArrayList<Subscription>();
        String newName = "TwentyFourHHHHHHHHHH";
        double newCost = 7.99;
        Date newDate = new Date();
        String comment = "Double King";
        Subscription testSub = new Subscription(newName, newDate, newCost, comment);
        for (int i = 0; i < 20; i = i + 1) {
            userSubscriptions.add(testSub);
        }
        SubscriptionAdapter subAdapter = new SubscriptionAdapter(this, this.userSubscriptions);
        sub_ListView.setAdapter(subAdapter);

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
        intent.putExtra("SCREEN_TITLE", subEntryTitle);
        intent.putExtra("SUBSCRIPTION_INDEX", subIndex);
        startActivity(intent);
    }

    // Recalculate total monthly charge and display to screen.
    public void displayTotalCost() {
        TextView view_TotalCost = (TextView) findViewById(R.id.text_total_cost);
        double double_TotalCost = 0;
        int size = this.userSubscriptions.size();
        for (int i = 0; i < size; i = i + 1) {
            double_TotalCost += this.userSubscriptions.get(i).getCost();
        }
        String string_TotalCost = String.format("%,.2f", double_TotalCost);
        string_TotalCost = getString(R.string.total_cost, string_TotalCost);
        view_TotalCost.setText(string_TotalCost);
    }
}
