package com.example.mobilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PlayersAdapter extends ArrayAdapter<Player> {

    public PlayersAdapter(Context context, List<Player> playersTable) {
        super(context, 0, playersTable);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View playersTableView = convertView;
        if (playersTableView == null) {
            playersTableView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_player,parent,false);
        }

        Player singlePlayer = getItem(position);

        TextView listViewScorePlayerNumber = (TextView) playersTableView.findViewById(R.id.listViewPlayersNumber);
        TextView listViewScorePlayerName = (TextView) playersTableView.findViewById(R.id.listViewPlayersName);
        TextView listViewScorePlayerPosition = (TextView) playersTableView.findViewById(R.id.listViewPlayersPosition);
        TextView listViewScorePlayerGkSkills = (TextView) playersTableView.findViewById(R.id.listViewPlayersGkSkills);
        TextView listViewScorePlayerDefSkills = (TextView) playersTableView.findViewById(R.id.listViewPlayersDefSkills);
        TextView listViewScorePlayerAttSkills = (TextView) playersTableView.findViewById(R.id.listViewPlayersAttSkills);


        listViewScorePlayerNumber.setText(String.valueOf(singlePlayer.getNumber()));
        listViewScorePlayerName.setText(String.valueOf(singlePlayer.getName()));
        listViewScorePlayerPosition.setText(String.valueOf(singlePlayer.getPosition()));
        listViewScorePlayerGkSkills.setText(String.valueOf(singlePlayer.getGkSkills()));
        listViewScorePlayerDefSkills.setText(String.valueOf(singlePlayer.getDefSkills()));
        listViewScorePlayerAttSkills.setText(String.valueOf(singlePlayer.getAttSkills()));

        return playersTableView;
    }
}
