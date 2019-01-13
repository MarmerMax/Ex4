package Algo;

import java.util.LinkedList;

import Geom.Point3D;

public class Path {

	private LinkedList<Point3D> path;
	private double distance;
	
	public Path(Point3D player) {
		path = new LinkedList<>();
		path.add(player);
	}
	
	public void add(Point3D point) {
		path.add(point);
		distance += point.distance2D(path.get(path.size() - 1));
	}
	
	public LinkedList<Point3D> getPath(){
		return path;
	}
	
	public double getDistance() {
		return distance;
	}
}
