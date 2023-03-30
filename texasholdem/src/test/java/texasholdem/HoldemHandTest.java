package texasholdem;
import org.junit.Test;
import static org.junit.Assert.*;

import poker.*;
import TexasHoldem.HoldemHand;

public class HoldemHandTest {
    @Test
    public void testIsFourOfAKind() {
        // Test for matching handvb
        HoldemHand hand = new HoldemHand(new Card[] {
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("King", "SUIT", 13),
        }, new DeckOfCards());
        assertTrue(hand.isFourOfAKind());

        // Test for non-matching hand
        hand = new HoldemHand(new Card[] {
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("Ace", "SUIT", 14),
                new NumberCard("King", "SUIT", 13),
                new NumberCard("King", "SUIT", 13),
        }, new DeckOfCards());
        assertFalse(hand.isFourOfAKind());
    }
}