package com.example.mobilemanager;

public class TeamTableScores  {

    int position;
    String teamName;
    int matches;
    int points;
    int goalsScored;
    int goalsLost;

    public TeamTableScores(int position, String teamName, int matches, int points, int goalsScored, int goalsLost) {
        this.position = position;
        this.teamName = teamName;
        this.matches = matches;
        this.points = points;
        this.goalsScored = goalsScored;
        this.goalsLost = goalsLost;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsLost() {
        return goalsLost;
    }

    public void setGoalsLost(int goalsLost) {
        this.goalsLost = goalsLost;
    }
}
