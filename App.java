package Pacman;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the  main class where your Pacman game will start.
  * The main method of this application calls the App constructor. You
  * will need to fill in the constructor to instantiate your game.
  *
  * Class comments here...
  *
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
    	PaneOrganizer Organizer = new PaneOrganizer();//instantiates new pane organizer
    	Scene scene = new Scene(Organizer.getRoot());//instantiates new scene with pane organizer as parameter
    	stage.setScene(scene);//sets scene and scene specs
    	stage.setWidth(Constants.GAME_WIDTH);
    	stage.setHeight(Constants.GAME_HEIGHT);
    	stage.show();
    }
    

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
