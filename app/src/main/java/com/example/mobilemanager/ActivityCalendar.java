package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    DatabaseResults resultsDb;
    DatabaseTeams teamsDb;
    String lastMatch1;
    String lastMatch2;
    String lastMatch3;
    String nextMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        resultsDb = new DatabaseResults(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());

        teamsDb.open();
        resultsDb.open();
        Log.d("teamPoint", teamsDb.getClubName(0));

        Cursor lastMatchCursor = resultsDb.getPlayedMatchesOfTeam(teamsDb.getClubName(0));
        Cursor nextMatchCursor = resultsDb.getScheduledMatchesOfTeam(teamsDb.getClubName(0));

        if (lastMatchCursor.getCount() > 0){
            lastMatchCursor.moveToLast();
            lastMatch1 = "[" + lastMatchCursor.getString(2) + "] "
                    + lastMatchCursor.getString(4) + " - "
                    + lastMatchCursor.getString(5) + " ["
                    + lastMatchCursor.getString(3) + "]";
        } else {
            lastMatch1 = "Not played";
        }
        if (lastMatchCursor.moveToPosition(lastMatchCursor.getPosition()-1)){
            lastMatch2 = "[" + lastMatchCursor.getString(2) + "] "
                    + lastMatchCursor.getString(4) + " - "
                    + lastMatchCursor.getString(5) + " ["
                    + lastMatchCursor.getString(3) + "]";
        } else {
            lastMatch2 = "Not played";
        }

        if (lastMatchCursor.moveToPosition(lastMatchCursor.getPosition()-1)){
            lastMatch3 = "[" + lastMatchCursor.getString(2) + "] "
                    + lastMatchCursor.getString(4) + " - "
                    + lastMatchCursor.getString(5) + " ["
                    + lastMatchCursor.getString(3) + "]";
        } else {
            lastMatch3 = "Not played";
        }

        nextMatch = "Not Arranged Yet";
        nextMatchCursor.moveToFirst();
        if (nextMatchCursor.getString(6).equals("no") ) {
            nextMatch = "[" + nextMatchCursor.getString(2) + "] "
                    + nextMatchCursor.getString(4) + " - "
                    + nextMatchCursor.getString(5) + " ["
                    + nextMatchCursor.getString(3) + "]"
            ;
        } else {
            nextMatch = "No more games";
        }

        lastMatchCursor.close();
        nextMatchCursor.close();

        resultsDb.close();
        teamsDb.close();

        activityCalendarLastMatchBtn = (Button) findViewById(R.id.activityCalendarLastMatchBtn);
        activityCalendarLastMatchBtn.setText(lastMatch1);
        activityCalendarNextMatchBtn = (Button) findViewById(R.id.activityCalendarNextMatchBtn);
        activityCalendarNextMatchBtn.setText(nextMatch);
        activityCalendarLastResult1 = (Button) findViewById(R.id.activityCalendarLastResult1);
        activityCalendarLastResult1.setText(lastMatch3);
        activityCalendarLastResult2 = (Button) findViewById(R.id.activityCalendarLastResult2);
        activityCalendarLastResult2.setText(lastMatch2);
        activityCalendarLastResult3 = (Button) findViewById(R.id.activityCalendarLastResult3);
        activityCalendarLastResult3.setText(lastMatch1);

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
