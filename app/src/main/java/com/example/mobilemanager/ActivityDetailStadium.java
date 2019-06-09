package com.example.mobilemanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stadium);

        activityDetailStadiumUpgradeStadium = (Button) findViewById(R.id.activityDetailStadiumUpgradeStadium);
        activityDetailStadiumLevel = (TextView) findViewById(R.id.activityDetailStadiumLevel);
        activityDetailStadiumSeats = (TextView) findViewById(R.id.activityDetailStadiumSeats);
        activityDetailStadiumTicketPrice = (TextView) findViewById(R.id.activityDetailStadiumTicketPrice);
        activityDetailStadiumUpdateCost = (TextView) findViewById(R.id.activityDetailStadiumUpdateCost);

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

    }

    @Override
    public void onResume(){
        super.onResume();
        setTextViews();

    }

    public String getLogin() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        loggedUserName = sp1.getString("Username", null);
        Toast.makeText(this, "LOGIN" + loggedUserName, Toast.LENGTH_LONG).show();
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

    public void setNewTicketPrice(){}

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
