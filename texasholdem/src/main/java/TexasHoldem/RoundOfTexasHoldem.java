
package TexasHoldem;
import poker.*;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's


//TODO

public class RoundOfTexasHoldem {	
	public static final int DELAY_BETWEEN_ACTIONS	=	1000;  // number of milliseconds between game actions
	
	private PlayerInterface[] players;
	
	private DeckOfCards deck;
	private int numPlayers;
	
	private int button = 0; // Player starts as the dealer;		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoundOfTexasHoldem(DeckOfCards deck, PlayerInterface[] players) {
		this.players = players; //init players
		this.deck    = deck; //init deck
		numPlayers   = players.length; //get totalPlayers

		button++;

		System.out.println("\n\nNew Deal:\n\n"); 
		deal();
		
		canOpen();

		//openRound();

		//discard();
	}
		

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Accessors
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumPlayers() {
		return numPlayers;	
		
	}
	
	
	public PlayerInterface getPlayer(int num) {
		if (num >= 0 && num <= numPlayers)
			return players[num];
		else
			return null;
	}
	
								
	
	public int getNumActivePlayers() {
	    // how many players have not folded yet?

		int count = 0;
		
		for (int i = 0; i < getNumPlayers(); i++)
			if (getPlayer(i) != null && !getPlayer(i).hasFolded() && !getPlayer(i).isBankrupt())
				count++;
		
		return count;
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Modifiers
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void removePlayer(int num) {
		if (num >= 0 && num < numPlayers)
		{
			System.out.println("\n> " + players[num].getName() + " leaves the game.\n");
			
			players[num] = null;
		}
	}	
	

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Find the player with the best hand
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public int getNumBestPlayer(boolean display) {
		int bestHandScore = 0, score = 0, bestPos = 0;
		PlayerInterface bestPlayer = null, currentPlayer = null;
			
		for (int i = 0; i < getNumPlayers(); i++) {
			currentPlayer = getPlayer(i);
				
			if (currentPlayer == null || currentPlayer.hasFolded())
				continue;
				
			score = currentPlayer.getHand().getValue();
				
			if (score > bestHandScore) {
				if (display) {
					if (bestHandScore == 0)
						System.out.println("> " + currentPlayer.getName() + " goes first:\n" + 
											 currentPlayer.getHand());
					else
						System.out.println("> " + currentPlayer.getName() + " says 'Read them and weep:'\n" + 
											 currentPlayer.getHand());
				}
				
				bestPos		  = i;
				bestHandScore = score;
				bestPlayer    = currentPlayer;
			}
			else
			if (display)
				System.out.println("> " + currentPlayer.getName() + " says 'I lose':\n" + 
										  currentPlayer.getHand());
		}

		return bestPos;
	}
		
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Restart all the players and deal them into the game
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	public void deal() {

		for (int i = 0; i < getNumPlayers(); i++) {
			int index = (button+ i+ 1) % getNumPlayers();

			if (getPlayer(index) != null) {
				if (getPlayer(index).isBankrupt())
					removePlayer(index);
				else {
					getPlayer(index).reset();
					getPlayer(index).dealTo(deck);
					
					System.out.println(getPlayer(index));
				}
			}
		}
		
		System.out.println("\n");
	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Allow each player to discard some cards and receive new ones
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	/* public void discard() {
		for (int i = 0; i < getNumPlayers(); i++) {
			if (getPlayer(i) != null)
				getPlayer(i).discard();
		}
	} */

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// See if we can open a round (at least one player must have atg least
	// a pair)
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	private final int SMALL_BLIND = 1;
	private final int BIG_BLIND = 2;

	public void canOpen() {

		//Player to the left of the dealer posts the small blind
		{
			if(players[button+1].getBank()<SMALL_BLIND){
				//Player does not have enough chips to open
				System.out.println(players[button+2].getName() + "says: I cannot post the Small Blind. I am All-In");			
			}
			else if(players[button+1].getBank()>=SMALL_BLIND){
				System.out.println(players[button+1].getName() + "says: I can post the Small Blind. ");			
			}
		}

		//Player to the left of the small blind posts the big blind
		{
			if(players[button+2].getBank()<BIG_BLIND)
			//Player does not have enough chips to open
			System.out.println(players[button+2].getName() + "says: I cannot post the Big Blind. I am All-In");
			else if(players[button+2].getBank()<BIG_BLIND)
			//Player does not have enough chips to open
			System.out.println(players[button+2].getName() + "says: I can post the Big Blind.");
		}


	}

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Open this round of poker
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public void openRound()	{
		
		PlayerInterface player = null;
		System.out.println("");
		
		for (int i = 0; i < numPlayers; i++) {
			player = getPlayer(i);
			
			if (player == null || player.isBankrupt()) 
				continue;
			
			if (player.getHand() instanceof High) //TODO - This doesnt apply to texasHoldem
				System.out.println("> " + player.getName() + " says: I cannot open.");
			else
				System.out.println("> " + player.getName() + " says: I can open.");
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of Texas Hold'em
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	//TODO: Side bets Showdown
	public void play(){

		// Initialize bank and print the values for each player;
		PotOfMoney pot = new PotOfMoney();
		int numActive = getNumActivePlayers();
		int stake = -1;
		PlayerInterface currentPlayer = null;
		deck.reset();
		
		roundOpen(pot, players[button+1], players[button+2]);
		
		// Game actions
		// (call, raise, fold);
		// Start betting sequence left of the big blind;
		preflop(pot);

		//Whilst there are >= 2 players are still active;
		//CHECK?? is also possible (no idea)
		flop(pot);

		// Turn 4th community card (turn) is turned while there are >= 2 players active.
		//TODO
		turn(pot);

		// Turn 5th community card (river) is turned if there are still >= 2 players active.
		river(pot);

		



	}

	private void roundOpen(PotOfMoney pot, PlayerInterface smallBlind, PlayerInterface bigBlind ){

		//Post small blind
		smallBlind.postBlind(pot, SMALL_BLIND, "Small Blind");
		//Post big blind
		smallBlind.postBlind(pot, SMALL_BLIND, "Big Blind");
		
	}
	private void preflop(PotOfMoney pot){
		int playerStart = button+3;	//3 becouse player left to big blind starts
		
		for(int i =0;i<getNumPlayers();i++){
			int current = (playerStart+i) % getNumPlayers();
			players[current].nextAction(pot);
		}

	}
	private void flop(PotOfMoney pot){
		// Turn 3 community (flop) cards 

		int playerStart = button+1;	//1 becouse player left to dealer starts
		
		for(int i =0;i<getNumPlayers();i++){
			int current = (playerStart+i) % getNumPlayers();
			players[current].nextAction(pot);
		}

	}

	private void turn(PotOfMoney pot){
		// Deal the turn card card 

		int playerStart = button+1;	//1 becouse player left to dealer starts
		
		for(int i =0;i<getNumPlayers();i++){
			int current = (playerStart+i) % getNumPlayers();
			players[current].nextAction(pot);
		}

	}

	private void river(PotOfMoney pot){
		// Deal the river card 

		int playerStart = button+1;	//1 becouse player left to dealer starts
		
		for(int i =0;i<getNumPlayers();i++){
			int current = (playerStart+i) % getNumPlayers();
			players[current].nextAction(pot);
		}

	}

	private void showdown(PotOfMoney pot){

		// Check for winner of side-pot(s);
		// Check for winner of main-pot;

		//Last person to bet or raise shows their card , unless there was no bet on the final round
		//in which case the player immediately clockwise from the button shows their cards first


	}

	// public void play() {
	// 	PotOfMoney pot = new PotOfMoney();
	// 	int numActive = getNumActivePlayers();
	// 	int stake = -1;
	// 	PlayerInterface currentPlayer = null;
	// 	deck.reset();

	// 	// while the stakes are getting bigger and there is at least one active player,
	// 	// then continue to go around the table and play

	// 	while (stake < pot.getCurrentStake() && numActive > 0) {
	// 		stake = pot.getCurrentStake();

	// 		for (int i = 0; i < getNumPlayers(); i++) {
	// 			currentPlayer = getPlayer(i);

	// 			if (currentPlayer == null || currentPlayer.hasFolded())
	// 				continue;

	// 			//delay(DELAY_BETWEEN_ACTIONS);

	// 			if (numActive == 1) { //if only one player remains
	// 				currentPlayer.takePot(pot);
	// 				System.out.println("\nNo Players left in the game.\n");
	// 				return;
	// 			}
				
	// 			currentPlayer.nextAction(pot);

	// 			if (currentPlayer.hasFolded()){ //checks for fold
	// 				numActive--;
	// 			} 
	// 		}
	// 	}

	// 	PlayerInterface bestPlayer = getPlayer(getNumBestPlayer(true));
	// 	if (bestPlayer != null)
	// 		bestPlayer.takePot(pot);
	// }
	
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	/* private void delay(int numMilliseconds) {
		try {
			Thread.sleep(numMilliseconds);
		} catch (Exception e) {}
	} */
}  