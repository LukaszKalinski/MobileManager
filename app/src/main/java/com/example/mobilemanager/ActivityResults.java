package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    DatabaseStadium stadiumDb;
    DatabaseClubFinance clubFinanceDb;
    public static ArrayList<TeamTableScores> teamTableScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsDb = new DatabaseResults(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        stadiumDb = new DatabaseStadium(this, getLogin());
        clubFinanceDb = new DatabaseClubFinance(this, getLogin());
        activityResultsTable = (ListView) findViewById(R.id.activityResultsTable);

        fastLogTableCheck();
        refreshScoreTable();

    }

    @Override
    public void onResume(){
        super.onResume();
        refreshScoreTable();
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
                return(true);
            case R.id.team:
                intent = new Intent(this, ActivityTeam.class);
                startActivity(intent);
                return(true);
            case R.id.results:
                intent = new Intent(this, ActivityResults.class);
                startActivity(intent);
                return(true);
            case R.id.calendar:
                intent = new Intent(this, ActivityCalendar.class);
                startActivity(intent);
                return(true);
            case R.id.stadium:
                intent = new Intent(this, ActivityStadium.class);
                startActivity(intent);
                return(true);
            case R.id.finance:
                intent = new Intent(this, ActivityFinance.class);
                startActivity(intent);
                return(true);
            case R.id.mainPage:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                return(true);
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
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
        Toast.makeText(this,"LOGIN" + loggedUserName, Toast.LENGTH_LONG).show();
        return loggedUserName;
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

        moneyForTicket(x1, x2);

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

                    moneyForTicket(x1, x2);

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
        teamsDb.open();
        resultsDb.open();


//        for (int a = 1; a <= 30; a ++){
//            for (int b = 1; b <= 8; b ++){
//                Cursor cursor = resultsDb.getAllResults();
//                cursor.moveToFirst();
//                while(){resultsDb.setRoundOfMatch(String.valueOf(a), homeTeam, awayTeam);}
//
//
//
//            }
//        }

        resultsDb.close();
        teamsDb.close();
    }

    public Building getStadiumInfo(){
        stadiumDb.open();
        Cursor cursor = stadiumDb.getSpecificBuilding("Stadium");
        cursor.moveToFirst();

        int currentLevel = Integer.parseInt(cursor.getString(2));
        int upgradeCost = Integer.parseInt(cursor.getString(3));
        int ticketPrice = Integer.parseInt(cursor.getString(4));
        int seatsAmount = Integer.parseInt(cursor.getString(5));
        int newSeatCost = Integer.parseInt(cursor.getString(6));
        cursor.close();

        Building stadium = new Building("Stadium", currentLevel, upgradeCost, ticketPrice, seatsAmount, newSeatCost);
        return stadium;
    }

    public void earnedMoney(double incomes) {
        clubFinanceDb.open();
        String login = getLogin();
        clubFinanceDb.refreshClubFinance(login, incomes, 0, 0);
        System.out.println("New BALANCE is: " + clubFinanceDb.getAccountBalance(login));
    }

    public void moneyForTicket(int x1, int x2) {
        if (x1 == 0) {
            int ticketPrice = getStadiumInfo().firstValue;
            int seatsAmount = getStadiumInfo().secondValue;
            int income = ticketPrice * seatsAmount;
            Log.d("teamPoint", "Tickets income: " + String.valueOf(income));
            earnedMoney(Double.valueOf(income));
        }

        if (x2 == 0) {
            int ticketPrice = getStadiumInfo().firstValue;
            int seatsAmount = getStadiumInfo().secondValue;
            double income = 0.1 * ticketPrice * seatsAmount;
            Log.d("teamPoint", "Tickets income: " + String.valueOf(income));
            earnedMoney(income);
        }
    }

}
