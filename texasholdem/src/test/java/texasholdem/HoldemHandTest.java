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
}