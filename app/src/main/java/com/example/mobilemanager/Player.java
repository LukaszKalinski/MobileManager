package com.example.mobilemanager;

public class Player {

    int number;
    String name;
    String position;
    int gkSkills;
    int defSkills;
    int attSkills;
    int wage;
    int value;

    public Player(int number, String name, String position, int gkSkills, int defSkills, int attSkills, int wage, int value) {
        this.number = number;
        this.name = name;
        this.position = position;
        this.gkSkills = gkSkills;
        this.defSkills = defSkills;
        this.attSkills = attSkills;
        this.wage = wage;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGkSkills() {
        return gkSkills;
    }

    public void setGkSkills(int gkSkills) {
        this.gkSkills = gkSkills;
    }

    public int getDefSkills() {
        return defSkills;
    }

    public void setDefSkills(int defSkills) {
        this.defSkills = defSkills;
    }

    public int getAttSkills() {
        return attSkills;
    }

    public void setAttSkills(int attSkills) {
        this.attSkills = attSkills;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
