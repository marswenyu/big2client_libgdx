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
//            if(getStreamHead(withCard) != null){
//                toReturn = createStreamWith(hand, withCard);
//            }else
            if(isPairs(withCard)){
                toReturn = createPairs(hand, withCard);
            }else {
                toReturn = createOneCard(hand, withCard);
            }
        }else {
//            toReturn = createStreamWith(hand, null);
//            if(toReturn == null) {
                toReturn = createPairs(hand, null);
//            }
            if(toReturn == null) {
                toReturn = createOneCard(hand, null);
            }
        }

        return toReturn;
    }

    protected LinkedList<OneCard> createStreamWith(LinkedList<OneCard> hand, LinkedList<OneCard> withCard) {
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();


        if(withCard != null){
            if (withCard.size()<5){
                return null;
            }
            OneCard headOfStream = getStreamHead(withCard);
            if(headOfStream == null){
                return null;
            }

            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                if(hand.get(i).getValue() > headOfStream.getValue() ||
                        (hand.get(i).getValue() == headOfStream.getValue() && hand.get(i).getSuit().getRawValue() > headOfStream.getSuit().getRawValue())){
                    toReturn.add(hand.get(i));

                    for(int kk=i+1;kk<hand.size();kk++){
                        if(toReturn.get(toReturn.size() -1).getValue() == hand.get(kk).getValue() -1){
                            toReturn.add(hand.get(kk));
                        }
                    }

                    if(toReturn.size() == 5){
                        return toReturn;
                    }
                }
            }
        }else if(isContainClubsThree(hand)){

        }else {
            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                toReturn.add(hand.get(i));

                for(int kk=i+1;kk<hand.size();kk++){
                    if(toReturn.get(toReturn.size() -1).getValue() == hand.get(kk).getValue() -1){
                        toReturn.add(hand.get(kk));
                    }
                }

                if(toReturn.size() == 5){
                    return toReturn;
                }
            }
        }

        return null;
    }

    public List<OneCard> createBig2Stream(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();

        if(withCard != null){

        }else if(isContainClubsThree(hand)){

            for(int i=0;i<hand.size();i++){
                if(isClubsThree(hand.get(i))) {

                    toReturn.add(hand.get(i));

                    for (int kk = i + 1; kk < hand.size(); kk++) {
                        if (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1) {
                            if (toReturn.size() < 4 || (toReturn.size() == 4 && hand.get(i).getValue() == 15)) {
                                toReturn.add(hand.get(kk));
                            }
                        }
                    }

                    if (toReturn.size() == 5) {
                        return toReturn;
                    }
                }
            }
        }else {
            for(int i=0;i<hand.size();i++){
                if(hand.get(i).getValue() == 3) {

                    toReturn.add(hand.get(i));

                    for (int kk = i + 1; kk < hand.size(); kk++) {
                        if (toReturn.get(toReturn.size() - 1).getValue() == hand.get(kk).getValue() - 1) {
                            if (toReturn.size() < 4 || (toReturn.size() == 4 && hand.get(i).getValue() == 15)) {
                                toReturn.add(hand.get(kk));
                            }
                        }
                    }

                    if (toReturn.size() == 5) {
                        return toReturn;
                    }
                }
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

    public LinkedList<OneCard> createFullHouse(LinkedList<OneCard> hand, LinkedList<OneCard> withCard){
        LinkedList<OneCard> toReturn = new LinkedList<OneCard>();
        LinkedList<OneCard> tmpHand = (LinkedList)hand.clone();

        if(withCard != null){
            OneCard bigPair = null;

            int count=1;
            if(withCard.size() == 5){
                bigPair = withCard.get(0);
                for (int i=1;i<withCard.size();i++){
                    if(bigPair.getValue() == withCard.get(i).getValue()){
                        count++;
                    }else {
                        bigPair = withCard.get(i);
                        count = 1;
                    }
                    if(count == 3){
                        break;
                    }
                }
                if(count != 3){
                    bigPair = null;
                }
            }

            if(bigPair == null){
                return null;
            }

            toReturn.clear();
            for(int i=0;i<hand.size();i++){
                if(hand.get(i).getValue()>bigPair.getValue()){

                    toReturn.add(hand.get(i));
                    for(int j=i+1;j<hand.size();j++){
                        if(toReturn.get(0).getValue()==hand.get(j).getValue()){
                            toReturn.add(hand.get(j));
                        }
                    }
                }
                if(toReturn.size()>=3){
                    break;
                }
                toReturn.clear();
            }

            if(toReturn.size() >=3){
                tmpHand.removeAll(toReturn);
                if(toReturn.size() == 3){
                    toReturn.addAll(createPairs(tmpHand, null));
                    return toReturn;
                }else if(toReturn.size() == 4){
                    toReturn.addAll(createOneCard(tmpHand, null));
                    return toReturn;
                }
            }
        }else if(isContainClubsThree(hand)){

        }else {
            //withCard ==null
            for(int i=0;i<hand.size();i++){
                toReturn.clear();
                toReturn.add(hand.get(i));
                for(int j=i+1;j<hand.size();j++){
                    if(toReturn.get(0).getValue()==hand.get(j).getValue()){
                        toReturn.add(hand.get(j));
                    }
                }
            }
            if(toReturn.size()>=3){
                tmpHand.removeAll(toReturn);
                if(toReturn.size() == 3){
                    toReturn.addAll(createPairs(tmpHand, null));
                    return toReturn;
                }else if(toReturn.size() == 4){
                    toReturn.addAll(createOneCard(tmpHand, null));
                    return toReturn;
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
            return withCard.get(0);
        }
        return null;
    }

    public OneCard getStreamHead(LinkedList<OneCard> withCard){
        OneCard tmpCard = null;
        if(withCard.size() == 5){
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
                return withCard.get(0);
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
                return withCard.get(0);
            }
        }
        return null;
    }

    public OneCard getFullHouseHead(List<OneCard> withCard){
        if(withCard.size() == 5){
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

    public OneCard getFourKindHead(List<OneCard> withCard){
        if(withCard.size() == 5){
            if(withCard.get(0).getValue() == withCard.get(3).getValue()){
                return withCard.get(0);
            }else if(withCard.get(1).getValue() == withCard.get(4).getValue()){
                return withCard.get(1);
            }
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

}
