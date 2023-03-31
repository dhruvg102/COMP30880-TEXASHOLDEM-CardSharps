package TexasHoldem;
import poker.*;

public interface PlayerInterface {
    public void reset();
    public String toString();
    public HoldemHand getHand();
    public int getBank();
    public int getStake();
    public String getName();
    public boolean isBankrupt();
    public boolean hasFolded();
    //public void reorganizeHand();
    public void dealTo(DeckOfCards deck);
    public void takePot(PotOfMoney pot);
    public void fold();
    public void openBetting(PotOfMoney pot);
    public void seeBet(PotOfMoney pot);
    public void raiseBet(PotOfMoney pot);
    public boolean shouldOpen(PotOfMoney pot);
    public boolean shouldSee(PotOfMoney pot);
    public boolean shouldRaise(PotOfMoney pot);
    public void nextAction(PotOfMoney pot);
}
