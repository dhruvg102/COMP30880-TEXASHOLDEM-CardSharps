package texasholdem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import poker.*;
import java.util.*;
import TexasHoldem.HoldemHand.CardValue;
import TexasHoldem.HoldemHand.HandValue;
import TexasHoldem.HoldemHand.Suit;
import TexasHoldem.HoldemHand;

public class HoldemHandTest {
        private HoldemHand holdemHand;
        private DeckOfCards deck;

        @Before
        public void setUp(){
                deck = new DeckOfCards();
                holdemHand = new HoldemHand(deck);
                deck.shuffle();
        }

        @Test
        public void testPlayerHandSize() {
                HoldemHand hand = new HoldemHand(deck);
                List<Card> playerCards = hand.getHand();
                assertEquals(2, playerCards.size());
        }

        @Test
        public void testAddCommunityCards() {
                List<Card> communityCards = new ArrayList<>();
                communityCards.add(new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue(true)));
                communityCards.add(new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue(false)));
                holdemHand.addCommunityCards(communityCards);
                assertEquals(2, communityCards.size());
        }

        @Test
        public void testCommunityCardsSize() {
                HoldemHand hand = new HoldemHand(deck);
                List<Card> communityCards = new ArrayList<>();
                communityCards.add(deck.dealNext());
                communityCards.add(deck.dealNext());
                communityCards.add(deck.dealNext());
                hand.addCommunityCards(communityCards);
                List<Card> allCards = hand.getFullHand();
                assertEquals(5, allCards.size());
        }

        @Test
        public void testNoDuplicateCards() {
                HoldemHand hand = new HoldemHand(deck);
                List<Card> allCards = hand.getFullHand();
                Set<Card> uniqueCards = new HashSet<>(allCards);
                assertEquals(allCards.size(), uniqueCards.size());
        }

        @Test
        public void testHoldemHand() {
                List<Card> hand = new ArrayList<>();
                hand.add(new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue(false)));
                hand.add(new FaceCard("King", Suit.DIAMONDS.toString(), CardValue.KING.getCardValue(false)));
                DeckOfCards deck = new DeckOfCards();
                List<Card> communityCards = new ArrayList<>();
                HoldemHand holdemHand = new HoldemHand(hand, deck, communityCards);
                assertEquals(holdemHand.getHand(), hand);
        }

        @Test
        public void testIsStraight(){
                            
        List<Card> handX = new ArrayList<>();
        handX.add(new NumberCard("Ace", Suit.HEARTS.toString(), CardValue.ACE.getCardValue(false)));
        handX.add(new NumberCard("Four", Suit.DIAMONDS.toString(), CardValue.FOUR.getCardValue(false)));
        handX.add(new NumberCard("Three", Suit.DIAMONDS.toString(), CardValue.THREE.getCardValue(false)));
        handX.add(new NumberCard("Deuce", Suit.DIAMONDS.toString(), CardValue.DEUCE.getCardValue(false)));
        handX.add(new NumberCard("Eigth", Suit.DIAMONDS.toString(), CardValue.EIGHT.getCardValue(false)));
        handX.add(new NumberCard("Five", Suit.DIAMONDS.toString(), CardValue.FIVE.getCardValue(false)));
        DeckOfCards deck = new DeckOfCards();
        
        HoldemHand newHand =  new HoldemHand(handX, deck, null);

        String result = "[(Ace of Hearts), (Deuce of Diamonds), (Three of Diamonds), (Four of Diamonds), (Five of Diamonds)]";
        assertEquals(result,newHand.getBestHand().toString());
        }
    
}