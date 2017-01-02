package com.big2.game.AI;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by jay on 17/1/1.
 */
public class GameAI {

    public static LinkedList<PlayerObjUnit> caculateHowToPlay(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();
        Collections.sort(hand, NumberAndSuit);

        if(withCard !=null){
            Collections.sort(withCard, NumberAndSuit);

            toReturn = createFlush(hand, withCard);
//            Log.printCard("[withCard]createFlush",toReturn);

            if(toReturn == null){
                toReturn = createFourKing(hand, withCard);
//                Log.printCard("[withCard]createFlush",toReturn);
            }

            if(toReturn == null){
                if(getFullHouseHead(withCard) != null){
                    toReturn = createFullHouse(hand, withCard);
//                    Log.printCard("[withCard]createFullHouse", toReturn);
                }else if(getStreamHead(withCard) != null){
                    toReturn = createStreamWith(hand, withCard);
//                    Log.printCard("[withCard]createStreamWith", toReturn);
                }else if(isPairs(withCard)){
                    toReturn = createPairs(hand, withCard);
//                    Log.printCard("[withCard]createPairs", toReturn);
                }else {
                    toReturn = createOneCard(hand, withCard);
//                    Log.printCard("[withCard]createOneCard", toReturn);
                }
            }
        }else {
            toReturn = createFlush(hand, null);
//            Log.printCard("[null]createFlush", toReturn);

            if(toReturn == null){
                toReturn = createFourKing(hand, null);
//                Log.printCard("[null]createFourKing", toReturn);
            }

            if(toReturn == null){
                toReturn = createFullHouse(hand, null);
//                Log.printCard("[null]createFullHouse", toReturn);
            }

            if(toReturn == null){
                toReturn = createStreamWith(hand, null);
//                Log.printCard("[null]createStreamWith", toReturn);
            }

            if(toReturn == null) {
                toReturn = createPairs(hand, null);
//                Log.printCard("[null]createPairs", toReturn);
            }

            if(toReturn == null) {
                toReturn = createOneCard(hand, null);
//                Log.printCard("[null]createOneCard", toReturn);
            }
        }

//        Log.log("===============this round end============");
        return toReturn;
    }

    public static LinkedList<PlayerObjUnit> createFlush(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit>[] splitCardByColor = getListSplitCardByColor(hand);
        LinkedList<PlayerObjUnit> toReturn = null;

        for(LinkedList<PlayerObjUnit> listOneCard:splitCardByColor){
            toReturn = createStreamWith(listOneCard, withCard);

            if(toReturn != null){
                return toReturn;
            }
        }

        return null;
    }

    public static LinkedList<PlayerObjUnit> createBig2Stream(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();
        PlayerObjUnit toBeatBig2 = null;
        PlayerObjUnit toBeatAce = null;

        if(withCard != null){
            toBeatBig2 = getBig2StreamHead(withCard);

            if(toBeatBig2 == null){
                toBeatAce = getRoyalStreamHead(withCard);
            }
        }

        for(int i=0;i<hand.size();i++){
            if(hand.get(i).cardNumber == 3 || hand.get(i).cardNumber == 10) {
                toReturn.add(hand.get(i));

                for (int kk = i + 1; kk < hand.size(); kk++) {
                    if(toReturn.size() < 4 && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)){
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 4 && toReturn.get(0).cardNumber== 3 && hand.get(kk).cardNumber == 15) {
                        if (toBeatBig2 != null && hand.get(kk).cardSuit > toBeatBig2.cardSuit) {
                            toReturn.add(hand.get(kk));
                        } else if(toBeatBig2 == null){
                            toReturn.add(hand.get(kk));
                        }
                    }else if(toBeatBig2 == null && toReturn.size() == 4 && toReturn.get(0).cardNumber == 10 && hand.get(kk).cardNumber == 14){
                        if (toBeatAce != null && hand.get(kk).cardSuit > toBeatAce.cardSuit) {
                            toReturn.add(hand.get(kk));
                        } else if(toBeatAce == null){
                            toReturn.add(hand.get(kk));
                        }
                    }
                }

                if (toReturn.size() == 5) {
                    return toReturn;
                }

                if(isClubsThree(toReturn.get(0))){
                    //若第一張為梅花三,但湊不出3,4,5,6,15,則跳出
                    return null;
                }

                toReturn.clear();
            }
        }

        return null;
    }

    public static LinkedList<PlayerObjUnit> createFullHouse(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        PlayerObjUnit toBeat = getFullHouseHead(withCard);
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();

        // TODO: 16/12/26

        for(int i=0;i<hand.size();i++){
            LinkedList<PlayerObjUnit> tmpHand = (LinkedList)hand.clone();
            toReturn.add(hand.get(i));

            for(int j=i+1;j<hand.size();j++){
                if(toReturn.get(0).isValueEqual(hand.get(j))){
                    toReturn.add(hand.get(j));

                    if(toReturn.size() == 3)
                        break;
                }
            }

            if(toReturn.size() == 3){
                //找到三張一樣的,若此三張一樣的並不高於toBeat,則重新找
                if(toBeat != null && !toReturn.get(0).isHigher(toBeat.cardNumber)){
                    toReturn.clear();
                    continue;
                }


                tmpHand.removeAll(toReturn);
                LinkedList<PlayerObjUnit> tmpToReturn = createPairs(tmpHand, null);
                if(tmpToReturn != null){
                    toReturn.addAll(tmpToReturn);
                    if(getFullHouseHead(toReturn) != null){
                        return toReturn;
                    }
                }
            }

            tmpHand.clear();
            toReturn.clear();
        }

        return null;
    }

    //單張出牌
    public static LinkedList<PlayerObjUnit> createOneCard(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();

        if(withCard != null) {
            LinkedList<PlayerObjUnit> tmpCard = new LinkedList<PlayerObjUnit>();
            for (PlayerObjUnit oneCard : hand) {

                if (oneCard.cardNumber > withCard.get(0).cardNumber ||
                        (oneCard.cardNumber == withCard.get(0).cardNumber && oneCard.cardSuit > withCard.get(0).cardSuit)) {
                    tmpCard.add(oneCard);
                }
            }

            if (!tmpCard.isEmpty()) {
                Collections.shuffle(tmpCard);
                toReturn.add(tmpCard.get(0));
                return toReturn;
            }
        }else {
            if(isClubsThree(hand.get(0))){
                toReturn.add(hand.get(0));
                return toReturn;
            }else{
                Random rand = new Random();
                int n = rand.nextInt(hand.size());

                toReturn.add(hand.get(n));
                return toReturn;
            }
        }

        return null;
    }

    public static LinkedList<PlayerObjUnit> createPairs(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();
        PlayerObjUnit bigPair = null;

        if(withCard != null){

            bigPair = getPairsHead(withCard);

            if(bigPair == null) {
                return null;
            }

            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                if(hand.get(i).cardNumber>bigPair.cardNumber ||
                        (hand.get(i).cardNumber==bigPair.cardNumber && hand.get(i).cardSuit>bigPair.cardSuit)){

                    toReturn.add(hand.get(i));
                    for(int j=i+1;j<hand.size();j++){
                        if(toReturn.get(0).cardNumber==hand.get(j).cardNumber){
                            toReturn.add(hand.get(j));
                            return toReturn;
                        }
                    }
                }
            }
        }else {
            //withCard==null
            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                toReturn.add(hand.get(i));

                for(int j=i+1;j<hand.size();j++){

                    if(isClubsThree(toReturn.get(0))){
                        if(hand.get(j).cardNumber == 3){
                            toReturn.add(hand.get(j));
                            return toReturn;
                        }

                        if(j == hand.size()-1){
                            //若有梅花三,但組合不到pairs,則跳出
                            return null;
                        }
                    }else {
                        if(toReturn.get(0).cardNumber == hand.get(j).cardNumber){
                            toReturn.add(hand.get(j));
                            return toReturn;
                        }
                    }
                }
            }
        }

        return null;
    }

    protected static LinkedList<PlayerObjUnit> createStreamWith(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard) {

        LinkedList<PlayerObjUnit> toReturn;

        toReturn = createBig2Stream(hand, withCard);
        if(toReturn != null){
//            Log.printCard("[withCard]big2 or Ace", toReturn);
            return toReturn;
        }else if(withCard != null && (getBig2StreamHead(withCard) != null || getRoyalStreamHead(withCard)!=null) && toReturn==null){
            //若withCard為3,4,5,6,15或10,11,12,13,14 且沒有更大牌型,則退出
            return null;
        }

        toReturn = createMiniStream(hand, withCard);
        if(toReturn != null){
//            Log.printCard("[withCard]mini", toReturn);
            return toReturn;
        }

        toReturn = new LinkedList<PlayerObjUnit>();

        PlayerObjUnit toBeat = getStreamHead(withCard);

        for(int i=0;i<hand.size();i++){
            toReturn.clear();
            toReturn.add(hand.get(i));

            for (int kk = i + 1; kk < hand.size(); kk++) {
                if(toReturn.size() < 4 && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)){
                    toReturn.add(hand.get(kk));
                }else if(toReturn.size() == 4) {
                    if (toBeat != null && hand.get(kk).cardNumber > toBeat.cardNumber
                            && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)) {

                        toReturn.add(hand.get(kk));
                    } else if(toBeat != null && hand.get(kk).cardNumber == toBeat.cardNumber && hand.get(kk).isHigher(toBeat.cardNumber)
                            && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)){

                        toReturn.add(hand.get(kk));
                    }else if(toBeat == null && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)){
                        toReturn.add(hand.get(kk));
                    }
                }
            }

            if (toReturn.size() == 5) {
//                Log.printCard("[withCard]normal", toReturn);
                return toReturn;
            }

            if(isClubsThree(toReturn.get(0))){
                //若第一張為梅花三,但湊不出3,4,5,6,15,則跳出
                return null;
            }

            toReturn.clear();
        }

        return null;
    }

    public static LinkedList<PlayerObjUnit> createMiniStream(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();
        PlayerObjUnit toBeat = null;

        if(withCard != null){
            toBeat = getMiniStream(withCard);
        }

        for(int i=0;i<hand.size();i++){
            if(hand.get(i).cardNumber == 3) {
                toReturn.add(hand.get(i));

                for (int kk = i + 1; kk < hand.size(); kk++) {
                    if(toReturn.size() < 2 && (toReturn.get(toReturn.size() - 1).cardNumber == hand.get(kk).cardNumber - 1)){
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 2 && toReturn.get(0).cardNumber == 3 && hand.get(kk).cardNumber == 5){
                        if(toBeat != null && hand.get(kk).isHigher(toBeat.cardNumber)){
                            toReturn.add(hand.get(kk));
                        }else if(toBeat == null){
                            toReturn.add(hand.get(kk));
                        }
                    }else if(toReturn.size() == 3 && toReturn.get(0).cardNumber == 3 && hand.get(kk).cardNumber == 14) {
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 4 && toReturn.get(0).cardNumber == 3 && hand.get(kk).cardNumber == 15){
                        toReturn.add(hand.get(kk));
                    }
                }

                if (toReturn.size() == 5) {
                    return toReturn;
                }

                if(isClubsThree(toReturn.get(0))){
                    //若第一張為梅花三,但湊不出3,4,5,6,15,則跳出
                    return null;
                }

                toReturn.clear();
            }
        }

        return null;
    }

    public static LinkedList<PlayerObjUnit> createFourKing(LinkedList<PlayerObjUnit> hand, LinkedList<PlayerObjUnit> withCard){
    	PlayerObjUnit toBeat = getFourKindHead(hand);
        LinkedList<PlayerObjUnit> toReturn = new LinkedList<PlayerObjUnit>();
        // TODO: 16/12/26

        for(int i=0;i<hand.size();i++){
            toReturn.add(hand.get(i));

            if(toBeat != null && !toReturn.get(0).isHigher(toBeat.cardNumber)){
                //要找鐵支的目標若小於toBeat,則直接搜尋下一個目標
                toReturn.clear();
                continue;
            }

            for(int j=i+1;j<hand.size();j++){
                if(toReturn.get(0).isValueEqual(hand.get(j))){
                    toReturn.add(hand.get(j));
                }
            }

            if(toReturn.size() == 4){
                //找到鐵支
                for(PlayerObjUnit card:hand){
                    if(!toReturn.get(0).isValueEqual(card)){
                        toReturn.add(card);
                    }
                }

                if(getFourKindHead(toReturn) != null) {
                    return toReturn;
                }
            }

            toReturn.clear();
        }

        return null;
    }

    //數字：小>大,花色：梅花>黑桃
    public static Comparator<PlayerObjUnit> NumberAndSuit = new Comparator<PlayerObjUnit>(){
        public int compare(PlayerObjUnit s1, PlayerObjUnit s2) {

            if(s1.cardNumber != s2.cardNumber){
                return s1.cardNumber - s2.cardNumber;
            }else {
                return s1.cardSuit - s2.cardSuit;
            }
        }
    };

    public static PlayerObjUnit getFourKindHead(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 5){
            if(withCard.get(0).cardNumber == withCard.get(3).cardNumber){
                return withCard.get(0);
            }else if(withCard.get(1).cardNumber == withCard.get(4).cardNumber){
                return withCard.get(1);
            }
        }
        return null;
    }

    public static PlayerObjUnit getPairsHead(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 2 && withCard.get(0).cardNumber == withCard.get(1).cardNumber){
            return withCard.get(1);
        }
        return null;
    }

    public static PlayerObjUnit getStreamHead(LinkedList<PlayerObjUnit> withCard){
        if(withCard != null && withCard.size() == 5){

            if(getBig2StreamHead(withCard) != null){
                return withCard.get(4);
            }else if(getRoyalStreamHead(withCard) != null){
                return withCard.get(4);
            }else if(getMiniStream(withCard) != null){
                return withCard.get(2);
            }

            int check1 = withCard.get(0).cardNumber + withCard.get(4).cardNumber;
            int check2 = withCard.get(1).cardNumber + withCard.get(3).cardNumber;
            int check3 = withCard.get(2).cardNumber;

//            Log.log("check1:"+check1+" check2:"+check2+" check3:"+check3);

            if(check1 == check2 && check1==2*check3){
                return withCard.get(4);
            }
        }
        return null;
    }

    //10,11,12,13,14 > 10,11,12,13,1
    public static PlayerObjUnit getRoyalStreamHead(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).cardNumber;
            int check2 = withCard.get(0).cardNumber + withCard.get(1).cardNumber + withCard.get(2).cardNumber+withCard.get(3).cardNumber;
            if(check1 == 14 && check2 == 46){
                return withCard.get(4);
            }
        }
        return null;
    }

    //3,4,5,6,15 > 3,4,5,6,2
    public static PlayerObjUnit getBig2StreamHead(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).cardNumber;
            int check2 = withCard.get(0).cardNumber + withCard.get(1).cardNumber + withCard.get(2).cardNumber+withCard.get(3).cardNumber;
            if(check1 == 15 && check2 == 18){
                return withCard.get(4);
            }
        }
        return null;
    }

    //3,4,5,14,15 > 3,4,5,1,2  此為最小順子,則比較第三位數字：5
    public static PlayerObjUnit getMiniStream(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).cardNumber;
            int check2 = withCard.get(3).cardNumber;
            int check3 = withCard.get(0).cardNumber + withCard.get(1).cardNumber + withCard.get(2).cardNumber;
            if(check1 == 15 && check2 == 14 && check3==12){
                return withCard.get(2);
            }
        }
        return null;
    }

    public static PlayerObjUnit getFullHouseHead(List<PlayerObjUnit> withCard){
        if(withCard != null && withCard.size() == 5){
            if(withCard.get(0).cardNumber == withCard.get(2).cardNumber &&
                    withCard.get(3).cardNumber == withCard.get(4).cardNumber){
                return withCard.get(0);
            }else if(withCard.get(0).cardNumber == withCard.get(1).cardNumber &&
                    withCard.get(2).cardNumber == withCard.get(4).cardNumber){
                return withCard.get(2);
            }
        }
        return null;
    }

    public static boolean isPairs(LinkedList<PlayerObjUnit> withCard){
        if(withCard.size() == 2){
            return withCard.get(0).cardNumber == withCard.get(1).cardNumber;
        }
        return false;
    }

    public static boolean isClubsThree(PlayerObjUnit card){

        if(card.cardNumber == 3 && card.cardSuit == 1){
            return true;
        }
        return false;
    }

    public static LinkedList<PlayerObjUnit>[] getListSplitCardByColor(LinkedList<PlayerObjUnit> tmpHand){
        LinkedList<PlayerObjUnit>[] toReturn = new LinkedList[4];
        toReturn[0] = new LinkedList<PlayerObjUnit>();
        toReturn[1] = new LinkedList<PlayerObjUnit>();
        toReturn[2] = new LinkedList<PlayerObjUnit>();
        toReturn[3] = new LinkedList<PlayerObjUnit>();


        for(PlayerObjUnit card:tmpHand){
            switch (card.cardSuit) {
                case 4:
                    toReturn[3].add(card);
                    break;
                case 3:
                    toReturn[2].add(card);
                    break;
                case 2:
                    toReturn[1].add(card);
                    break;
                case 1:
                    toReturn[0].add(card);
                    break;
                default:
                    break;

            }
        }

        return toReturn;
    }
}
