package Game;

import java.util.ArrayList;
import java.util.LinkedList;

import GUI.Map;
import Objects.Box;
import Objects.Fruit;
import Objects.Ghost;
import Objects.Pacman;
import Objects.Player;

/**
 * This class need for initialization game's properties.
 * @author Max Marmer
 *
 */
public class Game {

	private Map map;
	private LinkedList<Pacman> pacmanList;
	private LinkedList<Fruit> fruitList;
	private LinkedList<Ghost> ghostList;
	private LinkedList<Box> boxList;
	private Player player;

	public Game() {}

	/**
	 * This method need for keep all data in right classes. 
	 * @param objectsData
	 */
	public void initialization(ArrayList<String> objectsData) {
		pacmanList = new LinkedList<>();
		fruitList = new LinkedList<>();
		ghostList = new LinkedList<>();
		boxList = new LinkedList<>();
		for(int i = 0; i < objectsData.size(); i++) {
			String row = objectsData.get(i);
			String [] data = row.split(",");
			switch(row.charAt(0)) {
			case 'M':
				if(!data[2].equals("0") && !data[3].equals("0")) {
					player = new Player(data[2], data[3], data[1]);
					break;
				}
			case 'P':
				Pacman pacman = new Pacman(data[2], data[3], data[1]);
				pacmanList.add(pacman);
				break;
			case 'G':
				Ghost ghost = new Ghost(data[2], data[3], data[1]);
				ghostList.add(ghost);
				break;
			case 'F':
				Fruit fruit = new Fruit(data[2], data[3], data[1]);
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
	
	/**
	 * This method create player for our game.
	 * @param coor coordinates of player
	 */
	public void createPlayer(double [] coor) {
		player = new Player(coor[0] + "", coor[1] + "", 0 + "");
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
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Map getMap() {
		return map;
	}
}
