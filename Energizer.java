package Pacman;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * Energizer class. Constructs energizer and includes getNode method.
 */
public class Energizer extends Circle implements Collidable {
	
	private Circle _energizer;
	
	public Energizer(double y, double x){
		_energizer = new Circle();
		_energizer.setRadius(10);
		_energizer.setCenterY(y+15);
		_energizer.setCenterX(x+15);
		_energizer.setFill(Color.WHITE);
		
		
	}
	
	@Override
	public Node getNode(){
		return _energizer;
	}

}
