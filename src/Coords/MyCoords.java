package Coords;

import Geom.Point3D;

public class MyCoords implements coords_converter{
	
	private double earth_radius = 6371;

	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		return null;
	}

	@Override
	public double distance3d(Point3D p1, Point3D p2) {
		double earthRadius = 6371000; //meters
		double dLat = Math.toRadians(p2.x() - p1.x());
		double dLng = Math.toRadians(p2.y() - p1.y());
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
				Math.cos(Math.toRadians(p1.x())) * Math.cos(Math.toRadians(p2.x())) *
				Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = (double) (earthRadius * c);
		return dist;
	}

	@Override
	public Point3D vector3D(Point3D p1, Point3D p2) {
	    double b = earth_radius * 1000 + p2.z();
	    double c = earth_radius * 1000 + p1.z();

	    double b2 = b * b;
	    double c2 = c * c;
	    double bc2 = 2 * b * c;	    
	    
	    double z = p2.z() - p1.z();
	    double y = Math.sqrt(b2 + c2 - bc2*Math.cos(Math.toRadians(p2.y() - p1.y())));
	    double x = Math.sqrt(b2 + c2 - bc2*Math.cos(Math.toRadians(p2.x() - p1.x())));

	    return new Point3D(x, y, z);
	}

	@Override
	public double[] azimuth_elevation_dist(Point3D p1, Point3D p2) {
		//find distance
		double earthRadius = 6371000;
		double dLat = Math.toRadians(p2.x() - p1.x());
		double dLon = Math.toRadians(p2.y() - p1.y());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + 
				Math.cos(Math.toRadians(p1.x())) * Math.cos(Math.toRadians(p2.x())) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt( 1- a));
		double dist = (double) (earthRadius * c);
		
		//find azimuth
		dLat = p2.x() - p1.x();
		dLon = p2.y() - p1.y();
		double tetha = Math.atan2(Math.sin(dLon) * Math.cos(p2.x()),
				Math.cos(p1.x()) * Math.sin(p2.x()) - Math.sin(p1.x()) * Math.cos(p2.x()) * Math.cos(dLon));
		tetha = Math.toDegrees(tetha);

		//elevation
		double elev = p2.z() - p1.z();
		
		double arr[] = {tetha, elev, dist};
		return arr;
	}

	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		if(p.x() < -180 && p.x() > 180) {
			return false;
		}
		if(p.y() < -90 || p.y() > 90) {
			return false;
		}
		if(p.z() < -450) {
			return false;
		}
		return true;
	}
	
	public boolean thisPointInRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int x, int y) { 
		float A = area(x1, y1, x2, y2, x3, y3) + area(x1, y1, x4, y4, x3, y3); 
		float A1 = area(x, y, x1, y1, x2, y2);
		float A2 = area(x, y, x2, y2, x3, y3);
		float A3 = area(x, y, x3, y3, x4, y4); 
		float A4 = area(x, y, x1, y1, x4, y4); 

		return (A == A1 + A2 + A3 + A4); 
	}
	
	private float area(int x1, int y1, int x2, int y2, int x3, int y3) { 
		return (float)Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0); 
	} 

}
