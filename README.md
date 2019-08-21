# B
README: Pacman

DESIGN CHOICES:
I wrote Pacman with the following classes: App, BoardCoordinate, Collidable, Constants, Direction, Dot, Energizer, Game, Ghost, GhostQueue, Pacman, PaneOrganizer, SmartSquare, SquareType, and State

App: Self-explanatory

BoardCoordinate: Provided, however I added getter and setter methods for Direction in order for the class to be more useful.

Collidable: Interface that serves no other purpose other than to link collidable objects as well as return the node associated with collidable.

Constants: Self-explanatory

Direction: Enum class for Directions

Dot: Dot class that constructs the dot object and has getNode() method

Energizer: Energizer class that constructs energizer object and has getNode() method

Game: This is my main class that deals with creating the game as well as handling any game logic involved in this project

	Constructor: Takes in gamePane and all necessary labels. Sets up maze, timelines, etc. 

	SetupMaze: SEts up my maze by iterating through the support map and creating a smart square at correct locations

	MoveHandler: sets the _direction instance variable to a value and the move will be conducted in the timeline 
		     using this instance variable.

	TimeHandler: main timeline interactions that need to be consistently checked regarding the game. Checks for
		     game ending conditions, wrapping, handles pacman movement, ghost/pacman collisions, dot/energizer
		     collisions, frightened ghost movement, normal ghost movement.

	TimeHandler2: deals with the ghost pen and releasing a ghost every 3 seconds from the pen

Ghost: Deals with creating ghosts as well as any methods related to the ghosts

	Constructor: constructs ghosts, sets position, color, direction, and takes in smartsquare map

	GetNode: self-explanatory

	Direction Setters and Getters

	Color Setter

	Position setter and getters

	Ghost state setter and getters

	moveGhost: moves ghost one square in specified direction

	frightenDirection: method that returns the random direction a ghost will move in its frightened state. More
			   detailed explanation in code

	BFS: method that takes in target parameters and a ghost and determines the correct direction in which to move.
	     deeper explanation in code commments.

GhostQueue: Class that deals with ghostqueue and adding and removing elements


Pacman: Deals with creating pacman and any related methods to pacman

	Constructor: Creates pacman and sets position and takes in smartsquare map

	getNode(): self expalantory

	Position getter and setters

	Movement methods(4)

	Movement validity checker methods(4)

PaneOrganizer: GUI creation and handling

SmartSquare: class that creates smart squares

SquareType: enum class for smart square type

State: enum class for ghost state


KNOWN BUGS: NONE however, when I try to compile my project, even though eclipse shows no errors, it always says do you want to proceed with errors in project. However, the project runs smoothly.
