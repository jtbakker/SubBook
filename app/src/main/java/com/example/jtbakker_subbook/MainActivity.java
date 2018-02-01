package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    public void subscriptionListClick(View view) {
        System.out.print("YOU DID THE THING");
    }

    public double getTotalCost() {
        double total = 0;
        int size = this.userSubscriptions.size();
        for (int i = 0; i < size; i = i + 1) {
            total += this.userSubscriptions.get(i).getCost();
        }
        return total;
    }
}
