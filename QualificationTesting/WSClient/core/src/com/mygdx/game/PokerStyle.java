package com.mygdx.game;

/**
 * Created by matt1201 on 2016/12/20.
 */
public enum PokerStyle {
    Spade("spades"),
    Heart("heart"),
    Diamond("diamonds"),
    Club("clubs");

    public final static int MAXNUM = 13;

    private String m_name;
    public String getName(){return m_name;}

    private PokerStyle(String name){
        m_name = name;
    }
}
