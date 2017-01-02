package com.mygdx.game.GameData;

import com.mygdx.game.GameSwitch;
import com.mygdx.game.Log;
import com.mygdx.game.OneCard;
import com.mygdx.game.Player;

import java.util.LinkedList;

/**
 * Created by jay on 17/1/1.
 */
public class TranformToPlayerObj {

    public static PlayerObj handleSendToServer(int order, Player player, LinkedList<OneCard> withCard){
        PlayerObj playerObj = new PlayerObj();
        LinkedList<PlayerObjUnit> myHand = new LinkedList<PlayerObjUnit>();
        LinkedList<PlayerObjUnit> myReturn = new LinkedList<PlayerObjUnit>();
        LinkedList<PlayerObjUnit> myWithCard = new LinkedList<PlayerObjUnit>();


        LinkedList<OneCard> playerOneCards = (LinkedList<OneCard>)player.playerOneCards.clone();

        for(OneCard oneCard:playerOneCards){
            myHand.add(new PlayerObjUnit(oneCard.getValue(), oneCard.getSuit().getRawValue()));
        }

        if(player.playerToReturn != null && player.playerToReturn.size() > 0){
            LinkedList<OneCard> playerToReturn = (LinkedList<OneCard>)player.playerToReturn.clone();

            for(OneCard oneCard:playerToReturn){
                myReturn.add(new PlayerObjUnit(oneCard.getValue(), oneCard.getSuit().getRawValue()));
            }
        }

        if(withCard != null && withCard.size() > 0){
            LinkedList<OneCard> playerToBeat = (LinkedList<OneCard>)withCard.clone();

            for(OneCard oneCard:playerToBeat){
                myWithCard.add(new PlayerObjUnit(oneCard.getValue(), oneCard.getSuit().getRawValue()));
            }
        }

        playerObj.setPlayerOrder(order);
        playerObj.setmGameState(GameSwitch.GAME_GOING);
        playerObj.setMyHandCard(myHand);
        playerObj.setMyReturnCard(myReturn);
        playerObj.setMyToBeatCard(myWithCard);

        return playerObj;
    }

    public static void handleSendToClient(PlayerObj playerObj, Player player){
        LinkedList<PlayerObjUnit> myHand =playerObj.getMyHandCard();
        LinkedList<PlayerObjUnit> myReturn = playerObj.getMyReturnCard();

        //mean player pass
        if(myReturn == null && myReturn.size() < 1){
            return;
        }

        for(PlayerObjUnit returnUtil:myReturn){
            for(int i=0;i<player.playerOneCards.size();i++){
                if(player.playerOneCards.get(i).isValueEqual(returnUtil.cardNumber, returnUtil.cardSuit)){
                    player.playerToReturn.add(player.playerOneCards.get(i));
                    player.playerOneCards.remove(i);
                }
            }
        }

    }
}
