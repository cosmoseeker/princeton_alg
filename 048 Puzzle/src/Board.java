public class Board {

	/*
	 * construct a board from an N-by-N array ofblocks (where blocks[i][j] =
	 * block in rowi, column j)
	 */public Board(int[][] blocks) {

	}

	public int dimension() { // board dimension N

	}

	public int hamming() { // number of blocks out of place

	}

	public int manhattan() { // sum of Manhattan distances between blocks and
								// goal

	}

	public boolean isGoal() { // is this board the goal board?

	}

	public Board twin() { // a board that is obtained by exchanging any pair
							// of¡¡blocks

	}

	public boolean equals(Object y) { // does this board equal y?

	}

	public Iterable<Board> neighbors() {// all neighboring boards

	}

	/*
	 * string representation of this board (in the¡¡output format specified
	 * below)
	 */public String toString() {

	}

	public static void main(String[] args) {// unit tests (not graded)

	}

}
