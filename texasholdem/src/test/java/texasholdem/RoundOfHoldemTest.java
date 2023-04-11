package texasholdem;
import TexasHoldem.ComputerHoldemPlayer;
import TexasHoldem.HumanHoldemPlayer;
import TexasHoldem.PlayerInterface;
import TexasHoldem.PotTexasHoldem;
import TexasHoldem.RoundOfTexasHoldem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import poker.*;
import java.util.*;

public class RoundOfHoldemTest {


    private int numPlayers = 5;
    private HumanHoldemPlayer player1 = new HumanHoldemPlayer("p1",5);
    private ComputerHoldemPlayer player2 = new ComputerHoldemPlayer("p2",4);
    private ComputerHoldemPlayer player3 = new ComputerHoldemPlayer("p3",3);
    private ComputerHoldemPlayer player4 = new ComputerHoldemPlayer("p4",4);
    private ComputerHoldemPlayer player5 = new ComputerHoldemPlayer("p5",1);

    private PlayerInterface[] players = {player1,player2,player3,player4,player5};

    private RoundOfTexasHoldem round = new RoundOfTexasHoldem(new DeckOfCards(), players, 1, 0);

    ArrayList<PotTexasHoldem> pots = new ArrayList<PotTexasHoldem>();

    @Before
    public void setUp() {
        ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
        PotTexasHoldem mainPot = new PotTexasHoldem(listPlayers);
        pots.add(mainPot);
        //RoundOfTexasHoldem round = new RoundOfTexasHoldem(new DeckOfCards(), players);
    }

    /*@Test
    public void testAllIn() {
        round.setPots(pots);

        //1st cycle
        player1.raiseBet(pots, 0);
        player2.seeBet(pots, 0);
        player3.seeBet(pots,0);
        player4.seeBet(pots, 0);
        player5.seeBet(pots,0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        assertEquals(1, pots.get(0).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());

        //2nd cycle
        player1.raiseBet(pots, 0);
        player2.seeBet(pots,0);
        player3.seeBet(pots,0);
        player4.seeBet(pots,0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        player5.allIn(pots.get(0));
        round.newSidePot(player5, 0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        System.out.println("pot1 total: " + pots.get(1).getTotal());

        assertEquals(1, pots.get(0).getMaxStake());
        assertEquals(5,  pots.get(0).getTotal());
        assertEquals(4,  pots.get(1).getTotal());

        //3rd cycle
        player1.raiseBet(pots, 1);
        player2.seeBet(pots, 1);
        player3.seeBet(pots, 1);
        player4.seeBet(pots, 1);

        assertEquals(3, pots.get(1).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());

        //4th cycle
        player1.raiseBet(pots, 1);
        player2.seeBet(pots, 1);
        //player3.nextAction(pots, 1);
        player3.allIn(pots.get(0));
        assertTrue(player3.isAllIn());
        round.addSidePot(player3, 1);
        player4.seeBet(pots, 1);

        assertEquals(4, pots.get(2).getCurrentStake());
        assertEquals(3, pots.get(2).getTotal());
        assertEquals(8, pots.get(1).getTotal());

        //5th cycle
        player1.raiseBet(pots, 2);
        player2.allIn(pots.get(2));
        round.addSidePot(player2, 2);
        player4.allIn(pots.get(2));

        //round.bettingCycle(2,1,0);
        round.newSidePot(player2, 0);


        System.out.println("-----");
        for(int i=0;i<round.getPots().size();i++){
            System.out.println(" Pot"+i+": " + round.getPots().get(i).getTotal());
        }
        System.out.println("-----");

    }
*/
   /* @Test
    public void testAddSidePot() {
        round.setPots(pots);
        assertEquals(pots.get(0).getNumPlayers(), 5);
        round.addSidePot(player4, 0);
        assertEquals(pots.get(0).getNumPlayers(), 4);
        round.addSidePot(player2, 0);
        assertEquals(pots.get(0).getNumPlayers(), 5);
    }
*/

    @Test
    public void testNewSidePotsIdea() {
        //round.setPots(pots);
        //player1.raiseBet(pots, );
    }

    @Test
    public void testNewSidePot() {
//1st cycle
        PotTexasHoldem pot = pots.get(0);
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        player5.seeBet(pot);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        assertEquals(1, pots.get(0).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());

        //2nd cycle
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        player5.allIn(pots.get(0));
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        //System.out.println("pot1 total: " + pots.get(1).getTotal());

        /*assertEquals(1, pots.get(0).getMaxStake());
        assertEquals(5,  pots.get(0).getTotal());
        assertEquals(4,  pots.get(1).getTotal());*/

        //3rd cycle
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);

        /*assertEquals(3, pots.get(1).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());*/

        //pots = round.newSidePots(pots.get(0));
        round.newSidePots(pots.get(0));
        for (PotTexasHoldem aPot:pots) {
            System.out.println(aPot.getTotal() + " total|players " + aPot.getNumPlayers());
        }
        System.out.println("TEST tot size: "+pots.size());

        assertEquals(5, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());

    }

    @Test
    public void testAnotherNewSidePot() {
        PotTexasHoldem pot = pots.get(0);

        //1st cycle - stake 1
        player1.raiseBet(pot);  //1
        player2.seeBet(pot);    //1
        player3.seeBet(pot);
        player4.seeBet(pot);
        //player5.seeBet(pot);
        player5.allIn(pot);

        //2nd cycle - stake2
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        //player5.allIn(pot);      //1

        //3rd cycle - stake 3
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player3.allIn(pot);
        player4.seeBet(pot);

        //4th cycle - stake 4
        player1.raiseBet(pot);
        player2.seeBet(pot);
        player4.seeBet(pot);


        pots = round.newSidePots(pots.get(0));
        for (PotTexasHoldem aPot:pots) {
            System.out.println(aPot.getTotal() + " total|players " + aPot.getNumPlayers());
        }
        System.out.println("TEST tot size: "+pots.size());
    }

    
/*
    @Test
    public void testOverflow() {
        round.setPots(pots);

        ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
        listPlayers.remove(player5);
        PotTexasHoldem pot2 = new PotTexasHoldem(listPlayers);
        pots.add(pot2);

        player1.seeBet(pots,0);
        player1.raiseBet(pots,0);
        for (PlayerInterface player: pots.get(0).getPlayers()) {
            if(player != player1)
                player.seeBet(pots,0);
        }
        player4.seeBet(pots,0);

        pots.get(0).setMaxStake(1);

        System.out.println("pre overflow");
        for (PotTexasHoldem pot: pots) {
            System.out.println(pot.getTotal());
        }
        //round.potOverflow(pots, 0);
        System.out.println("post overflow");
        for (PotTexasHoldem pot: pots) {
            System.out.println(pot.getTotal());
        }
        System.out.println("size:"+pots.size());

    }*/


}