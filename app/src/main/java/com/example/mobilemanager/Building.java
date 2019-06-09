package com.example.mobilemanager;

public class Building {

    String name;
    int level;
    int upgradeCost;
    int firstValue;
    int secondValue;
    int thirdValue;

    public Building(String name, int level, int upgradeCost, int firstValue, int secondValue, int thirdValue) {
        this.name = name;
        this.level = level;
        this.upgradeCost = upgradeCost;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public int getThirdValue() {
        return thirdValue;
    }
}
