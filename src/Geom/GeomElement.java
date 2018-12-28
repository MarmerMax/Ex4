package Geom;

public class GeomElement implements Geom_element{
	
	private Point3D myPoint;
	
	public GeomElement(Point3D p) {
		this.myPoint = p;
	}
	
	public Point3D getMyPoint() {
		return myPoint;
	}

	public void setMyPoint(Point3D myPoint) {
		this.myPoint = myPoint;
	}

	@Override
	public double distance3D(Point3D p) {
		double dLat = Math.toRadians(myPoint.x()-p.x());
		double dLon = Math.toRadians(myPoint.y()-p.y());
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(p.x())) * Math.cos(Math.toRadians(myPoint.x())) * 
				Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return 6378137 * c; 
	}

	@Override
	public double distance2D(Point3D p) {
		double degTokm = 111111;
		return Math.sqrt(Math.pow(myPoint.x() - p.x(), 2) + Math.pow(myPoint.y() - p.y(), 2)) * degTokm;
	}

}
