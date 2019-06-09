package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityMain extends AppCompatActivity {

    String loggedUserName;
    DatabaseClubFinance clubFinanceDb;
    DatabaseTeams teamsDb;
    DatabaseResults resultsDb;
    DatabaseTeam playersDb;

    Button getResultsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResultsButton = (Button) findViewById(R.id.getResultsButton);

        clubFinanceDb = new DatabaseClubFinance(this, getLogin());
        teamsDb = new DatabaseTeams(this, getLogin());
        resultsDb = new DatabaseResults(this, getLogin());
        playersDb = new DatabaseTeam(this, getLogin());

        //Checking if Databases exists
        checkIfClubFinanceDbExist();
        checkIfPlayersDbExist();
        checkIfTeamsDbExist();
        checkIfResultsDbExist();

        getResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityResults.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
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
                finish();
                return (true);
            case R.id.team:
                intent = new Intent(this, ActivityTeam.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.calendar:
                intent = new Intent(this, ActivityCalendar.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.stadium:
                intent = new Intent(this, ActivityStadium.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.finance:
                intent = new Intent(this, ActivityFinance.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.mainPage:
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                startActivity(intent);
                finish();
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
                int attackPower = (int) (Math.random() * 2000);
                int defencePower = (int) (Math.random() * 2000);
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
        } else {
            System.out.println("Need to create first match later");
        }
        resultsDb.close();
    }

    public void earnedMoney(double incomes) {
        clubFinanceDb.open();
        String login = getLogin();
        clubFinanceDb.refreshClubFinance(login, incomes, 0, 0);
        System.out.println("New BALANCE is: " + clubFinanceDb.getAccountBalance(login));
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

    public void createPlayer(int number, int nextNumber, String position, int startNumber, int end){

        int skillsValue = 100000;
        String[] firstName = {"Antoni", "Jakub", "Jan", "Szymon", "Aleksander", "Franciszek", "Filip", "Mikolaj", "Wojciech", "Kacper", "Adam", "Marcel", "Stanislaw", "Michal", "Lukasz", "Wiktor", "Leon", "Piotr", "Nikodem", "Igor", "Ignacy", "Sebastian"};
        String[] lastName = {"Nowak", "Kowalski", "Wisniewski", "Wojcik", "Wojcicki", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski" , "Wozniak", "Dabrowski", "Kozlowski", "Jankowski", "Wojciechowski", "Kwiatkowski", "Mazur", "Krawczyk"};

        for (int a = startNumber; a <= end; a++) {
            number = number + nextNumber;
            String name = firstName[(int) (Math.random() * firstName.length)] + " " + lastName[(int) (Math.random() * lastName.length)];

            int gkSkills;
            int defSkills;
            int attSkills;
            int value;

            switch (position){
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
            playersDb.createPlayer(number, name, position, gkSkills, defSkills, attSkills, wage, value);
        }
    }

}
