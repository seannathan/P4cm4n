package Pacman;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * Dot class. Constructs dot and includes getNode method.
 */
public class Dot extends Circle implements Collidable{
	public Circle _dot;
	
	public Dot(double y, double x){
		_dot = new Circle();
		_dot.setFill(Color.WHITE);
		_dot.setRadius(5);
		_dot.setCenterY(y+15);
		_dot.setCenterX(x+15);
		
	}
	
	@Override
	public Node getNode(){
		return _dot;
	}
	


}
