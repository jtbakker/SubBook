package com.example.jtbakker_subbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView userSubscriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSubscriptions = (ListView) findViewById(R.id.subList);
        ArrayList<Subscription> allSubs = new ArrayList<Subscription>();
        String newName = "TwentyFourHHHHHHHHHH";
        double newCost = 7.99;
        Date newDate = new Date();
        String comment = "Double King";
        Subscription testSub = new Subscription(newName, newDate, newCost, comment);
        for (int i = 0; i < 20; i = i + 1) {
            allSubs.add(testSub);
        }
        SubscriptionAdapter subAdapter = new SubscriptionAdapter(this, allSubs);
        userSubscriptions.setAdapter(subAdapter);

    }


}
