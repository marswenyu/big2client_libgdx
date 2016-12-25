package com.mygdx.game;

public enum PlayerNameEnum {
    Jhon("Player0", 0), Mary("Player1", 1), Tom("Player2", 2), Joe("Player3", 3);

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

    public static PlayerNameEnum getPlayerEnum(int playerCode){
        if(playerCode == 0){
            return Jhon;
        }else if(playerCode == 1){
            return Mary;
        }else if(playerCode == 2){
            return Tom;
        }else {
            return Joe;
        }
    }

}
