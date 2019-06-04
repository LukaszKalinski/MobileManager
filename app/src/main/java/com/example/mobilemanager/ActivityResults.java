package com.example.mobilemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityResults extends AppCompatActivity {

    String loggedUserName;
    ListView activityResultsTable;
    DatabaseResults resultsDb;
    DatabaseTeams teamsDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsDb = new DatabaseResults(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        activityResultsTable = (ListView) findViewById(R.id.activityResultsTable);

        checkIfResultsDbExist();

        fastLogTableCheck();

    }

    public String getLogin(){
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        Toast.makeText(this,"LOGIN" + loggedUserName, Toast.LENGTH_LONG).show();
        return loggedUserName;
    }

    public void checkIfResultsDbExist(){
        resultsDb.open();
        if (resultsDb.getAllResults().getCount() > 0){
            Log.d("teamPoint", "DatabaseResults already EXISTS");
            Log.d("teamPoint", "and amount of matches are: " + String.valueOf(resultsDb.getAllResults().getCount()));
            resultsDb.close();
        } else {
            resultsDb.close();
            Log.d("teamPoint", "Creating and playing matches");
            createAllMatches();
            playAllMatches();
        }
    }

    public void createMatch(int a, int b){
        teamsDb.open();
        String homeTeam = teamsDb.getClubName(a);
        String awayTeam = teamsDb.getClubName(b);
        resultsDb.open();
        resultsDb.createMatch("someDay", homeTeam, awayTeam);
        System.out.println("First match is: " + homeTeam + " - " + awayTeam);
        System.out.println("Matches in DB are: " + resultsDb.getAllResults().getCount());
        teamsDb.close();
        resultsDb.close();
    }

    public void createAllMatches(){
        teamsDb.open();
        int teamsAmount = teamsDb.getAllClubs().getCount();
        resultsDb.open();
        int x1;
        int x2;
        for (x1 = 0; x1 < teamsAmount; x1++){
            for (x2 = 0; x2 < teamsAmount; x2++){
                if (x1 != x2){
                    String homeTeam = teamsDb.getClubName(x1);
                    String awayTeam = teamsDb.getClubName(x2);
                    resultsDb.createMatch("someDay", homeTeam, awayTeam);
//                    System.out.println("Created Match: " + homeTeam + " - " + awayTeam);
                }
            }
        }
        teamsDb.close();
        resultsDb.close();
    }

    public void playSingleMatch(int a, int b){
        teamsDb.open();
        String homeTeam = teamsDb.getClubName(a);
        String awayTeam = teamsDb.getClubName(b);
        resultsDb.open();
        int homeScore = 3;
        int awayScore = 1;
        resultsDb.playMatch(homeTeam, homeScore, awayTeam, awayScore);
        String homeScoreDb =  String.valueOf(resultsDb.getSpecifiedResultHomeScore(homeTeam, awayTeam));
        String awayScoreDb = String.valueOf(resultsDb.getSpecifiedResultAwayScore(homeTeam, awayTeam));
        System.out.println("Match: " + homeTeam + " " + String.valueOf(homeScoreDb) + " - " + String.valueOf(awayScoreDb) + " " + awayTeam);
        resultsDb.close();
        teamsDb.close();
    }

    public void playAllMatches(){
        teamsDb.open();
        int teamsAmount = teamsDb.getAllClubs().getCount();
        resultsDb.open();
        int x1;
        int x2;
        for (x1 = 0; x1 < teamsAmount; x1++){
            for (x2 = 0; x2 < teamsAmount; x2++){
                if (x1 != x2){
                    String homeTeam = teamsDb.getClubName(x1);
                    String awayTeam = teamsDb.getClubName(x2);
                    int homeScore = (int) (Math.random()*3);
                    int awayScore = (int) (Math.random()*3);
                    resultsDb.playMatch(homeTeam, homeScore, awayTeam, awayScore);
//                    System.out.println("Match: " + homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam);
                }
            }
        }
        teamsDb.close();
        resultsDb.close();
    }

    public String getSingleResult(int a, int b){
        teamsDb.open();
        String homeTeam = teamsDb.getClubName(a);
        String awayTeam = teamsDb.getClubName(b);
        resultsDb.open();
        int homeScore = resultsDb.getSpecifiedResultHomeScore(homeTeam, awayTeam);
        int awayScore = resultsDb.getSpecifiedResultAwayScore(homeTeam, awayTeam);
        teamsDb.close();
        resultsDb.close();
        String result = "Result: " + homeTeam + " " + String.valueOf(homeScore) + " - " + String.valueOf(awayScore) + " " + awayTeam;
        return result;
    }

    public int getPointsOfTeam(int a){
        resultsDb.open();
        teamsDb.open();
        int result = resultsDb.getPointsOfTeam(teamsDb.getClubName(a));
        teamsDb.close();
        resultsDb.close();
        return result;
    }

    public int getScoredGoalsOfTeam(int a){
        resultsDb.open();
        teamsDb.open();
        int result = resultsDb.getScoredGoalsOfTeam(teamsDb.getClubName(a));
        teamsDb.close();
        resultsDb.close();
        return result;
    }

    public int getLostGoalsOfTeam(int a){
        resultsDb.open();
        teamsDb.open();
        int result = resultsDb.getLostGoalsOfTeam(teamsDb.getClubName(a));
        teamsDb.close();
        resultsDb.close();
        return result;
    }

    public void fastLogTableCheck(){
        teamsDb.open();
        int aMax = teamsDb.getAllClubs().getCount();
        Log.d("teamPoint", "aMax: " + aMax);
        int a;
        teamsDb.close();
        for (a = 0; a < aMax; a++){
            teamsDb.open();
            String teamName = teamsDb.getClubName(a);
            teamsDb.close();
            Log.d("teamPoint", teamName + ": " + getPointsOfTeam(a) + " pkt., goals: " + getScoredGoalsOfTeam(a) + "-" + getLostGoalsOfTeam(a));
        }
    }
}
