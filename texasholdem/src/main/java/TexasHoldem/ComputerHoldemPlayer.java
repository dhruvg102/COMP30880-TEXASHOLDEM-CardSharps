package TexasHoldem;
import poker.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerHoldemPlayer implements PlayerInterface{
    private int bank       		= 0;		 // the total amount of money the player has left, not counting his/her
    private int stake      		= 0;		 // the amount of money the player has thrown into the current pot
    private String name    		= "Player";  // the unique identifying name given to the player
    private HoldemHand hand 	= null;      // the hand dealt to this player
    private boolean folded 		= false;     // set to true when the player folds (gives up)
    private boolean allIn       = false;
    private int allInAddition   = 0;        //increase stake to bank value

    public static final int VARIABILITY		= 50;
    private int riskTolerance				= 0;
    private Random dice						= new Random(System.currentTimeMillis());



    public ComputerHoldemPlayer(String name, int money) {
        this.name = name;
        this.bank = money;

        riskTolerance = Math.abs(dice.nextInt())%VARIABILITY
                - VARIABILITY/2;
    }

    public int getRiskTolerance() {
        return riskTolerance - getStake(); // tolerance drops as stake increases
    }


    @Override
    public void reset() {
        folded = false;
        stake  = 0;
    }

    @Override
    public String toString() {
        if (hasFolded())
            return "> " + getName() + " has folded, and has " + addCount(getBank(), "chip", "chips") + " in the bank.";
        else
            return "> " + getName() + " has  " + addCount(getBank(), "chip", "chips") + " in the bank";
    }

    @Override
    public HoldemHand getHand() {
        return hand;
    }

    @Override
    public int getBank() {
        return bank;
    }

    @Override
    public int getStake() {
        return stake;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBankrupt() {
        return bank == 0;
    }

    @Override
    public boolean hasFolded() {
        return folded;
    }

    @Override
    public boolean isAllIn() {
        return allIn;
    }

    public int getAllInAddition() {
        return allInAddition;
    }

    /* @Override
    public void reorganizeHand() {
        hand = hand.categorize();
    } */

    @Override
    public void dealTo(DeckOfCards deck) {
        hand = deck.dealHoldemHand();
    }

    @Override
    public void addCommunityCards(List<Card> cards){
        this.hand.addCommunityCards(cards);
    }


    @Override
    public void takePot(PotOfMoney pot) {
        System.out.println("\n> " + getName() + " says: I WIN " + addCount(pot.getTotal(), "chip", "chips") + "!\n");
        System.out.println(hand.toString());

        bank += pot.takePot();

        System.out.println(this);
    }

    @Override
    public void fold() {
        if (!folded)
            System.out.println("\n> " + getName() + " says: I fold!\n");

        folded = true;
    }

    @Override
    public void openBetting(PotOfMoney pot) {

        if (bank == 0) return;

        stake++;
        bank--;

        pot.raiseStake(1);

        System.out.println("\n> " + getName() + " says: I open with one chip!\n");

    }
    @Override
    public boolean postBlind(PotOfMoney pot, int blindAmt, String type) {
		if (bank == 0) return false;

		//FLAG to check if player had enough for blind
		boolean enough = true;

		if(bank < blindAmt) {
			stake = stake + bank;
			pot.addStake(bank);
			bank = 0;

			//Change state to all - in ??
			//TODO
			enough=false;
		}
		else{
			stake = stake + blindAmt;
			pot.addStake(stake);
			bank = bank-blindAmt;

		}

		System.out.println("\n> " + getName() + " says: I post " + type + " with "+ blindAmt +" chip!\n");
		return enough;
	}
    @Override
    public void seeBet(ArrayList<PotTexasHoldem> pots , int currPotIndex) {
        int needed  = pots.get(pots.size()-1).getCurrentStake() - getStake();   //stake last pot

        if (needed == 0 || needed > getBank())
            return;

        stake += needed;
        bank  -= needed;

        if(getStake() > pots.get(currPotIndex).getMaxStake()){
            pots.get(currPotIndex+1).addToPot(needed);
        } else {
            pots.get(currPotIndex).addToPot(needed);
        }

        System.out.println("\n> " + getName() + " says: I see that " + addCount(needed, "chip", "chips") + "!\n");

    }

    @Override
    public void raiseBet(ArrayList<PotTexasHoldem> pots, int currPotIndex) {
        if (getBank() == 0) return;

        stake++;
        bank--;

        if(getStake() > pots.get(currPotIndex).getMaxStake()){
            pots.get(currPotIndex+1).raiseStake(1);
        } else {
            pots.get(currPotIndex).raiseStake(1);
        }

        System.out.println("\n> " + getName() + " says: I raise you 1 chip!\n");

    }

    @Override
    public void allIn(PotOfMoney pot) {
        int previousStake = stake;
        stake += bank;
        bank = 0;
        allIn = true;
        allInAddition = stake - previousStake;

        System.out.println("\n> " + getName() + " says: I'm all in!\n");
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
        return true;
    }

    @Override
    public boolean shouldSee(PotOfMoney pot) {
        if (getStake() == 0)
            return true;
        else
            return Math.abs(dice.nextInt())%100 < getHand().getRiskWorthiness() +
                    getRiskTolerance();
    }

    @Override
    public boolean shouldRaise(PotOfMoney pot) {
        return Math.abs(dice.nextInt()) % 80 < getHand().getRiskWorthiness() +
                getRiskTolerance();
    }

    @Override
    public boolean shouldAllIn(PotOfMoney pot) {
        if(pot.getCurrentStake() < getStake() + getBank()){
            return false;
        } else {
            /*return Math.abs(dice.nextInt()) % 100 + getBank() < getHand().getRiskWorthiness() +
                    getRiskTolerance();*/
            return true;
        }
    }


    @Override
    public void nextAction(ArrayList<PotTexasHoldem> pots , int currPotIndex) {
        PotTexasHoldem pot = pots.get(currPotIndex);
        if (hasFolded()) return;  // no longer in the game

        if(shouldAllIn(pot)){
            allIn(pot);
            return;
        }

        if (isBankrupt()) {
            // not enough money to cover the bet
            System.out.println("\n> " + getName() + " says: I'm out!\n");

            fold();

            return;
        }


        if (pot.getCurrentStake() == 0) {
            // first mover of the game

            if (shouldOpen(pot))  // will this player open the betting?
                openBetting(pot);
            else
                fold();
        }
        else {
            if (pot.getCurrentStake() > getStake()) {
                if (shouldSee(pot)) {
                    seeBet(pots, currPotIndex);
                }
                else {
                    fold();
                    return;
                }
            }
            if (shouldRaise(pot)){
                raiseBet(pots, currPotIndex);
            }
            else {
                System.out.println(getName() + " says: I check!");
            }
        }
    }

    private String addCount(int count, String singular, String plural) {
        if (count == 1 || count == -1)
            return count + " " + singular;
        else
            return count + " " + plural;
    }

}
