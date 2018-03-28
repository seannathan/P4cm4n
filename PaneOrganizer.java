package Pacman;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
 * PaneOrganizer class that deals with setting up my GUI and all graphical components
 */
public class PaneOrganizer {
	
	private BorderPane _BorderPane;//instance variables
	private Pane _gamePane;
	private Label _scoreLabel;
	private Label _livesLabel;
	private Label _gameStateLabel;
	private Button _b1;
	
	
	/*
	 * Constructor that instantiates new BorderPane and then creates the GamePane
	 */
	public PaneOrganizer(){
		_BorderPane = new BorderPane();
		_BorderPane.setStyle("-fx-background-color: #FFFFFF");
		_BorderPane.setPrefSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		this.createGamePane();
	}
	
	/*
	 * creates my game pane with all necessary labels and instantiates my game
	 */
	public void createGamePane(){
		_gamePane = new Pane();
		_BorderPane.setCenter(_gamePane);
		_gamePane.setStyle("-fx-background-color: #000000");
		_gamePane.setPrefSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		_scoreLabel = new Label();
		_scoreLabel.setTranslateX(200);
		_scoreLabel.setTranslateY(660);
		_scoreLabel.setTextFill(Color.web("#FFA500"));
		_scoreLabel.setFont(new Font("Arial", 20));
		_livesLabel = new Label();
		_livesLabel.setTranslateX(400);
		_livesLabel.setTranslateY(660);
		_livesLabel.setTextFill(Color.web("#FFA500"));
		_livesLabel.setFont(new Font("Arial", 20));
		_gameStateLabel = new Label();
		_gameStateLabel.setTranslateX(290);
		_gameStateLabel.setTranslateY(390);
		_gameStateLabel.setTextFill(Color.web("#FFA500"));
		_gameStateLabel.setFont(new Font("Arial", 20));
		_b1 = new Button("Quit");
		_b1.setTranslateX(600);
		_b1.setTranslateY(660);
		_b1.setStyle("-fx-focus-color: transparent;");
		_b1.setFocusTraversable(false);
		_b1.setOnAction(e -> Platform.exit()); //quit functionality
		_gamePane.getChildren().addAll(_scoreLabel, _livesLabel, _gameStateLabel, _b1);
		Game game = new Game(_gamePane, _scoreLabel, _livesLabel, _gameStateLabel, _b1);
		
	}
	
	/*
	 * getRoot for BorderPane
	 */
	public Pane getRoot(){
		return _BorderPane;
	}

}
