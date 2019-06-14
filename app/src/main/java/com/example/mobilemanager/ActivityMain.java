package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {

    String loggedUserName;
    DatabaseClubFinance clubFinanceDb;
    DatabaseTeams teamsDb;
    DatabaseResults resultsDb;
    DatabaseTeam playersDb;
    DatabaseStadium stadiumDb;
    DatabaseNewFoundPlayer newFoundPlayerDb;
    Button activityMainPlayNextRound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clubFinanceDb = new DatabaseClubFinance(this, getLogin());
        stadiumDb = new DatabaseStadium(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        resultsDb = new DatabaseResults(this, getLogin());
        playersDb = new DatabaseTeam(this, getLogin());
        newFoundPlayerDb = new DatabaseNewFoundPlayer(this, getLogin());

        activityMainPlayNextRound = (Button) findViewById(R.id.activityMainPlayNextRound);

        //Checking if Databases exists
        checkIfClubFinanceDbExist();
        checkIfStadiumExists();
        checkIfPlayersDbExist();
        checkIfTeamsDbExist();
        checkIfResultsDbExist();
        checkIfNewPlayerFound();

        activityMainPlayNextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextRoundMatches();
                Intent intent = new Intent(ActivityMain.this, ActivityDetailLastRound.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.msg:
                intent = new Intent(this, ActivityMessages.class);
                startActivity(intent);
                return (true);
            case R.id.team:
                intent = new Intent(this, ActivityTeam.class);
                startActivity(intent);
                return (true);
            case R.id.results:
                intent = new Intent(this, ActivityResults.class);
                startActivity(intent);
                return (true);
            case R.id.calendar:
                intent = new Intent(this, ActivityCalendar.class);
                startActivity(intent);
                return (true);
            case R.id.stadium:
                intent = new Intent(this, ActivityStadium.class);
                startActivity(intent);
                return (true);
            case R.id.finance:
                intent = new Intent(this, ActivityFinance.class);
                startActivity(intent);
                return (true);
            case R.id.mainPage:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                return (true);
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
                return (true);
            case R.id.exit:
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public String getLogin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        Toast.makeText(this, "LOGIN" + loggedUserName, Toast.LENGTH_LONG).show();
        return loggedUserName;
    }

    public void checkIfClubFinanceDbExist() {
        clubFinanceDb.open();
        if (clubFinanceDb.getRecords().getCount() > 0) {
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and BALANCE is: " + clubFinanceDb.getAccountBalance(getLogin()));
        } else {
            clubFinanceDb.firstAddition(getLogin());
        }
        clubFinanceDb.close();
    }

    public void checkIfTeamsDbExist() {
        teamsDb.open();
        if (teamsDb.getAllClubs().getCount() > 0) {
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and amount of teams is: " + String.valueOf(teamsDb.getAllClubs().getCount()));
        } else {
            teamsDb.createTeams(getLogin(), 1, 1);
            for (int i = 0; i < 15; i++) {
                int attackPower = (int) (Math.max((Math.random() * 2000), 500));
                int defencePower = (int) (Math.max((Math.random() * 2000), 500));
                teamsDb.createTeams(String.valueOf(i), attackPower, defencePower);
            }
        }
        teamsDb.close();
    }

    public void checkIfResultsDbExist() {
        resultsDb.open();
        if (resultsDb.getAllResults().getCount() > 0) {
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and amount of matches are: " + String.valueOf(resultsDb.getAllResults().getCount()));
            resultsDb.close();
        } else {
            System.out.println("Created all matches");
            createAllMatches();
        }
    }

    public void checkIfPlayersDbExist() {
        playersDb.open();
        if (playersDb.getAllPlayers().getCount() > 0) {
            Log.d("teamPoint", "DatabasePlayers already EXISTS");
            Log.d("teamPoint", "and amount of players are: " + String.valueOf(playersDb.getAllPlayers().getCount()));
        } else {
            Log.d("teamPoint", "Creating players list");

            createPlayer(1, 0, "Goalkeeper", 1, 1);
            createPlayer(1, 10, "Goalkeeper", 1, 2);
            createPlayer(1, 1, "Defender", 1, 4);
            createPlayer(11, 1, "Defender", 1, 4);
            createPlayer(5, 1, "Midfielder", 1, 4);
            createPlayer(21, 1, "Midfielder", 1, 4);
            createPlayer(9, 1, "Attacker", 1, 2);
            createPlayer(31, 1, "Attacker", 1, 2);

        }
        playersDb.close();
    }

    public void createPlayer(int number, int nextNumber, String position, int startNumber, int end) {
        int value;
        int skillsValue = 1000;
        int gkSkills;
        int defSkills;
        int attSkills;
        int factor;
        String[] firstName = {"Antoni", "Jakub", "Jan", "Szymon", "Aleksander", "Franciszek", "Filip", "Mikolaj", "Wojciech", "Kacper", "Adam", "Marcel", "Stanislaw", "Michal", "Lukasz", "Wiktor", "Leon", "Piotr", "Nikodem", "Igor", "Ignacy", "Sebastian"};
        String[] lastName = {"Nowak", "Kowalski", "Wisniewski", "Wojcik", "Wojcicki", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak", "Dabrowski", "Kozlowski", "Jankowski", "Wojciechowski", "Kwiatkowski", "Mazur", "Krawczyk"};

        for (int a = startNumber; a <= end; a++) {
            number = number + nextNumber;
            String name = firstName[(int) (Math.random() * firstName.length)] + " " + lastName[(int) (Math.random() * lastName.length)];

            switch (position) {
                case "Goalkeeper":
                    gkSkills = (int) (Math.random() * 100);
                    defSkills = (int) (Math.random() * 10);
                    attSkills = (int) (Math.random() * 10);
                    factor = (int) Math.pow(2, (int) (gkSkills / 10));
                    value = (int) (Math.max(1000, gkSkills * skillsValue * factor));
                    break;
                case "Defender":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (defSkills / 10));
                    value = (int) (Math.max(1000, defSkills * skillsValue * factor + attSkills * skillsValue));
                    break;
                case "Midfielder":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) ((defSkills + attSkills) / 20));
                    value = (int) (Math.max(1000, defSkills * skillsValue * factor + attSkills * skillsValue * factor));
                    break;
                case "Attacker":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (attSkills / 10));
                    value = (int) (Math.max(1000, attSkills * skillsValue * factor + skillsValue * defSkills));
                    break;
                default:
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (gkSkills / 10));
                    value = (int) (Math.max(1000, gkSkills * skillsValue * factor));
                    break;
            }

            int wage = (int) (value * 0.1 / 51);
            playersDb.createPlayer(number, name, position, gkSkills, defSkills, attSkills, wage, value);
        }
    }

    public void checkIfStadiumExists() {
        stadiumDb.open();
        if (stadiumDb.getAllBuildings().getCount() > 0) {
            Log.d("teamPoint", "DatabaseStadium already EXISTS");
            Log.d("teamPoint", "and amount of buildings are: " + String.valueOf(stadiumDb.getAllBuildings().getCount()));
        } else {
            Log.d("teamPoint", "Creating stadium buildings");

            stadiumDb.createBuilding("Stadium", 1, 10000, 15, 1000, 0);
            stadiumDb.createBuilding("Scouting", 1, 10000, 1000, 0, 0);

        }
        stadiumDb.close();
    }

    public void checkIfNewPlayerFound() {

        newFoundPlayerDb.open();
        if (newFoundPlayerDb.getAllPlayers().getCount() > 0) {
            Log.d("teamPoint", "DatabaseFoundPlayer already EXISTS");
            Log.d("teamPoint", "and amount of players are: " + String.valueOf(newFoundPlayerDb.getAllPlayers().getCount()));
        } else {
            Log.d("teamPoint", "Creating new found player");

            int skillsValue = 100000;
            String[] firstName = {"Antoni", "Jakub", "Jan", "Szymon", "Aleksander", "Franciszek", "Filip", "Mikolaj", "Wojciech", "Kacper", "Adam", "Marcel", "Stanislaw", "Michal", "Lukasz", "Wiktor", "Leon", "Piotr", "Nikodem", "Igor", "Ignacy", "Sebastian"};
            String[] lastName = {"Nowak", "Kowalski", "Wisniewski", "Wojcik", "Wojcicki", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak", "Dabrowski", "Kozlowski", "Jankowski", "Wojciechowski", "Kwiatkowski", "Mazur", "Krawczyk"};

            String name = firstName[(int) (Math.random() * firstName.length)] + " " + lastName[(int) (Math.random() * lastName.length)];

            int gkSkills;
            int defSkills;
            int attSkills;
            int value;

            String position;
            int a = (int) (Math.random() * 4);
            switch (a) {
                case 0:
                    position = "Goalkeeper";
                    break;
                case 1:
                    position = "Defender";
                    break;
                case 2:
                    position = "Midfielder";
                    break;
                case 3:
                    position = "Attacker";
                    break;
                default:
                    position = "Goalkeeper";
                    break;
            }

            switch (position) {
                case "Goalkeeper":
                    gkSkills = (int) (Math.random() * 100);
                    defSkills = (int) (Math.random() * 10);
                    attSkills = (int) (Math.random() * 10);
                    value = (gkSkills * skillsValue * 3 + defSkills * skillsValue + attSkills * skillsValue);
                    break;
                case "Defender":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    value = (gkSkills * skillsValue + defSkills * skillsValue * 3 + attSkills * skillsValue * 1);
                    break;
                case "Midfielder":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    value = (gkSkills * skillsValue + defSkills * skillsValue * 2 + attSkills * skillsValue * 2);
                    break;
                case "Attacker":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    value = (gkSkills * skillsValue * 0 + defSkills * skillsValue + attSkills * skillsValue * 4);
                    break;
                default:
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    value = (gkSkills * skillsValue + defSkills * skillsValue + attSkills * skillsValue);
                    break;
            }

            int wage = (int) (0.1 * value);
            newFoundPlayerDb.createPlayer(99, name, position, gkSkills, defSkills, attSkills, wage, value);
        }
        newFoundPlayerDb.close();
    }

    public void createAllMatches() {
        teamsDb.open();
        int teamsAmount = teamsDb.getAllClubs().getCount();
        resultsDb.open();
        int x1;
        int x2;
        for (x1 = 0; x1 < teamsAmount; x1++) {
            for (x2 = 0; x2 < teamsAmount; x2++) {
                if (x1 != x2) {
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

    public void setRoundsToMatches() {
        teamsDb.open();
        resultsDb.open();

        ArrayList<String> involvedTeams = new ArrayList<>();
        String homeTeam;
        String awayTeam;
        int round;
        int matchNumber;

        for (round = 1; round <= 30; round++) {
            involvedTeams.clear();
            for (matchNumber = 1; matchNumber <= 8; matchNumber++) {
                if (round == 1 && matchNumber == 1) {
                    Cursor cursor = resultsDb.getRoundMatches(0);
                    cursor.moveToFirst();
                    homeTeam = cursor.getString(2);
                    awayTeam = cursor.getString(3);
                    involvedTeams.add(homeTeam);
                    involvedTeams.add(awayTeam);
                    resultsDb.setRoundOfMatch(String.valueOf(round), homeTeam, awayTeam);
                    cursor.close();
                } else {
                    Cursor cursor = resultsDb.getRoundMatches(0);
                    cursor.moveToFirst();
                    homeTeam = cursor.getString(2);
                    awayTeam = cursor.getString(3);

                    while (involvedTeams.contains(homeTeam) || involvedTeams.contains(awayTeam)) {
                        cursor.moveToNext();
                        homeTeam = cursor.getString(2);
                        awayTeam = cursor.getString(3);
                    }

                    involvedTeams.add(homeTeam);
                    involvedTeams.add(awayTeam);
                    resultsDb.setRoundOfMatch(String.valueOf(round), homeTeam, awayTeam);
                    cursor.close();
                }
            }
        }


        resultsDb.close();
        teamsDb.close();
    }

    public void getRoundMatches(int a) {
        resultsDb.open();
        Cursor cursor = resultsDb.getRoundMatches(a);
        for (int c = 0; c < cursor.getCount(); c++) {
            cursor.moveToPosition(c);
            Log.d("teamPoint", "Round " +
                    String.valueOf(a) + ": " + cursor.getString(2) + " - " + cursor.getString(3));

        }
        cursor.close();
        resultsDb.close();
    }

    public void playNextRoundMatches() {
        resultsDb.open();
        teamsDb.open();
        Cursor roundCursor = resultsDb.getScheduledMatchesOfTeam(teamsDb.getMyClubName());
        roundCursor.moveToFirst();
        int round = Integer.valueOf(roundCursor.getString(1));
        roundCursor.close();
        Cursor cursor = resultsDb.getRoundMatches(round);
        int match = cursor.getCount();
        int x1 = 1;
        int x2 = 1;

        for (int a = 0; a < match; a ++){
            cursor.moveToPosition(a);
            String homeTeam = cursor.getString(2);
            String awayTeam = cursor.getString(3);

            if (homeTeam.equals(teamsDb.getMyClubName())){
                x1 = 0;
                moneyForTicket(x1, x2);
            }
            if (awayTeam.equals(teamsDb.getMyClubName())){
                x2 = 0;
                moneyForTicket(x1, x2);
            }

            //Logika meczu
            int homeTeamAttackPower = teamsDb.getAttackClubPower(x1);
            int homeTeamDefencePower = teamsDb.getDefenceClubPower(x1);
            int awayTeamAttackPower = teamsDb.getAttackClubPower(x2);
            int awayTeamDefencePower = teamsDb.getDefenceClubPower(x2);

            double homeScoreLuck = (Math.random()*100 + 30);
            double awayScoreLuck = (Math.random()*100 + 30);

            int homeScore = (int) (Math.min(10, ((homeTeamAttackPower/awayTeamDefencePower)^3)*(homeScoreLuck/100)));
            int awayScore = (int) (Math.min(10, ((awayTeamAttackPower/homeTeamDefencePower)^3)*(awayScoreLuck/100)));
            resultsDb.playMatch(homeTeam, homeScore, awayTeam, awayScore);
            System.out.println("Round " + round + " , match: " + homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam);

        }

            cursor.close();
            teamsDb.close();
            resultsDb.close();

    }

    public void moneyForTicket(int x1, int x2) {
        if (x1 == 0) {
            int ticketPrice = getStadiumInfo().firstValue;
            int seatsAmount = getStadiumInfo().secondValue - (int) (Math.random()*getStadiumInfo().secondValue*0.1);
            int income = ticketPrice * seatsAmount;
            Log.d("teamPoint", "Tickets income: " + String.valueOf(income));
            earnedMoney(Double.valueOf(income));
            playersDb.open();
            int wages = playersDb.getTotalPlayersWages() * (-1);
            playersDb.close();
            Log.d("teamPoint", "Wages outcome: " + String.valueOf(wages));
            earnedMoney(Double.valueOf(wages));
        }

        if (x2 == 0) {
            int ticketPrice = getStadiumInfo().firstValue;
            int seatsAmount = getStadiumInfo().secondValue - (int) (Math.random()*getStadiumInfo().secondValue*0.1);
            double income = 0.1 * ticketPrice * seatsAmount;
            Log.d("teamPoint", "Tickets income: " + String.valueOf(income));
            earnedMoney(Double.valueOf(income));
            playersDb.open();
            int wages = playersDb.getTotalPlayersWages() * (-1);
            playersDb.close();
            Log.d("teamPoint", "Wages outcome: " + String.valueOf(wages));
            earnedMoney(Double.valueOf(wages));
        }
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

}
