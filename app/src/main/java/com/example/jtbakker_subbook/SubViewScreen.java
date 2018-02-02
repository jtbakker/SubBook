package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class SubViewScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_view_screen);

        Intent intent = getIntent();
        Bundle subBundle = intent.getExtras();
        Subscription viewSub = (Subscription) subBundle.getSerializable("VIEW");

        TextView textView = (TextView) findViewById(R.id.text_display_name);
        textView.setText(viewSub.getName());
        textView = (TextView) findViewById(R.id.text_display_date);
        textView.setText(viewSub.getDate().toString());
        textView = (TextView) findViewById(R.id.text_display_cost);
        textView.setText(String.format(Locale.CANADA, "%,.2f", viewSub.getCost()));
        textView = (TextView) findViewById(R.id.text_display_comment);
        textView.setText(viewSub.getComment());
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
