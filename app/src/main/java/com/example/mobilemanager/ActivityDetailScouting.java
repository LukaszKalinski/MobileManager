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

public class ActivityDetailScouting extends AppCompatActivity {

    DatabaseStadium scoutingDb;
    DatabaseClubFinance financeDb;
    DatabaseNewFoundPlayer newFoundPlayerDb;
    DatabaseTeam teamDb;
    public String loggedUserName;
    TextView activityDetailScoutingLevel;
    TextView activityDetailScoutingUpgradeCost;
    TextView activityDetailScoutingUnitCost;
    TextView activityDetailScoutingFoundPlayer;
    Button activityDetailScoutingUpgrade;
    Button activityDetailScoutingFindAnother;
    Button activityDetailScoutingBuyPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scouting);

        activityDetailScoutingLevel = (TextView) findViewById(R.id.activityDetailScoutingLevel);
        activityDetailScoutingUpgradeCost = (TextView) findViewById(R.id.activityDetailScoutingUpgradeCost);
        activityDetailScoutingUnitCost = (TextView) findViewById(R.id.activityDetailScoutingUnitCost);
        activityDetailScoutingFoundPlayer = (TextView) findViewById(R.id.activityDetailScoutingFoundPlayer);
        activityDetailScoutingUpgrade = (Button) findViewById(R.id.activityDetailScoutingUpgrade);
        activityDetailScoutingFindAnother = (Button) findViewById(R.id.activityDetailScoutingFindAnother);
        activityDetailScoutingBuyPlayer = (Button) findViewById(R.id.activityDetailScoutingBuyPlayer);

        scoutingDb = new DatabaseStadium(ActivityDetailScouting.this, getLogin());
        financeDb = new DatabaseClubFinance(ActivityDetailScouting.this, getLogin());
        newFoundPlayerDb = new DatabaseNewFoundPlayer(ActivityDetailScouting.this, getLogin());
        teamDb = new DatabaseTeam(ActivityDetailScouting.this, getLogin());

        setTextViews();
        setNewPlayerText();

        activityDetailScoutingUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseScoutingLevel();
                setTextViews();
            }
        });

        activityDetailScoutingFindAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAnother();
                setNewPlayerText();
                setTextViews();
            }
        });


        activityDetailScoutingBuyPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyThisOne();
                setNewPlayerText();
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

    public Building getScoutingInfo(){
        scoutingDb.open();
        Cursor cursor = scoutingDb.getSpecificBuilding("Scouting");
        cursor.moveToFirst();

        int currentLevel = Integer.parseInt(cursor.getString(2));
        int upgradeCost = Integer.parseInt(cursor.getString(3));
        int playerPrice = Integer.parseInt(cursor.getString(4));
        cursor.close();

        Building scouting = new Building("Scouting", currentLevel, upgradeCost, playerPrice, 0, 0);
        return scouting;
    }

    public void raiseScoutingLevel(){
        Building stadiumToUpdate = new Building("Scouting", getScoutingInfo().getLevel(), getScoutingInfo().getUpgradeCost(), getScoutingInfo().getFirstValue(), getScoutingInfo().getSecondValue(), getScoutingInfo().getThirdValue());
        int upgradeCost = getScoutingInfo().getUpgradeCost();

        financeDb.open();
        int balance = (int) financeDb.getAccountBalance(getLogin());


        if (balance >= upgradeCost){
            scoutingDb.open();
            scoutingDb.updateBuilding(
                    stadiumToUpdate.getName(),
                    stadiumToUpdate.getLevel(),
                    stadiumToUpdate.getUpgradeCost() * 5,
                    stadiumToUpdate.getFirstValue() * 2 * stadiumToUpdate.getLevel(),
                    stadiumToUpdate.getSecondValue(),
                    stadiumToUpdate.getThirdValue());
            scoutingDb.close();
            upgradeCost = upgradeCost * (-1);
            financeDb.refreshClubFinance(getLogin(),upgradeCost,0, 0);
            Toast.makeText(this, "Scouting upgraded by +1", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Do not have enough money", Toast.LENGTH_LONG).show();
        }

        financeDb.close();
    }

    public void setTextViews(){
        activityDetailScoutingLevel.setText(String.valueOf(getScoutingInfo().getLevel()));
        activityDetailScoutingUpgradeCost.setText(String.valueOf(getScoutingInfo().getUpgradeCost()) + " $");
        activityDetailScoutingUnitCost.setText(String.valueOf(getScoutingInfo().getFirstValue()) + " $");

    }

    public void buyThisOne(){
        financeDb.open();
        int balance = (int) financeDb.getAccountBalance(getLogin());
        int charge = getScoutingInfo().getFirstValue();

        if (balance >= charge){
            Player player = getPlayer(0);
            teamDb.open();
            Cursor cursor = teamDb.getAllPlayers();
            cursor.moveToLast();
            teamDb.createPlayer(Integer.parseInt(cursor.getString(1)) + 1, player.getName(), player.getPosition(), player.getGkSkills(),
                    player.getDefSkills(), player.getAttSkills(), (int) player.getWage(), (int) player.getValue());
            teamDb.close();
            Toast.makeText(this, "Bought player", Toast.LENGTH_LONG).show();
            findAnother();}
        else {
                Toast.makeText(this, "Do not have enough money", Toast.LENGTH_LONG).show();
            }

            financeDb.close();
    }

    public void findAnother(){
        financeDb.open();
        int balance = (int) financeDb.getAccountBalance(getLogin());
        int charge = getScoutingInfo().getFirstValue();
        int currentLevel = getScoutingInfo().getLevel();

        if (balance >= charge){
            newFoundPlayerDb.open();
            int value;
            int skillsValue = 1000;
            int gkSkills;
            int defSkills;
            int attSkills;
            int factor;
            String[] firstName = {"Antoni", "Jakub", "Jan", "Szymon", "Aleksander", "Franciszek", "Filip", "Mikolaj", "Wojciech", "Kacper", "Adam", "Marcel", "Stanislaw", "Michal", "Lukasz", "Wiktor", "Leon", "Piotr", "Nikodem", "Igor", "Ignacy", "Sebastian"};
            String[] lastName = {"Nowak", "Kowalski", "Wisniewski", "Wojcik", "Wojcicki", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak", "Dabrowski", "Kozlowski", "Jankowski", "Wojciechowski", "Kwiatkowski", "Mazur", "Krawczyk"};

            String name = firstName[(int) (Math.random() * firstName.length)] + " " + lastName[(int) (Math.random() * lastName.length)];

            String position;
            int a = (int) (Math.random()*4);
            switch (a){
                case 0:
                    position = "Goalkeeper";
                    break;
                case 1:
                    position = "Defender";
                    break;
                case 2:
                    position = "Midfielder";
                    break;
                case 3:
                    position = "Attacker";
                    break;
                default:
                    position = "Goalkeeper";
                    break;
            }

            switch (position) {
                case "Goalkeeper":
                    gkSkills = (int) (Math.random() * 100);
                    defSkills = (int) (Math.random() * 10);
                    attSkills = (int) (Math.random() * 10);
                    factor = (int) Math.pow(2, (int) (gkSkills / 10));
                    value = (int) (Math.max(1000, gkSkills * skillsValue * factor));
                    break;
                case "Defender":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (defSkills / 10));
                    value = (int) (Math.max(1000, defSkills * skillsValue * factor + attSkills * skillsValue));
                    break;
                case "Midfielder":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) ((defSkills + attSkills) / 20));
                    value = (int) (Math.max(1000, defSkills * skillsValue * factor + attSkills * skillsValue * factor));
                    break;
                case "Attacker":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (attSkills / 10));
                    value = (int) (Math.max(1000, attSkills * skillsValue * factor + skillsValue * defSkills));
                    break;
                default:
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.random() * 100);
                    attSkills = (int) (Math.random() * 100);
                    factor = (int) Math.pow(2, (int) (gkSkills / 10));
                    value = (int) (Math.max(1000, gkSkills * skillsValue * factor));
                    break;
            }

            int wage = (int) (value * 0.1 / 51);
            newFoundPlayerDb.findNextPlayer(name, position, gkSkills, defSkills, attSkills, wage, value);
            newFoundPlayerDb.close();
            charge = charge * (-1);
            financeDb.refreshClubFinance(getLogin(), charge,0, 0);
            setNewPlayerText();
        } else {
            Toast.makeText(this, "Do not have enough money", Toast.LENGTH_LONG).show();
        }

        financeDb.close();

    }

    public void setNewPlayerText(){
        String result;
        String name = getPlayer(0).getName();
        String position = getPlayer(0).getPosition();
        int gkSkills = getPlayer(0).getGkSkills();
        int defSkills = getPlayer(0).getDefSkills();
        int attSkills = getPlayer(0).getAttSkills();
        result = name + " (" + position + ") [" + String.valueOf(gkSkills) + ", " + String.valueOf(defSkills) + ", " + String.valueOf(attSkills) + "]";
        activityDetailScoutingFoundPlayer.setText(result);
    }

    public Player getPlayer(int a){
        newFoundPlayerDb.open();

        Cursor cursor = newFoundPlayerDb.getAllPlayers();
        cursor.moveToPosition(a);
        int number = Integer.valueOf(cursor.getString(1));
        String name = cursor.getString(2);
        String position = cursor.getString(3);
        int gkSkills = Integer.valueOf(cursor.getString(4));
        int defSkills = Integer.valueOf(cursor.getString(5));
        int attSkills = Integer.valueOf(cursor.getString(6));
        int wage = Integer.valueOf(cursor.getString(7));
        int value = Integer.valueOf(cursor.getString(8));
        newFoundPlayerDb.close();

        Player result = new Player(number, name, position, gkSkills, defSkills, attSkills, wage, value);
        return result;
    }



}
