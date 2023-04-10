package TexasHoldem;

/*Pre-Flop Move Analyser based on Sklansky hand groups
*
* Hole Card pairs are ranked from 1 to 9 based on hand quality and what position they should be played from.
* Lower Hand rank indicates higher quality pairings.
* 9 indicates one should always fold
*
* Also relevant is whether the pair is of the same suit ("suited") or not ("unsuited").
* */


public class PreFlopMoveAnalyser {
    private static char ERROR_MOVE = 0; //error, indicates erroneous hand values

    private char[][] SUITED_PRE_FLOP_DECISION_MATRIX; //If card pair is of same suit

    private char[][] OFFSUIT_PRE_FLOP_DECISION_MATRIX; //If card pair is not of same suit

    private int player_position;

    private int number_of_players;

    private HoldemHand hand;

    //constructor
    public PreFlopMoveAnalyser(int player_position, int number_of_players, HoldemHand hand){

        this.player_position = player_position;
        this.number_of_players = number_of_players;
        this.hand = hand;

        //If card pair is of same suit
        SUITED_PRE_FLOP_DECISION_MATRIX = new char[][]{
                // First card in range of values 0-A
                //0   1   2   3   4   5   6   7   8   9  10   J   Q   K   A
                {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'}, //second card value = 0
                {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'}, //second card value = 1
                {'0','0','7','8','8','9','9','9','9','9','9','9','9','7','5'}, //second card value = 2
                {'0','0','8','7','7','7','9','9','9','9','9','9','9','7','5'}, //second card value = 3
                {'0','0','8','7','7','6','7','8','9','9','9','9','9','7','5'}, //second card value = 4
                {'0','0','9','7','6','6','7','6','8','9','9','9','9','7','5'}, //second card value = 5
                {'0','0','9','9','7','7','6','5','6','8','9','9','9','7','5'}, //second card value = 6
                {'0','0','9','9','8','6','5','5','5','5','7','8','9','7','5'}, //second card value = 7
                {'0','0','9','9','9','8','6','5','4','4','5','6','7','7','5'}, //second card value = 8
                {'0','0','9','9','9','9','8','5','4','3','4','4','5','6','5'}, //second card value = 9
                {'0','0','9','9','9','9','9','7','5','4','2','3','4','4','3'}, //second card value = 10
                {'0','0','9','9','9','9','9','8','6','4','3','1','3','3','2'}, //second card value = 11 (Jack)
                {'0','0','9','9','9','9','9','9','7','5','4','3','1','2','2'}, //second card value = 12 (Queen)
                {'0','0','7','7','7','7','7','7','7','6','4','3','2','1','1'}, //second card value = 13 (King)
                {'0','0','5','5','5','5','5','5','5','5','3','2','2','1','1'}, //second card value = 14 (Ace)
        };

        //If card pair is not of same suit
        OFFSUIT_PRE_FLOP_DECISION_MATRIX = new char[][]{
                // First card in range of values 0-A
                //0   1   2   3   4   5   6   7   8   9  10   J   Q   K   A
                {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'}, //second card value = 0
                {'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'}, //second card value = 1
                {'0','0','7','9','9','9','9','9','9','9','9','9','9','9','9'}, //second card value = 2
                {'0','0','9','7','9','9','9','9','9','9','9','9','9','9','9'}, //second card value = 3
                {'0','0','9','9','7','8','9','9','9','9','9','9','9','9','9'}, //second card value = 4
                {'0','0','9','9','8','6','8','9','9','9','9','9','9','9','9'}, //second card value = 5
                {'0','0','9','9','9','8','6','8','9','9','9','9','9','9','9'}, //second card value = 6
                {'0','0','9','9','9','9','8','5','5','8','9','9','9','9','9'}, //second card value = 7
                {'0','0','9','9','9','9','9','5','4','7','8','8','9','9','9'}, //second card value = 8
                {'0','0','9','9','9','9','9','8','7','3','7','7','8','8','8'}, //second card value = 9
                {'0','0','9','9','9','9','9','9','8','7','2','5','6','6','6'}, //second card value = 10
                {'0','0','9','9','9','9','9','9','8','7','5','1','5','5','4'}, //second card value = 11 (Jack)
                {'0','0','9','9','9','9','9','9','9','8','6','5','1','3','4'}, //second card value = 12 (Queen)
                {'0','0','9','9','9','9','9','9','9','8','6','5','3','1','2'}, //second card value = 13 (King)
                {'0','0','9','9','9','9','9','9','9','8','6','4','4','2','1'}, //second card value = 14 (Ace)
        };
    }

    private int getHandRank() {
        if(hand.getCard(0).getSuit() == hand.getCard(1).getSuit()) {
            return SUITED_PRE_FLOP_DECISION_MATRIX[hand.getCard(0).getValue()][hand.getCard(1).getValue()];
        }
        else {
            return OFFSUIT_PRE_FLOP_DECISION_MATRIX[hand.getCard(0).getValue()][hand.getCard(1).getValue()];
        }
    }

    public boolean shouldFold() {
        return getHandRank() == 9;
    }
}
