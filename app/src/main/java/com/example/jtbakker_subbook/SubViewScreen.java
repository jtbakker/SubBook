package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class SubViewScreen extends AppCompatActivity {
    private Subscription displayedSub;
    private int subIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_view_screen);

        Intent intent = getIntent();
        Bundle subBundle = intent.getExtras();
        this.displayedSub = (Subscription) subBundle.getSerializable("VIEW");
        this.subIndex = subBundle.getInt("INDEX");

        this.displaySubAttributes();
    }

    public void displaySubAttributes() {
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
    public void cancel(View view) {
        Intent intent = new Intent();
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("RESULT_SUB", this.displayedSub);
        intent.putExtras(subBundle);
        intent.putExtra("INDEX", this.subIndex);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void editButton (View view) {
        Intent intent = new Intent(this, SubEntryScreen.class);
        Bundle subBundle = new Bundle();
        subBundle.putSerializable("EDIT", this.displayedSub);
        intent.putExtras(subBundle);
        intent.putExtra("SCREEN_TITLE", "Edit Subscription");
        intent.putExtra("DONE_BUTTON", "Save");
        startActivityForResult(intent, 1);
    }

    public void deleteButton (View view) {
        Intent intent = new Intent();
        intent.putExtra("INDEX", subIndex);
        intent.putExtra("DELETE", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle subBundle = data.getExtras();
            displayedSub = (Subscription) subBundle.getSerializable("RESULT_SUB");
            this.displaySubAttributes();
        }
    }


}
