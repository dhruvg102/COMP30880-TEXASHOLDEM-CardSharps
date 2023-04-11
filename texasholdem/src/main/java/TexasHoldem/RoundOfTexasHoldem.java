
package TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

	private int smallBlind = 1;
	private int bigBlind = 2;

	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Constructor
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	
	public RoundOfTexasHoldem(DeckOfCards deck, PlayerInterface[] players, int smallBlind) {
		this.players = players; //init players
		this.deck    = deck; //init deck
		this.smallBlind = smallBlind;
		this.bigBlind = 2*smallBlind;
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


	public void canOpen() {

		//Player to the left of the dealer posts the small blind
		{
			if(players[button+1].getBank()<smallBlind){
				//Player does not have enough chips to open
				System.out.println(players[button+2].getName() + "says: I cannot post the Small Blind. \n" +
						"I can't afford to play anymore");
			}
			else if(players[button+1].getBank()>=smallBlind){
				System.out.println(players[button+1].getName() + "says: I can post the Small Blind");
			}
		}

		//Player to the left of the small blind posts the big blind
		{
			if(players[button+2].getBank()<bigBlind)
			//Player does not have enough chips to open
				System.out.println(players[button+2].getName() + "says: I cannot post the Big Blind. . \n " +
						"I can't afford to play anymore");
			else if(players[button+2].getBank()<bigBlind)
			//Player does not have enough chips to open
				System.out.println(players[button+2].getName() + "says: I can post the Big Blind.");
		}


	}
	// public boolean canOpen() {
	// 	HoldemHand hand = getPlayer(getNumBestPlayer(false)).getHand();
	// 	if (hand.isHigh()) // not good enough
	// 		return false;
	// 	else
	// 		return true;
	// }

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

	public ArrayList<PotTexasHoldem> getPots() {
		return pots;
	}

	public void setPots(ArrayList<PotTexasHoldem> pots) {
		this.pots = pots;
	}

	//TODO: Side bets Showdown
	public void play(){

		ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
		PotTexasHoldem mainPot = new PotTexasHoldem(listPlayers);
		pots.add(mainPot);

		// Initialize bank and print the values for each player;
		Integer numActive = mainPot.getNumPlayers();
		Integer stake = -1;
		PlayerInterface currentPlayer = null;
		deck.reset();


		roundOpen(mainPot, players[button+1], players[button+2]);

		//PRINTING PLAYER HAND
		for (Card card : listPlayers.get(0).getHand().getHand()) {
			System.out.println(card.getName());
		}
		

		// Game actions
		// (call, raise, fold);
		// Start betting sequence left of the big blind;
		preflop(stake, numActive,pots.size()-1);

		//Whilst there are >= 2 players are still active;
		flop(stake, numActive , pots.size()-1);

		// Turn 4th community card (turn) is turned while there are >= 2 players active.
		turn(stake, numActive , pots.size()-1);

		// Turn 5th community card (river) is turned if there are still >= 2 players active.
		river(stake, numActive , pots.size()-1);

		showdown();
	}

	private void roundOpen(PotTexasHoldem pot, PlayerInterface playerSmallBlind, PlayerInterface playerBigBlind) {
		playerSmallBlind.postBlind(pot, smallBlind, "Small Blind");
		playerBigBlind.postBlind(pot, bigBlind, "Big Blind");
	}

	//TODO
	//Deal only to players still in game
	private void dealCommunity(int numCards){
		
		List<Card> list = new ArrayList<>(); //define community cards as an array of cards for reference
		
		for(int j = 0; j < numCards; j++){
			list.add(deck.dealNext());
		}

		for(int i = 0; i < getNumPlayers();i++){
			players[i].addCommunityCards(list);
		}

	}


	//private void goAround(Integer playerStart, Integer numActive, PotTexasHoldem pot){
	/*private void goAround(Integer playerStart, Integer numActive, int indexCurrPot){

		PotTexasHoldem activePot = pots.get(indexCurrPot);

		for (int i = 0; i < activePot.getNumPlayers() ; i++) {
			PlayerInterface currentPlayer = activePot.getPlayer((playerStart + i)% activePot.getNumPlayers());

			if (currentPlayer == null || currentPlayer.hasFolded() || currentPlayer.isAllIn())
				continue;

			//delay(DELAY_BETWEEN_ACTIONS);

			if (numActive == 1) { //if only one player remains
				currentPlayer.takePot(pots.get(pots.size()-1));

				System.out.println("\nNo Players left in the game.\n");
				return;
			}

			currentPlayer.nextAction(pots, indexCurrPot);

			if (currentPlayer.hasFolded()){ //checks for fold
				numActive--;
			}

			if(currentPlayer.isAllIn()){
				addSidePot(currentPlayer, indexCurrPot);
			}
		}
	}*/

	private void preflop(Integer stake, Integer numActive, int potIndex){

		System.out.println("---PREFLOP---");


		int playerStart = button+3;	//3 becouse player left to big blind starts

		bettingCycle(playerStart);
	}

	private void flop(Integer stake,Integer numActive, int potIndex){

		System.out.println("---FLOP---");

		// Turn 3 community (flop) cards
		dealCommunity(3);

		int playerStart = button+1;	//3 becouse player left to big blind starts


		bettingCycle(playerStart);

	}

	private void turn(Integer stake,Integer numActive, int potIndex){

		System.out.println("---TURN---");


		// Deal the turn card card
		dealCommunity(1);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		bettingCycle(playerStart);
	}

	private void river(Integer stake, Integer numActive, int potIndex){


		System.out.println("---RIVER---");


		// Deal the river card
		dealCommunity(1);

		int playerStart = button+1;	//3 becouse player left to big blind starts

		bettingCycle(playerStart);
	}

	private void showdown(){

		System.out.println("---SHOWDOWN---");

		int bestHandScore = 0, score = 0, bestPos = 0, potNum =0;
		PlayerInterface bestPlayer = null, currentPlayer = null;

		for (PotTexasHoldem pot: pots) {

			if(pots.size() > 1) {		//if there's more than 1 pot say which pot it is
				System.out.println("---For pot " + potNum + " ---");
			}

			System.out.println("Pot total:" + pot.getTotal());
			System.out.println("Pot max stake:" + pot.getMaxStake());
			System.out.println("Pot stake:" + pot.getCurrentStake());
			System.out.println("Pot num players:" + pot.getNumPlayers());
			for (PlayerInterface player: pot.getPlayers()) {
				System.out.println("	player:" + player.getName());
			}

			for (int i = 0; i < pot.getNumPlayers(); i++) {
				currentPlayer = pot.getPlayer(i);
				if (currentPlayer == null || currentPlayer.hasFolded())
					continue;

				score = currentPlayer.getHand().getValue();
				if (score > bestHandScore) {
					bestPos = i;
					bestHandScore = score;
					bestPlayer = currentPlayer;
				}
			}
			System.out.println(bestPlayer.getName() + " takes pot of " + pot.getTotal() + " chips!");

			bestPlayer.takePot(pot);
			potNum++;
		}
	}


	public void bettingCycle(int playerStart) {
		int indexCurrPot = pots.size()-1;
		int stake = -1;
		int numActive = pots.get(pots.size()-1).getNumPlayers();

		while (stake < pots.get(pots.size()-1).getCurrentStake() && numActive > 1) {
			stake = pots.get(pots.size() - 1).getCurrentStake();

			PotTexasHoldem activePot = pots.get(indexCurrPot);

			for (int i = 0; i < activePot.getNumPlayers(); i++) {
				PlayerInterface currentPlayer = activePot.getPlayer((playerStart + i) % activePot.getNumPlayers());

				if (currentPlayer == null || currentPlayer.hasFolded() || currentPlayer.isAllIn())
					continue;

				//delay(DELAY_BETWEEN_ACTIONS);

				if (numActive == 1) { //if only one player remains
					currentPlayer.takePot(pots.get(pots.size() - 1));

					System.out.println("\nNo Players left in the game.\n");
					return;
				}

				System.out.println("indexcurrpot:  " + indexCurrPot);
				currentPlayer.nextAction(pots, indexCurrPot);


				//actions after player's move
				if (currentPlayer.hasFolded()) { //checks for fold
					numActive--;
				}

				if (currentPlayer.isAllIn()) {
					addSidePot(currentPlayer, indexCurrPot);
					numActive--;
				}

				//increase pot index when all players one of: at max stake, all-in, folded
				PotTexasHoldem currPot = pots.get(indexCurrPot);
				boolean potFull = true;
				for (PlayerInterface player: currPot.getPlayers()) {
					//if no change possible in pot
					if(player == null || !(player.getStake() >= currPot.getMaxStake() || player.isAllIn() || player.hasFolded())){
						potFull = false;
					}
				}
				if(potFull){
					indexCurrPot++;
				}
			}
		}
	}

	public void addSidePot(PlayerInterface allInPlayer, int indexCurrPot) {
		boolean potAdded = false;

		for (PlayerInterface otherPlayer: pots.get(indexCurrPot).getPlayers()) {
			if(!(otherPlayer == null) && otherPlayer.isAllIn() && !Objects.equals(otherPlayer.getName(), allInPlayer.getName())){
				if(otherPlayer.getStake() == pots.get(indexCurrPot).getMaxStake() && otherPlayer.getAllInAddition() == allInPlayer.getAllInAddition()){
					pots.get(indexCurrPot).addToPot(allInPlayer.getAllInAddition());
					potAdded = true;
				}
				else if(otherPlayer.getAllInAddition() > allInPlayer.getAllInAddition()){
					//this side pot is before the other side pot
					newSidePot(allInPlayer, indexCurrPot);
					potAdded = true;
				}
				else {	//otherPlayer.getAllInAddition() < allInPlayer.getAllInAddition()
					//this side pot is after the other side pot
					newSidePot(allInPlayer, indexCurrPot+1);
					potAdded = true;
				}
			}
		}

		if(!potAdded){		//if no other side pots
			newSidePot(allInPlayer, indexCurrPot);
		}
	}

	public void newSidePot(PlayerInterface allInPlayer, int currPot) {
		//current pot - can only have stake matching all-in player in this pot
		int potMax = allInPlayer.getStake();	//max value allowed for each player in current pot
		pots.get(currPot).setMaxStake(potMax);


		System.out.println("Init: " + pots.get(currPot).getNumPlayers());
		ArrayList<PlayerInterface> newPotPlayers = pots.get(currPot).getPlayers();
		newPotPlayers.remove(allInPlayer);

		System.out.println("post remove: " + players.length);
		System.out.println("post remove: " + pots.get(currPot).getNumPlayers());


		PotTexasHoldem newPot = new PotTexasHoldem(newPotPlayers);
		/*//System.out.println("Num player pre remove newpot: " + newPot.getNumPlayers());
		System.out.println("Init: " + pots.get(currPot).getNumPlayers());
		newPot.removePlayer(allInPlayer);*/
		newPot.newPotStake(pots.get(currPot).getCurrentStake());
		pots.add(currPot+1, newPot);
		System.out.println("Num player post remove: " + pots.get(currPot).getNumPlayers());
		System.out.println("players: " + pots.get(currPot+1).getNumPlayers());

		potOverflow(pots, currPot);
	}

	public void potOverflow(ArrayList<PotTexasHoldem> pots, int currPot){
		int overflow;	//remove excess from pot & add to next pot
		for(int i =currPot; i < pots.size()-1; i++) {
			overflow = 0;
			for (PlayerInterface player: pots.get(i).getPlayers()) {
				if(player.getStake() > pots.get(i).getMaxStake()){
					overflow += player.getStake() - pots.get(i).getMaxStake();
				}
			}
			pots.get(i).removeFromPot(overflow);
			pots.get(i+1).addToPot(overflow);
		}
	}
	
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//
	// Some small but useful helper routines
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//

	private void delay(int numMilliseconds) {
		try {
			Thread.sleep(numMilliseconds);
		} catch (Exception e) {}
	}
}  