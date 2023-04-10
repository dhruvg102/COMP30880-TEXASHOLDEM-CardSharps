package TexasHoldem;
import poker.*;

import java.util.*;

public class HoldemHand {
    //enum for cardvalue, suit, risk and handvalue

    public enum CardValue{
        ACE(1, false, "Ace"),
        DEUCE(2, false, "Two"),
        THREE(3, false, "Three"),
        FOUR(4, false, "Four"),
        FIVE(5, false, "Five"),
        SIX(6, false, "Six"),
        SEVEN(7, false, "Seven"),
        EIGHT(8, false, "Eight"),
        NINE(9, false, "Nine"),
        TEN(10, false, "Ten"),
        JACK(11, false, "Jack"),
        QUEEN(12, false, "Queen"),
        KING(13, false, "King");

        private final int value;
        private final boolean isAceHigh;
        private final String name;
        public static final int ACE_HIGH_VALUE = 14;

        private CardValue(int value, boolean isAceHigh, String name){
            this.value = value;
            this.isAceHigh = isAceHigh;
            this.name = name;
        }

        public int getCardValue(boolean isAceHigh) {
            return this == ACE ? (isAceHigh ? ACE_HIGH_VALUE : 1) : value;
        }

        private int getCardValue() {
            return getCardValue(isAceHigh);
        }

        @Override
        public String toString() {
            return name;
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
    public static final int INIT_PLAYER_CARDS = 2; // total number of cards to be determined
    public static final int NUM_COMMUNITY_CARDS = 5;
    public static final int TOTAL_CARDS = INIT_PLAYER_CARDS+NUM_COMMUNITY_CARDS;
    
    private List<Card> communityCards; //define community cards as an array of cards for reference
    private List<Card> playerHand; //define playerHand as an array of cards
    private DeckOfCards deck; //define field of deck

    public HoldemHand(List<Card> hand, DeckOfCards deck, List<Card> communityCards){
        this.playerHand = hand; //init playerHand
        this.deck = deck; //init deck
        this.communityCards = communityCards;
    }

    public HoldemHand(DeckOfCards deck){
        this.deck = deck;
        
        this.playerHand = new ArrayList<>();

        this.playerHand.add(deck.dealNext());
        this.playerHand.add(deck.dealNext());
        
        this.communityCards = new ArrayList<>();
    }

    public void addCommunityCards(List<Card> cards){
        this.communityCards.addAll(cards);
    }

    public List<Card> getBestHand(){
        List<Card> cards = new ArrayList<>();

        cards.addAll(playerHand);
        //cards.addAll(communityCards);
        

        List<List<Card>> possibleHands = generatePossibleHands(cards);
        List<Card> bestHand = null;
        int bestHandValue = 0;

        for (List<Card> hand : possibleHands){
            int handValue = evaluateHand(hand.subList(0, 5));
            if (handValue > bestHandValue) {
                bestHandValue = handValue;
                bestHand = hand.subList(0, 5);
            }
        }
        return bestHand;
    }
  

    
    //Combinations not permutation
    public static List<List<Card>> generatePossibleHands(List<Card> list) {
        if (list.size() == 0) {
            List<List<Card>> result = new ArrayList<List<Card>>();
            result.add(new ArrayList<Card>());
            return result;
        }
    
        List<List<Card>> returnMe = new ArrayList<List<Card>>();
    
        Card firstElement = list.remove(0);
    
        List<List<Card>> recursiveReturn = generatePossibleHands(list);
        for (List<Card> li : recursiveReturn) {
    
            for (int index = 0; index <= li.size(); index++) {
                List<Card> temp = new ArrayList<Card>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }
    
        }
        return returnMe;
    }

    public int evaluateHand(List<Card> hand) {
        sortHand();
        if (isRoyalFlush()){
            return HandValue.ROYALFLUSH_VALUE.getHandValue();
        } else if (isStraightFlush(hand)){
            return HandValue.STRAIGHTFLUSH_VALUE.getHandValue() + hand.get(4).getValue();
        } else if (isFourOfAKind()){
            return HandValue.FOURS_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isFullHouse()){
            return HandValue.FULLHOUSE_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isFlush()){
            int value = 0;
            for (int i = 0; i < 5; i++) {
                value += hand.get(i).getValue() * Math.pow(100, i);
            }
            return HandValue.FLUSH_VALUE.getHandValue() + value;
        } else if (isStraight(hand)){
            return HandValue.STRAIGHT_VALUE.getHandValue() + hand.get(4).getValue();
        } else if (isThreeOfAKind()){
            return HandValue.THREES_VALUE.getHandValue() + hand.get(2).getValue();
        } else if (isPair()){ //added pair
            return HandValue.PAIR_VALUE.getHandValue() + hand.get(2).getValue();
        } else {
            int value = 0;
            for (int i = 0; i < 4; i++){
                value += hand.get(i).getValue() * Math.pow(100, i);
            }
            return HandValue.HIGHCARD_VALUE.getHandValue() + value;
        }
    }


    public void sortHand() {
        //playerHand.sort(Collections.reverseOrder());
        Collections.sort(playerHand, Collections.reverseOrder()); // descending order, maybe need to sort by value
                                                                  // instead? I think this does by name
    }


    public int getRiskWorthiness(){ //We override this value for specific hands such as Straight, FullHouse etc..
        return DEFAULT_RISK; //TODO - Change to enum with setter inside

        //use evaluate hand and return value in enum based on the card evaluation, e.g if royal flush return RiskWorthiness.ROYALFLUSH_RISK
    }


    //Display a hand
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player Hand: ");
        for (Card card : playerHand) {
            sb.append(card.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("\n");
        sb.append("Community Cards: ");
        for (Card card : communityCards) {
            sb.append(card.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("\n");
        return sb.toString();
    }


    //modifiers
    public void setCard(int num, Card card){
        if (num >= 0 && num < TOTAL_CARDS)
            playerHand.set(num, card);
    }

    //Accesors
    public Card getCard(int num){ //get card at index
        if (num >= 0 && num < TOTAL_CARDS)
            return playerHand.get(num);
        else
            return null;
    }

    public List<Card> getCommunityCards(){ //may make things easier with this
        return communityCards;
    }

    public int getValue(){
        return getCard(0).getValue(); // simply return the value of the higest card
    }

    public List<Card> getHand(){
        return new ArrayList<>(playerHand);
    }

    public List<Card> getFullHand(){
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerHand);
        allCards.addAll(communityCards);
        return allCards;
    }


    //Hand Classifiers TODO: Ensure this works on implmentation of game - may need to fix sort to cardvalue!
    public boolean isFourOfAKind() {
        sortHand();
        /*
         * IF card 1 == card 4 (checks index for sorted cards etc)
         * || card 2 == card 5
         */
        return playerHand.get(0).getValue() == playerHand.get(3).getValue()
                || playerHand.get(1).getValue() == playerHand.get(4).getValue();
    }

    public boolean isFullHouse() {
        sortHand();
        return playerHand.get(0).getValue() == playerHand.get(1).getValue()
                && playerHand.get(2).getValue() == playerHand.get(4).getValue()
                || playerHand.get(0).getValue() == playerHand.get(2).getValue()
                        && playerHand.get(3).getValue() == playerHand.get(4).getValue();
    }

    public boolean isStraight(List<Card> hand) {
        sortHand();
		boolean x =  ( hand.get(0).getValue() == hand.get(1).getValue() + 1 && // ordered by rank
        hand.get(1).getValue() == hand.get(2).getValue() + 1 &&
        hand.get(2).getValue() == hand.get(3).getValue() + 1 &&	
        hand.get(3).getValue() == hand.get(4).getValue() + 1)
				||
			   (hand.get(0).getValue() == hand.get(1).getValue() - 1 &&  // ordered by game value
               hand.get(1).getValue()  == hand.get(2).getValue() - 1 &&
               hand.get(2).getValue()  == hand.get(3).getValue() - 1 &&	
               hand.get(3).getValue()  == hand.get(4).getValue() - 1);
        return x;
        }
	
    // public boolean isStraight() {
    //     sortHand();
    //     boolean isAceLow = playerHand.get(0).getValue() == CardValue.ACE.getCardValue(false);
    //     int count = 0;
    //     for (int i = 4; i > 1; i--) {
    //         if (playerHand.get(i).getValue() == playerHand.get(i - 1).getValue() + 1) {
    //             count++;
    //         } else if (playerHand.get(i).getValue() == playerHand.get(i - 1).getValue()) {
    //             continue; //this may break stuff as there is no condition (e.g get stuck here)
    //         } else if (isAceLow && playerHand.get(i).getValue() == CardValue.FIVE.getCardValue()) {
    //             count++;
    //         } else {
    //             break;
    //         }
    //     }
    //     return count == 4;
    // }

    public boolean isFlush() {
        // returns true if all cards in the hand are the same suit
        for (int i = 0; i < 4; i++) {
            if (playerHand.get(i).getSuit().equals(playerHand.get(0).getSuit())) {
                return false;
            }
        }
        return true;
    }

    public boolean isStraightFlush(List<Card> hand){ //returns true if hand is both a flush and a straight
        return isFlush() && isStraight(hand);
    }

    public boolean isRoyalFlush() { // returns true if the hand is a straight flush from 'Ten -> Ace'
        sortHand();
        /*
         * If card 1 == value of 10
         * && card 2 == value of jack
         * && card 3 == value of queen
         * && card 4 == value of king
         * && card 5 == value of ace (assuming that its treated as a high card)
         */
        return isFlush() && playerHand.get(0).getValue() == CardValue.ACE.getCardValue(true)
                && playerHand.get(1).getValue() == CardValue.KING.getCardValue()
                && playerHand.get(2).getValue() == CardValue.QUEEN.getCardValue()
                && playerHand.get(3).getValue() == CardValue.JACK.getCardValue()
                && playerHand.get(4).getValue() == CardValue.TEN.getCardValue();
    }
    
    public boolean isThreeOfAKind() { // returns true if the hand has three cards of the same value
        /*
         * If card 1 == card 3
         * || card 2 == card 4
         * || card 3 == card 5
         */

        sortHand();
        return playerHand.get(0).getValue() == playerHand.get(2).getValue()
                || playerHand.get(1).getValue() == playerHand.get(3).getValue()
                || playerHand.get(2).getValue() == playerHand.get(4).getValue();
    }
    
    public boolean isTwoPair() { // returns true if the hand has two pairs of cards with the same values
        /*
         * If card 1 == card 2 || card 3 == card 4
         * && card 4 == card 5 || card 2 == card 3
         * && card 4 == card 5
         */

        sortHand();
        return playerHand.get(0).getValue() == playerHand.get(1).getValue()
                && playerHand.get(2).getValue() == playerHand.get(3).getValue()
                || playerHand.get(0).getValue() == playerHand.get(1).getValue()
                && playerHand.get(3).getValue() == playerHand.get(4).getValue()
                || playerHand.get(1).getValue() == playerHand.get(2).getValue()
                && playerHand.get(3).getValue() == playerHand.get(4).getValue();
    }
    
    public boolean isPair() { // returns true if the hand has one pair of cards with the same value
        sortHand();
        for (int i = 0; i < 5 - 1; i++) {
            if (playerHand.get(i).getValue() == playerHand.get(i + 1).getValue())
                return true;
        }
        return false;
    }
    
    public boolean isHigh(List<Card> hand) { // returns true if the hand has no classification
        sortHand(); // assuming that its sorted in desencding order when sorting. e.g ace will be
                    // defaulted to being 14 and will be sorted to the front
        if (isFlush() || isStraight(hand) || isThreeOfAKind() || isTwoPair() || isPair() || isFullHouse()
                || isFourOfAKind()) {
            return false;
        } else {
            if (playerHand.get(0).getValue() == CardValue.ACE.getCardValue()) {
                CardValue.ACE.getCardValue(true);
                return true; // hand has no classification - highest card is Ace
            } else {
                CardValue.ACE.getCardValue(false);
                return true; // hand has no classification - highest card is !Ace
            }
        }
    }

    public static void main(String[] args) {
        
                
        List<Card> handX = new ArrayList<>();
        handX.add(new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue(false)));
        handX.add(new NumberCard("Four", Suit.DIAMONDS.toString(), CardValue.FOUR.getCardValue(false)));
        handX.add(new NumberCard("Three", Suit.DIAMONDS.toString(), CardValue.THREE.getCardValue(false)));
        handX.add(new NumberCard("Deuce", Suit.DIAMONDS.toString(), CardValue.DEUCE.getCardValue(false)));
        handX.add(new NumberCard("Eigth", Suit.DIAMONDS.toString(), CardValue.EIGHT.getCardValue(false)));
        handX.add(new NumberCard("Five", Suit.DIAMONDS.toString(), CardValue.FIVE.getCardValue(false)));

        /*
         * 
         *  A - 2- 3- 4- 5
         * 
         */
        

        DeckOfCards deck = new DeckOfCards();
        
        HoldemHand newHand =  new HoldemHand(handX, deck, null);

        
        
       
        System.out.println(newHand.getBestHand().toString());

        // for(int i=0;i<24;i++){
        //     newHand.generatePossibleHands().get(i).toString();
        //     System.out.print("\t");
        //     newHand.generatePossibleHands1().get(i).toString(); 
        // }
    }
}
