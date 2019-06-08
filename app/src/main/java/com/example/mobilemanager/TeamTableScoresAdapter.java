package com.example.mobilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TeamTableScoresAdapter extends ArrayAdapter<TeamTableScores> {

    public TeamTableScoresAdapter(Context context, List<TeamTableScores> teamTableScores) {
        super(context, 0, teamTableScores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View teamTableScoresView = convertView;
        if (teamTableScoresView == null) {
            teamTableScoresView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_scoretable,parent,false);
        }

        TeamTableScores singleTeamTableScore = getItem(position);

        TextView listViewScoreTablePosition = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTablePosition);
        TextView listViewScoreTableClubName = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTableClubName);
        TextView listViewScoreTableMatches = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTableMatches);
        TextView listViewScoreTablePoints = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTablePoints);
        TextView listViewScoreTableScoredGoals = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTableScoredGoals);
        TextView listViewScoreTableLostGoals = (TextView) teamTableScoresView.findViewById(R.id.listViewScoreTableLostGoals);

        listViewScoreTablePosition.setText(String.valueOf(singleTeamTableScore.getPosition()));
        listViewScoreTableClubName.setText(singleTeamTableScore.getTeamName());
        listViewScoreTableMatches.setText(String.valueOf(singleTeamTableScore.getMatches()));
        listViewScoreTablePoints.setText(String.valueOf(singleTeamTableScore.getPoints()));
        listViewScoreTableScoredGoals.setText(String.valueOf(singleTeamTableScore.getGoalsScored()));
        listViewScoreTableLostGoals.setText(String.valueOf(singleTeamTableScore.getGoalsLost()));

        return teamTableScoresView;
    }
}
