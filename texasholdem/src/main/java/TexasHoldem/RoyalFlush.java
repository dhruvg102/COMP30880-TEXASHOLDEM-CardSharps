
package TexasHoldem;

// This package provides classes necessary for implementing a game system for playing poker


public class RoyalFlush extends StraightFlush {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoyalFlush(Card[] hand, DeckOfCards deck) {
		super(hand, deck);
	}

	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Display
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public String toString() {
		return "Royal Flush: " + super.toString();
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getRiskWorthiness() {
		return 100 - PokerHand.ROYALFLUSH_RISK; 
	}
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getValue() {
		return PokerHand.ROYALFLUSH_VALUE;
	}

}

