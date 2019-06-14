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
import android.widget.TextView;

public class ActivityDetailPlayer extends AppCompatActivity {

    DatabaseTeam playersDb;
    DatabaseClubFinance financeDb;
    public String loggedUserName;
    int clickedPosition;
    Player selectedPlayer;
    TextView activityDetailPlayerName;
    TextView activityDetailPlayerNumber;
    TextView activityDetailPlayerPosition;
    TextView activityDetailPlayerValue;
    TextView activityDetailPlayerWage;
    TextView activityDetailPlayerGkSkills;
    TextView activityDetailPlayerDefSkills;
    TextView activityDetailPlayerAttSkills;
    Button activityDetailPlayerSellBtn;
    Button activityDetailPlayerCloseBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player);

        playersDb = new DatabaseTeam(this, getLogin());
        financeDb = new DatabaseClubFinance(this, getLogin());
        selectedPlayer = getPlayer();

        activityDetailPlayerName = (TextView) findViewById(R.id.activityDetailPlayerName);
        activityDetailPlayerName.setText(String.valueOf(selectedPlayer.getName()));
        activityDetailPlayerNumber = (TextView) findViewById(R.id.activityDetailPlayerNumber);
        activityDetailPlayerNumber.setText(String.valueOf(selectedPlayer.getNumber()));
        activityDetailPlayerPosition = (TextView) findViewById(R.id.activityDetailPlayerPosition);
        activityDetailPlayerPosition.setText(String.valueOf(selectedPlayer.getPosition()));
        activityDetailPlayerValue = (TextView) findViewById(R.id.activityDetailPlayerValue);
        setValue();
        activityDetailPlayerWage = (TextView) findViewById(R.id.activityDetailPlayerWage);
        setWage();
        activityDetailPlayerGkSkills = (TextView) findViewById(R.id.activityDetailPlayerGkSkills);
        activityDetailPlayerGkSkills.setText(String.valueOf(selectedPlayer.getGkSkills()));
        activityDetailPlayerDefSkills = (TextView) findViewById(R.id.activityDetailPlayerDefSkills);
        activityDetailPlayerDefSkills.setText(String.valueOf(selectedPlayer.getDefSkills()));
        activityDetailPlayerAttSkills = (TextView) findViewById(R.id.activityDetailPlayerAttSkills);
        activityDetailPlayerAttSkills.setText(String.valueOf(selectedPlayer.getAttSkills()));

        activityDetailPlayerCloseBtn = (Button) findViewById(R.id.activityDetailPlayerCloseBtn);
        activityDetailPlayerSellBtn = (Button) findViewById(R.id.activityDetailPlayerSellBtn);

        activityDetailPlayerSellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playersDb.open();
                Double value = selectedPlayer.getValue();
                playersDb.sellPlayer(selectedPlayer.getNumber());
                Log.d("teamPoint", "Selling player number: " + String.valueOf(selectedPlayer.getNumber()));
                playersDb.close();

                financeDb.open();
                financeDb.refreshClubFinance(loggedUserName, value, 0, 0);
                financeDb.close();


                Intent intent = new Intent(ActivityDetailPlayer.this, ActivityTeam.class);
                startActivity(intent);
                finish();
            }
        });

        activityDetailPlayerCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public Player getPlayer(){

        Bundle extras = getIntent().getExtras();
        clickedPosition = Integer.parseInt(extras.getString("sendingPosition"));

        playersDb.open();

        Cursor cursor = playersDb.getAllPlayers();
        cursor.moveToPosition(clickedPosition);
        int number = Integer.valueOf(cursor.getString(1));
        String name = cursor.getString(2);
        String position = cursor.getString(3);
        int gkSkills = Integer.valueOf(cursor.getString(4));
        int defSkills = Integer.valueOf(cursor.getString(5));
        int attSkills = Integer.valueOf(cursor.getString(6));
        int wage = Integer.valueOf(cursor.getString(7));
        int value = Integer.valueOf(cursor.getString(8));
        playersDb.close();

        Player result = new Player(number, name, position, gkSkills, defSkills, attSkills, wage, value);
        return result;
    }

    public void setValue(){
        Double balance = Double.parseDouble(String.valueOf(selectedPlayer.getValue()));
        if (balance >= 1000000){
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            activityDetailPlayerValue.setText(String.valueOf("$ " + df.format(balance/1000000) + " M"));
        } else {
            if (balance < 1000000 && balance >= 1000) {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(0);
                df.setMinimumFractionDigits(0);
                activityDetailPlayerValue.setText(String.valueOf("$ " + df.format(balance)));
            } else {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(0);
                df.setMinimumFractionDigits(0);
                activityDetailPlayerValue.setText(String.valueOf("$ " + df.format(balance)));
            }
        }
    }

    public void setWage(){
        Double balance = Double.parseDouble(String.valueOf(selectedPlayer.getWage()));
        if (balance >= 1000000){
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            activityDetailPlayerWage.setText(String.valueOf("$ " + df.format(balance/1000000) + " M per week"));
        } else {
            if (balance < 1000000 && balance >= 1000) {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(0);
                df.setMinimumFractionDigits(0);
                activityDetailPlayerWage.setText(String.valueOf("$ " + df.format(balance) + " per week"));
            } else {
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(0);
                df.setMinimumFractionDigits(0);
                activityDetailPlayerWage.setText(String.valueOf("$ " + df.format(balance) + " per week"));
            }
        }
    }

}
