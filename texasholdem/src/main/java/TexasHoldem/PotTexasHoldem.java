package TexasHoldem;
import java.util.ArrayList;

import poker.PotOfMoney;

public class PotTexasHoldem extends PotOfMoney{
    
    private ArrayList<PlayerInterface> players;
    
    public PotTexasHoldem(){
        players = new ArrayList<PlayerInterface>();
    }
    
    public ArrayList<PlayerInterface> getPlayers() {
        return players;
    }

}
