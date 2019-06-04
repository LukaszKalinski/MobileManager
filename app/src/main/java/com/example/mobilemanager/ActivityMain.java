package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityMain extends AppCompatActivity {

    String loggedUserName;
    DatabaseClubFinance clubFinanceDb;
    DatabaseTeams teamdDb;
    DatabaseResults resultsDb;

    Button getResultsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResultsButton = (Button) findViewById(R.id.getResultsButton);

        clubFinanceDb = new DatabaseClubFinance(this,getLogin());
        teamdDb = new DatabaseTeams(this, getLogin());
        resultsDb = new DatabaseResults(this, getLogin());

        //Checking if Databases exists
        checkIfClubFinanceDbExist();
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
        Toast.makeText(this,"LOGIN" + loggedUserName, Toast.LENGTH_LONG).show();
        return loggedUserName;
    }

    public void checkIfClubFinanceDbExist(){
        clubFinanceDb.open();
        if (clubFinanceDb.getRecords().getCount() > 0){
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and BALANCE is: " + clubFinanceDb.getAccountBalance(getLogin()));
        } else {
            clubFinanceDb.firstAddition(getLogin());
        }
        clubFinanceDb.close();
    }

    public void checkIfTeamsDbExist(){
        teamdDb.open();
        if (teamdDb.getAllClubs().getCount() > 0){
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and amount of teams is: " + String.valueOf(teamdDb.getAllClubs().getCount()));
        } else {
            teamdDb.createTeams(getLogin());
            for (int i = 0; i < 15; i ++){
                teamdDb.createTeams("Player" + i);
            }
        }
        teamdDb.close();
    }

    public void checkIfResultsDbExist(){
        resultsDb.open();
        if (resultsDb.getAllResults().getCount() > 0){
            System.out.println("DatabaseResults already EXISTS");
            System.out.println("and amount of matches are: " + String.valueOf(resultsDb.getAllResults().getCount()));
        } else {
            System.out.println("Need to create first match later");
        }
        resultsDb.close();
    }

    public void earnedMoney(double incomes){
        clubFinanceDb.open();
        String login = getLogin();
        clubFinanceDb.refreshClubFinance(login,incomes,0,0);
        System.out.println("New BALANCE is: " + clubFinanceDb.getAccountBalance(login));
    }
}
