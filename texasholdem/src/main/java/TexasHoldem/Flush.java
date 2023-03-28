
package TexasHoldem;

// This package provides classes necessary for implementing a game system for playing poker


public class Flush extends PokerHand  {
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public Flush(Card[] hand, DeckOfCards deck) {
		super(hand, deck);
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the riskworthiness of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
		
	public int getRiskWorthiness() {
		return 100 - PokerHand.FLUSH_RISK; 
	}
		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// What is the value of this hand?
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public int getValue() 	{
		return PokerHand.FLUSH_VALUE + getCard(0).getValue()*10 + getCard(1).getValue();
	}
	

	//--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
	// Display
	//--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
	
	public String toString() {
		return "Flush: " + super.toString();
	}
	
}

