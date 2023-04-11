package TexasHoldem;

import poker.Card;

import java.util.ArrayList;

/*
TODO ensure hold cards are preserved in position 0 and 1 in HoldemHand class
 */

public class PostFlopHandAnalyser {
    private enum handType {
        MONSTER,
        OVERPAIR,
        STRONGTWOPAIR,
        WEAKTWOPAIR,
        MONSTERDRAW,
        OESD,
        FDRAW,
        DBGUNSHOT,
        GUTSHOT,
        OVERCARDS,
        TRASH
    }

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

    private boolean isTopPair() {
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

    private boolean isOverPair() {
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

    private boolean isWeakTwoPair() {
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

    private boolean isStrongTwoPair() {
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

    private boolean isMonsterHand() {
        return (isStrongTwoPair() || (hand.evaluateHand(mergedHand) >= 10000));//hand.HandValue.THREES_VALUE.getHandValue()) );
    }
}
