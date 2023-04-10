
package poker;

// This package provides classes necessary for implementing a game system for playing poker


public class FaceCard extends Card implements Comparable<FaceCard>{
	public FaceCard(String face, String suit, int rank) {
		super(face, suit, rank);
	}

	@Override
	public int compareTo(FaceCard other) {
		if (this.getRank() < other.getRank()) {
			return -1;
		} else if (this.getRank() > other.getRank()) {
			return 1;
		} else {
			return 0;
		}
	}
}