package Pacman;



import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import cs015.fnl.PacmanSupport.SupportMap;
import cs015.fnl.PacmanSupport.BoardLocation;

/*
 * Game class, which deals with setting up the map, launching the game, and handles all game logic and game interaction.
 */
public class Game {
	
	private Pacman _pacman; //instance variables
	private Pane _pane;
	private BoardLocation[][] _supportMap;
	private SmartSquare[][] _map;
	private Ghost _binky;
	private Ghost _pinky;
	private Ghost _inky;
	private Ghost _clyde;
	private Timeline _timeline;
	private Timeline _ghostTimeline;
	private int _score = 0;
	private Label _scoreLabel;
	private Direction _direction;
	private int _ghostTimer = 0;
	private int _frightenTimer = 0;
	private GhostQueue _ghostQueue;
	private Label _livesLabel;
	private Label _gameStateLabel;
	private int _pacmanLives = 3;
	private int _numEaten = 0;
	private Button _quit;
	
	/*
	 * Constructor for game, takes in the gamePane and all necessary labels from PaneOrganizer.
	 */
	public Game(Pane pane, Label scorelabel, Label liveslabel, Label gameLabel, Button quit){
		_pane = pane;
		_map = new SmartSquare[23][23]; //new map of smart squares
		_supportMap = SupportMap.getMap();
		_pane.requestFocus();
		_pane.setFocusTraversable(true);
		_pane.setOnKeyPressed(new MoveHandler());
		_ghostQueue = new GhostQueue();//instantiate ghostQueue
		this.setupMaze();//setup maze
		_pacman.getNode().toFront();
		this.setupPacmanTimeline();//setup timelines 
		this.setupGhostpenTimeline();
		_scoreLabel = scorelabel;//assign labels to instance labels
		_livesLabel = liveslabel;
		_gameStateLabel = gameLabel;
		_quit = quit;
		_quit.toFront();
		_livesLabel.setText("Lives: " +_pacmanLives);
		_gameStateLabel.toFront();
		_scoreLabel.toFront();
		_livesLabel.toFront();

		
		
	}
	
	/*
	 * Sets up maze using support map to generate my own map of smart squares 
	 */
	public void setupMaze(){
		for(int i = 0; i<_supportMap.length; i++){//iterate through supportMap
			for(int j = 0; j<_supportMap[i].length; j++){
				switch(_supportMap[i][j]){
				case WALL://create a wall and add to my map
					SmartSquare squareWall = new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLUE, SquareType.WALL);
					_map[i][j] = squareWall;
					_pane.getChildren().add(squareWall.getNode());
					break;
				case FREE://create free space and add to my map
					SmartSquare squareFree = new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLACK, SquareType.FREE);
					_map[i][j] = squareFree;
					_pane.getChildren().add(squareFree.getNode());
					break;
				case DOT://create dot space and add to my map
					SmartSquare squareDot = new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLACK, SquareType.DOT);
					Dot dot = new Dot(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE);
					squareDot.getList().add(dot);
					_map[i][j] = squareDot;
					_pane.getChildren().addAll(squareDot.getNode(), dot.getNode());
					break;
				case ENERGIZER://create energizer space and add to map
					SmartSquare squareEnergizer = new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLACK, SquareType.ENERGIZER);
					Energizer energizer = new Energizer(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE);
					squareEnergizer.getList().add(energizer);
					_map[i][j] = squareEnergizer;
					_pane.getChildren().addAll(squareEnergizer.getNode(), energizer.getNode());
					break;
					
				case PACMAN_START_LOCATION://create pacman start space
					SmartSquare squarePacman = new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLACK, SquareType.PACMANSTART);
					squarePacman.getList().add(_pacman);
					_map[i][j] = squarePacman;
					_pane.getChildren().add(squarePacman.getNode());
					break;
				case GHOST_START_LOCATION://create ghost start location
					SmartSquare squareGhost= new SmartSquare(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.BLACK, SquareType.GHOSTSTART);//add ghosts to square
					_binky = new Ghost((i*Constants.SQUARE_SIDE)-2*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.RED, _map);
					_pinky = new Ghost(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE-Constants.SQUARE_SIDE, Color.PINK, _map);
					_inky = new Ghost(i*Constants.SQUARE_SIDE, j*Constants.SQUARE_SIDE, Color.CYAN, _map);
					_clyde = new Ghost(i*Constants.SQUARE_SIDE, (j*Constants.SQUARE_SIDE)+Constants.SQUARE_SIDE, Color.ORANGE, _map);
					squareGhost.getList().add(_binky);//add ghosts to arraylist of ghost start smart square
					squareGhost.getList().add(_pinky);
					squareGhost.getList().add(_inky);
					squareGhost.getList().add(_clyde);
					_ghostQueue.add(_pinky);//add everyone besides binky to ghostQueue
					_ghostQueue.add(_inky);
					_ghostQueue.add(_clyde);
					_map[i][j] = squareGhost;
					_pane.getChildren().addAll(squareGhost.getNode());

					
				}
			}
		}
		_pacman = new Pacman(17*Constants.SQUARE_SIDE, 11*Constants.SQUARE_SIDE, _map);//new pacman
		_pane.getChildren().addAll(_pacman.getNode(), _binky.getNode(), _pinky.getNode(), _inky.getNode(), _clyde.getNode());//add all nodes graphically
	}
	
	/*
	 * Movehandler for handling key input of my Pacman
	 */
	private class MoveHandler implements EventHandler<KeyEvent>{
		@Override
		public void handle(KeyEvent event){
			if(event.getCode()==KeyCode.LEFT && _pacman.canMoveLeft()){
				_direction = Direction.LEFT;
			}
			else if(event.getCode()==KeyCode.RIGHT && _pacman.canMoveRight()){
				_direction = Direction.RIGHT;
			}
			else if(event.getCode()==KeyCode.UP&&_pacman.canMoveUp()){
				_direction = Direction.UP;
			}
			else if(event.getCode()==KeyCode.DOWN && _pacman.canMoveDown()){
				_direction = Direction.DOWN;
			}
		}
	}
	
	/*
	 * Timehandler that handles all actions that need to be consistently checked
	 */
	private class TimeHandler implements EventHandler<ActionEvent>{
		
		public void handle(ActionEvent event){
			double pacmanX = _pacman.getX();//get pacman position and convert to board indices
			double pacmanY = _pacman.getY();
			int pacmanBoardY = (int)pacmanY/Constants.SQUARE_SIDE;
			int pacmanBoardX = (int)pacmanX/Constants.SQUARE_SIDE;
			if(_pacmanLives==0){//check for game end by losing
				_timeline.stop();
				_ghostTimeline.stop();
				_gameStateLabel.setText("Game Over...");
			}
			if(_numEaten == 186){//check for game end by winning
				_timeline.stop();
				_ghostTimeline.stop();
				_gameStateLabel.setText("You Win!");
			}
			if(pacmanBoardY==11 && pacmanBoardX==21 && _direction == Direction.RIGHT){//wrap to the left side
				_pacman.setX(15);
				_pacman.setY(345);
			}
			
			if(pacmanBoardY==11 && pacmanBoardX==1 && _direction==Direction.LEFT){//wrap to the right side
				_pacman.setX(675);
				_pacman.setY(345);
			}
			if(_direction == Direction.LEFT && _pacman.canMoveLeft()){ //handles pacman movement by takign in direction parameter from movehandler.
				_pacman.moveLeft();
			}
			if(_direction == Direction.RIGHT && _pacman.canMoveRight()){
				_pacman.moveRight();
			}
			if(_direction == Direction.UP&& _pacman.canMoveUp()){
				_pacman.moveUp();
			}
			if(_direction == Direction.DOWN && _pacman.canMoveDown()){
				_pacman.moveDown();
			}
			for(int i=0; i<4; i++){//checks for ghost and pacman collisions after moving pacman
				Ghost ghost = (Ghost) _map[10][11].getList().get(i);
				double x = ghost.getXPosition(); //gets ghost position and converts to board indices
				double y = ghost.getYPosition();
				int boardY = (int)(y/Constants.SQUARE_SIDE);
				int boardX = (int)(x/Constants.SQUARE_SIDE);
				if(pacmanBoardY == boardY && pacmanBoardX == boardX && ghost.getState()==State.FRIGHTENED){ //if ghosts are frightened, send them back to ghost pen and add to queue
					ghost.setXPosition(Constants.GHOST_Y_START);
					ghost.setYPosition(Constants.GHOST_X_START);
					_ghostQueue.add(ghost);
					_score = _score +200;
				}
				if(pacmanBoardY == boardY && pacmanBoardX == boardX && ghost.getState()==State.NORMAL){// if ghosts are normal, reset all positions and subtract a life
					_pacmanLives = _pacmanLives-1;
					_livesLabel.setText("Lives "+ _pacmanLives);
					_ghostQueue.add(_binky);
					_ghostQueue.add(_pinky);
					_ghostQueue.add(_inky);
					_ghostQueue.add(_clyde);
					_binky.setXPosition(Constants.GHOST_X_START);
					_binky.setYPosition(Constants.GHOST_Y_START);
					_pinky.setXPosition(Constants.GHOST_X_START);
					_pinky.setYPosition(Constants.GHOST_Y_START);
					_inky.setXPosition(Constants.GHOST_X_START);
					_inky.setYPosition(Constants.GHOST_Y_START);
					_clyde.setXPosition(Constants.GHOST_X_START);
					_clyde.setYPosition(Constants.GHOST_Y_START);
					_pacman.setX(345);
					_pacman.setY(525);
				}
			}
			if(_map[pacmanBoardY][pacmanBoardX].getType()==SquareType.DOT&& _map[pacmanBoardY][pacmanBoardX].getList().size()!=0){//check for pacman dot collision, add score, remove dot logically and graphically
				_score = _score + 10;
				_numEaten++;
				Collidable collidable = _map[pacmanBoardY][pacmanBoardX].getList().get(0);
				_pane.getChildren().remove(collidable.getNode());
				_map[pacmanBoardY][pacmanBoardX].getList().remove(0);
			}
			if(_map[pacmanBoardY][pacmanBoardX].getType()==SquareType.ENERGIZER && _map[pacmanBoardY][pacmanBoardX].getList().size()!=0){//check for pacman energizer collision, add score, remove energizer logically and graphically, start the frighten timer
				_score = _score + 100;
				_numEaten++;
				_frightenTimer = 35;
				Collidable collidable = _map[pacmanBoardY][pacmanBoardX].getList().get(0);
				_pane.getChildren().remove(collidable.getNode());
				_map[pacmanBoardY][pacmanBoardX].getList().remove(0);
				_binky.setState(State.FRIGHTENED);
				_pinky.setState(State.FRIGHTENED);
				_inky.setState(State.FRIGHTENED);
				_clyde.setState(State.FRIGHTENED);
				_binky.setColor(Color.CHARTREUSE);
				_inky.setColor(Color.CHARTREUSE);
				_pinky.setColor(Color.CHARTREUSE);
				_clyde.setColor(Color.CHARTREUSE);
			}
			_scoreLabel.setText("Score "+_score); //change score label after collisions handled
			
			if(_frightenTimer!=0){//if frighten timer has been started, move ghosts according to frighten protocol
				_binky.moveGhost(_binky.frightenDirection(_binky));
				_pinky.moveGhost(_pinky.frightenDirection(_pinky));
				_inky.moveGhost(_inky.frightenDirection(_inky));
				_clyde.moveGhost(_clyde.frightenDirection(_clyde));
				_frightenTimer--;

			}
			if(_frightenTimer==0){//once the frighten timer is over, revert the ghosts colors and change their state back to normal

				_binky.setColor(Color.RED);
				_binky.setState(State.NORMAL);
				_pinky.setColor(Color.PINK);
				_pinky.setState(State.NORMAL);
				_inky.setColor(Color.CYAN);
				_inky.setState(State.NORMAL);
				_clyde.setColor(Color.ORANGE);
				_clyde.setState(State.NORMAL);
			}
			
			
			if(_ghostTimer ==70){//if ghost timer hits its max of 40, reset it to 0
				_ghostTimer=0;
			}
			if(_ghostTimer >= 35 && _binky.getState()==State.NORMAL){//if ghost timer is from 35-69, chase pacman. each ghosts targets different square relative to pacman
				_binky.moveGhost(_binky.BFS((int)((_pacman.getY()-15)/Constants.SQUARE_SIDE), (int)((_pacman.getX()-15)/Constants.SQUARE_SIDE), _binky));
				_pinky.moveGhost(_pinky.BFS((int)((_pacman.getY()-15)/Constants.SQUARE_SIDE), (int)((_pacman.getX()+45)/Constants.SQUARE_SIDE), _pinky));
				_inky.moveGhost(_inky.BFS((int)((_pacman.getY()-75)/Constants.SQUARE_SIDE), (int)((_pacman.getX()-15)/Constants.SQUARE_SIDE), _inky));
				_clyde.moveGhost(_clyde.BFS((int)((_pacman.getY()+45)/Constants.SQUARE_SIDE), (int)((_pacman.getX()-15)/Constants.SQUARE_SIDE), _clyde));
				

			}
			if(_ghostTimer<35 && _binky.getState()==State.NORMAL){//if ghost timer is from 0-34, ghosts scatter to their respective corners
				_binky.moveGhost(_binky.BFS(1, 1, _binky));
				_pinky.moveGhost(_pinky.BFS(1, 22, _pinky));
				_inky.moveGhost(_inky.BFS(22, 1, _inky));
				_clyde.moveGhost(_clyde.BFS(22, 22, _clyde));
				
			}
			_ghostTimer++;
			for(int i=0; i<4; i++){//check for collisions after ghosts move, copy and pasted from above
				Ghost ghost = (Ghost) _map[10][11].getList().get(i);
				double x = ghost.getXPosition();
				double y = ghost.getYPosition();
				int boardY = (int)(y/Constants.SQUARE_SIDE);
				int boardX = (int)(x/Constants.SQUARE_SIDE);
				if(pacmanBoardY == boardY && pacmanBoardX == boardX && ghost.getState()==State.FRIGHTENED){
					_score = _score +200;
					ghost.setXPosition(Constants.GHOST_Y_START);
					ghost.setYPosition(Constants.GHOST_X_START);
					_ghostQueue.add(ghost);
				}
				else if(pacmanBoardY == boardY && pacmanBoardX == boardX && ghost.getState()==State.NORMAL){
					_pacmanLives = _pacmanLives-1;
					_livesLabel.setText("Lives "+ _pacmanLives);
					if(_pacmanLives==0){
						_timeline.stop();
						_ghostTimeline.stop();
						_gameStateLabel.setText("Game Over...");
					}
					if(_numEaten == 186){
						_timeline.stop();
						_ghostTimeline.stop();
						_gameStateLabel.setText("You Win!");
					}		_ghostQueue.add(_binky);
					_ghostQueue.add(_pinky);
					_ghostQueue.add(_inky);
					_ghostQueue.add(_clyde);
					_binky.setXPosition(Constants.GHOST_X_START);
					_binky.setYPosition(Constants.GHOST_Y_START);
					_pinky.setXPosition(Constants.GHOST_X_START);
					_pinky.setYPosition(Constants.GHOST_Y_START);
					_inky.setXPosition(Constants.GHOST_X_START);
					_inky.setYPosition(Constants.GHOST_Y_START);
					_clyde.setXPosition(Constants.GHOST_X_START);
					_clyde.setYPosition(Constants.GHOST_Y_START);
					_pacman.setX(345);
					_pacman.setY(525);
				}
			}
			if(_pacmanLives==0){//check for game ending parameters after timeline action has commenced. copy and pasted from above as well.
				_timeline.stop();
				_ghostTimeline.stop();
				_gameStateLabel.setText("Game Over...");
			}
			if(_numEaten == 186){
				_timeline.stop();
				_ghostTimeline.stop();
				_gameStateLabel.setText("You Win!");
			}
		}
	}
	
	/*
	 * setup pacman timeline with a keyframe duration of 0.2 seconds
	 */
	public void setupPacmanTimeline(){
		
		KeyFrame pacman = new KeyFrame(Duration.seconds(0.2), new TimeHandler());//keyframe instantiation that sets a specific duration and my timehandler
		_timeline = new Timeline(pacman);//new timeline that takes in my keyframe as parameter
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}
	
	/*
	 * setup ghostPen interactions regarding letting ghosts free
	 */
	private class TimeHandler2 implements EventHandler<ActionEvent>{
		
		public void handle(ActionEvent event){
			if(_ghostQueue.peek()!=null && _binky.getState()!=State.FRIGHTENED){
				Ghost ghost = _ghostQueue.get();
				ghost.setXPosition(Constants.GHOST_X_START);
				ghost.setYPosition(240);
			}
			
		}
	}
	
	/*
	 * setup ghostPen timeline with a duration of 3 seconds in between letting each ghost out
	 */
	public void setupGhostpenTimeline(){
		
		KeyFrame ghostPen = new KeyFrame(Duration.seconds(3), new TimeHandler2());//keyframe instantiation that sets a specific duration and my timehandler
		_ghostTimeline = new Timeline(ghostPen);//new timeline that takes in my keyframe as parameter
		_ghostTimeline.setCycleCount(Animation.INDEFINITE);
		_ghostTimeline.play();
	}
}
