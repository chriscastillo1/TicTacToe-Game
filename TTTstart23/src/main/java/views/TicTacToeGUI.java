package views;

/**
 * Play TicTacToe the computer that can have different AIs to beat you. 
 * Select the Options menus to begin a new game, switch strategies for 
 * the computer player (BOT or AI), and to switch between the two views.
 * 
 * This class represents an event-driven program with a graphical user 
 * interface as a controller between the view and the model. It has 
 * event handlers to mediate between the view and the model.
 * 
 * This controller employs the Observer design pattern that updates two 
 * views every time the state of the tic tac toe game changes:
 * 
 *    1) whenever you make a move by clicking a button or an area of either view
 *    2) whenever the computer AI makes a move
 *    3) whenever there is a win or a tie
 *    
 * You can also select two different strategies to play against from the menus
 * 
 * @author Rick Mercer
 * @author Chris Castillo
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.IntermediateAI;
import model.OurObserver;
import model.RandomAI;
import model.TicTacToeGame;

public class TicTacToeGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private TicTacToeGame theGame;

	// Menu Options for Tic-Tac-Toe Game
	private Menu selectAI;
	private MenuBar menuBar;
	private Menu options;
	private MenuItem startNewGame;
	private Menu strats;
	private MenuItem randAI;
	private MenuItem intAI;
	private Menu views;
	private MenuItem bViewOpt;
	private MenuItem tViewOpt;

	private OurObserver currentView;
	private OurObserver buttonView;
	private OurObserver textAreaView;

	private BorderPane window;
	public static final int width = 350;
	public static final int height = 540;

	public void start(@SuppressWarnings("exports") Stage stage) {
		stage.setTitle("Tic Tac Toe");
		stage.setResizable(false);
		window = new BorderPane();
		setupMenus();

		Scene scene = new Scene(window, width, height);
		initializeGameForTheFirstTime();

		// Set up the views
		buttonView = new ButtonView(theGame);
		textAreaView = new TextAreaView(theGame);

		theGame.addObserver(buttonView);
		theGame.addObserver(textAreaView);

		setViewTo(textAreaView);

		registerHandlers();
		stage.setScene(scene);
		stage.show();
	}

	// Setup the layout of the menus
	private void setupMenus() {
		options = new Menu("Options");
		startNewGame = new MenuItem("New Game");

		strats = new Menu("Strategies");
		randAI = new MenuItem("RandomAI");
		intAI = new MenuItem("IntermediateAI");
		strats.getItems().addAll(randAI, intAI);

		views = new Menu("Views");
		bViewOpt = new MenuItem("Button");
		tViewOpt = new MenuItem("Text Area");
		views.getItems().addAll(bViewOpt, tViewOpt);

		options = new Menu("Options");
		options.getItems().addAll(startNewGame, strats, views);
		options.setStyle("-fx-border-width: 2px;" + "-fx-border-style: solid; " + "-fx-border-color: black;");

		selectAI = new Menu("AI: RandomAI");
		selectAI.setDisable(true);

		menuBar = new MenuBar();
		menuBar.getMenus().addAll(options);
		menuBar.getMenus().addAll(selectAI);

		window.setTop(menuBar);
	}

	// Set the game to the default of an empty board and the random AI.
	public void initializeGameForTheFirstTime() {
		theGame = new TicTacToeGame();
		// This event driven program will always have
		// a computer player who takes the second turn
		theGame.setComputerPlayerStrategy(new RandomAI());
	}

	// Sets the View of the GUI
	private void setViewTo(OurObserver newView) {
		window.setCenter(null);
		currentView = newView;
		window.setCenter((Node) currentView);
	}

	private void registerHandlers() {
		startNewGame.setOnAction(new HandleMenu());
		randAI.setOnAction(new HandleMenu());
		intAI.setOnAction(new HandleMenu());
		bViewOpt.setOnAction(new HandleMenu());
		tViewOpt.setOnAction(new HandleMenu());
	}

	/*
	 * 
	 */
	private class HandleMenu implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String menuName = ((MenuItem) event.getSource()).getText();

			if (menuName.equals("New Game")) {
				theGame.startNewGame();

			} else if (menuName.equals("RandomAI")) {
				selectAI.setText("AI: RandomAI");
				theGame.setComputerPlayerStrategy(new RandomAI());

			} else if (menuName.equals("IntermediateAI")) {
				selectAI.setText("AI: IntermediateAI");
				theGame.setComputerPlayerStrategy(new IntermediateAI());

			} else if (menuName.equals("Button")) {
				setViewTo(buttonView);

			} else if (menuName.equals("Text Area")) {
				setViewTo(textAreaView);
			}
		}
	}
}