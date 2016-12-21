package com.mygdx.game;

/**
 * Created by matt1201 on 2016/12/20.
 */
public enum PokerStyle {
    Spade("spades", 0),
    Heart("heart", 1),
    Club("clubs", 2),
    Diamond("diamonds", 3);

    public final static int MAXNUM = 13;

    private String m_name;
    private int m_rank;

    public String getName(){
        return m_name;
    }

    public int getRank(){
        return m_rank;
    }

    PokerStyle(String name, int rank){
        m_name = name;
        m_rank = rank;
    }
}
