package TexasHoldem;
import poker.*;

import java.util.Comparator;
import java.util.Arrays;

public class HoldemHand {
    //enum for cardvalue, suit, risk and handvalue
    public enum CardValue{
        ACE(1, true),
        DEUCE(2, false),
        THREE(3, false),
        FOUR(4, false),
        FIVE(5, false),
        SIX(6, false),
        SEVEN(7, false),
        EIGHT(8, false),
        NINE(9, false),
        TEN(10, false),
        JACK(11, false),
        QUEEN(12, false),
        KING(13, false);

        private final int value;
        private final boolean isAceHigh;
        public static final int ACE_HIGH_VALUE = 14;

        private CardValue(int value, boolean isAceHigh){
            this.value = value;
            this.isAceHigh = isAceHigh;
        }

        public int getCardValue(boolean isAceHigh){
            if (this == ACE) {
                return isAceHigh ? ACE_HIGH_VALUE : 1;
            } else {
                return value;
            }
        }

        public int getCardValue() {
            return getCardValue(isAceHigh);
        }
    }

    public enum Suit {
        HEARTS("Hearts"),
        DIAMONDS("Diamonds"),
        CLUBS("Clubs"),
        SPADES("Spades");

        private final String cardSuit;

        private Suit(String cardSuit){
            this.cardSuit = cardSuit;
        }

        @Override
        public String toString(){
            return cardSuit;
        }
    }

    public enum HandValue {
        ROYALFLUSH_VALUE(2000000000),
        STRAIGHTFLUSH_VALUE(1000000000),
        STRAIGHT_VALUE(100000000),
        FOURS_VALUE(10000000),
        FLUSH_VALUE(1000000),
        FULLHOUSE_VALUE(100000),
        THREES_VALUE(10000),
        TWOPAIR_VALUE(1000),
        PAIR_VALUE(100),
        HIGHCARD_VALUE(0);

        private final int value;

        HandValue(int value){
            this.value = value;
        }

        public int getHandValue(){
            return value;
        }
    }

    public enum RiskWorthiness{
        ROYALFLUSH_RISK(0),
        STRAIGHTFLUSH_RISK(5),
        STRAIGHT_RISK(10),
        FOURS_RISK(15),
        FLUSH_RISK(20),
        FULLHOUSE_RISK(25),
        THREES_RISK(30),
        TWOPAIR_RISK(35),
        PAIR_RISK(40),
        HIGHCARD_RISK(90);

        private final int risk;

        RiskWorthiness(int risk){
            this.risk = risk;
        }

        public int getRiskValue(){
            return risk;
        }
    }


    public static final int DEFAULT_RISK = 20;

    public static final int INIT_PLAYER_CARDS = 2; // total number of cards to be determined TODO - as players get 2 cards only their total hand is accounted for their best 5 cards.
    public static final int NUM_COMMUNITY_CARDS = 5;
    public static final int TOTAL_CARDS = INIT_PLAYER_CARDS+NUM_COMMUNITY_CARDS;
    
    private Card[] communityCards; //define community cards as an array of cards for reference
    private Card[] playerHand; //define playerHand as an array of cards
    private DeckOfCards deck; //define field of deck

    private int communityCardsCount; //counter for community cards

    public HoldemHand(Card[] hand, DeckOfCards deck){
        this.playerHand = hand; //init playerHand
        this.deck = deck; //init deck
    }

    public HoldemHand(DeckOfCards deck){
        this.deck = deck; //init deck
        playerHand = new Card[INIT_PLAYER_CARDS];
        communityCards = new Card[NUM_COMMUNITY_CARDS]; // Init community cards which uses count to determine where we are in the game.
        communityCardsCount = 3;
    }

    public int getRiskWorthiness(){ //We override this value for specific hands such as Straight, FullHouse etc..
        return DEFAULT_RISK; //TODO - Change to enum with setter inside
    }


    //Display a hand
    public String toString(){
        String desc = "";
        for (int i = 0; i < NUM_COMMUNITY_CARDS; i++)
            desc = desc + "\n      " + i + ":  " + getCard(i).toString();

        return desc + "\n";
    }


    //modifiers
    public void setCard(int num, Card card){
        if (num >= 0 && num < TOTAL_CARDS)
            playerHand[num] = card;
    }



    //Accesors
    public Card getCard(int num){ //get card at index
        if (num >= 0 && num < TOTAL_CARDS)
            return playerHand[num];
        else
            return null;
    }

    public int getValue(){
        return getCard(0).getValue(); // simply return the value of the higest card
    }






    //Hand Classifiers TODO
    public boolean isFourOfAKind(){
        sortHand();

        /*
        IF card 1 == card 4 (checks index for sorted cards etc)
        || card 2 == card 5
        */
        return playerHand[0].getValue() == playerHand[3].getValue() || playerHand[1].getValue() == playerHand[4].getValue();
    }

    public boolean isFullHouse(){
        sortHand();
        return playerHand[0].getValue() == playerHand[1].getValue()
                && playerHand[2].getValue() == playerHand[4].getValue()
                || playerHand[0].getValue() == playerHand[2].getValue()
                && playerHand[3].getValue() == playerHand[4].getValue();
    }

    public boolean isStraight(){
        sortHand();
        boolean isAceLow = playerHand[0].getValue() == CardValue.ACE.getCardValue(false);
        int count = 0;
        for (int i = 1; i < 5; i++) {
            if (playerHand[i].getValue() == playerHand[i - 1].getValue() - 1){
                count++;
            } else if (playerHand[i].getValue() == playerHand[i - 1].getValue()){
                continue;
            } else if (isAceLow && playerHand[i].getValue() == CardValue.FIVE.getCardValue()){
                count++;
            } else{
                break;
            }
        }
        return count == 4;
    }

    public boolean isFlush(){ //returns true if all cards in the hand are the same suit
        for (int i = 1; i < 5; i++){
            if (getCard(i).getSuit() != getCard(0).getSuit()){
                return false;
            }
        }
        return true;
    }

    public boolean isStraightFlush(){ //returns true if hand is both a flush and a straight
        return isFlush() && isStraight();
    }

    public boolean isRoyalFlush(){ //returns true if the hand is a straight flush from 'Ten -> Ace'
        sortHand();

        /*
         If card 1 == value of 10
         && card 2 == value of jack
         && card 3 == value of queen
         && card 4 == value of king
         && card 5 == value of ace (assuming that its treated as a high card)
         */
        return isFlush() && playerHand[0].getValue() == CardValue.ACE.getCardValue(true)
                && playerHand[1].getValue() == CardValue.KING.getCardValue()
                && playerHand[2].getValue() == CardValue.QUEEN.getCardValue()
                && playerHand[3].getValue() == CardValue.JACK.getCardValue()
                && playerHand[4].getValue() == CardValue.TEN.getCardValue();
    }
    
    public boolean isThreeOfAKind(){ //returns true if the hand has three cards of the same value
        /* 
         If card 1 == card 3
         || card 2 == card 4
         || card 3 == card 5
         */
        
        sortHand();
        return playerHand[0].getValue() == playerHand[2].getValue()
                || playerHand[1].getValue() == playerHand[3].getValue()
                || playerHand[2].getValue() == playerHand[4].getValue();
    }
    
    public boolean isTwoPair(){ //returns true if the hand has two pairs of cards with the same values
        /*
        If card 1 == card 2 || card 3 == card 4 
        && card 4 == card 5 || card 2 == card 3 
        && card 4 == card 5
        */

        sortHand();
        return playerHand[0].getValue() == playerHand[1].getValue()
                && playerHand[2].getValue() == playerHand[3].getValue()
                || playerHand[0].getValue() == playerHand[1].getValue()
                && playerHand[3].getValue() == playerHand[4].getValue()
                || playerHand[1].getValue() == playerHand[2].getValue()
                && playerHand[3].getValue() == playerHand[4].getValue();
    }
    
    public boolean isPair(){ //returns true if the hand has one pair of cards with the same value
        sortHand();
        for (int i = 0; i < 5 - 1; i++){
            if (playerHand[i].getValue() == playerHand[i + 1].getValue())
                return true;
        }
        return false;
    }
    
    public boolean isHigh(){ //returns true if the hand has no classification
        sortHand(); //assuming that its sorted in desencding order when sorting. e.g ace will be defaulted to being 14 and will be sorted to the front
        if (isFlush() || isStraight() || isThreeOfAKind() || isTwoPair() || isPair() || isFullHouse()|| isFourOfAKind()){
            return false;
        } else{
            if (playerHand[0].getValue() == CardValue.ACE.getCardValue()){
                CardValue.ACE.getCardValue(true);
                return true; // hand has no classification - highest card is Ace
            } else{
                CardValue.ACE.getCardValue(false);
                return true; // hand has no classification - highest card is !Ace
            }
        }
    }

    public void dealFlop(){ //TODO - Im not sure how we're implementing this yet
        communityCards[0] = deck.dealNext();
        communityCards[1] = deck.dealNext();
        communityCards[2] = deck.dealNext();
    }

    public void dealTurn(){
        communityCards[3] = deck.dealNext();
        communityCardsCount++;
    }

    public void dealRiver(){
        communityCards[4] = deck.dealNext();
        communityCardsCount++;
    }

    public void showDown() {
        //
    }

    // showdown ->
    // for each player -> player.setBestHand based on a getBestHand method which
    // takes community cards into account.

    private void sortHand(){
        Arrays.sort(playerHand, new Comparator<Card>(){
            @Override
            public int compare(Card card1, Card card2){
                return Integer.compare(card2.getValue(), card1.getValue()); // sort in descending order
            }
        });
    }

    //Sorting should be done in desending order, otherwise we will need to change the above classifications code
    //unsure on catergorization?
}
