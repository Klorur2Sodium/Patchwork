import java.util.Objects;

public record Piece(int[][] body, int cost, int nbMove, int nbButton) {
	public Piece {
		Objects.requireNonNull(body);
		Objects.requireNonNull(cost);
		Objects.requireNonNull(nbMove);
		Objects.requireNonNull(nbButton);

		if (body.length > 9 || body[0].length > 9) {
			throw new IllegalArgumentException("The piece can't be bigger than the quilt board");
		}

		if (body.length != body[0].length) {
			throw new IllegalArgumentException("The two-dimensional array 'body' must be square");
		}

		if (cost < 0 || nbMove < 0 || nbButton < 0) {
			throw new IllegalArgumentException("The piece must have a positive cost, number of moves and number of buttons");
		}
	}
	
	public void displayPiece() {
		System.out.println("Your piece :");
		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < body.length; j++) {
				if (body[i][j] == 1) {
					System.out.print('x');
				} else {
					System.out.print(' ');
				}
			}
			System.out.println();
		}
	}

	// Not useful for the moment but will maybe be
	/*
	 * private int findMax(int[][] lstPair, int x) { int max = body[1][x];
	 * 
	 * for (var i = 1; i < body.length; i++) { if (body[i][x] >= max) { max =
	 * body[i][x]; } } return max + 1; }
	 * 
	 * public Piece rotateClockWise() { var newBody = new int[body.length][2]; var
	 * newWidth = height; var newHeight = width;
	 * 
	 * for (var i = 0; i < newBody.length; i++) { newBody[i][0] = width - 1 -
	 * body[i][1]; newBody[i][1] = body[i][0]; }
	 * 
	 * return new Piece(newBody, newHeight, newWidth, cost, nbMove, nbButton); }
	 */
}
