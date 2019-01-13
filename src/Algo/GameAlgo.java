package Algo;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import com.mysql.fabric.xmlrpc.base.Array;

import Coords.MyCoords;
import GUI.Map;
import Game.Game;
import Geom.Point3D;
import Objects.Box;
import Objects.Fruit;
import Objects.Pacman;
import Objects.Player;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;
public class GameAlgo {

	private Game game;
	private double dir = 0;
	private ArrayList<Point3D> targetList;
	private ArrayList<LinkedList<Point3D>> anglesList;

	public GameAlgo(Game game) {
		this.game = game;
	}

	public void initialization() {
		checkRotate();
	}

	private void checkRotate(){
		getAnglesOfBoxes();
		addTargetToList();
		int next [] = findNextTarget();
		dir = findDir(next);
	}

	private void addTargetToList() {
		targetList = new ArrayList<>();
		addFruitToList();
		addPacmansToList();
	}

	private double findDir(int [] next) {
		double temp;
		if(next[0] == 0) {
			temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), targetList.get(next[1]))[0];
		}
		else {
			int [] angleId = nextAngle(next[1]);
			temp = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), anglesList.get(angleId[0]).get(angleId[1]))[0];
		}
		return temp;
	}

	private double[] findRoute(int targetIndex) {
		LinkedList<Point3D> pp = new LinkedList<>();//points3d of source and all angles in route and target

		Graph graph = new Graph(); 
		String source = "Source";
		String target = "Target";
		
		LinkedList<String> nodes = new LinkedList<>();//names of points in route
		
		//add source
		Point3D player = new Point3D(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y(),0);
		int [] playerXY = game.getMap().fromLatLonToPixel(player.x(), player.y());
		pp.add(new Point3D(playerXY[0], playerXY[1]));//add point to list
		graph.add(new Node(source));//add name to graph
		nodes.add(source);//add name to nodes

		int index = 1;//name in nodes list
		for(int i = 0; i < anglesList.size(); i++) {
			for(int j = 0; j < anglesList.get(i).size(); j++) {
				Node d = new Node("" + index);//add to node
				graph.add(d);
				//add to point list (list with Point3D)
				int [] angleXY = game.getMap().fromLatLonToPixel(anglesList.get(i).get(j).x(), anglesList.get(i).get(j).y());
				pp.add(new Point3D(angleXY[0], angleXY[1]));
				nodes.add("" + index);//add to nodes list (list of names of nodes)
				index++;
			}
		}
		
		//add target
		int [] targetXY = game.getMap().fromLatLonToPixel(targetList.get(targetIndex).x(), targetList.get(targetIndex).y());
		pp.add(new Point3D(targetXY[0], targetXY[1]));
		graph.add(new Node(target));
		nodes.add(target);

		for(int i = 0; i < pp.size() - 1; i++) {
			System.out.print(nodes.get(i) + "");
			for(int j = 0; j < pp.size(); j++) { 
				if(i != j) {
					double [] sourceTemp = game.getMap().fromPixelToLatLon((int)pp.get(i).x(), (int)pp.get(i).y());
					double [] targetTemp = game.getMap().fromPixelToLatLon((int)pp.get(j).x(), (int)pp.get(j).y());

					Point3D sourcePoint = new Point3D(sourceTemp[0], sourceTemp[1]);
					Point3D targetPoint = new Point3D(targetTemp[0], targetTemp[1]);
					
					if(isGoodWay(sourcePoint, targetPoint) == -1) {
						System.out.print("-> " + nodes.get(j));
						graph.addEdge(nodes.get(i), nodes.get(j), pp.get(i).distance2D(pp.get(j)));
					}
				}
			}
			System.out.println();
		}
		print();
		
		Graph_Algo.dijkstra(graph, source);
		Node b = graph.getNodeByName(target);
		System.out.println("***** Graph Demo for OOP_Ex4 *****");
		System.out.println(b);
		System.out.println("Dist: "+b.getDist());
		//System.exit(1);
		
		ArrayList<String> shortestPath = b.getPath();
		
		for(int i=0;i<shortestPath.size();i++) {
			if(i == 0) {
				System.out.print(shortestPath.get(i));
			}
			else {
				System.out.print(", "+shortestPath.get(i));
			}
		}
		System.exit(1);
		String nextAngle = shortestPath.get(1);
		double temp [] = {b.getDist(), Double.parseDouble(nextAngle)};
		return temp;
	}
	
	public void print() {
		for(int i = 0 ; i< anglesList.size();i++) {
			System.out.println();
			for(int j = 0; j < anglesList.get(i).size(); j++) {
				int coor[] = game.getMap().fromLatLonToPixel(anglesList.get(i).get(j).x(), anglesList.get(i).get(j).y());
				System.out.print(" -> " + (j+1)+ "" + Arrays.toString(coor));
			}
			System.out.println();
		}
		
		for(int i = 0; i < game.getBoxList().size(); i++) {
			Point3D temp1 = game.getBoxList().get(i).getP1();
			Point3D temp2 = game.getBoxList().get(i).getP2();
			
			int coor1[] = game.getMap().fromLatLonToPixel(temp1.x(), temp1.y());
			int coor2[] = game.getMap().fromLatLonToPixel(temp1.x(), temp2.y());
			int coor3[] = game.getMap().fromLatLonToPixel(temp2.x(), temp2.y());
			int coor4[] = game.getMap().fromLatLonToPixel(temp2.x(), temp1.y());
			System.out.println();
			System.out.println(" -> " + Arrays.toString(coor1) + " -> " + Arrays.toString(coor2)
							+ " -> " + Arrays.toString(coor3) + " -> " + Arrays.toString(coor4));
		}
	}
	
	private void printSourceToTarget(double[]temp1, double[]temp2, int i, int j) {
		int [] arr1 = game.getMap().fromLatLonToPixel(temp1[0], temp1[1]);
		int [] arr2 = game.getMap().fromLatLonToPixel(temp2[0], temp2[1]);
		System.out.println(i + " (" + arr1[0] +", "+arr1[1]+") -> " + j +" ("+arr2[0]+", "+arr2[1]+")");
	}

	private LinkedList<Line2D> createLinesOfBox(int boxIndex){//all line codcods in pixel
		LinkedList<Line2D> lineList = new LinkedList<>();
//		Line2D line1 = createLine(anglesList.get(boxIndex).get(0), anglesList.get(boxIndex).get(1));
//		Line2D line2 = createLine(anglesList.get(boxIndex).get(1), anglesList.get(boxIndex).get(2));
//		Line2D line3 = createLine(anglesList.get(boxIndex).get(2), anglesList.get(boxIndex).get(3));
//		Line2D line4 = createLine(anglesList.get(boxIndex).get(3), anglesList.get(boxIndex).get(0));

		Point3D temp1 = new Point3D(game.getBoxList().get(boxIndex).getP1().x(), game.getBoxList().get(boxIndex).getP1().y());
		Point3D temp2 = new Point3D(game.getBoxList().get(boxIndex).getP1().x(), game.getBoxList().get(boxIndex).getP2().y());
		Point3D temp3 = new Point3D(game.getBoxList().get(boxIndex).getP2().x(), game.getBoxList().get(boxIndex).getP2().y());
		Point3D temp4 = new Point3D(game.getBoxList().get(boxIndex).getP2().x(), game.getBoxList().get(boxIndex).getP1().y());

		Line2D line1 = createLine(temp1, temp2);
		Line2D line2 = createLine(temp2, temp3);
		Line2D line3 = createLine(temp3, temp4);
		Line2D line4 = createLine(temp4, temp1);
		
//		System.out.println("1: (" + line1.getX1()+", "+line1.getY1()+")  ->  ("+line1.getX2()+", " + line1.getY2() + ")");
//		System.out.println("2: (" + line2.getX1()+", "+line2.getY1()+")  ->  ("+line2.getX2()+", " + line2.getY2() + ")");
//		System.out.println("3: (" + line3.getX1()+", "+line3.getY1()+")  ->  ("+line3.getX2()+", " + line3.getY2() + ")");
//		System.out.println("4: (" + line4.getX1()+", "+line4.getY1()+")  ->  ("+line4.getX2()+", " + line4.getY2() + ")");

		lineList.add(line1);
		lineList.add(line2);
		lineList.add(line3);
		lineList.add(line4);

		return lineList;
	}

	private int isGoodWay(Point3D source, Point3D target) {

		double actualWidth = game.getMap().getWidthPercent();
		double actualHeight = game.getMap().getHeightPercent();
		
		int coorSource [] = game.getMap().fromLatLonToPixel(source.x(), source.y());
		int coorTarget [] = game.getMap().fromLatLonToPixel(target.x(), target.y());
		
//		Line2D playerToTarget = new Line2D.Float((int)(coorSource[0] * actualWidth),
//												 (int)(coorSource[1] * actualHeight),
//												 (int)(coorTarget[0] * actualWidth),
//												 (int)(coorTarget[1] * actualHeight));
		
		Line2D playerToTarget = new Line2D.Float(coorSource[0],coorSource[1], coorTarget[0],coorTarget[1]);

		int indexOfNearestBoxInRoute = -1;
		double minDistToBox = Double.MAX_VALUE;

		for(int i = 0; i < game.getBoxList().size(); i++) {

			//sides of rectangle

			LinkedList<Line2D> lineList = createLinesOfBox(i);

			for(int j = 0; j < lineList.size(); j++) {
				Line2D temp = lineList.get(j);
				if(playerToTarget.intersectsLine(temp)) {
					double dist = 0;

					int intersectionPointInPixel [] = intersectionPoint(playerToTarget, lineList.get(j));

					double intersectionPointInLatLon [] = 
							game.getMap().fromPixelToLatLon(intersectionPointInPixel[0], intersectionPointInPixel[1]);

					Point3D intersection = new Point3D(intersectionPointInLatLon[0], intersectionPointInLatLon[1]);

					dist = new MyCoords().distance3d(game.getPlayer().getPoint(), intersection);

					if(dist < minDistToBox) {
						minDistToBox = dist;
						indexOfNearestBoxInRoute = i;
					}
				}
			}
		}
		return indexOfNearestBoxInRoute;// -1 mean that no boxes on the way to target, other number mean there box on the way
	}

	public double getDir() {
		return dir;
	}

	private Line2D createLine(Point3D p1, Point3D p2) {
		
		double actualWidth = game.getMap().getWidthPercent();
		double actualHeight = game.getMap().getHeightPercent();
		
		int coor1 [] = game.getMap().fromLatLonToPixel(p1.x(), p1.y());
		int coor2 [] = game.getMap().fromLatLonToPixel(p2.x(), p2.y());
		
//		Line2D line = new Line2D.Float((int)(coor1[0] * actualWidth),
//									   (int)(coor1[1] * actualHeight), 
//									   (int)(coor2[0] * actualWidth), 
//									   (int)(coor2[1] * actualHeight));
		
		Line2D line = new Line2D.Float(coor1[0], coor1[1], coor2[0], coor2[1]);
		return line;
	}
	
	private int[] nextAngle(int indexOfAngle) {
		int [] angleId = new int[2];
		int temp = 1;
		for(int i = 0; i < anglesList.size(); i++) {
			for(int j = 0; j < anglesList.size(); j++) {
				if(temp == indexOfAngle) {
					angleId[0] = i;
					angleId[1] = j;
				}
				temp++;
			}
		}
		return angleId;
	}

	private int[] intersectionPoint(Line2D line1, Line2D line2) {
		double p1x, p1y, p2x, p2y, q1x, q1y, q2x, q2y;
		p1x = line1.getX1();
		p1y = line1.getY1();
		p2x = line1.getX2();
		p2y = line1.getY2();

		q1x = line2.getX1();
		q1y = line2.getY1();
		q2x = line2.getX2();
		q2y = line2.getY2();

		double a1 = p2y - p1y;
		double b1 = p1x - p2x;
		double c1 = a1 * p1x + b1 * p1y;

		double a2 = q2y - q1y;
		double b2 = q1x - q2x;
		double c2 = a2 * q2x + b2 * q2y;

		double delta = a1 * b2 - a2 * b1;

		int [] arr = {(int)((b2 * c1 - b1 * c2) / delta), (int)((a1 * c2 - a2 * c1) / delta)};

		return arr;
	}

	private ArrayList<Box> createBigBox() {
		ArrayList<Box> algoBox = new ArrayList<>();

		double xStep = (32.105728 - 32.101898) / game.getMap().getStartHeight();
		double yStep = (35.212416 - 35.202369) / game.getMap().getStartWidth(); 
		
		for(int i = 0; i < game.getBoxList().size(); i++) {
			Box temp = game.getBoxList().get(i); //get box
			
			Point3D temp1 = temp.getP1();
			Point3D temp2 = temp.getP2();
			
//			temp1.add(-10 * xStep, 10 * yStep);
//			temp2.add(10 * xStep, 10 * yStep);
			
			Point3D newP1 = new Point3D(temp1.x(), temp1.y());
			Point3D newP2 = new Point3D(temp2.x(), temp2.y());
			
			newP1.add(-10 * xStep, -10 * yStep);
			newP2.add(10 * xStep, 10 * yStep);
			
			Box bigBox = new Box(newP1.x() + "", newP1.y()+"", newP2.x()+"", newP2.y()+"");
			algoBox.add(bigBox);
		}
		return algoBox;
	}

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
			
			int [] A = new Map().fromLatLonToPixel(temp.getP1().x(), temp.getP1().y());
			int [] B = new Map().fromLatLonToPixel(temp.getP1().x(), temp.getP2().y());
			int [] C = new Map().fromLatLonToPixel(temp.getP2().x(), temp.getP2().y());
			int [] D = new Map().fromLatLonToPixel(temp.getP2().x(), temp.getP1().y());
			
			System.out.println("A" + " " + Arrays.toString(A));
			System.out.println("B" + " " + Arrays.toString(B));
			System.out.println("C" + " " + Arrays.toString(C));
			System.out.println("D" + " " + Arrays.toString(D));
		}
	}

	private void addFruitToList() {
		for(int i = 0; i < game.getFruitList().size(); i++) {
			targetList.add(game.getFruitList().get(i).getPoint());
		}
	}

	private void addPacmansToList() {
		for(int i = 0; i < game.getPacmanList().size(); i++) {
			targetList.add(game.getPacmanList().get(i).getPoint());
		}
	}
	
	private int[] findNextTarget() {
		int index[] = new int[3];
		double minRoute = Double.MAX_VALUE;
		for(int i = 0; i < targetList.size(); i++) {
			double tempDist;
			int boxOnRoute = isGoodWay(game.getPlayer().getPoint(), targetList.get(i));
			
			if(boxOnRoute == -1) {//good way
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
