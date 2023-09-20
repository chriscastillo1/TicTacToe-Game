package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.OurObserver;
import model.TicTacToeGame;

/*
 * Author: Chris Castillo
 * Purpose: Creates a buttonview model for the game TicTacToe
 */
public class ButtonView extends BorderPane implements OurObserver {

	private TicTacToeGame theGame;
	private VBox container = new VBox(30);
	private Font font = new Font("Lucida Console", 26);
	private Font buttonFont = new Font("Lucida Console", 34);
	
	Button[][] buttons;
	private GridPane buttonGrid;
	
	private Label endMessage;
	
	public ButtonView(@SuppressWarnings("exports") TicTacToeGame theModel) {
		theGame = theModel;
		buttonGrid = new GridPane();
		endMessage = new Label("Click To Make A Move");
		
		initializeButtons();
		buttonGridLayout();
		initEndMessage();
		registerHandlers();
		
		container.setAlignment(Pos.TOP_CENTER);
		this.setCenter(container);
	}

	private void initializeButtons() {
		buttons = new Button[3][3];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new Button("_");
				buttons[i][j].setMinSize(85, 85);
				buttons[i][j].setMaxSize(85, 85);
				buttons[i][j].setFont(buttonFont);
			}
		}
	}
	
	private void buttonGridLayout() {
		buttonGrid.setAlignment(Pos.CENTER);
		buttonGrid.setPadding(new Insets(70, 10, 10, 10));
		buttonGrid.setVgap(5);
		buttonGrid.setHgap(5);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttonGrid.add(buttons[i][j], j, i);
			}
		}
		container.getChildren().add(buttonGrid);
	}
	
	private void initEndMessage() {
		endMessage.setFont(font);
		endMessage.setAlignment(Pos.CENTER);
		container.getChildren().add(endMessage);
	}
	
	private void boardUpdating() {
		char[][] board = theGame.getTicTacToeBoard();
		
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				String s = Character.toString(board[r][c]);
				if (!s.equals("_")) {
					buttons[r][c].setText(s);
					buttons[r][c].setMouseTransparent(true);
				}
				buttons[r][c].setText(s);
			}
		}
	}
	
	private void registerHandlers() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j].setOnAction(new ButtonHandler());
			}
		}
	}

	@Override
	public void update(Object theObserved) {
		boardUpdating();
		didWin();
		reset();
		System.out.println("THis is a message from buttonview observable");
	}
	
	private void reset() {
		if (theGame.maxMovesRemaining() == 9) {
			
			boardUpdating();
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					buttons[i][j].setMouseTransparent(false);
				}
			}
			
			buttonGrid.setMouseTransparent(false);
			endMessage.setText("Click To Make A Move");
			endMessage.setStyle("-fx-text-fill: black;");
		}
	}
	
	private void didWin() {
		if (theGame.didWin('X')) {
			endMessage.setText("X Wins!");
			endMessage.setStyle("-fx-text-fill: green;");
			buttonGrid.setMouseTransparent(true);
		}
		
		if (theGame.didWin('O')) {
			endMessage.setText("O Wins!");
			endMessage.setStyle("-fx-text-fill: red;");
			buttonGrid.setMouseTransparent(true);
		}
		
		if (theGame.tied()) {
			endMessage.setText("Tied!");
			endMessage.setStyle("-fx-text-fill: brown;");
			buttonGrid.setMouseTransparent(true);
		}
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Button clickedButton = (Button) event.getSource();
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (buttons[i][j] == clickedButton) {
						
						theGame.humanMove(i, j, false);
						didWin();
						boardUpdating();
					}
				}
			}
		}
		
		private void didWin() {
			if (theGame.didWin('X')) {
				endMessage.setText("X Wins!");
				buttonGrid.setMouseTransparent(true);
				return;
			}
			
			if (theGame.didWin('O')) {
				endMessage.setText("O Wins!");
				buttonGrid.setMouseTransparent(true);
				return;
			}
			
			if (theGame.tied()) {
				endMessage.setText("Tied!");
				buttonGrid.setMouseTransparent(true);
				return;
			}
		}
	}
}
