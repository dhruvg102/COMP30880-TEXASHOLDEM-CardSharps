package texasholdem;
import org.junit.Test;
import static org.junit.Assert.*;

import poker.*;
import TexasHoldem.HoldemHand;
import TexasHoldem.HoldemHand.CardValue;
import TexasHoldem.HoldemHand.Suit;

public class HoldemHandTest {

    /*
     * WORKING TESTS --- NOTE: Cards are sorted in order of their value using the handSort() method in HoldemHand.
     * FOUR OF A KIND
     * THREE OF A KIND
     * PAIR
     * FULL HOUSE
     * FLUSH
     * ROYAL FLUSH
     * HIGH
     * STRAIGHT
     * STRAIGHT FLUSH
     */

    @Test
    public void testIsFourOfAKind() { 
        // TEST TRUE
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.SPADES.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.HEARTS.toString(), CardValue.KING.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isFourOfAKind());

        // TEST FALSE
        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.SPADES.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("King", Suit.CLUBS.toString(), CardValue.KING.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isFourOfAKind());
    }

    @Test
    public void testIsThreeOfAKind() {
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isThreeOfAKind());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ten", Suit.HEARTS.toString(), CardValue.TEN.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isThreeOfAKind());
    }

    @Test
    public void isPair() {
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ten", Suit.HEARTS.toString(), CardValue.TEN.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isPair());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Three", Suit.CLUBS.toString(), CardValue.THREE.getCardValue()),
                new NumberCard("Ten", Suit.HEARTS.toString(), CardValue.TEN.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isPair());
    }

    @Test
    public void testIsFullHouse(){
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.CLUBS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isFullHouse());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.CLUBS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isFullHouse());
    }

    @Test
    public void isFlush(){
        HoldemHand hand = new HoldemHand(new Card[]{
                    new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                    new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                    new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                    new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
                    new NumberCard("Nine", Suit.DIAMONDS.toString(), CardValue.NINE.getCardValue()),
        },new DeckOfCards());
        assertTrue(hand.isFlush());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                new NumberCard("Ten", Suit.CLUBS.toString(), CardValue.TEN.getCardValue()),
                new NumberCard("Nine", Suit.HEARTS.toString(), CardValue.NINE.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isFlush());
    }

    @Test
    public void isRoyalFlush(){
        HoldemHand hand = new HoldemHand(new Card[] {
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                new FaceCard("Jack", Suit.DIAMONDS.toString(), CardValue.JACK.getCardValue()),
                new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isRoyalFlush());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.CLUBS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.HEARTS.toString(), CardValue.QUEEN.getCardValue()),
                new NumberCard("Nine", Suit.DIAMONDS.toString(), CardValue.NINE.getCardValue()),
                new NumberCard("Three", Suit.DIAMONDS.toString(), CardValue.THREE.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isRoyalFlush());
    }

    @Test
    public void isHigh(){
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.HEARTS.toString(), CardValue.KING.getCardValue()),
                new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
                new NumberCard("Six", Suit.SPADES.toString(), CardValue.SIX.getCardValue()),
                new NumberCard("Four", Suit.CLUBS.toString(), CardValue.FOUR.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isHigh());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue()),
                new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
                new NumberCard("Six", Suit.SPADES.toString(), CardValue.SIX.getCardValue()),
                new NumberCard("Four", Suit.CLUBS.toString(), CardValue.FOUR.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isHigh());
    }

    @Test
    public void isStraight(){
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Eight", Suit.DIAMONDS.toString(), CardValue.EIGHT.getCardValue()),
                new NumberCard("Seven", Suit.HEARTS.toString(), CardValue.SEVEN.getCardValue()),
                new NumberCard("Six", Suit.DIAMONDS.toString(), CardValue.SIX.getCardValue()),
                new NumberCard("Five", Suit.SPADES.toString(), CardValue.FIVE.getCardValue()),
                new NumberCard("Four", Suit.CLUBS.toString(), CardValue.FOUR.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isStraight());

        hand = new HoldemHand(new Card[]{
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                new NumberCard("Ten", Suit.HEARTS.toString(), CardValue.TEN.getCardValue()),
                new NumberCard("Eight", Suit.DIAMONDS.toString(), CardValue.EIGHT.getCardValue()),
                new NumberCard("Five", Suit.SPADES.toString(), CardValue.FIVE.getCardValue()),
                new NumberCard("Four", Suit.CLUBS.toString(), CardValue.FOUR.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isStraight());
    }


    @Test
    public void isStraightFlush(){
        HoldemHand hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                new FaceCard("Jack", Suit.DIAMONDS.toString(), CardValue.JACK.getCardValue()),
                new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
        }, new DeckOfCards());
        assertTrue(hand.isStraightFlush());

        hand = new HoldemHand(new Card[]{
                new NumberCard("Ace", Suit.DIAMONDS.toString(), CardValue.ACE.getCardValue()),
                new FaceCard("King", Suit.CLUBS.toString(), CardValue.KING.getCardValue()),
                new FaceCard("Queen", Suit.DIAMONDS.toString(), CardValue.QUEEN.getCardValue()),
                new FaceCard("Jack", Suit.HEARTS.toString(), CardValue.JACK.getCardValue()),
                new NumberCard("Ten", Suit.DIAMONDS.toString(), CardValue.TEN.getCardValue()),
        }, new DeckOfCards());
        assertFalse(hand.isStraightFlush());
    }
}