package Algo;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import Coords.MyCoords;
import Game.Game;
import Geom.Point3D;
import Objects.Box;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;


/**
 * This class check best route to player for eat all fruits and pacmans.
 * @author Max Marmer
 *
 */
public class GameAlgo {

	private Game game;
	private double dir = 0;
	private ArrayList<Point3D> targetList;
	private ArrayList<LinkedList<Point3D>> anglesList;

	/**
	 * Algorithm constructor.
	 * @param game
	 */
	public GameAlgo(Game game) {
		this.game = game;
	}

	/**
	 * This function initialize actually situation in game and check when player need to run.  
	 */
	public void initialization() {
		checkRotate();
	}

	/**
	 * This function check rotate of player.
	 */
	private void checkRotate(){
		getAnglesOfBoxes();
		addTargetToList();
		int next [] = findNextTarget();
		dir = findDir(next);
	}

	/**
	 * This function create target list and add all targets.
	 */
	private void addTargetToList() {
		targetList = new ArrayList<>();
		addFruitToList();
		addPacmansToList();
	}

	/**
	 * This method check azimuth to target.
	 * @param next index of next target
	 * @return azimuth
	 */
	private double findDir(int [] next) {
		double temp;
		if(next[0] == 0) {
			try {
				temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), targetList.get(next[1]))[0];
			}catch(IndexOutOfBoundsException e){
				int x = game.getMap().getWidth() / 2;
				int y = game.getMap().getHeight() / 2;
				Point3D point = new Point3D(x , y);
				temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), point)[0];
			}
		}
		else {
			try {
				int [] angleId = nextAngle(next[1]);
				temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), anglesList.get(angleId[0]).get(angleId[1]))[0];
			}catch(IndexOutOfBoundsException e){
				int x = game.getMap().getWidth() / 2;
				int y = game.getMap().getHeight() / 2;
				Point3D point = new Point3D(x , y);
				temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), point)[0];
			}

		}
		return temp;
	}

	/**
	 * This function check distance to target with boxes on route.
	 * @param targetIndex
	 * @return array with next angle/target and distance to target
	 */
	private double[] findRoute(int targetIndex) {
		LinkedList<Point3D> pp = new LinkedList<>();//points3d of source and all angles in route and target

		Graph graph = new Graph(); 
		String source = "Source";
		String target = "Target";
		//System.out.println(targetList.size());
		LinkedList<String> nodes = new LinkedList<>();//names of points in route
		LinkedList<Point3D> firstCoor = new LinkedList<>();
		//add source
		Point3D player = new Point3D(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y(),0);
		int [] playerXY = game.getMap().fromLatLonToPixel(player.x(), player.y());
		pp.add(new Point3D(playerXY[0], playerXY[1]));//add point to list
		graph.add(new Node(source));//add name to graph
		nodes.add(source);//add name to nodes
		firstCoor.add(player);

		int index = 1;//name in nodes list
		for(int i = 0; i < anglesList.size(); i++) {
			for(int j = 0; j < anglesList.get(i).size(); j++) {
				Node d = new Node("" + index);//add to node
				graph.add(d);
				//add to point list (list with Point3D)
				int [] angleXY = game.getMap().fromLatLonToPixel(anglesList.get(i).get(j).x(), anglesList.get(i).get(j).y());
				pp.add(new Point3D(angleXY[0], angleXY[1]));
				nodes.add("" + index);//add to nodes list (list of names of nodes)
				firstCoor.add(anglesList.get(i).get(j));
				index++;
			}
		}
		
		//add target
		int [] targetXY = game.getMap().fromLatLonToPixel(targetList.get(targetIndex).x(), targetList.get(targetIndex).y());
		pp.add(new Point3D(targetXY[0], targetXY[1]));
		graph.add(new Node(target));
		nodes.add(target);
		firstCoor.add(targetList.get(targetIndex));

		for(int i = 0; i < pp.size() - 1; i++) {

			for(int j = 0; j < pp.size(); j++) { 
				if(i != j) {	
					Point3D sourcePoint = firstCoor.get(i);
					Point3D targetPoint = firstCoor.get(j);

					if(isGoodWay(sourcePoint, targetPoint)) {
						graph.addEdge(nodes.get(i), nodes.get(j), pp.get(i).distance2D(pp.get(j)));
					}
				}
			}
		}
		
		Graph_Algo.dijkstra(graph, source);
		
		Node b = graph.getNodeByName(target);
		ArrayList<String> shortestPath = b.getPath();
		String nextAngle = shortestPath.get(1);
		double temp [] = {b.getDist(), Double.parseDouble(nextAngle)};
		
		return temp;
	}
	

	/**
	 * This method create lines of box.
	 * @param boxIndex index of box
	 * @return list of lines
	 */
	private LinkedList<Line2D> createLinesOfBox(int boxIndex){
		LinkedList<Line2D> lineList = new LinkedList<>();

		Point3D temp1 = new Point3D(game.getBoxList().get(boxIndex).getP1().x(), game.getBoxList().get(boxIndex).getP1().y());
		Point3D temp2 = new Point3D(game.getBoxList().get(boxIndex).getP1().x(), game.getBoxList().get(boxIndex).getP2().y());
		Point3D temp3 = new Point3D(game.getBoxList().get(boxIndex).getP2().x(), game.getBoxList().get(boxIndex).getP2().y());
		Point3D temp4 = new Point3D(game.getBoxList().get(boxIndex).getP2().x(), game.getBoxList().get(boxIndex).getP1().y());

		Line2D line1 = createLine(temp1, temp2);
		Line2D line2 = createLine(temp2, temp3);
		Line2D line3 = createLine(temp3, temp4);
		Line2D line4 = createLine(temp4, temp1);
		
		lineList.add(line1);
		lineList.add(line2);
		lineList.add(line3);
		lineList.add(line4);

		return lineList;
	}
	
	/**
	 * Method for create Line.
	 * @param p1 start point
	 * @param p2 end point
	 * @return Line2D
	 */
	private Line2D createLine(Point3D p1, Point3D p2) {
		int coor1 [] = game.getMap().fromLatLonToPixel(p1.x(), p1.y());
		int coor2 [] = game.getMap().fromLatLonToPixel(p2.x(), p2.y());
		Line2D line = new Line2D.Float(coor1[0], coor1[1], coor2[0], coor2[1]);
		return line;
	}

	/**
	 * This function check if there boxes on route of player.
	 * @param source source point
	 * @param target target point
	 * @return true if no boxes in route, else false
	 */
	private boolean isGoodWay(Point3D source, Point3D target) {
		int coorSource [] = game.getMap().fromLatLonToPixel(source.x(), source.y());
		int coorTarget [] = game.getMap().fromLatLonToPixel(target.x(), target.y());
		
		Line2D playerToTarget = new Line2D.Float(coorSource[0],coorSource[1], coorTarget[0],coorTarget[1]);

		for(int i = 0; i < game.getBoxList().size(); i++) {
			
			LinkedList<Line2D> lineList = createLinesOfBox(i);
			
			for(int j = 0; j < lineList.size(); j++) {
				Line2D temp = lineList.get(j);
				if(playerToTarget.intersectsLine(temp)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method return azimuth of player to next target.
	 * @return
	 */
	public double getDir() {
		return dir;
	}
	
	/**
	 * This method convert index from all numbers of angles to concrete angle. 
	 * @param indexOfAngle 
	 * @return array with box number and angle index.
	 */
	private int[] nextAngle(int indexOfAngle) {
		int [] angleId = new int[2];
		int temp = 1;
		for(int i = 0; i < anglesList.size(); i++) {
			for(int j = 0; j < anglesList.get(i).size(); j++) {
				if(temp == indexOfAngle) {
					angleId[0] = i;
					angleId[1] = j;
				}
				temp++;
			}
		}
		return angleId;
	}

	/**
	 * This method increase boxes for create after this angles for player.
	 * @return list list boxes
	 */
	private ArrayList<Box> createBigBox() {
		ArrayList<Box> algoBox = new ArrayList<>();

		double xStep = (32.105728 - 32.101898) / game.getMap().getStartHeight();
		double yStep = (35.212416 - 35.202369) / game.getMap().getStartWidth(); 
		
		for(int i = 0; i < game.getBoxList().size(); i++) {
			Box temp = game.getBoxList().get(i); //get box
			
			Point3D temp1 = temp.getP1();
			Point3D temp2 = temp.getP2();
			
			Point3D newP1 = new Point3D(temp1.x(), temp1.y());
			Point3D newP2 = new Point3D(temp2.x(), temp2.y());
			
			newP1.add(-35 * xStep, -35 * yStep);
			newP2.add(35 * xStep, 35 * yStep);
			
			Box bigBox = new Box(newP1.x() + "", newP1.y()+"", newP2.x()+"", newP2.y()+"");
			algoBox.add(bigBox);
		}
		return algoBox;
	}

	/**
	 * This method create points of angles of bosex.
	 */
	private void getAnglesOfBoxes() {
		ArrayList<Box> bigBox = createBigBox();
		
		anglesList = new ArrayList<>();
		for(int i = 0; i < bigBox.size(); i++) {
			LinkedList<Point3D> boxPoints = new LinkedList<>();
			Box temp = bigBox.get(i);
			boxPoints.add(new Point3D(temp.getP1().x(), temp.getP1().y()));
			boxPoints.add(new Point3D(temp.getP1().x(), temp.getP2().y()));
			boxPoints.add(new Point3D(temp.getP2().x(), temp.getP2().y()));
			boxPoints.add(new Point3D(temp.getP2().x(), temp.getP1().y()));
			anglesList.add(boxPoints);
		}
	}

	/**
	 *This method add fruits to target list of player.
	 */
	private void addFruitToList() {
		for(int i = 0; i < game.getFruitList().size(); i++) {
			targetList.add(game.getFruitList().get(i).getPoint());
		}
	}

	/**
	 *This method add pacmans to target list of player.
	 */
	private void addPacmansToList() {
		for(int i = 0; i < game.getPacmanList().size(); i++) {
			if(canEat(game.getPacmanList().get(i).getPoint())) {
				targetList.add(game.getPacmanList().get(i).getPoint());
			}
		}
	}
	
	/**
	 * This method check area of triangle.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return area value
	 */
	private float area(int x1, int y1, int x2, int y2, int x3, int y3) { 
		return (float)Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0); 
	} 

	/**
	 * Math method to check if point place into rectangle.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @param x pacman x
	 * @param y pacman y
	 * @return true if inside, false outside
	 */
	private boolean thisPointInRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int x, int y) { 
		float A = area(x1, y1, x2, y2, x3, y3) + area(x1, y1, x4, y4, x3, y3); 
		float A1 = area(x, y, x1, y1, x2, y2);
		float A2 = area(x, y, x2, y2, x3, y3);
		float A3 = area(x, y, x3, y3, x4, y4); 
		float A4 = area(x, y, x1, y1, x4, y4); 

		return (A == A1 + A2 + A3 + A4); 
	}
	
	/**
	 * This function check if pacman place out of boxes.
	 * @param pacman pacman that we check
	 * @return true if player can eat pacman, else false
	 */
	private boolean canEat(Point3D pacman) {
		boolean flag = true;
		for(int i = 0; i < anglesList.size(); i++) {
			//System.out.println(i);
			int [] a = game.getMap().fromLatLonToPixel(anglesList.get(i).get(0).x(), anglesList.get(i).get(0).y());
			int [] b = game.getMap().fromLatLonToPixel(anglesList.get(i).get(1).x(), anglesList.get(i).get(1).y());
			int [] c = game.getMap().fromLatLonToPixel(anglesList.get(i).get(2).x(), anglesList.get(i).get(2).y());
			int [] d = game.getMap().fromLatLonToPixel(anglesList.get(i).get(3).x(), anglesList.get(i).get(3).y());
			
			int [] pac = game.getMap().fromLatLonToPixel(pacman.x(), pacman.y());
			if(a[0] < pac[0] && b[0] > pac[0] && a[1] < pac[1] && d[1] > pac[1]) {
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * This function check next target of player.
	 * @return array with index of next target
	 */
	private int[] findNextTarget() {
		int index[] = new int[2];
		double minRoute = Double.MAX_VALUE;
		for(int i = 0; i < targetList.size(); i++) {
			double tempDist;
			if(isGoodWay(game.getPlayer().getPoint(), targetList.get(i))) {//good way
				double [] temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), targetList.get(i));
				tempDist = temp[2];
				if(tempDist < minRoute) {
					index[0] = 0;
					index[1] = i;
					minRoute = tempDist;
				}
			}
			else {//anyway need to count bad way to this target
				double [] temp = findRoute(i);//0-distance, 1-next target
				if(temp[0] < minRoute) {
					minRoute = temp[0];
					index[0] = 1;
					index[1] = (int)temp[1];
				}
			}
		}
		return index;
	}
}
