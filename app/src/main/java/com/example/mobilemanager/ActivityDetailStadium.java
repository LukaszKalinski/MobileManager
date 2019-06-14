package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDetailStadium extends AppCompatActivity {

    public String loggedUserName;
    DatabaseStadium stadiumDb;
    DatabaseClubFinance financeDb;
    Button activityDetailStadiumUpgradeStadium;
    TextView activityDetailStadiumLevel;
    TextView activityDetailStadiumSeats;
    TextView activityDetailStadiumTicketPrice;
    TextView activityDetailStadiumUpdateCost;
    Button activityStadiumPlusBtn;
    Button activityStadiumMinutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stadium);

        activityDetailStadiumUpgradeStadium = (Button) findViewById(R.id.activityDetailStadiumUpgradeStadium);
        activityDetailStadiumLevel = (TextView) findViewById(R.id.activityDetailStadiumLevel);
        activityDetailStadiumSeats = (TextView) findViewById(R.id.activityDetailStadiumSeats);
        activityDetailStadiumTicketPrice = (TextView) findViewById(R.id.activityDetailStadiumTicketPrice);
        activityDetailStadiumUpdateCost = (TextView) findViewById(R.id.activityDetailStadiumUpdateCost);
        activityStadiumPlusBtn = (Button) findViewById(R.id.activityStadiumPlusBtn);
        activityStadiumMinutBtn = (Button) findViewById(R.id.activityStadiumMinutBtn);

        stadiumDb = new DatabaseStadium(ActivityDetailStadium.this, getLogin());
        financeDb = new DatabaseClubFinance(ActivityDetailStadium.this, getLogin());

        setTextViews();

        activityDetailStadiumUpgradeStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseStadiumLevel();
                setTextViews();
            }
        });

        activityStadiumPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newTicketPrice = getStadiumInfo().getFirstValue() + 1;
                setNewTicketPrice(newTicketPrice);
                setTextViews();
            }
        });

        activityStadiumMinutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newTicketPrice = getStadiumInfo().getFirstValue() - 1;
                setNewTicketPrice(newTicketPrice);
                setTextViews();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setTextViews();

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

    public String getLogin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        return loggedUserName;
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

    public void setNewTicketPrice(int newTicketPrice){
        stadiumDb.open();
        stadiumDb.setNewTicketPrice(newTicketPrice);
        stadiumDb.close();
    }

    public void raiseStadiumLevel(){
        Building stadiumToUpdate = new Building("Stadium", getStadiumInfo().getLevel(), getStadiumInfo().getUpgradeCost(), getStadiumInfo().getFirstValue(), getStadiumInfo().getSecondValue(), getStadiumInfo().getThirdValue());
        int upgradeCost = getStadiumInfo().getUpgradeCost();

        financeDb.open();
        int balance = (int) financeDb.getAccountBalance(getLogin());


        if (balance >= upgradeCost){
            stadiumDb.open();
            stadiumDb.updateBuilding(
                    stadiumToUpdate.getName(),
                    stadiumToUpdate.getLevel(),
                    stadiumToUpdate.getUpgradeCost() * 5,
                    stadiumToUpdate.getFirstValue(),
                    stadiumToUpdate.getSecondValue() * 2,
                    stadiumToUpdate.getThirdValue());
            upgradeCost = upgradeCost * (-1);
            financeDb.refreshClubFinance(getLogin(), upgradeCost,0, 0);
            stadiumDb.close();
            Toast.makeText(this, "Stadium upgraded by +1", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Do not have enough money", Toast.LENGTH_LONG).show();
        }
        financeDb.close();
    }

    public void setTextViews(){
        activityDetailStadiumLevel.setText(String.valueOf(getStadiumInfo().getLevel()));
        activityDetailStadiumSeats.setText(String.valueOf(getStadiumInfo().getSecondValue()));
        activityDetailStadiumTicketPrice.setText(String.valueOf(getStadiumInfo().getFirstValue()) + " $");
        activityDetailStadiumUpdateCost.setText(String.valueOf(getStadiumInfo().getUpgradeCost()) + " $");
    }

}
