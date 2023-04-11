
package TexasHoldem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import poker.*;

// This package provides classes necessary for implementing a game system for playing poker

// A RoundOfPoker is a single round/deal in a game
// A PokerGame is a sequence of RoundOfPoker's


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
	
	public RoundOfTexasHoldem(DeckOfCards deck, PlayerInterface[] players, int smallBlind, int button) {
		this.players = players; //init players
		this.deck    = deck; //init deck
		this.smallBlind = smallBlind;
		this.bigBlind = 2*smallBlind;
		numPlayers   = players.length; //get totalPlayers

		this.button = button;

		System.out.println("\n\nNew Deal:\n\n");
		deal();


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

			PlayerInterface[] newPlayers = new PlayerInterface[players.length-1];
			int count = 0;
			for(int i = 0; i<players.length; i++){
				if( i!=num){
					newPlayers[count] = players[i];
					count++;
				}
			}
			players = newPlayers;
			numPlayers = players.length;
		}
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
	// See if we can open a round (at least one player must have atg least
	// a pair)
	//--------------------------------------------------------------------//
	//--------------------------------------------------------------------//


	public void canOpen(PotTexasHoldem pot) {
		if(numPlayers<=1){
			return;
		}
		int i = 1;
		//Player to the left of the dealer posts the small blind
		while( players[(button+i)%numPlayers]!=null && players[(button+i)%numPlayers].getBank()<bigBlind ){
			if(numPlayers<=1){
				return;
			}
			//Player does not have enough chips to open
			System.out.println(players[(button+i)%numPlayers].getName() + "says: I cannot post the Small Blind. \n" + "I can't afford to play anymore");
			
			removePlayer((button + i)%numPlayers);
			i++;
			
		}
		players[(button+i)%numPlayers].postBlind(pot, smallBlind, "Small Blind");

		if(numPlayers<=1){
			return;
		}
		//Player to the left of the small blind posts the big blind
		while( players[(button+i+1)%numPlayers]!=null && players[(button+i+1)%numPlayers].getBank()<bigBlind ){
			if(numPlayers<=1){
				return;
			}
			//Player does not have enough chips to open
				System.out.println(players[(button+i+1)%numPlayers].getName() + "says: I cannot post the Big Blind. . \n " +"I can't afford to play anymore");
				
				removePlayer((button+i+1)%numPlayers);

				i++;
			
		}
		players[(button+i+1)%numPlayers].postBlind(pot, bigBlind, "Big Blind");


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

	public void play(){

		ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
		PotTexasHoldem mainPot = new PotTexasHoldem(listPlayers);
		pots.add(mainPot);

		// Initialize bank and print the values for each player;
		Integer numActive = mainPot.getNumPlayers();
		Integer stake = -1;
		deck.reset();


		//roundOpen(mainPot, players[(button+1)%numPlayers], players[(button+2)%numPlayers]);
		canOpen(mainPot);
		//PRINTING PLAYER HAND
		printPlayerHand();

		// Game actions
		// (call, raise, fold);
		// Start betting sequence left of the big blind;
		preflop(stake, numActive,pots.size()-1);

		printPlayerHand();
		//Whilst there are >= 2 players are still active;
		flop(stake, numActive , pots.size()-1);

		printPlayerHand();
		// Turn 4th community card (turn) is turned while there are >= 2 players active.
		turn(stake, numActive , pots.size()-1);

		printPlayerHand();
		// Turn 5th community card (river) is turned if there are still >= 2 players active.
		river(stake, numActive , pots.size()-1);

		printPlayerHand();
		showdown();
	}



	//TODO
	//Deal only to players still in game
	private void dealCommunity(int numCards){
		List<Card> list = new ArrayList<>(); //define community cards as an array of cards for reference
		
		for(int j = 0; j < numCards; j++){
			list.add(deck.dealNext());
		}

		System.out.println(list);

		for(int i = 0; i < getNumPlayers();i++){
			players[i].addCommunityCards(list);
		}

	}

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


			
			for (int i = 0; i < pot.getNumPlayers(); i++) {
				currentPlayer = pot.getPlayer(i);
				if (currentPlayer == null || currentPlayer.hasFolded())
					continue;

				score = currentPlayer.getHand().evaluateHand(currentPlayer.getHand().getBestHand());
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

				delay(DELAY_BETWEEN_ACTIONS);


				currentPlayer.nextAction(pots, indexCurrPot);


				//actions after player's move
				if (currentPlayer.hasFolded()) { //checks for fold
					numActive--;

					removePlayer((playerStart + i) % activePot.getNumPlayers());
					pots.get(indexCurrPot).removePlayer(currentPlayer);
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

					if(!(player.getStake() >= currPot.getMaxStake() || player.isAllIn() || player.hasFolded() || player == null)){
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

		ArrayList<PlayerInterface> newPotPlayers = new ArrayList<PlayerInterface>(pots.get(currPot).getPlayers());
		newPotPlayers.remove(allInPlayer);

		PotTexasHoldem newPot = new PotTexasHoldem(newPotPlayers);
		newPot.newPotStake(pots.get(currPot).getCurrentStake());
		pots.add(currPot+1, newPot);

		potOverflow(pots, currPot);
		System.out.println("Initial total new pot: " + pots.get(pots.size()-1).getTotal());

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
	private void printPlayerHand(){
		
		System.out.println(">Your Cards : ");
		for (Card card : players[0].getHand().getHand()) {
			System.out.print(card + " ");
		}
		System.out.println("");
		
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