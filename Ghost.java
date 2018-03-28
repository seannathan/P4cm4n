package Pacman;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.Math;

/*
 * Ghost class that handles constructing the ghosts as well as any methods relating to the ghosts ranging from movement, BFS,
 */
public class Ghost extends Rectangle implements Collidable {
	
	private Rectangle _ghost;//instance variables
	private SmartSquare[][] _map;
	private Direction _currentDirection;
	private State _ghostState = State.NORMAL;
	
	/*
	 * ghost constructor: sets position, color, current direction, and takes in smartsquare map
	 */
	public Ghost(double y, double x, Color color, SmartSquare[][] map){
		_ghost = new Rectangle(28,28);
		_ghost.setY(y);
		_ghost.setX(x);
		_ghost.setFill(color);
		_map = map;
		_currentDirection = Direction.NULL;
	}
	
	/*
	 *returns ghost node graphically
	 */
	@Override
	public Node getNode(){ 
		return _ghost;
	}
	
	/*
	 * set and get current directions
	 */
	public void setCurrentDirection(Direction direction){
		_currentDirection = direction;
	}
	
	public Direction getCurrentDirection(){
		return _currentDirection;
	}
	
	/*
	 * sets color of ghost
	 */
	public void setColor(Color color){
		_ghost.setFill(color);
	}
	
	/*
	 * set and get both x and y positions of ghosts
	 */
	public void setXPosition(double x){
		_ghost.setX(x);
	}
	public double getXPosition(){
		return _ghost.getX();
	}
	
	public void setYPosition(double y){
		_ghost.setY(y);
	}
	
	public double getYPosition(){
		return _ghost.getY();
	}
	
	/*
	 * set and get state
	 */
	public State getState(){
		return _ghostState;
	}
	
	public void setState(State state){
		_ghostState = state;
	}
	
	/*
	 * move ghost based off direction returned from BFS 
	 */
	public void moveGhost(Direction direction){
		switch(direction){
			case LEFT:
				_ghost.setX(_ghost.getX()-Constants.SQUARE_SIDE);
				_currentDirection = Direction.LEFT;
				break;
			case RIGHT:
				_ghost.setX(_ghost.getX()+Constants.SQUARE_SIDE);
				_currentDirection = Direction.RIGHT;
				break;
			case UP:
				_ghost.setY(_ghost.getY()-Constants.SQUARE_SIDE);
				_currentDirection = Direction.UP;
				break;
			case DOWN:
				_ghost.setY(_ghost.getY()+Constants.SQUARE_SIDE);	
				_currentDirection = Direction.DOWN;
				break;
			default:
				break;

		}
	}

	/*
	 * frighten direction method that returns the random direction a ghost will move in its frightened state
	 */
	public Direction frightenDirection(Ghost ghost){
		Direction currentDirection = ghost.getCurrentDirection();//get current direction
		Direction newDirection = Direction.NULL;//intialize newDirection to be returned at the end
		int BoardY = (int)(ghost.getYPosition()/Constants.SQUARE_SIDE);//get board indices
		int BoardX = (int)(ghost.getXPosition()/Constants.SQUARE_SIDE);
		ArrayList<Direction> possibleDirections = new ArrayList<Direction>();//create array list of potential directions
		if(currentDirection==Direction.LEFT){//if currently going left, check if it can go down, up, and left and add potentials to arraylist
			if(_map[BoardY+1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.DOWN);
			}
			if(_map[BoardY-1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.UP);
			}
			if(_map[BoardY][BoardX-1].getType()!=SquareType.WALL){
				possibleDirections.add(Direction.LEFT);
			}	
		}
		else if(currentDirection==Direction.RIGHT){//if going right, check if it can go up, down, and right and add potentials to arraylist
			if(_map[BoardY+1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.DOWN);
			}
			if(_map[BoardY-1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.UP);
			}
			if(_map[BoardY][BoardX-1].getType()!=SquareType.WALL){
				possibleDirections.add(Direction.RIGHT);
			}
		}
		else if(currentDirection==Direction.UP){//if going up, check if it can go right, left, and up and add potentials to arraylist
			if(_map[BoardY-1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.UP);
			}
			if(_map[BoardY][BoardX-1].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.LEFT);
			}
			if(_map[BoardY][BoardX+1].getType()!=SquareType.WALL){
				possibleDirections.add(Direction.RIGHT);
			}
		}
		else if(currentDirection==Direction.DOWN){//if  going down, check if it can go right, left,and down and add potentials to arraylist
			if(_map[BoardY+1][BoardX].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.DOWN);
			}
			if(_map[BoardY][BoardX-1].getType()!= SquareType.WALL){
				possibleDirections.add(Direction.LEFT);
			}
			if(_map[BoardY][BoardX+1].getType()!=SquareType.WALL){
				possibleDirections.add(Direction.RIGHT);
			}
		}
		if(possibleDirections.size()==3){//if 3 possible directions, pick one randomly
			int rand = (int)Math.random()*3;
			newDirection = possibleDirections.get(rand);
		}
		else if(possibleDirections.size()==2){//if 2 possible directions, pick one randomly
			int rand = (int)Math.random()*2;
			newDirection = possibleDirections.get(rand);
		}
		else if(possibleDirections.size()==1){//pick the only direction available
			newDirection = possibleDirections.get(0);
		}
		return newDirection;//return the direction
		
	}
	
	public Direction BFS(int yInd, int xInd, Ghost ghost){
		Direction[][] direction = new Direction[23][23];
		LinkedList<BoardCoordinate> queue = new LinkedList<BoardCoordinate>(); //create queue for BoardCoordinates
		BoardCoordinate closest = null; //set closest BoardCoordinate to null for now
		double rowDistance = 0;//Initialize variables
		double colDistance = 0;
		double combinedDistance = 0;
		double shortestDistance = 1000000;
		BoardCoordinate targetLocation = new BoardCoordinate(yInd, xInd, true); //store targetLocation
		int targetRow = targetLocation.getRow();// get row and column of target
		int targetColumn = targetLocation.getColumn();
		int ghostY =(int)(ghost.getYPosition()/Constants.SQUARE_SIDE);//find board indices of ghost
		int ghostX = (int)(ghost.getXPosition()/Constants.SQUARE_SIDE);
		if(_map[ghostY][ghostX-1].getType()!=SquareType.WALL && ghost.getCurrentDirection()!=Direction.RIGHT){//if ghost can go left and is not currently going right, add potential board coordinate to queue
			BoardCoordinate ghostLeft = new BoardCoordinate(ghostY, ghostX-1, false);
			ghostLeft.setDirection(Direction.LEFT);
			direction[ghostY][ghostX-1] = Direction.LEFT;
			queue.add(ghostLeft);
		}
		if(_map[ghostY][ghostX+1].getType()!=SquareType.WALL && ghost.getCurrentDirection()!=Direction.LEFT){//if ghost can go right and is currently not going left, add to queue
			BoardCoordinate ghostRight = new BoardCoordinate(ghostY, ghostX+1, false);
			ghostRight.setDirection(Direction.RIGHT);
			direction[ghostY][ghostX+1] = Direction.RIGHT;
			queue.add(ghostRight);
			
		}
		if(_map[ghostY-1][ghostX].getType()!=SquareType.WALL && ghost.getCurrentDirection()!=Direction.DOWN){//if ghost can go up and is not currently going down, add to queue
			BoardCoordinate ghostUp = new BoardCoordinate(ghostY-1, ghostX, false);
			ghostUp.setDirection(Direction.UP);
			direction[ghostY-1][ghostX] = Direction.UP;
			queue.add(ghostUp);
		}
		if(_map[ghostY+1][ghostX].getType()!=SquareType.WALL && ghost.getCurrentDirection()!=Direction.UP){//if ghost can go down and is not currently going up, add to queue
			BoardCoordinate ghostDown = new BoardCoordinate(ghostY+1, ghostX, false);
			ghostDown.setDirection(Direction.DOWN);
			direction[ghostY+1][ghostX] = Direction.DOWN;
			queue.add(ghostDown);
		}
		while(queue.peek()!=null){//while queue is not empty
			BoardCoordinate current = queue.remove();//current boardcoordinate becomes first of queue
			int currentRow = current.getRow();//get row and column of queue
			int currentColumn = current.getColumn();
			rowDistance = Math.pow((targetRow*Constants.SQUARE_SIDE-currentRow*Constants.SQUARE_SIDE), 2);//calculate distance between this and target
			colDistance = Math.pow((targetColumn*Constants.SQUARE_SIDE-currentColumn*Constants.SQUARE_SIDE), 2);
			combinedDistance = Math.sqrt(rowDistance + colDistance);
			if(combinedDistance<shortestDistance){//if distance is shorter than current shortest distance, replace shortest distance
				shortestDistance = combinedDistance;
				closest = current;//make the closest boardcoordinate the current boardcoordinate
			}
			if(currentColumn>0){//if not out of bounds, not a wall, and has not already been visited
				if(_map[currentRow][currentColumn-1].getType()!=SquareType.WALL && direction[currentRow][currentColumn-1]==null){
					BoardCoordinate ghostLeftLoop = new BoardCoordinate(currentRow, currentColumn-1, false);//create new BoardCoordinate
					ghostLeftLoop.setDirection(current.getDirection());//set direction related to its parent
					direction[currentRow][currentColumn-1] = current.getDirection();//set direction array to this direction
					queue.add(ghostLeftLoop);//add to queue
				}
				
			}
			if(currentColumn<22){ //same as first
				if(_map[currentRow][currentColumn+1].getType()!=SquareType.WALL && direction[currentRow][currentColumn+1]==null){
					BoardCoordinate ghostRightLoop = new BoardCoordinate(currentRow, currentColumn+1, false);
					ghostRightLoop.setDirection(current.getDirection());
					direction[currentRow][currentColumn+1] = current.getDirection();
					queue.add(ghostRightLoop);
				}
			}
			if(currentRow>0){//same as first
				if(_map[currentRow-1][currentColumn].getType()!=SquareType.WALL && direction[currentRow-1][currentColumn]==null){
					BoardCoordinate ghostUpLoop = new BoardCoordinate(currentRow-1, currentColumn, false);
					ghostUpLoop.setDirection(current.getDirection());
					direction[currentRow-1][currentColumn] = current.getDirection();
					queue.add(ghostUpLoop);
				}
			}
			if(currentRow<23){//same as first
				if(_map[currentRow+1][currentColumn].getType()!=SquareType.WALL && direction[currentRow+1][currentColumn]==null){
					BoardCoordinate ghostDownLoop = new BoardCoordinate(currentRow+1, currentColumn, false);
					ghostDownLoop.setDirection(current.getDirection());
					direction[currentRow+1][currentColumn] = current.getDirection();
					queue.add(ghostDownLoop);
				}
			}
		}
		Direction finalDirection = closest.getDirection();//get the direction of closest boardCoordinate
		return finalDirection; //return the direction
	}
}
