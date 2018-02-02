package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ListView sub_ListView;
    private ArrayList<Subscription> userSubscriptions;
    private SubscriptionAdapter subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub_ListView = (ListView) findViewById(R.id.list_sub);
        userSubscriptions = new ArrayList<Subscription>();
        this.subAdapter = new SubscriptionAdapter(this, this.userSubscriptions);
        sub_ListView.setAdapter(subAdapter);
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
        intent.putExtra("SCREEN_TITLE", subEntryTitle);
        intent.putExtra("SUBSCRIPTION_INDEX", subIndex);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle subBundle = data.getExtras();
            Subscription newSub = (Subscription) subBundle.getSerializable("NEW");
            this.userSubscriptions.add(newSub);
            this.subAdapter.notifyDataSetChanged();
            this.displayTotalCost();
        }
    }
    // Recalculate total monthly charge and display to screen.
    public void displayTotalCost() {
        TextView view_TotalCost = (TextView) findViewById(R.id.text_total_cost);
        double double_TotalCost = 0;
        int size = this.userSubscriptions.size();
        for (int i = 0; i < size; i = i + 1) {
            double_TotalCost += this.userSubscriptions.get(i).getCost();
        }
        String string_TotalCost = String.format(Locale.CANADA, "%,.2f", double_TotalCost);
        string_TotalCost = getString(R.string.main_total_cost, string_TotalCost);
        view_TotalCost.setText(string_TotalCost);
    }
}
