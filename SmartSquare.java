package Pacman;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
 * SmartSquare class for each square on my pacman board
 */
public class SmartSquare extends Rectangle {
	
	private Rectangle _rectangle;//instance variables
	private ArrayList<Collidable> _collidable;
	private SquareType _type;
	
	/*
	 * Constructor that takes in position, color, and type of square
	 */
	public SmartSquare(double y, double x, Color color, SquareType type){
		_rectangle = new Rectangle(Constants.SQUARE_SIDE, Constants.SQUARE_SIDE);
		_rectangle.setX(x);
		_rectangle.setY(y);
		_rectangle.setFill(color);
		_collidable = new ArrayList<Collidable>();
		_type = type;
		
		
	}
	
	/*
	 * returns ArrayList of collidables
	 */
	public ArrayList<Collidable> getList(){
		return _collidable;
	}
	
	/*
	 * getNode for each smartSquare
	 */
	public Node getNode(){
		return _rectangle;
	}
	
	/*
	 * get Type of square
	 */
	public SquareType getType(){
		return _type;
	}

}
