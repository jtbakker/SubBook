package com.example.jtbakker_subbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView userSubscriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSubscriptions = (ListView) findViewById(R.id.subList);
        String[] subList = new String[12];
        subList[0] = "One";
        subList[1] = "Two";
        subList[2] = "Three";
        subList[3] = "Four";
        subList[4] = "Five";
        subList[5] = "Six";
        subList[6] = "Seven";
        subList[7] = "Eight";
        subList[8] = "Nine";
        subList[9] = "Ten";
        subList[10] = "Eleven";
        subList[11] = "Twelve";

        ArrayAdapter subAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subList);
        userSubscriptions.setAdapter(subAdapter);

    }
}
