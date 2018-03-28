package Pacman;

import java.util.LinkedList;

/*
 * ghost queue class that is used to moderate my queue of ghosts
 */
public class GhostQueue {
	
	private LinkedList<Ghost> ghostList = new LinkedList<Ghost>(); //instantiate linked list
	
	public void add(Ghost ghost){//create add method
		ghostList.addLast(ghost);
	}
	
	public Ghost get(){//create get(remove) method
		if(ghostList.isEmpty()){
			return null;
		}
		return ghostList.removeFirst();
	}
	
	public Ghost peek(){//create peek method
		return ghostList.peekFirst();
	}
}
