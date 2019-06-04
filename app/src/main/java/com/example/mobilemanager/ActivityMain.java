package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityMain extends AppCompatActivity {

    String loggedUserName;
    DatabaseClubFinance clubFinanceDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clubFinanceDb = new DatabaseClubFinance(this,getLogin());

        //Checking if Databases exists
        checkIfClubFinanceDbExist();



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
            System.out.println("DatabaseClubFinance already EXISTS");
            System.out.println("and BALANCE is: " + clubFinanceDb.getAccountBalance(getLogin()));
        } else {
            clubFinanceDb.firstAddition(getLogin());
        }
        clubFinanceDb.close();
    }

    public void earnedMoney(double incomes){
        clubFinanceDb.open();
        String login = getLogin();
        clubFinanceDb.refreshClubFinance(login,incomes,0,0);
        System.out.println("New BALANCE is: " + clubFinanceDb.getAccountBalance(login));
    }
}
