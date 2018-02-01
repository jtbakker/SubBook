package com.example.jtbakker_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SubEntryScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__entry_screen);

        Intent intent = getIntent();
        String screenTitleString = intent.getStringExtra("SCREEN_TITLE");

        TextView screenTitleView = findViewById(R.id.text_sub_purpose);
        screenTitleView.setText(screenTitleString);
    }

    public void done(View view) {
        // Check that all entered values are valid.
        EditText view_NameEntry = (EditText) findViewById(R.id.user_entry_name);
        String string_NameEntry = view_NameEntry.getText().toString();
        if (string_NameEntry.length() > 20) {
            // Error message.
            return;
        }
    }
}
