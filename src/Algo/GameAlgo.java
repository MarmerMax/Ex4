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
public class GameAlgo {
	
	private Game game;
	private double dir = 0;
	private ArrayList<LinkedList<Double>> targetList;
	private ArrayList<LinkedList<Point3D>> boxAnglesList;
	private ArrayList<Box> algoBox;
	

	public GameAlgo(Game game) {
		this.game = game;
		increaseBoxes();
	}
	
	public void initialization() {
		checkRotate();
	}
	
	private void checkRotate(){
		addTargetToList();
		findNextTarget();
		int next = findNextTarget();
		dir = targetList.get(next).get(3);
	}
	
	private void addTargetToList() {
		targetList = new ArrayList<>();
		addFruitToList();
		addPacmansToList();
	}
	

	
	private int findNextTarget() {
		int index = 0;
		double min = Double.MAX_VALUE;
		for(int i = 0; i < targetList.size(); i++) {
			if(isGoodWay(targetList.get(i)) == -1) {
				if(targetList.get(i).get(2) < min) {
					index = i;
					min = targetList.get(i).get(2);
				}
			}
			else {//anyway need to count bad way to this target
				//boaz algorithm dijkstra
				System.out.println(isGoodWay(targetList.get(i)));
				
			}
			isGoodWay(targetList.get(index));
		}
		return index;
	}
	
	private int isGoodWay(LinkedList<Double> temp) {
			//boolean ans = true;
			Player player = game.getPlayer();
			int coorPlayer [] = game.getMap().fromLatLonToPixel(player.getPoint().x(), player.getPoint().y());
			int coorTarget [] = game.getMap().fromLatLonToPixel(temp.get(0), temp.get(1));
			Line2D playerToTarget = new Line2D.Float(coorPlayer[0], coorPlayer[1], coorTarget[0], coorTarget[1]);
			
			int indexOfNearestBoxInRoute = -1;
			double minDistToBox = Double.MAX_VALUE;
			
			for(int i = 0; i < algoBox.size(); i++) {
				
				Line2D line1 = createLine(boxAnglesList.get(i).get(0), boxAnglesList.get(i).get(1));
				Line2D line2 = createLine(boxAnglesList.get(i).get(1), boxAnglesList.get(i).get(2));
				Line2D line3 = createLine(boxAnglesList.get(i).get(2), boxAnglesList.get(i).get(3));
				Line2D line4 = createLine(boxAnglesList.get(i).get(3), boxAnglesList.get(i).get(0));
				
				LinkedList<Line2D> lineList = new LinkedList<>();
				
				lineList.add(line1);
				lineList.add(line2);
				lineList.add(line3);
				lineList.add(line4);
			
				for(int j = 0; j < lineList.size(); j++) {
					if(playerToTarget.intersectsLine(lineList.get(j))) {
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
						//System.out.println("Intersect box: " + i);
					}
				}
			}
			
			return indexOfNearestBoxInRoute;// -1 mean that no boxes on the way to target, other number mean there box on the way
	}
	
	public double getDir() {
		return dir;
	}
	
	
	private Line2D createLine(Point3D p1, Point3D p2) {
		int coor1 [] = game.getMap().fromLatLonToPixel(p1.x(), p1.y());
		int coor2 [] = game.getMap().fromLatLonToPixel(p2.x(), p2.y());
		Line2D line = new Line2D.Float(coor1[0], coor1[1], coor2[0], coor2[1]);
		return line;
	}
	
	private int[] intersectionPoint(Line2D line1, Line2D line2) {
		//System.out.println(line1.getP1().toString() + " ----> " + line1.getP2().toString());
		//System.out.println(line2.getP1().toString() + " ----> " + line2.getP2().toString());
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

	private void increaseBoxes() {
		algoBox = new ArrayList<>();
		for(int i = 0; i < game.getBoxList().size(); i++) {
			Box temp = game.getBoxList().get(i); //get box
			Point3D temp1 = temp.getP1();
			Point3D temp2 = temp.getP2();
			
			int [] p1 = game.getMap().fromLatLonToPixel(temp1.x(), temp1.y()); // get x,y in pixel
			p1[0] += -10; // point away 10 px
			p1[1] += -10; // point away 10 px
			int [] p2 = game.getMap().fromLatLonToPixel(temp2.x(), temp2.y()); // get x,y in pixel
			p2[0] += 10; // point away 10 px
			p2[1] += 10; // point away 10 px
			
			double [] pp1 = game.getMap().fromPixelToLatLon(p1[0], p1[1]); // return point to lat, lon
			double [] pp2 = game.getMap().fromPixelToLatLon(p2[0], p2[1]); // return point to lat, lon
			
			Box bigBox = new Box(pp1[0] + "", pp1[1] + "", pp2[0] + "", pp2[1] + ""); //create big box wit new points
			
			algoBox.add(bigBox);
		}
		getAnglesOfBoxes();
	}
	
	private void getAnglesOfBoxes() {
		boxAnglesList = new ArrayList<>();
		for(int i = 0; i < algoBox.size(); i++) {
			LinkedList<Point3D> boxPoints = new LinkedList<>();
			Box temp = algoBox.get(i);
			boxPoints.add(new Point3D(temp.getP1().x(), temp.getP1().y()));
			boxPoints.add(new Point3D(temp.getP1().x(), temp.getP2().y()));
			boxPoints.add(new Point3D(temp.getP2().x(), temp.getP2().y()));
			boxPoints.add(new Point3D(temp.getP2().x(), temp.getP1().y()));
			boxAnglesList.add(boxPoints);
		}
	}
	
	private void addFruitToList() {
		Iterator<Fruit> iteratorFruit = game.getFruitList().iterator();
		while(iteratorFruit.hasNext()) {
			Fruit temp = iteratorFruit.next();
			double [] aed = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), temp.getPoint());
			LinkedList<Double> xyda = new LinkedList<>();
			xyda.add(temp.getPoint().x());
			xyda.add(temp.getPoint().y());
			xyda.add(aed[2]);
			xyda.add(aed[0]);
			targetList.add(xyda);
		}
	}
	
	private void addPacmansToList() {
		Iterator<Pacman> iteratorPacman = game.getPacmanList().iterator();
		while(iteratorPacman.hasNext()) {
			Pacman temp = iteratorPacman.next();
			double [] aed = new MyCoords().azimuth_elevation_dist(game.getPlayer().getPoint(), temp.getPoint());
			LinkedList<Double> xyda = new LinkedList<>();
			xyda.add(temp.getPoint().x());
			xyda.add(temp.getPoint().y());
			xyda.add(aed[2]);
			xyda.add(aed[0]);
			targetList.add(xyda);
		}
	}

}
  