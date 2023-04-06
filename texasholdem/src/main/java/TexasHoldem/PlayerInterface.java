package TexasHoldem;
import poker.*;

import java.util.ArrayList;

public interface PlayerInterface {
    public void reset();
    public String toString();
    public HoldemHand getHand();
    public int getBank();
    public int getStake();
    public String getName();
    public boolean isBankrupt();
    public boolean hasFolded();
    public boolean isAllIn();
    //public void reorganizeHand();
    public void dealTo(DeckOfCards deck);
    public void takePot(PotOfMoney pot);
    public void fold();
    public void openBetting(PotOfMoney pot);
    public void seeBet(ArrayList<PotTexasHoldem> pots , int currPotIndex);
    //public void raiseBet(PotOfMoney pot);
    public void raiseBet(ArrayList<PotTexasHoldem> pots, int currPotIndex);
    public void allIn(PotOfMoney pot);
    public boolean shouldOpen(PotOfMoney pot);
    public boolean shouldSee(PotOfMoney pot);
    public boolean shouldRaise(PotOfMoney pot);
    public boolean shouldAllIn(PotOfMoney pot);
    public void nextAction(ArrayList<PotTexasHoldem> pots , int currPotIndex);

    //Different method to open bet
    public boolean postBlind(PotOfMoney pot, int blindAmt, String type);

}
