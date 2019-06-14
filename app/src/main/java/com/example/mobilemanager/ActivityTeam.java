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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityTeam extends AppCompatActivity {

    String loggedUserName;
    ListView activityTeamListView;
    TextView activityTeamPower;
    Spinner activityTeamStyle;
    DatabaseTeams teamsDb;
    DatabaseTeam playersDb;
    public static ArrayList<Player> playersList = new ArrayList<>();
    public int defPower;
    public int attPower;
    public int results[];
    Button playersFoundedByScouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        activityTeamListView = (ListView) findViewById(R.id.activityTeamListView);
        activityTeamStyle = (Spinner) findViewById(R.id.activityChosenTeamStyle);
        activityTeamPower = (TextView) findViewById(R.id.activityTeamPower);
        playersFoundedByScouts = (Button) findViewById(R.id.playersFoundedByScouts);

        teamsDb = new DatabaseTeams(this, getLogin());
        playersDb = new DatabaseTeam(this, getLogin());

        refreshPlayersTable();
        createStyleList();
        activityTeamStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosen = parent.getItemAtPosition(position).toString();
                int Def;
                int Mid;
                int Att;

                int gkSkillsTotal;
                int defSkillsTotal[];
                int midSkillsTotal[];
                int attSkillsTotal[];

                switch (chosen) {
                    case "1-4-4-2":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 4;
                        Mid = 4;
                        Att = 2;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);

                        break;
                    case "1-5-4-1":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 5;
                        Mid = 4;
                        Att = 1;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-5-3-2":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 5;
                        Mid = 3;
                        Att = 2;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-4-5-1":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 4;
                        Mid = 5;
                        Att = 1;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-4-3-3":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 4;
                        Mid = 3;
                        Att = 3;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-3-5-2":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 3;
                        Mid = 5;
                        Att = 2;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-3-4-3":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 3;
                        Mid = 4;
                        Att = 3;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                    case "1-3-3-4":
                        Toast.makeText(parent.getContext(), "You have chosen: " + chosen, Toast.LENGTH_SHORT).show();
                        Def = 3;
                        Mid = 3;
                        Att = 4;

                        gkSkillsTotal = getGroupSpecificSkills("Goalkeeper",1)[0];
                        defSkillsTotal = getGroupSpecificSkills("Defender", Def);
                        midSkillsTotal = getGroupSpecificSkills("Midfielder", Mid);
                        attSkillsTotal = getGroupSpecificSkills("Attacker", Att);

                        attPower = calculateAttPower(gkSkillsTotal, defSkillsTotal[2], midSkillsTotal[2], attSkillsTotal[2]);
                        defPower = calculateDefPower(gkSkillsTotal, defSkillsTotal[1], midSkillsTotal[1], attSkillsTotal[1]);
                        break;
                }

                activityTeamPower.setText("Defpower: " + defPower + ", Attpower: " + attPower);
                setTeamPower(0, attPower, defPower);
            }

            @Override
            public void onNothingSelected(AdapterView<?> activityTeamStyle) {}
        });

        playersFoundedByScouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTeam.this, ActivityDetailScouting.class);
                startActivity(intent);
            }
        });

        activityTeamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listViewPosition = String.valueOf(position);
                sendingListViewPosition(listViewPosition);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
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

    public void createStyleList(){
        String[] styles = new String[] {"1-4-4-2", "1-5-4-1", "1-5-3-2", "1-4-5-1", "1-4-3-3", "1-3-5-2", "1-3-4-3", "1-3-3-4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, styles);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTeamStyle.setAdapter(adapter);

    }

    public int[] getGroupSpecificSkills(final String position, int howManyPlayers){
        ArrayList<Player> list = new ArrayList<>();
        list.clear();
        for (int a = 0; a < playersList.size(); a ++){
            if (playersList.get(a).getPosition().equals(position)){
                Log.d("teamPoint", "Added " + playersList.get(a).getName());
                list.add(playersList.get(a));
            }
        }

        Log.d("teamPoint", position + " before selection - " + String.valueOf(list.size()));

        //Sorting by skills

        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                switch(position){
                    case "Goalkeeper":
                        return o2.getGkSkills() - o1.getGkSkills();
                    case "Defender":
                        return o2.getDefSkills() - o1.getDefSkills();
                    case "Midfielder":
                        return (o2.getAttSkills() + o2.getDefSkills()) - (o1.getAttSkills() + o1.getDefSkills());
                    case "Attacker":
                        return o2.getAttSkills() - o1.getAttSkills();
                    default:
                        return o2.getGkSkills() - o1.getGkSkills();
                }
            }
        });

        //printing list after sort
        int x;
        for (x = 0; x < list.size(); x++){
            Log.d("teamPoint", "Sorted by skills " + list.get(x).getName() + " with skills ["
                            + String.valueOf(list.get(x).getGkSkills()) + ", "
                            + String.valueOf(list.get(x).getDefSkills()) + ", "
                            + String.valueOf(list.get(x).getAttSkills()) + "]"
                    );
        }

        //removing not needed players
        int b;
        int bMax;
        for (bMax = list.size() - 1; bMax >= howManyPlayers; bMax--){
            Log.d("teamPoint", "Removed " + list.get(bMax).getName());
            list.remove(bMax);
        }

        //printing list
        int a1 = 0;
        int a2 = 0;
        int a3 = 0;

        int c;
        Log.d("teamPoint", position + " after selection - " + String.valueOf(list.size()));
        for (c = 0; c < list.size(); c++){

            a1 = a1 + list.get(c).getGkSkills();
            a2 = a2 + list.get(c).getDefSkills();
            a3 = a3 + list.get(c).getAttSkills();

            results = new int[] {a1,a2,a3};

            Log.d("teamPoint", "ListContains " + list.get(c).getName()
                    + " (" + list.get(c).getPosition() + ") and separated ["
                    + String.valueOf(list.get(c).getGkSkills()) + ", "
                    + String.valueOf(list.get(c).getDefSkills()) + ", "
                    + String.valueOf(list.get(c).getAttSkills()) + "]");
        }

        return results;
    }

    public int calculateDefPower(int gkSkillsTotal, int defSkillsTotal, int midSkillsTotal, int attSkillsTotal){
        int defPower = 4 * gkSkillsTotal + 3 * defSkillsTotal + 2 * midSkillsTotal + attSkillsTotal;
        return defPower;
    }

    public int calculateAttPower(int gkSkillsTotal, int defSkillsTotal, int midSkillsTotal, int attSkillsTotal){
        int attPower = 4 * attSkillsTotal + 2 * midSkillsTotal + defSkillsTotal + gkSkillsTotal;
        return attPower;
    }

    public void sendingListViewPosition(String position){
        Intent i = new Intent(this, ActivityDetailPlayer.class);
        i.putExtra("sendingPosition", position);
        startActivity(i);
    }

}
