package com.mygdx.game;

public enum PlayerNameEnum {
    Jhon("Jhon", 0), Mary("Mary", 1), Tom("Tom", 2), Joe("Joe", 3);

    private String name;
    private int index;

    PlayerNameEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.index + "_" + this.name;
    }

    public String getName(){
        return name;
    }

    public int getIndex(){
        return index;
    }

}
