package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

/**
 * Created by matt1201 on 2016/12/20.
 */
public class GameManager {
    class Card{
        public String Name;
        public float X;
        public float Y;
        public float Width;
        public float Height;
        public float Rotation;
        public Sprite Image;

        public Card(String name, float x, float y, float width, float height, float rotation, Sprite img){
            Name = name;
            X = x;
            Y = y;
            Width = width;
            Height = height;
            Rotation = rotation;
            Image = img;
        }
    }

    private Map<String, Texture> m_cards = new HashMap<String, Texture>();

    private List<Card> m_table_cards = new LinkedList<Card>();

    public void loadPokerAsset(){
        PokerStyle[] styles = PokerStyle.values();

        for(PokerStyle style: styles){
            float x =0;
            float y =0;
            float delta_x = 0;
            float delta_y = 0;
            float roation = 0;

            switch (style){
                case Spade:
                    x =350;
                    y =200;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Heart:
                    x =350;
                    y =600;
                    delta_x = 25;
                    break;
                case Diamond:
                    x =980;
                    y =200;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Club:
                    x =350;
                    y =10;
                    delta_x = 25;
                    break;
            }

            for(int i=1; i<=PokerStyle.MAXNUM; i++){
                String asset_name = String.format("pic_poker_%s_%d.png", style.getName(), i);

                String name = String.format("%s_%d", style.getName(), i);
                Texture img = new Texture("poker/" + asset_name);
                Sprite sprite = new Sprite(img);

                m_cards.put(name, img);
                m_table_cards.add(new Card(name, x, y, 130, 200, roation, sprite));
                x+=delta_x;
                y+=delta_y;
            }
        }
    }

    public void render(SpriteBatch batch){
        for(int i=0; i<m_table_cards.size(); i++){
            batch.draw(m_table_cards.get(i).Image,
                    m_table_cards.get(i).X, m_table_cards.get(i).Y,
                    0, 0,
                    m_table_cards.get(i).Width, m_table_cards.get(i).Height,
                    1, 1,
                    m_table_cards.get(i).Rotation);
        }

    }
}
