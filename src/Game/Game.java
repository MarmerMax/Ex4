package Game;

import java.util.ArrayList;
import java.util.LinkedList;

import Objects.Box;
import Objects.Fruit;
import Objects.Ghost;
import Objects.Pacman;
import Objects.Player;

public class Game {

	private LinkedList<Pacman> pacmanList;
	private LinkedList<Fruit> fruitList;
	private LinkedList<Ghost> ghostList;
	private LinkedList<Box> boxList;
	private Player player;

	public Game() {
		pacmanList = new LinkedList<>();
		fruitList = new LinkedList<>();
		ghostList = new LinkedList<>();
		boxList = new LinkedList<>();
		player = null;
	}

	public void initialization(ArrayList<String> objectsData) {
		for(int i = 0; i < objectsData.size(); i++) {
			String row = objectsData.get(i);
			String [] data = row.split(",");
			switch(row.charAt(0)) {
			case 'P':
				Pacman pacman = new Pacman(data[2], data[3]);
				pacmanList.add(pacman);
				break;
			case 'G':
				Ghost ghost = new Ghost(data[2], data[3]);
				ghostList.add(ghost);
				break;
			case 'F':
				Fruit fruit = new Fruit(data[2], data[3]);
				fruitList.add(fruit);
				break;
			case 'B':
				Box box = new Box(data[2], data[3], data[5], data[6]);
				boxList.add(box);
				break;
			default: 
				break;
			}
		}
	}
	
	public void createPlayer(double [] coor) {
		player = new Player(coor[0] + "", coor[1] + "");
	}
	
	public Player getPlayer() {
		return player;
	}

	public LinkedList<Pacman> getPacmanList(){
		return pacmanList;
	}

	public LinkedList<Fruit> getFruitList(){
		return fruitList;
	}

	public LinkedList<Ghost> getGhostList(){
		return ghostList;
	}

	public LinkedList<Box> getBoxList(){
		return boxList;
	}

}
