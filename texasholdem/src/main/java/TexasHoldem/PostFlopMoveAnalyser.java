package TexasHoldem;

public class PostFlopMoveAnalyser {
    private enum gameStage {
        FLOP,
        TURN,
        RIVER
    }

    PostFlopHandAnalyser handAnalyser;

    private boolean playerHasBet = false;

    private boolean playerHasChecked = false;

    private boolean opponentHasBet = false;

    private gameStage stage = gameStage.FLOP;

    public PostFlopMoveAnalyser(HoldemHand hand) {
        this.handAnalyser = new PostFlopHandAnalyser(hand);
    }
}
