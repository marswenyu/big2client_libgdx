package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Sprite;

public class Card {
    public String Name;
    public float X;
    public float Y;
    public float Width;
    public float Height;
    public float Rotation;
    public Sprite Image;

    public int mPokeSuitRank;   //黑桃,紅心,方塊,梅花的index
    public int mPokerRank;      //同花色的排序

    public Card(int styleRank, int rank, String name, float width, float height, Sprite img){
        mPokeSuitRank = styleRank;
        mPokerRank = rank;
        Name = name;
        Width = width;
        Height = height;
        Image = img;
    }

    public Card(String name, float x, float y, float width, float height, float rotation, Sprite img){
        Name = name;
        X = x;
        Y = y;
        Width = width;
        Height = height;
        Rotation = rotation;
        Image = img;
    }

    public void setPlayerAndPosition(float x, float y, float rotation){
        X = x;
        Y = y;
        Rotation = rotation;
    }
}
