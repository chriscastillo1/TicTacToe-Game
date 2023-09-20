package views;

import java.util.Scanner;

import model.ComputerPlayer;
import model.TicTacToeGame;

/*
 * @author: Chris Castillo
 * @Purpose: Construct a console view of the game of TicTacToe
 */
public class TicTacToeConsole {

	private static TicTacToeGame game = new TicTacToeGame();

	public static void main(String[] args) {
		// Gets the computerPlayer for TicTacToe Game
		ComputerPlayer compPlayer = game.getComputerPlayer();

		while (game.stillRunning()) {
			int[] coords = getUserInput();

			if (validCoords(coords[0], coords[1]) && game.available(coords[0], coords[1])) {
				if (game.maxMovesRemaining() == 1) {
					game.humanMove(coords[0], coords[1], false);
					System.out.println(game + "\n");
					break;
				}

				game.humanMove(coords[0], coords[1], false);
				compPlayer.desiredMove(game);

				System.out.println(game + "\n");
			} else {
				System.out.println("Invalid Row and Column\n");
			}
		}
		gameResultMessage();
	}

	private static int[] getUserInput() {
		Scanner userTicTacToeInput = new Scanner(System.in);

		System.out.print("Enter row and column: ");
		int xCord = userTicTacToeInput.nextInt();
		int yCord = userTicTacToeInput.nextInt();

		int[] coords = { xCord, yCord };
		
		return coords;
	}

	private static void gameResultMessage() {
		if (game.didWin('X')) {
			System.out.println("X Wins");
		}
		if (game.didWin('O')) {
			System.out.println("O Wins");
		}
		if (game.tied()) {
			System.out.println("Tied!");
		}
	}

	private static boolean validCoords(int row, int col) {
		if (row < 3 && row >= 0 && col < 3 && col >= 0) {
			return true;
		}
		return false;
	}
}