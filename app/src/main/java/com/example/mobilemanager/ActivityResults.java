package com.example.mobilemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class ActivityResults extends AppCompatActivity {

    ListView activityResultsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        activityResultsTable = (ListView) findViewById(R.id.activityResultsTable);
    }
}
