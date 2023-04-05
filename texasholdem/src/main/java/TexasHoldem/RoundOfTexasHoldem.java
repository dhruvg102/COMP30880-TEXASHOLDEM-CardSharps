
package TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private int SMALL_BLIND = 1;
	private int BIG_BLIND = 2*SMALL_BLIND;


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
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Play a round of Texas Hold'em
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//


	ArrayList<PotTexasHoldem> pots = new ArrayList<PotTexasHoldem>();

	//TODO: Side bets Showdown
	public void play(){

		List<PlayerInterface> listPlayers = Arrays.asList(players);
		PotTexasHoldem mainPot = new PotTexasHoldem((ArrayList<PlayerInterface>) listPlayers);
		pots.add(mainPot);

		// Initialize bank and print the values for each player;
		Integer numActive = mainPot.getNumPlayers();
		Integer stake = -1;
		PlayerInterface currentPlayer = null;
		deck.reset();



		roundOpen(mainPot, players[button+1], players[button+2]);

		// Game actions
		// (call, raise, fold);
		// Start betting sequence left of the big blind;
		preflop(stake, numActive,pots.size()-1);

		//Whilst there are >= 2 players are still active;
		//CHECK?? is also possible (no idea)
		flop(stake, numActive , pots.size()-1);

		// Turn 4th community card (turn) is turned while there are >= 2 players active.
		//TODO
		turn(stake, numActive , pots.size()-1);

		// Turn 5th community card (river) is turned if there are still >= 2 players active.
		river(stake, numActive , pots.size()-1);

		//if all players call or fold then it stops and goes to next part
		//if raise goes through loop again all the players that called not the folded

	}

	private void roundOpen(PotTexasHoldem pot, PlayerInterface smallBlind, PlayerInterface bigBlind ){

		//Post small blind
		smallBlind.postBlind(pot, SMALL_BLIND, "Small Blind");
		//Post big blind
		smallBlind.postBlind(pot, BIG_BLIND, "Big Blind");

	}
	//TODO
	//Deal only to players still in game
	private void dealCommunity(int numCards){
		for(int i = 0; i < getNumPlayers();i++){
			for(int j = 0; j < numCards; j++){
				//players[i].getHand().dealCard(deck.dealNext());
			}
		}
	}

	//private void goAround(Integer playerStart, Integer numActive, PotTexasHoldem pot){
	private void goAround(Integer playerStart, Integer numActive, int indexCurrPot){

		PotTexasHoldem activePot = pots.get(indexCurrPot);

		for (int i = 0; i < activePot.getNumPlayers() ; i++) {
			PlayerInterface currentPlayer = activePot.getPlayer((playerStart + i)% activePot.getNumPlayers());

			if (currentPlayer == null || currentPlayer.hasFolded())
				continue;

			//delay(DELAY_BETWEEN_ACTIONS);

			if (numActive == 1) { //if only one player remains
				currentPlayer.takePot(activePot);
				System.out.println("\nNo Players left in the game.\n");
				return;
			}

			currentPlayer.nextAction(pots, indexCurrPot);

			if (currentPlayer.hasFolded()){ //checks for fold
				numActive--;
			}

			if(currentPlayer.isAllIn()){
				newSidePot(currentPlayer);
			}
		}
	}

	private void preflop(Integer stake, Integer numActive, int potIndex){

		System.out.println("---PREFLOP---");


		int playerStart = button+3;	//3 becouse player left to big blind starts

		while (stake < pots.get(pots.size()-1).getCurrentStake() && numActive > 0) {
			stake = pots.get(pots.size()-1).getCurrentStake();

			goAround(playerStart, pots.get(pots.size()-1).getNumPlayers(), potIndex);
		}


	}

	private void flop(Integer stake,Integer numActive, int potIndex){

		System.out.println("---FLOP---");

		// Turn 3 community (flop) cards
		dealCommunity(3);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		while (stake < pots.get(pots.size()-1).getCurrentStake() && numActive > 0) {
			stake = pots.get(pots.size()-1).getCurrentStake();

			goAround(playerStart,  pots.get(potIndex).getNumPlayers(), potIndex);
		}

	}

	private void turn(Integer stake,Integer numActive, int potIndex){

		System.out.println("---TURN---");


		// Deal the turn card card
		dealCommunity(1);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		while (stake < pots.get(pots.size()-1).getCurrentStake() && numActive > 0) {
			stake = pots.get(pots.size()-1).getCurrentStake();

			goAround(playerStart, pots.get(potIndex).getNumPlayers(), potIndex);
		}
	}

	private void river(Integer stake, Integer numActive, int potIndex){


		System.out.println("---RIVER---");


		// Deal the river card
		dealCommunity(1);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		while (stake < pots.get(pots.size()-1).getCurrentStake() && numActive > 0) {
			stake = pots.get(pots.size()-1).getCurrentStake();

			goAround(playerStart, pots.get(potIndex).getNumPlayers(), potIndex);
		}

	}

	private void showdown(PotOfMoney pot){

		System.out.println("---SHOWDOWN---");

		// Check for winner of side-pot(s);
		// Check for winner of main-pot;

		//Last person to bet or raise shows their card , unless there was no bet on the final round
		//in which case the player immediately clockwise from the button shows their cards first

		//TODO - Conor - winner of each pot is best hand of players in pot

	}

	public void newSidePot(PlayerInterface allInPlayer) {
		//current pot - can only have stake matching all-in player in this pot
		int currPot = pots.size() - 1;
		int potMax = allInPlayer.getStake();	//max value allowed for each player in current pot
		pots.get(currPot).setMaxStake(potMax);

		int overflow = 0;	//remove excess from pot & add to 2nd pot
		for (PlayerInterface player: pots.get(currPot).getPlayers()) {
			overflow += player.getStake() - potMax;
		}

		pots.get(currPot).removeFromPot(overflow);

		//new pot - add excess from previous pot
		PotTexasHoldem newPot = new PotTexasHoldem(pots.get(currPot).getPlayers());
		newPot.removePlayer(allInPlayer);
		newPot.addToPot(overflow);
		newPot.newPotStake(pots.get(currPot).getCurrentStake());
		pots.add(newPot);

		//TODO Conor - manage later players within round (not in this method)
	}

	public int getRoundStake() {	//must match last pot's stake
		return pots.get(pots.size()-1).getCurrentStake();
	}

	/*
	When player goes all-in current pot splits
	1st pot contains all-in amount from all-in player for each player
	2nd pot contains each remaining player's stake minus all-in players stake
	 */
	//public void allIn(PlayerInterface[] activePlayers,PlayerInterface allInPlayer) {

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