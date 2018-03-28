package Pacman;


import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * pacman class that deals with creating my pacman object and any methods that pacman needs in order to function
 */
public class Pacman extends Circle implements Collidable {
	
	public Circle _pacman; //instance variables
	private SmartSquare[][] _map;
	
	/*
	 * pacman constructor that takes in position coordinates and the map of smart squares
	 */
	public Pacman(double y, double x, SmartSquare[][] map){
		_pacman = new Circle();
		_pacman.setCenterX(x+15);
		_pacman.setCenterY(y+15);
		_pacman.setRadius(13);
		_pacman.setFill(Color.YELLOW);
		_map = map;
	}
	
	/*
	 * getNode for pacman
	 */
	public Node getNode(){
		return _pacman;
	}
	
	/*
	 * getters and setters for pacman x and y position
	 */
	public double getX(){
		return _pacman.getCenterX();
	}
	
	public double getY(){
		return _pacman.getCenterY();
	}
	
	public void setX(double x){
		_pacman.setCenterX(x);
	}
	
	public void setY(double y){
		_pacman.setCenterY(y);
	}
	
	/*
	 * movement methods for pacman 
	 */
	public void moveLeft(){
		_pacman.setCenterX(_pacman.getCenterX()-Constants.SQUARE_SIDE);
	}
	
	public void moveRight(){
		_pacman.setCenterX(_pacman.getCenterX()+Constants.SQUARE_SIDE);
	}
	
	public void moveUp(){
		_pacman.setCenterY(_pacman.getCenterY()-Constants.SQUARE_SIDE);
	}
	public void moveDown(){
		_pacman.setCenterY(_pacman.getCenterY()+Constants.SQUARE_SIDE);

	}
	
	/*
	 * methods that check whether moves are legal for pacman
	 */
	public boolean canMoveLeft(){
		double xPos = _pacman.getCenterX()-15; //get pacman position
		double yPos = _pacman.getCenterY()-15;
		double futureXPos = xPos -Constants.SQUARE_SIDE;//convert to a future position relative to the direction pacman is moving
		double futureYPos = yPos;
		int BoardX = (int)futureXPos/Constants.SQUARE_SIDE;//convert to indices
		int BoardY = (int)futureYPos/Constants.SQUARE_SIDE;
		if(_map[BoardY][BoardX].getType()==SquareType.WALL || BoardX == 0){//check to make sure the intended square is available to move into
			return false;
		}
		return true;
	}
	
	public boolean canMoveRight(){//same as first
		double xPos = _pacman.getCenterX()-15;
		double yPos = _pacman.getCenterY()-15;
		double futureXPos = xPos +Constants.SQUARE_SIDE;
		double futureYPos = yPos;
		int BoardX = (int)futureXPos/Constants.SQUARE_SIDE;
		int BoardY = (int)futureYPos/Constants.SQUARE_SIDE;
		if(_map[BoardY][BoardX].getType()==SquareType.WALL || BoardX==22){
			return false;
		}
		return true;
	}
	
	public boolean canMoveUp(){//same as first
		double xPos = _pacman.getCenterX()-15;
		double yPos = _pacman.getCenterY()-15;
		double futureXPos = xPos;
		double futureYPos = yPos-Constants.SQUARE_SIDE;
		int BoardX = (int)futureXPos/Constants.SQUARE_SIDE;
		int BoardY = (int)futureYPos/Constants.SQUARE_SIDE;
		if(_map[BoardY][BoardX].getType()==SquareType.WALL){
			return false;
		}
		return true;
	}
	
	public boolean canMoveDown(){//same as first
		double xPos = _pacman.getCenterX()-15;
		double yPos = _pacman.getCenterY()-15;
		double futureXPos = xPos;
		double futureYPos = yPos+Constants.SQUARE_SIDE;
		int BoardX = (int)futureXPos/Constants.SQUARE_SIDE;
		int BoardY = (int)futureYPos/Constants.SQUARE_SIDE;
		if(_map[BoardY][BoardX].getType()==SquareType.WALL){
			return false;
		}
		return true;
	}
	
	

}
