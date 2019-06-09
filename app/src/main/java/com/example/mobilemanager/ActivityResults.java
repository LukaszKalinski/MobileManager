package com.example.mobilemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ActivityResults extends AppCompatActivity {

    String loggedUserName;
    ListView activityResultsTable;
    DatabaseResults resultsDb;
    DatabaseTeams teamsDb;
    public static ArrayList<TeamTableScores> teamTableScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsDb = new DatabaseResults(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        activityResultsTable = (ListView) findViewById(R.id.activityResultsTable);

        checkIfResultsDbExist();
        fastLogTableCheck();

        refreshScoreTable();

        Log.d("teamPoint", getSingleResult(0,1));
        Log.d("teamPoint", getSingleResult(0,2));

    }

    @Override
    public void onResume(){
        super.onResume();
        refreshScoreTable();
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
                    resultsDb.createMatch("0", homeTeam, awayTeam);
//                    System.out.println("Created Match: " + homeTeam + " - " + awayTeam);
                }
            }
        }
        teamsDb.close();
        resultsDb.close();

        setRoundsToMatches();
    }

    public void playSingleMatch(int x1, int x2){
        teamsDb.open();
        String homeTeam = teamsDb.getClubName(x1);
        String awayTeam = teamsDb.getClubName(x2);

        //Logika meczu
        int homeTeamAttackPower = teamsDb.getAttackClubPower(x1);
        int homeTeamDefencePower = teamsDb.getDefenceClubPower(x1);
        int awayTeamAttackPower = teamsDb.getAttackClubPower(x2);
        int awayTeamDefencePower = teamsDb.getDefenceClubPower(x2);

        double homeScoreLuck = (Math.random()*100 + 50);
        double awayScoreLuck = (Math.random()*100 + 50);

        resultsDb.open();
        int homeScore = (int) (Math.min(10, (homeTeamAttackPower/awayTeamDefencePower)*(homeScoreLuck/100)));
        int awayScore = (int) (Math.min(10, (awayTeamAttackPower/homeTeamDefencePower)*(awayScoreLuck/100)));
        resultsDb.playMatch(homeTeam, homeScore, awayTeam, awayScore);
//                    System.out.println("Match: " + homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam);
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

                    //Logika meczu
                    int homeTeamAttackPower = teamsDb.getAttackClubPower(x1);
                    int homeTeamDefencePower = teamsDb.getDefenceClubPower(x1);
                    int awayTeamAttackPower = teamsDb.getAttackClubPower(x2);
                    int awayTeamDefencePower = teamsDb.getDefenceClubPower(x2);

                    double homeScoreLuck = (Math.random()*100 + 50);
                    double awayScoreLuck = (Math.random()*100 + 50);

                    int homeScore = (int) (Math.min(10, (homeTeamAttackPower/awayTeamDefencePower)*(homeScoreLuck/100)));
                    int awayScore = (int) (Math.min(10, (awayTeamAttackPower/homeTeamDefencePower)*(awayScoreLuck/100)));
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

    public int getMatchesAmountOfTeam(int a){
        resultsDb.open();
        teamsDb.open();
        int result = resultsDb.getMatchesAmount(teamsDb.getClubName(a));
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

    public ArrayList<TeamTableScores> loadScoreTable(){
        ArrayList<TeamTableScores> list = new ArrayList<>();
        teamsDb.open();
        int aMax = teamsDb.getAllClubs().getCount();
        teamsDb.close();
        int a;
            for (a = 0; a < aMax; a++){
                teamsDb.open();
                String teamName = teamsDb.getClubName(a);
                teamsDb.close();
                int position = a + 1;
                int matches = getMatchesAmountOfTeam(a);
                int points = getPointsOfTeam(a);
                int scoredGoals = getScoredGoalsOfTeam(a);
                int lostGoals = getLostGoalsOfTeam(a);
                TeamTableScores singleResult = new TeamTableScores(position, teamName, matches, points, scoredGoals, lostGoals);
                list.add(singleResult);
            }
        Collections.sort(list, new Comparator<TeamTableScores>() {
            @Override
            public int compare(TeamTableScores o1, TeamTableScores o2) {
                return o2.points - o1.points;
            }
        });
        return list;
        }

    private void refreshScoreTable(){
        teamTableScores = loadScoreTable();
        TeamTableScoresAdapter adapter = new TeamTableScoresAdapter(getApplicationContext(), teamTableScores);
        activityResultsTable.setAdapter(adapter);
    }

    public void setRoundsToMatches(){
//        teamsDb.open();
//        resultsDb.open();
//
//
//
//
//
//        Cursor mCursor = resultsDb.getRoundMatches(112,0);
//        int x = Integer.parseInt(mCursor.getString(1));
//        Log.d("teamPoint", "getRoundMatch (0,1) is:" + x);
//        resultsDb.close();
//        teamsDb.close();
    }


}
