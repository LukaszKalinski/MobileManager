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
    DatabaseClubFinance clubFinanceDb;
    DatabaseTeam playersDb;
    TextView activityFinanceAccountBalance;
    TextView activityFinanceTransferBudget;
    TextView activityFinanceWageBudget;
    TextView activityFinanceCurrentWage;
    TextView activityFinanceMaxWage;
    TextView activityFinanceCurrentMaxWage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        clubFinanceDb = new DatabaseClubFinance(this, getLogin());
        playersDb = new DatabaseTeam(this, getLogin());

        activityFinanceAccountBalance = (TextView) findViewById(R.id.activityFinanceAccountBalance);
        setAccountBalance();
        activityFinanceTransferBudget = (TextView) findViewById(R.id.activityFinanceTransferBudget);
        setTransferBudget();
        activityFinanceWageBudget = (TextView) findViewById(R.id.activityFinanceWageBudget);
        setWageBudget();
        activityFinanceCurrentWage = (TextView) findViewById(R.id.activityFinanceCurrentWage);
        getCurrentWage();
        activityFinanceMaxWage = (TextView) findViewById(R.id.activityFinanceMaxWage);
        setMaxPossibleWage();
        activityFinanceCurrentMaxWage = (TextView) findViewById(R.id.activityFinanceCurrentMaxWage);
        getMaxWage();

    }

    @Override
    public void onResume(){
        super.onResume();

        setAccountBalance();
        setTransferBudget();
        setWageBudget();
        getCurrentWage();
        setMaxPossibleWage();
        getMaxWage();
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
        return loggedUserName;
    }

    public void earnedMoney(double incomes){
        clubFinanceDb.open();
        String login = getLogin();
        clubFinanceDb.refreshClubFinance(login,incomes,0,0);
        System.out.println("New BALANCE is: " + clubFinanceDb.getAccountBalance(login));
    }

    public double getBalance(){
        clubFinanceDb.open();
        String login = getLogin();
        double currentBalance = clubFinanceDb.getAccountBalance(login);
        return currentBalance;
    }

    public void setAccountBalance(){
        clubFinanceDb.open();
        Double balance = Double.parseDouble(String.valueOf(clubFinanceDb.getAccountBalance(getLogin())));
        if (balance >= 1000000){
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            activityFinanceAccountBalance.setText(String.valueOf("$ " + df.format(balance/1000000) + " M"));
        } else {
            if (balance < 1000000 && balance >= 1000) {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                activityFinanceAccountBalance.setText(String.valueOf("$ " + df.format(balance/1000) + " k $"));
            } else {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                activityFinanceAccountBalance.setText(String.valueOf("$ " + df.format(balance) + " $"));
            }
        }
        clubFinanceDb.close();
    }

    public void setTransferBudget(){
        clubFinanceDb.open();
        Double transferBudget = Double.valueOf(clubFinanceDb.getTransferBudget(getLogin()));
        if (transferBudget>= 1000000){
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            activityFinanceTransferBudget.setText(String.valueOf("$ " + df.format(transferBudget/1000000) + " M"));
        } else {
            if (transferBudget < 1000000 && transferBudget >= 1000) {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                activityFinanceTransferBudget.setText(String.valueOf("$ " + df.format(transferBudget/1000) + " k"));
            } else {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                activityFinanceTransferBudget.setText(String.valueOf("$ " + df.format(transferBudget) + " $"));
            }
        }
        clubFinanceDb.close();
    }

    public void setWageBudget(){
        clubFinanceDb.open();
        Double wageBudget = Double.valueOf(clubFinanceDb.getWageBudget(getLogin()));
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        activityFinanceWageBudget.setText(String.valueOf("$ " + df.format(wageBudget/1000) + " k per week"));
        clubFinanceDb.close();
    }

    public void getCurrentWage(){
        playersDb.open();
        int currentWage = playersDb.getTotalPlayersWages();
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        activityFinanceCurrentWage.setText(String.valueOf("$ " + df.format(currentWage/1000) + " k per week"));
        playersDb.close();
    }

    public void setMaxPossibleWage(){
        clubFinanceDb.open();
        Double maxWage = Double.valueOf(clubFinanceDb.getMaxPossWage(getLogin()));
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        activityFinanceMaxWage.setText(String.valueOf("$ " + df.format(maxWage/1000) + " k per week"));
        clubFinanceDb.close();
    }

    public void getMaxWage(){
        playersDb.open();
        int currentMaxWage = playersDb.getCurrentMaxPlayerWage();
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        activityFinanceCurrentMaxWage.setText(String.valueOf("$ " + df.format(currentMaxWage/1000) + " k per week"));
        playersDb.close();
    }

}
