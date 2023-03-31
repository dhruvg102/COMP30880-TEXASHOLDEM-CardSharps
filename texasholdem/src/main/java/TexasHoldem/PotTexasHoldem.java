package TexasHoldem;
import java.util.ArrayList;

import poker.PotOfMoney;

public class PotTexasHoldem extends PotOfMoney{
    
    private ArrayList<PlayerInterface> players;
    
    public PotTexasHoldem(){
        players = new ArrayList<PlayerInterface>();
    }
    
    public int getNumPlayers(){
        return players.size();
    }

    public PlayerInterface getPlayer(int i){
        return players.get(i);
    }

    public ArrayList<PlayerInterface> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerInterface player){
        players.add(player);
    }

    public void removePlayer(PlayerInterface player){
        players.remove(player);
    }

    public void removePlayer(int i){
        players.remove(i);
    }

    public void addPlayers(PlayerInterface[] newPlayers){
        for(int i = 0 ; i< newPlayers.length;i++){
            players.add(newPlayers[i]);
        }
    }

}
