package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by jay on 16/12/24.
 */
public class OneCard {
    private int value;
    private Suit suit;
    private String oneCardName;
    private Sprite image;

    public OneCard(int value, Suit suit) {
        this.value = value;
        this.suit = suit;

        String asset_name = String.format("pic_poker_%s_%d.png", suit.getRawName(), value);
        Texture img = new Texture("poker/" + asset_name);

        this.oneCardName = String.format("%s_%d", suit.getRawName(), value);
        this.image = new Sprite(img);
    }

    //梅花三最小
    public static final OneCard LOWEST_BIG2_CARD = new OneCard(3, Suit.CLUBS);

    //黑桃二最大
    public static final OneCard BIG2 = new OneCard(2, Suit.SPADES);

    public static final OneCard TEST = new OneCard(10, Suit.SPADES);

    public boolean isAce() {
        return value == 14 || value == 1;
    }

    public boolean isHigher(OneCard card) {
        if (getBig2Value() > card.getBig2Value())
            return true;
        else if (getBig2Value() == card.getBig2Value()) {
            if (getSuit().isHigher(card.suit))
                return true;
        }
        return false;
    }

    public int getBig2Value() {
        return value == 2 ? 100 : value;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue(){
        if(value == 1)
            return 14;
        else if(value ==2)
            return 15;
        else
            return value;
    }

    public Sprite getImage(){
        return image;
    }

    public enum Suit {

        SPADES("spades",4),
        HEARTS("heart",3),
        DIAMONDS("clubs",2),
        CLUBS("diamonds",1);

        static final int EACH_MAX_NUM = 13;
        String m_name;
        int m_value;

        Suit(String name, int value) {
            this.m_name = name;
            this.m_value = value;
        }

        public boolean isHigher(Suit suit) {
            return suit.m_value < m_value;
        }

        public String getRawName(){
            return m_name;
        }

        public int getRawValue() {
            return m_value;
        }

        @Override
        public String toString() {
            return super.name().substring(0, 1);
        }
    }
}
