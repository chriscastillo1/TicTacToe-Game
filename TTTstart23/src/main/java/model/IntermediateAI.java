package model;

/*
 * @Author: Chris Castillo
 * @Purpose: Code an IntermediateAI that follows these heuristics:
 * 1. If there is a winning move, Take it
 * 2. If no winning move, if there is a block, take it
 * 3. Find next best possible move
 */
public class IntermediateAI implements TicTacToeStrategy {
	private static final int MAX_DEPTH = 12;

	@Override
	public OurPoint desiredMove(TicTacToeGame theGame) {
		if (theGame.maxMovesRemaining() == 0) {
			throw new IGotNowhereToGoException(" -- Hey there programmer, the board is filled");
		}

		if (oneMoveWin(theGame.getTicTacToeBoard()) != null) {
			return oneMoveWin(theGame.getTicTacToeBoard());
		}

		return bestMove(theGame);
	}
	
	private OurPoint bestMove(TicTacToeGame game) {
		char[][] b = game.getTicTacToeBoard();
		int i = 99, j = -99;

		int bestVal = Integer.MIN_VALUE;

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (b[row][col] == '_') {

					b[row][col] = 'O';
					int moveValue = minMax(b, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true, game);
					b[row][col] = '_';

					if (moveValue > bestVal) {
						i = row;
						j = col;
						bestVal = moveValue;
					}
				}
			}
		}
		return new OurPoint(i, j);
	}

	private int minMax(char[][] board, int depth, int alpha, int beta, boolean isMax, TicTacToeGame game) {
		int boardVal = evaluate(board, depth);

		if (Math.abs(boardVal) > 0 || depth == 0 || game.maxMovesRemaining() == 0) {
			return boardVal;
		}

		// Maximizing Player
		if (isMax) {
			int highScore = Integer.MIN_VALUE;

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col] == '_') {
						board[row][col] = 'O';
						highScore = Math.max(highScore, minMax(board, depth - 1, alpha, beta, false, game));
						board[row][col] = '_';

						alpha = Math.max(alpha, highScore);

						if (alpha >= beta) {
							return highScore;
						}
					}
				}
			}
			return highScore;

			// Minimizing Player
		} else {
			int lowestVal = Integer.MAX_VALUE;

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col] == '_') {
						board[row][col] = 'X';
						lowestVal = Math.max(lowestVal, minMax(board, depth - 1, alpha, beta, true, game));
						board[row][col] = '_';

						beta = Math.min(beta, lowestVal);

						if (beta >= alpha) {
							return lowestVal;
						}
					}
				}
			}
			return lowestVal;
		}
	}

	/*
	 * Evaluates relative position of board Returns +10 if its a win Returns -10 if
	 * its a loss Returns 0 if its neither better or worse
	 */
	private int evaluate(char[][] b, int depth) {
		// Check Rows for winner
		for (int row = 0; row < 3; row++) {
			if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
				if (b[row][0] == 'O') {
					return 10 + depth;
				} else if (b[row][0] == 'X') {
					return -10 - depth;
				}
			}
		}

		// Checks Cols for winner
		for (int col = 0; col < 3; col++) {
			if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
				if (b[0][col] == 'O') {
					return 10 + depth;
				} else if (b[0][col] == 'X') {
					return -10 - depth;
				}
			}
		}

		// Checks Diagonals for winner. Top Left to Bottom
		if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
			if (b[0][0] == 'O') {
				return 10 + depth;
			} else if (b[0][0] == 'X') {
				return -10 - depth;
			}
		}

		// Checks Diagonals for winner. Bottom Right to Top
		if (b[2][0] == b[1][1] && b[1][1] == b[0][2]) {
			if (b[2][0] == 'O') {
				return 10 + depth;
			} else if (b[2][0] == 'X') {
				return -10 - depth;
			}
		}
		return 0;
	}
	
	// Checks if a win is possible in 1 move
	// If not win, check if opponent can win in 1 move and block them
	private OurPoint oneMoveWin(char[][] b) {
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {

				if (b[r][c] == '_') {

					b[r][c] = 'O';

					if (evaluate(b, 0) == 10) {
						b[r][c] = '_';
						return new OurPoint(r, c);
					}

					b[r][c] = 'X';

					if (evaluate(b, 0) == -10) {
						b[r][c] = '_';
						return new OurPoint(r, c);
					}
					b[r][c] = '_';
				}

			}

		}
		return null;
	}
}