package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDetailPlayer extends AppCompatActivity {

    DatabaseTeam playersDb;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player);

        playersDb = new DatabaseTeam(this, getLogin());
        selectedPlayer = getPlayer();

        activityDetailPlayerName = (TextView) findViewById(R.id.activityDetailPlayerName);
        activityDetailPlayerName.setText(String.valueOf(selectedPlayer.getName()));
        activityDetailPlayerNumber = (TextView) findViewById(R.id.activityDetailPlayerNumber);
        activityDetailPlayerNumber.setText(String.valueOf(selectedPlayer.getNumber()));
        activityDetailPlayerPosition = (TextView) findViewById(R.id.activityDetailPlayerPosition);
        activityDetailPlayerPosition.setText(String.valueOf(selectedPlayer.getPosition()));
        activityDetailPlayerValue = (TextView) findViewById(R.id.activityDetailPlayerValue);
        activityDetailPlayerValue.setText(String.valueOf(selectedPlayer.getValue()));
        activityDetailPlayerWage = (TextView) findViewById(R.id.activityDetailPlayerWage);
        activityDetailPlayerWage.setText(String.valueOf(selectedPlayer.getWage()));
        activityDetailPlayerGkSkills = (TextView) findViewById(R.id.activityDetailPlayerGkSkills);
        activityDetailPlayerGkSkills.setText(String.valueOf(selectedPlayer.getGkSkills()));
        activityDetailPlayerDefSkills = (TextView) findViewById(R.id.activityDetailPlayerDefSkills);
        activityDetailPlayerDefSkills.setText(String.valueOf(selectedPlayer.getDefSkills()));
        activityDetailPlayerAttSkills = (TextView) findViewById(R.id.activityDetailPlayerAttSkills);
        activityDetailPlayerAttSkills.setText(String.valueOf(selectedPlayer.getAttSkills()));

        activityDetailPlayerSellBtn = (Button) findViewById(R.id.activityDetailPlayerSellBtn);

        activityDetailPlayerSellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailPlayer.this, ActivityTeam.class);
                startActivity(intent);
                finish();
            }
        });



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

}
