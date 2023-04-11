package TexasHoldem;

import poker.Card;

import java.util.ArrayList;

/*
TODO ensure hold cards are preserved in position 0 and 1 in HoldemHand class
 */

public class PostFlopHandAnalyser {
    private HoldemHand hand;
    private ArrayList<Card> holdCards = new ArrayList<Card>();

    private ArrayList<Card> communityCards = new ArrayList<Card>();

    private ArrayList<Card> mergedHand = new ArrayList<Card>();

    public PostFlopHandAnalyser(HoldemHand hand) {
        this.hand = hand;

        this.holdCards.add(hand.getCard(0));
        this.holdCards.add(hand.getCard(1));

        this.communityCards = (ArrayList<Card>) hand.getCommunityCards();

        this.mergedHand.addAll(holdCards);
        this.mergedHand.addAll(communityCards);
    }

    public void sortMergedHandAscending() {
        Card temp;

        for(int i = 0; i < mergedHand.size() - 1; i++) {
            for(int j = 0; j < mergedHand.size() - i - 1; j++) {
                if(mergedHand.get(j).getValue() > mergedHand.get(j + 1).getValue()) {
                    temp = mergedHand.get(j);
                    mergedHand.set(j, mergedHand.get(j + 1));
                    mergedHand.set(j + 1, temp);
                }
            }
        }
    }

    public boolean isTopPair() {
        boolean cardIsEqual = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for (int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    cardIsEqual = true;
                }
            }
        }

        return cardIsEqual;
    }

    public boolean isOverPair() {
        boolean cardIsHighest = true;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() <= communityCards.get(j).getValue()) {
                    cardIsHighest = false;
                }
            }
        }

        if(!(holdCards.get(0).getValue() == holdCards.get(1).getValue())) {
            cardIsHighest = false;
        }

        return cardIsHighest;
    }

    public boolean isWeakTwoPair() {
        boolean holdPairFound = false;
        boolean communityPairFound = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    holdPairFound = true;
                }
            }
        }

        for(int i = 0; i < communityCards.size(); i++) {
            for(int j = i; j < communityCards.size(); j++) {
                if(communityCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    communityPairFound = true;
                }
            }
        }

        return holdPairFound && communityPairFound;
    }

    public boolean isStrongTwoPair() {
        boolean firstPairFound = false;
        boolean secondPairFound = false;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(i==0 && holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    firstPairFound = true;
                }
                else if (i==1 && holdCards.get(i).getValue() == communityCards.get(j).getValue()) {
                    secondPairFound = true;
                }
            }
        }

        return firstPairFound && secondPairFound;
    }

    public boolean isMonsterHand() {
        return (isStrongTwoPair() || (hand.evaluateHand(mergedHand) >= 10000));//TODO hand.HandValue.THREES_VALUE.getHandValue()) );
    }

    public boolean isOESD() {
        int OESDCount = 4;
        int runningSequence = 0;
        int previousValue = -1;
        int currentValue;

        if(hand.evaluateHand(mergedHand) != 100000000) { //TODO value of straight
            sortMergedHandAscending();

            for(int i = 0; i < mergedHand.size(); i++){
                currentValue = mergedHand.get(i).getValue();

                if(currentValue == previousValue + 1){
                    runningSequence++;
                }
                else {
                    runningSequence = 0;
                }

                previousValue = currentValue;
            }
        }

        return runningSequence == OESDCount;
    }

    private boolean isOESD(int[] cardValues) {
        int OESDCount = 4;
        int runningSequence = 0;
        int previousValue = -1;
        int currentValue;

        if(hand.evaluateHand(mergedHand) != 100000000) { //TODO value of straight

            for(int i = 0; i < cardValues.length; i++){
                currentValue = cardValues[i];

                if(currentValue == previousValue + 1){
                    runningSequence++;
                }
                else {
                    runningSequence = 0;
                }

                previousValue = currentValue;
            }
        }

        return runningSequence == OESDCount;
    }

    public boolean isGutshot() {
        boolean gutshot = false;
        int[] mergedHandValues = new int[mergedHand.size()];
        int[] temp;

        sortMergedHandAscending();

        for (int i = 0; i < mergedHand.size(); i++) {
            mergedHandValues[i] = mergedHand.get(i).getValue();
        }
        temp = mergedHandValues;

        if (hand.evaluateHand(mergedHand) != 100000000 && !(isGutshot())) { //TODO value of straight)
            for (int i = 0; i < mergedHand.size(); i++) {
                for (int j = 2; j <= 14; j++) {
                    mergedHandValues[i] = j;

                    if (isOESD(mergedHandValues)) {
                        gutshot = true;
                    }
                }
                mergedHandValues = temp;
            }
        }

        return gutshot;
    }

    public boolean isDBGutshot() {
        boolean gutshot = false;
        boolean dbGutshot = false;
        int[] mergedHandValues = new int[mergedHand.size()];
        int[] temp;
        int[] reducedHand;
        int gutshotIndex = 0;

        sortMergedHandAscending();

        for (int i = 0; i < mergedHand.size(); i++) {
            mergedHandValues[i] = mergedHand.get(i).getValue();
        }
        temp = mergedHandValues;

        if (hand.evaluateHand(mergedHand) != 100000000 && !(isGutshot())) { //TODO value of straight)
            for (int i = 0; i < mergedHand.size(); i++) {
                int tempGutshotIndex = i;
                for (int j = 2; j <= 14; j++) {
                    mergedHandValues[i] = j;

                    if (isOESD(mergedHandValues)) {
                        gutshot = true;
                        gutshotIndex = tempGutshotIndex;
                    }
                }
                mergedHandValues = temp;
            }

            reducedHand = new int[mergedHand.size() - gutshotIndex + 1];

            int tempIndex = 0;
            for(int i = gutshotIndex; i < mergedHand.size(); i ++) {
                    reducedHand[tempIndex++] = mergedHand.get(i).getValue();
            }

            temp = reducedHand;

            if (gutshot) {
                for (int i = gutshotIndex; i < reducedHand.length; i++) {
                    for (int j = 2; j <= 14; j++) {
                        reducedHand[i] = j;

                        if (isOESD(reducedHand)) {
                            dbGutshot = true;
                        }
                    }
                    reducedHand = temp;
                }
            }
        }

        return dbGutshot;
    }

    public boolean isFlushDraw() { //TODO add case for monochrome community cards
        int heartCount = 0;
        int spadeCount = 0;
        int clubCount = 0;
        int diamondCount = 0;

        boolean flushDraw = false;

        for(int i = 0; i < mergedHand.size(); i ++){
            switch (mergedHand.get(i).getSuit()) {
                case "hearts":
                    heartCount++;
                case "spades":
                    spadeCount++;
                case "clubs":
                    clubCount++;
                case "diamonds":
                    diamondCount++;
            }
        }

        if(heartCount == 4 || spadeCount == 4 || clubCount == 4 || diamondCount == 4 ) {
            flushDraw = true;
        }

        return flushDraw;
    }

    public boolean isMonsterDraw() {
        if(isFlushDraw() && (isOESD() || isGutshot() || isTopPair())) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isOverCard() {
        boolean cardIsHighest = true;

        for(int i = 0; i < holdCards.size(); i++) {
            for(int j = 0; j < communityCards.size(); j++) {
                if(holdCards.get(i).getValue() <= communityCards.get(j).getValue()) {
                    cardIsHighest = false;
                }
            }
        }

        return cardIsHighest;
    }

    public boolean isTrash() {
        boolean trash = true;

        if(isOESD() || isGutshot() || isDBGutshot() || isFlushDraw() || isMonsterDraw()){
            trash = false;
        }

        if(isTopPair() || isOverPair() || isWeakTwoPair() || isStrongTwoPair() || isMonsterHand()){
            trash = false;
        }

        if(isOverCard()){
            trash = false;
        }

        return trash;
    }
}
