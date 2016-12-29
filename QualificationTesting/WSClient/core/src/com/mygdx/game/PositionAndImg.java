package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by jay on 16/12/24.
 */
public class PositionAndImg {

    class PositionDetail{
        public float X;
        public float Y;
        public float Width;
        public float Height;
        public float Rotation;
        public Sprite Image;

        PositionDetail(float x, float y, float width, float height, float rotation, Sprite img){
            X = x;
            Y = y;
            Width = width;
            Height = height;
            Rotation = rotation;
            Image = img;
        }
    }

    public LinkedList<PositionDetail> createPosition(Player[] players){
        LinkedList<PositionDetail> displayCardUI = new LinkedList<PositionDetail>();

        for(Player player:players){

            Collections.sort(player.playerOneCards, SuitAndNumber);

            float x =0;
            float y =0;
            float delta_x = 0;
            float delta_y = 0;
            float roation = 0;

            switch (player.mPlayerNameEnum){
                case Jhon:
                    x =300;
                    y =400;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Mary:
                    x =650;
                    y =850;
                    delta_x = 25;
                    break;
                case Tom:
                    x =1580;
                    y =400;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Joe:
                    x =650;
                    y =120;
                    delta_x = 25;
                    break;
                default:
                    break;
            }

            for(int i=0; i<player.playerOneCards.size(); i++){
                Sprite sprite = player.playerOneCards.get(i).getImage();
                displayCardUI.add(new PositionDetail(x, y, 130, 200, roation, sprite));
                x+=delta_x;
                y+=delta_y;
            }
        }
        return displayCardUI;
    }


    public static Comparator<OneCard> SuitAndNumber = new Comparator<OneCard>(){
        public int compare(OneCard s1, OneCard s2) {

            if(s1.getSuit().getRawValue() != s2.getSuit().getRawValue()){
                return s2.getSuit().getRawValue() - s1.getSuit().getRawValue();
            }else {
                return s1.getValue() - s2.getValue();
            }
        }
    };

    public LinkedList<PositionDetail> createFirePosition(Player[] playerFireCard){
        LinkedList<PositionDetail> displayCardUI = new LinkedList<PositionDetail>();

        for(Player player:playerFireCard) {

            float x =0;
            float y =0;
            float delta_x = 0;
            float delta_y = 0;
            float roation = 0;

            if(player.playerToReturn != null) {
                Collections.sort(player.playerToReturn, Player.NumberAndSuit);
            }

            switch (player.mPlayerNameEnum) {
                case Jhon://左
                    x = 550;
                    y = 400;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Mary://上
                    x = 650;
                    y = 600;
                    delta_x = 25;
                    break;
                case Tom://右
                    x = 1350;
                    y = 400;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Joe://下
                    x = 650;
                    y = 370;
                    delta_x = 25;
                    break;
                default:
                    break;
            }

            if(player.playerToReturn != null) {
                for (int i = 0; i < player.playerToReturn.size(); i++) {
                    Sprite sprite = player.playerToReturn.get(i).getImage();
                    displayCardUI.add(new PositionDetail(x, y, 130, 200, roation, sprite));
                    x += delta_x;
                    y += delta_y;
                }
            }else if(player.getPass()){
                displayCardUI.add(new PositionDetail(x, y, 130, 200, roation, NewGameManager.passImage));
            }
        }

        return  displayCardUI;
    }
}
