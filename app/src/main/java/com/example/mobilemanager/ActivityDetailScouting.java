package com.example.mobilemanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
            int skillsValue = 100000;
            String[] firstName = {"Antoni", "Jakub", "Jan", "Szymon", "Aleksander", "Franciszek", "Filip", "Mikolaj", "Wojciech", "Kacper", "Adam", "Marcel", "Stanislaw", "Michal", "Lukasz", "Wiktor", "Leon", "Piotr", "Nikodem", "Igor", "Ignacy", "Sebastian"};
            String[] lastName = {"Nowak", "Kowalski", "Wisniewski", "Wojcik", "Wojcicki", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski" , "Wozniak", "Dabrowski", "Kozlowski", "Jankowski", "Wojciechowski", "Kwiatkowski", "Mazur", "Krawczyk"};

            String name = firstName[(int) (Math.random() * firstName.length)] + " " + lastName[(int) (Math.random() * lastName.length)];

            int gkSkills;
            int defSkills;
            int attSkills;
            int value;

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

            switch (position){
                case "Goalkeeper":
                    gkSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    defSkills = (int) (Math.random() * 10);
                    attSkills = (int) (Math.random() * 10);
                    value = (gkSkills * skillsValue * 3 + defSkills * skillsValue + attSkills * skillsValue);
                    break;
                case "Defender":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    attSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    value = (gkSkills * skillsValue + defSkills * skillsValue * 3 + attSkills * skillsValue);
                    break;
                case "Midfielder":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    attSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    value = (gkSkills * skillsValue + defSkills * skillsValue * 2 + attSkills * skillsValue * 2);
                    break;
                case "Attacker":
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    attSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    value = (gkSkills * skillsValue * 0 + defSkills * skillsValue + attSkills * skillsValue * 4);
                    break;
                default:
                    gkSkills = (int) (Math.random() * 10);
                    defSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    attSkills = (int) (Math.max(Math.random() * 100, currentLevel * 10));
                    value = (gkSkills * skillsValue + defSkills * skillsValue + attSkills * skillsValue);
                    break;
            }

            int wage = (int) (0.1 * value);
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
