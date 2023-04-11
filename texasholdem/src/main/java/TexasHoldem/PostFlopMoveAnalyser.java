package TexasHoldem;

public class PostFlopMoveAnalyser {
    private enum gameStage {
        FLOP,
        TURN,
        RIVER
    }

    private enum holdemMoves {
        BET,
        CHECK,
        FOLD,
        CONTINUATIONBET,
        CALL,
        ALLIN,
        RAISE,
    }

    PostFlopHandAnalyser handAnalyser;

    private boolean playerHasBet = false;

    private boolean playerHasChecked = false;

    private boolean opponentHasBet = false;

    private boolean opponentHasRaisedAfterPlayer = false;

    private int numberOfPlayers;

    private gameStage stage = gameStage.FLOP;

    private holdemMoves nextMove = holdemMoves.FOLD;

    public PostFlopMoveAnalyser(HoldemHand hand, int numberOfPlayers) {
        this.handAnalyser = new PostFlopHandAnalyser(hand);
        this.numberOfPlayers = numberOfPlayers;
    }

    public void decideNextMove() {
        if(stage == gameStage.FLOP) {
            if(handAnalyser.isDry() && numberOfPlayers == 2) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CONTINUATIONBET;
                    }
                }
            }
            else if(handAnalyser.isDrawy() || numberOfPlayers > 2) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else if(handAnalyser.isGutshot() || handAnalyser.isOverCard() || handAnalyser.isTrash()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CONTINUATIONBET;
                    }
                }
            }
        }
        else if(stage == gameStage.TURN) {
            if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                if(opponentHasRaisedAfterPlayer) {
                    nextMove = holdemMoves.ALLIN;
                }
                else if(opponentHasBet) {
                    nextMove = holdemMoves.RAISE;
                }
                else {
                    nextMove = holdemMoves.BET;
                }
            }
            else if(handAnalyser.isTopPair()) {
                if(opponentHasRaisedAfterPlayer || opponentHasBet) {
                    nextMove = holdemMoves.FOLD;
                }
                else {
                    nextMove = holdemMoves.CONTINUATIONBET;
                }
            }
            else {
                if(opponentHasBet) {
                    nextMove = holdemMoves.FOLD;
                }
                else {
                    nextMove = holdemMoves.CHECK;
                }
            }
        }
        else if(stage == gameStage.RIVER) {
            if(playerHasBet) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isTopPair()) {
                    if(opponentHasRaisedAfterPlayer) {
                        nextMove = holdemMoves.ALLIN;
                    }
                    else if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else{
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
            }
            else if(playerHasChecked) {
                if(handAnalyser.isMonsterHand()) {
                    if(opponentHasRaisedAfterPlayer) {
                        nextMove = holdemMoves.ALLIN;
                    }
                    else if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else if(handAnalyser.isTopPair()) {
                    if(opponentHasRaisedAfterPlayer || opponentHasBet) {
                        nextMove = holdemMoves.CALL;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
            }
        }
    }
}
