package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class ActivityCalendar extends AppCompatActivity {

    String loggedUserName;
    Button activityCalendarLastMatchBtn;
    Button activityCalendarNextMatchBtn;
    Button activityCalendarLastResult1;
    Button activityCalendarLastResult2;
    Button activityCalendarLastResult3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        activityCalendarLastMatchBtn = (Button) findViewById(R.id.activityCalendarLastMatchBtn);
        activityCalendarLastMatchBtn.setText("home 0 - 0 away");
        activityCalendarNextMatchBtn = (Button) findViewById(R.id.activityCalendarNextMatchBtn);
        activityCalendarNextMatchBtn.setText("home    -    away");
        activityCalendarLastResult1 = (Button) findViewById(R.id.activityCalendarLastResult1);
        activityCalendarLastResult1.setText("home 1 - 0 away");
        activityCalendarLastResult2 = (Button) findViewById(R.id.activityCalendarLastResult2);
        activityCalendarLastResult2.setText("home 1 - 2 away");
        activityCalendarLastResult3 = (Button) findViewById(R.id.activityCalendarLastResult3);
        activityCalendarLastResult3.setText("home 3 - 0 away");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.msg:
                intent = new Intent(this, ActivityMessages.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.team:
                intent = new Intent(this, ActivityTeam.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.calendar:
                intent = new Intent(this, ActivityCalendar.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.stadium:
                intent = new Intent(this, ActivityStadium.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.finance:
                intent = new Intent(this, ActivityFinance.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.mainPage:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
                finish();
                return(true);
            case R.id.exit:
                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    public String getLogin(){
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        return loggedUserName;
    }
}
