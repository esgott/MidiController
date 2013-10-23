package hu.midicontroller.leap;

public class FingerNeighbours {

	public FingerHistory closerNeighbour;
	public FingerHistory fartherNeighbour;

	public FingerNeighbours(FingerHistory closerNeighbour,
			FingerHistory fartherNeighbour) {
		this.closerNeighbour = closerNeighbour;
		this.fartherNeighbour = fartherNeighbour;
	}

}
