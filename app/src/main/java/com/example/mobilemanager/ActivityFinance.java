package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ActivityFinance extends AppCompatActivity {

    String loggedUserName;
    DatabaseFinance financeDb;
    TextView activityFinanceAccountBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        financeDb = new DatabaseFinance(this, getLogin());
        activityFinanceAccountBalance = (TextView) findViewById(R.id.activityFinanceAccountBalance);
        setAccountBalance();



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

    public void earnedMoney(int incomes){
        financeDb.open();
        String login = getLogin();
        int currentBalance = financeDb.getAccountBalance(login);
        financeDb.refreshAccountBalance(login,currentBalance,incomes);
        int newBalance = financeDb.getAccountBalance(login);
        System.out.println("New BALANCE is: " + newBalance);
    }

    public int getBalance(){
        financeDb.open();
        String login = getLogin();
        int currentBalance = financeDb.getAccountBalance(login);
        return currentBalance;
    }

    public void setAccountBalance(){
        if (getBalance()>= 1000000){
            Double balance = Double.parseDouble(String.valueOf(getBalance()/1000000));
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            df.format(balance);
            activityFinanceAccountBalance.setText(String.valueOf(balance + " mln"));
        } else {
            if (getBalance() < 1000000 && getBalance() >= 1000) {
                Double balance = Double.parseDouble(String.valueOf(getBalance()/1000));
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                df.format(balance);
                activityFinanceAccountBalance.setText(String.valueOf(balance + " k"));
            } else {
                activityFinanceAccountBalance.setText(String.valueOf(getBalance()));
            }
        }
    }
}
