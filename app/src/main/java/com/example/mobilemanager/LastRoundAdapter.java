package com.example.mobilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LastRoundAdapter extends ArrayAdapter<Results> {

    public LastRoundAdapter (Context context, List<Results> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View resultsView = convertView;
        if (resultsView == null) {
            resultsView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_result,parent,false);
        }

        Results singleResult = getItem(position);

        TextView singleResultHomeTeam = (TextView) resultsView.findViewById(R.id.listViewHomeTeam);
        TextView singleResultHomeScore = (TextView) resultsView.findViewById(R.id.listViewHomeScore);
        TextView singleResultAwayScore = (TextView) resultsView.findViewById(R.id.listViewAwayScore);
        TextView singleResultAwayTeam = (TextView) resultsView.findViewById(R.id.listViewAwayTeam);

        singleResultHomeTeam.setText(String.valueOf(singleResult.getHomeTeam()));
        singleResultHomeScore.setText(String.valueOf(singleResult.getHomeScore()));
        singleResultAwayScore.setText(String.valueOf(singleResult.getAwayScore()));
        singleResultAwayTeam.setText(String.valueOf(singleResult.getAwayTeam()));

        return resultsView;
    }
}
