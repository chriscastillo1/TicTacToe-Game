package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.OurObserver;
import model.TicTacToeGame;

/*
 * Author: Chris Castillo
 * Purpose: Creates a TextAreaView model for the game TicTacToe
 */
public class TextAreaView extends BorderPane implements OurObserver {

	private TicTacToeGame theGame;
	private Font font = new Font("Lucida Console", 14);
	private Font bFont = new Font("Lucida Console", 20);
	private Font bigFont = new Font("Lucida Console", 46);

	private Label row = new Label("Row");
	private TextField rowVal = new TextField();

	private Label column = new Label("Column");
	private TextField colVal = new TextField();

	private StackPane b = new StackPane();
	private Label board;

	private Button makeMove = new Button("Make Move");
	private VBox container = new VBox(30);

	public TextAreaView(@SuppressWarnings("exports") TicTacToeGame theModel) {
		theGame = theModel;

		initializeInput();
		initializeBoard();
		registerHandlers();
		this.setCenter(container);
		this.setPadding(new Insets(10, 10, 10, 10));

	}

	private void initializeInput() {
		GridPane inputs = new GridPane();
		inputs.setVgap(10);
		inputs.setHgap(15);
		inputs.setPadding(new Insets(7, 7, 7, 7));
		inputs.setAlignment(Pos.CENTER);

		row.setFont(font);
		inputs.add(rowVal, 0, 0);
		inputs.add(row, 1, 0);

		column.setFont(font);
		inputs.add(colVal, 0, 1);
		inputs.add(column, 1, 1);

		makeMove.setFont(bFont);
		makeMove.setMinSize(220, 50);
		inputs.add(makeMove, 0, 3, 3, 1);

		container.getChildren().add(inputs);
	}

	private void initializeBoard() {
		b = new StackPane();

		board = new Label(theGame.toString());
		board.setFont(bigFont);
		board.setAlignment(Pos.CENTER);

		b.setStyle("-fx-background-color: white;" + "-fx-border-color: black;" + "-fx-border-width: 1;");
		b.setAlignment(Pos.CENTER);
		b.setMinSize(300, 300);
		b.setMaxSize(300, 300);
		b.getChildren().add(board);

		container.setAlignment(Pos.CENTER);
		container.getChildren().add(b);
	}

	private void registerHandlers() {
		makeMove.setOnAction(new InputMove());
	}

	// This method is called by Observable's notifyObservers()
	@Override
	public void update(Object observable) {
		board.setText(theGame.toString());
		checkWin();
		reset();
		System.out.println("update called from the Observable TicTacToeGame");
	}

	private void checkWin() {
		if (!theGame.stillRunning()) {
			rowVal.setText("");
			colVal.setText("");
			rowVal.setEditable(false);
			colVal.setEditable(false);
			rowVal.setMouseTransparent(true);
			colVal.setMouseTransparent(true);
		}

		if (theGame.didWin('X')) {
			makeMove.setText("X Wins!");
			makeMove.setMouseTransparent(true);
			b.setStyle("-fx-background-color: white;" + "-fx-border-color: green;" + "-fx-border-width: 5;");
		}

		if (theGame.didWin('O')) {
			makeMove.setText("O Wins!");
			makeMove.setMouseTransparent(true);
			b.setStyle("-fx-background-color: white;" + "-fx-border-color: red;" + "-fx-border-width: 5;");
		}

		if (theGame.tied()) {
			makeMove.setMouseTransparent(true);
			makeMove.setText("Tied!");
		}
	}

	private void reset() {
		if (theGame.maxMovesRemaining() == 9) {
			rowVal.setText("");
			colVal.setText("");
			rowVal.setEditable(true);
			colVal.setEditable(true);
			rowVal.setMouseTransparent(false);
			colVal.setMouseTransparent(false);
			
			makeMove.setText("Make Move");
			makeMove.setMouseTransparent(false);
			
			b.setStyle("-fx-background-color: white;" + "-fx-border-color: black;" + "-fx-border-width: 1;");
			board.setText(theGame.toString());
		}
	}

	/*
	 * Handles updating the TextAreaView Board Takes input from RowVal and ColVal
	 * and puts it into TicTacToeBoard
	 */
	private class InputMove implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			if (!validInputs()) {
				rowVal.setText("");
				colVal.setText("");
				makeMove.setText("Invalid Move");
			} else {

				enterMove();
				board.setText(theGame.toString());
				rowVal.setText("");
				colVal.setText("");
				makeMove.setText("Make Move");
			}
		}

		private void enterMove() {
			String r = rowVal.getText().trim().strip();
			String c = colVal.getText().trim().strip();
			
			int row = Integer.parseInt(r);
			int col = Integer.parseInt(c);

			theGame.humanMove(row, col, false);

			checkWin();
		}

		private void checkWin() {
			if (theGame.didWin('X')) {
				makeMove.setText("X Wins!");
				makeMove.setMouseTransparent(true);
				return;
			}

			if (theGame.didWin('O')) {
				makeMove.setText("O Wins!");
				makeMove.setMouseTransparent(true);
				return;
			}

			if (theGame.tied()) {
				makeMove.setMouseTransparent(true);
				makeMove.setText("Tied!");
				return;
			}
		}

		/*
		 * Checks if Row and Col are valid Between 0 - 3, and Available in the Game
		 */
		private boolean validInputs() {
			try {
				String r = rowVal.getText().trim().strip();
				String c = colVal.getText().trim().strip();
				
				int row = Integer.parseInt(r);
				int col = Integer.parseInt(c);

				if ((0 <= row && row < 3) && (0 <= col && col < 3) && theGame.available(row, col)) {
					return true;
				}

				return false;
			} catch (Exception e) {
				return false;
			}
		}
	}
}