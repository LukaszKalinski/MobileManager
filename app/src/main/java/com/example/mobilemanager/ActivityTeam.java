package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityTeam extends AppCompatActivity {

    String loggedUserName;
    ListView activityTeamListView;
    DatabaseTeams teamsDb;
    DatabaseTeam playersDb;
    public static ArrayList<Player> playersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        activityTeamListView = (ListView) findViewById(R.id.activityTeamListView);

        teamsDb = new DatabaseTeams(this, getLogin());
        playersDb = new DatabaseTeam(this, getLogin());

        refreshPlayersTable();

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

    public void setTeamPower(int a, int attackPower, int defencePower){
        teamsDb.open();
        teamsDb.setClubPower(teamsDb.getClubName(a),attackPower, defencePower);
        Log.d("teamPoint", teamsDb.getClubName(a) +
                " with attackPower: " + teamsDb.getAttackClubPower(a) +
                ", with defencePower: " + teamsDb.getDefenceClubPower(a));
        teamsDb.close();
    }

    public Player getPlayer(int a){
        playersDb.open();

        Cursor cursor = playersDb.getAllPlayers();
        cursor.moveToPosition(a);
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

    public ArrayList<Player> loadPlayerTable(){
        ArrayList<Player> playersList =  new ArrayList<>();
        playersDb.open();
        int aMax = playersDb.getAllPlayers().getCount();
        int a;
        for (a = 0; a < aMax; a++){
            getPlayer(a);

            Player singlePlayer = new Player(getPlayer(a).getNumber(), getPlayer(a).getName(), getPlayer(a).getPosition(), getPlayer(a).getGkSkills(), getPlayer(a).getDefSkills(), getPlayer(a).getAttSkills(), (int) getPlayer(a).getWage(), (int) getPlayer(a).getValue());
            playersList.add(singlePlayer);
        }
        playersDb.close();
        return playersList;
    }

    public void refreshPlayersTable(){
        playersList = loadPlayerTable();
        PlayersAdapter adapter = new PlayersAdapter(getApplicationContext(), playersList);
        activityTeamListView.setAdapter(adapter);

    }
}
