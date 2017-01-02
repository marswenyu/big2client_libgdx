package com.mygdx.game;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Player {
    public PlayerNameEnum mPlayerNameEnum;
    public LinkedList<OneCard> playerOneCards = new LinkedList<OneCard>();
    public LinkedList<OneCard> playerToReturn = new LinkedList<OneCard>();
    private boolean isYourTurn;
    private boolean isPass;

    public Player(PlayerNameEnum playerNameEnum) {
        super();
        mPlayerNameEnum = playerNameEnum;
        resetYourTurn();
        resetPass();
    }

    public boolean getIsYourTurn(){
        if(isContainClubsThree(playerOneCards)){
            isYourTurn = true;
        }

        return isYourTurn;
    }

    public void setYourTurn(){
        isYourTurn = true;
    }

    public void resetYourTurn(){
        isYourTurn = false;
    }

    public boolean getPass(){
        return isPass;
    }

    public void setPass(){
        isPass = true;
    }

    public void resetPass(){
        isPass = false;
    }


    public LinkedList<OneCard> caculateHowToPlay(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        Collections.sort(hand, NumberAndSuit);

        if(withCard !=null){
            Collections.sort(withCard, NumberAndSuit);

            toReturn = createFlush(hand, withCard);
            Log.printCard("[withCard]createFlush",toReturn);

            if(toReturn == null && !isFlushStream(withCard)){
                toReturn = createFourKing(hand, withCard);
                Log.printCard("[withCard]createFlush",toReturn);
            }

            if(toReturn == null && getFourKindHead(withCard)==null && !isFlushStream(withCard)){
                if(getFullHouseHead(withCard) != null){
                    toReturn = createFullHouse(hand, withCard);
                    Log.printCard("[withCard]createFullHouse", toReturn);
                }else if(getStreamHead(withCard) != null){
                    toReturn = createStreamWith(hand, withCard);
                    Log.printCard("[withCard]createStreamWith", toReturn);
                }else if(isPairs(withCard)){
                    toReturn = createPairs(hand, withCard);
                    Log.printCard("[withCard]createPairs", toReturn);
                }else {
                    toReturn = createOneCard(hand, withCard);
                    Log.printCard("[withCard]createOneCard", toReturn);
                }
            }
        }else {
            toReturn = createFlush(hand, null);
            Log.printCard("[null]createFlush", toReturn);

            if(toReturn == null){
                toReturn = createFourKing(hand, null);
                Log.printCard("[null]createFourKing", toReturn);
            }

            if(toReturn == null){
                toReturn = createFullHouse(hand, null);
                Log.printCard("[null]createFullHouse", toReturn);
            }

            if(toReturn == null){
                toReturn = createStreamWith(hand, null);
                Log.printCard("[null]createStreamWith", toReturn);
            }

            if(toReturn == null) {
                toReturn = createPairs(hand, null);
                Log.printCard("[null]createPairs", toReturn);
            }

            if(toReturn == null) {
                toReturn = createOneCard(hand, null);
                Log.printCard("[null]createOneCard", toReturn);
            }
        }

        Log.log("===============this round end============");
        return toReturn;
    }

    protected LinkedList<OneCard> createStreamWith(LinkedList<OneCard> hand, LinkedList<OneCard> withCard) {

        LinkedList<OneCard> toReturn;

        toReturn = createBig2Stream(hand, withCard);
        if(toReturn != null){
            Log.printCard("[withCard]big2 or Ace", toReturn);
            return toReturn;
        }else if(withCard != null && (getBig2StreamHead(withCard) != null || getRoyalStreamHead(withCard)!=null) && toReturn==null){
            //若withCard為3,4,5,6,15或10,11,12,13,14 且沒有更大牌型,則退出
            return null;
        }

        toReturn = createMiniStream(hand, withCard);
        if(toReturn != null){
            Log.printCard("[withCard]mini", toReturn);
            return toReturn;
        }

        toReturn = new LinkedList<OneCard>();

        OneCard toBeat = getStreamHead(withCard);

        for(int i=0;i<hand.size();i++){
            toReturn.clear();
            toReturn.add(hand.get(i));

            for (int kk = i + 1; kk < hand.size(); kk++) {
                if(toReturn.size() < 4 && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)){
                    toReturn.add(hand.get(kk));
                }else if(toReturn.size() == 4 && hand.get(kk).getValue() != 15) {
                    if (toBeat != null && hand.get(kk).getValue() > toBeat.getValue()
                            && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)) {

                        toReturn.add(hand.get(kk));
                    } else if(toBeat != null && hand.get(kk).getValue() == toBeat.getValue() && hand.get(kk).isHigher(toBeat)
                            && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)){

                        toReturn.add(hand.get(kk));
                    }else if(toBeat == null && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)){
                        toReturn.add(hand.get(kk));
                    }
                }
            }

            if (toReturn.size() == 5) {
                Log.printCard("[withCard]normal", toReturn);
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

    public LinkedList<OneCard> createBig2Stream(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        OneCard toBeatBig2 = null;
        OneCard toBeatAce = null;

        if(withCard != null){
            toBeatBig2 = getBig2StreamHead(withCard);

            if(toBeatBig2 == null){
                toBeatAce = getRoyalStreamHead(withCard);
            }
        }

        for(int i=0;i<hand.size();i++){
            if(hand.get(i).getValue() == 3 || hand.get(i).getValue() == 10) {
                toReturn.add(hand.get(i));

                for (int kk = i + 1; kk < hand.size(); kk++) {
                    if(toReturn.size() < 4 && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)){
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 4 && toReturn.get(0).getValue() == 3 && hand.get(kk).getValue() == 15) {
                        if (toBeatBig2 != null && hand.get(kk).getSuit().isHigher(toBeatBig2.getSuit())) {
                            toReturn.add(hand.get(kk));
                        } else if(toBeatBig2 == null){
                            toReturn.add(hand.get(kk));
                        }
                    }else if(toBeatBig2 == null && toReturn.size() == 4 && toReturn.get(0).getValue() == 10 && hand.get(kk).getValue() == 14){
                        if (toBeatAce != null && hand.get(kk).getSuit().isHigher(toBeatAce.getSuit())) {
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

    public LinkedList<OneCard> createMiniStream(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        OneCard toBeat = null;

        if(withCard != null){
            toBeat = getMiniStream(withCard);
        }

        for(int i=0;i<hand.size();i++){
            if(hand.get(i).getValue() == 3) {
                toReturn.add(hand.get(i));

                for (int kk = i + 1; kk < hand.size(); kk++) {
                    if(toReturn.size() < 2 && (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1)){
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 2 && toReturn.get(0).getValue() == 3 && hand.get(kk).getValue() == 5){
                        if(toBeat != null && hand.get(kk).isHigher(toBeat)){
                            toReturn.add(hand.get(kk));
                        }else if(toBeat == null){
                            toReturn.add(hand.get(kk));
                        }
                    }else if(toReturn.size() == 3 && toReturn.get(0).getValue() == 3 && hand.get(kk).getValue() == 14) {
                        toReturn.add(hand.get(kk));
                    }else if(toReturn.size() == 4 && toReturn.get(0).getValue() == 3 && hand.get(kk).getValue() == 15){
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

    //單張出牌
    public LinkedList<OneCard> createOneCard(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();

        if(withCard != null) {
            LinkedList<OneCard> tmpCard = new LinkedList<OneCard>();
            for (OneCard oneCard : hand) {

                if (oneCard.getValue() > withCard.get(0).getValue() ||
                        (oneCard.getValue() == withCard.get(0).getValue() && oneCard.getSuit().getRawValue() > withCard.get(0).getSuit().getRawValue())) {
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

    public LinkedList<OneCard> createPairs(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        OneCard bigPair = null;

        if(withCard != null){

            bigPair = getPairsHead(withCard);

            if(bigPair == null) {
                return null;
            }

            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                if(hand.get(i).getValue()>bigPair.getValue() ||
                        (hand.get(i).getValue()==bigPair.getValue() && hand.get(i).getSuit().getRawValue()>bigPair.getSuit().getRawValue())){

                    toReturn.add(hand.get(i));
                    for(int j=i+1;j<hand.size();j++){
                        if(toReturn.get(0).getValue()==hand.get(j).getValue()){
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
                        if(hand.get(j).getValue() == 3){
                            toReturn.add(hand.get(j));
                            return toReturn;
                        }

                        if(j == hand.size()-1){
                            //若有梅花三,但組合不到pairs,則跳出
                            return null;
                        }
                    }else {
                        if(toReturn.get(0).getValue() == hand.get(j).getValue()){
                            toReturn.add(hand.get(j));
                            return toReturn;
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean isPairs(LinkedList<OneCard> withCard){
        if(withCard.size() == 2){
            return withCard.get(0).getValue() == withCard.get(1).getValue();
        }
        return false;
    }

    public OneCard getPairsHead(LinkedList<OneCard> withCard){
        if(withCard.size() == 2 && withCard.get(0).getValue() == withCard.get(1).getValue()){
            return withCard.get(1);
        }
        return null;
    }

    public OneCard getStreamHead(LinkedList<OneCard> withCard){
        if(withCard != null && withCard.size() == 5){

            if(getBig2StreamHead(withCard) != null){
                return withCard.get(4);
            }else if(getRoyalStreamHead(withCard) != null){
                return withCard.get(4);
            }else if(getMiniStream(withCard) != null){
                return withCard.get(2);
            }

            int check1 = withCard.get(0).getValue() + withCard.get(4).getValue();
            int check2 = withCard.get(1).getValue() + withCard.get(3).getValue();
            int check3 = withCard.get(2).getValue();

            Log.log("check1:"+check1+" check2:"+check2+" check3:"+check3);

            if(check1 == check2 && check1==2*check3){
                return withCard.get(4);
            }
        }
        return null;
    }

    //10,11,12,13,14 > 10,11,12,13,1
    public OneCard getRoyalStreamHead(LinkedList<OneCard> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).getValue();
            int check2 = withCard.get(0).getValue() + withCard.get(1).getValue() + withCard.get(2).getValue()+withCard.get(3).getValue();
            if(check1 == 14 && check2 == 46){
                return withCard.get(4);
            }
        }
        return null;
    }

    //3,4,5,6,15 > 3,4,5,6,2
    public OneCard getBig2StreamHead(LinkedList<OneCard> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).getValue();
            int check2 = withCard.get(0).getValue() + withCard.get(1).getValue() + withCard.get(2).getValue()+withCard.get(3).getValue();
            if(check1 == 15 && check2 == 18){
                return withCard.get(4);
            }
        }
        return null;
    }

    //3,4,5,14,15 > 3,4,5,1,2  此為最小順子,則比較第三位數字：5
    public OneCard getMiniStream(LinkedList<OneCard> withCard){
        if(withCard.size() == 5){
            int check1 = withCard.get(4).getValue();
            int check2 = withCard.get(3).getValue();
            int check3 = withCard.get(0).getValue() + withCard.get(1).getValue() + withCard.get(2).getValue();
            if(check1 == 15 && check2 == 14 && check3==12){
                return withCard.get(2);
            }
        }
        return null;
    }

    public OneCard getFullHouseHead(List<OneCard> withCard){
        if(withCard != null && withCard.size() == 5){
            if(withCard.get(0).getValue() == withCard.get(2).getValue() &&
                    withCard.get(3).getValue() == withCard.get(4).getValue()){
                return withCard.get(0);
            }else if(withCard.get(0).getValue() == withCard.get(1).getValue() &&
                    withCard.get(2).getValue() == withCard.get(4).getValue()){
                return withCard.get(2);
            }
        }
        return null;
    }

    public LinkedList<OneCard> createFullHouse(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        OneCard toBeat = getFullHouseHead(withCard);
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();

        // TODO: 16/12/26

        for(int i=0;i<hand.size();i++){
            LinkedList<OneCard> tmpHand = (LinkedList)hand.clone();
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
                if(toBeat != null && !toReturn.get(0).isHigher(toBeat)){
                    toReturn.clear();
                    continue;
                }


                tmpHand.removeAll(toReturn);
                LinkedList<OneCard> tmpToReturn = createPairs(tmpHand, null);
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

    public OneCard getFourKindHead(LinkedList<OneCard> withCard){
        if(withCard !=null && withCard.size() == 5){
            if(withCard.get(0).getValue() == withCard.get(3).getValue()){
                return withCard.get(0);
            }else if(withCard.get(1).getValue() == withCard.get(4).getValue()){
                return withCard.get(1);
            }
        }
        return null;
    }

    public LinkedList<OneCard> createFourKing(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        OneCard toBeat = getFourKindHead(withCard);
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        // TODO: 16/12/26

        for(int i=0;i<hand.size();i++){
            toReturn.add(hand.get(i));

            if(toBeat != null && !toReturn.get(0).isHigher(toBeat)){
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
                for(OneCard card:hand){
                    if(!toReturn.get(0).isValueEqual(card)){
                        toReturn.add(card);
                        break;
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
    public static Comparator<OneCard> NumberAndSuit = new Comparator<OneCard>(){
        public int compare(OneCard s1, OneCard s2) {

            if(s1.getValue() != s2.getValue()){
                return s1.getValue() - s2.getValue();
            }else {
                return s1.getSuit().getRawValue() - s2.getSuit().getRawValue();
            }
        }
    };

    public boolean isContainClubsThree(List<OneCard> hand){

        for(OneCard card:hand){
            if(isClubsThree(card)){
                return true;
            }
        }

        return false;
    }

    public boolean isClubsThree(OneCard card){

        if(card.getValue() == 3 && card.getSuit() == OneCard.Suit.CLUBS){
            return true;
        }
        return false;
    }

    public LinkedList<OneCard> createFlush(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard>[] splitCardByColor = getListSplitCardByColor(hand);
        LinkedList<OneCard> toReturn = null;

        boolean isWithCardFlush = isFlushStream(withCard);

        for(LinkedList<OneCard> listOneCard:splitCardByColor){
            if(isWithCardFlush) {
                toReturn = createStreamWith(listOneCard, withCard);
            }else {
                toReturn = createStreamWith(listOneCard, null);
            }

            if(toReturn != null){
                if(isContainClubsThree(hand)){
                    if(isClubsThree(toReturn.get(0))){
                        return toReturn;
                    }
                }else {
                    return toReturn;
                }
            }
        }

        return null;
    }

    public boolean isFlushStream(LinkedList<OneCard> withCard){
        if(withCard != null && withCard.size() == 5){
            for(int i=0;i<(withCard.size()-1);i++){
                if(withCard.get(i).getSuit() != withCard.get(i+1).getSuit()){
                    return false;
                }
            }
            return getStreamHead(withCard) != null;
        }
        return false;
    }

    public LinkedList<OneCard>[] getListSplitCardByColor(LinkedList<OneCard> tmpHand){
        LinkedList<OneCard>[] toReturn = new LinkedList[4];
        toReturn[0] = new LinkedList<OneCard>();
        toReturn[1] = new LinkedList<OneCard>();
        toReturn[2] = new LinkedList<OneCard>();
        toReturn[3] = new LinkedList<OneCard>();


        for(OneCard card:tmpHand){
            switch (card.getSuit()) {
                case SPADES:
                    toReturn[3].add(card);
                    break;
                case HEARTS:
                    toReturn[2].add(card);
                    break;
                case DIAMONDS:
                    toReturn[1].add(card);
                    break;
                case CLUBS:
                    toReturn[0].add(card);
                    break;
                default:
                    break;

            }
        }

        return toReturn;
    }

}
