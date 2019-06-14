package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityDetailLastRound extends AppCompatActivity {

    String loggedUserName;
    DatabaseResults resultsDb;
    DatabaseTeams teamsDb;
    public static ArrayList<Results> resultsTable = new ArrayList<>();
    ListView lastRoundResults;
    Button activityLastRoundSeeTable;
    Button activityLastRoundClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lastround);

        resultsDb = new DatabaseResults(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        lastRoundResults = (ListView) findViewById(R.id.listViewLastRound);
        activityLastRoundSeeTable = (Button) findViewById(R.id.activityLastRoundSeeTable);
        activityLastRoundClose = (Button) findViewById(R.id.activityLastRoundClose);


        refreshLastResultsTable();

        activityLastRoundSeeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailLastRound.this, ActivityResults.class);
                startActivity(intent);
                finish();
            }
        });

        activityLastRoundClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public String getLogin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        return loggedUserName;
    }

    public Cursor getRoundMatches(int a) {
        resultsDb.open();
        teamsDb.open();
        Cursor roundCursor = resultsDb.getScheduledMatchesOfTeam(teamsDb.getMyClubName());
        roundCursor.moveToFirst();
        int round = Integer.valueOf(roundCursor.getString(1));
        roundCursor.close();

        Cursor cursor = resultsDb.getRoundMatches(a);
        for (int c = 0; c < cursor.getCount(); c++) {
            cursor.moveToPosition(c);
            Log.d("teamPoint", "Round " +
                    String.valueOf(a) + ": " + cursor.getString(2) + " - " + cursor.getString(3));

        }
        teamsDb.close();
        resultsDb.close();
        return cursor;
    }

    public int lastRoundNumber(){
        resultsDb.open();
        teamsDb.open();
        Cursor roundCursor = resultsDb.getPlayedMatchesOfTeam(teamsDb.getMyClubName());
        roundCursor.moveToLast();
        int round = Integer.valueOf(roundCursor.getString(1));
        roundCursor.close();
        teamsDb.close();
        resultsDb.close();
        return round;
    }

    public ArrayList<Results> loadLastRound(){
        ArrayList<Results> list = new ArrayList<>();

        int aMax = getRoundMatches(lastRoundNumber()).getCount();
        Cursor cursor = getRoundMatches(lastRoundNumber());
        int a;
        for (a = 0; a < aMax; a++){
            cursor.moveToPosition(a);
            int round = Integer.valueOf(cursor.getString(1));
            String homeTeam = cursor.getString(2);
            int homeScore = Integer.valueOf(cursor.getString(4));
            int awayScore = Integer.valueOf(cursor.getString(5));
            String awayTeam = cursor.getString(3);
            Results singleResult = new Results(round, homeTeam, awayTeam, homeScore, awayScore);
            list.add(singleResult);
        }
        cursor.close();
        return list;
    }

    private void refreshLastResultsTable(){
        resultsTable = loadLastRound();
        LastRoundAdapter adapter = new LastRoundAdapter(getApplicationContext(), resultsTable);
        lastRoundResults.setAdapter(adapter);
    }

}
