package Objects;

import Geom.Point3D;

public abstract class GameObject {
	
	private Point3D point;
	
	public GameObject(String lat, String lon) {
		setPoint(new Point3D(Double.parseDouble(lat),Double.parseDouble(lon),0));
	}

	public Point3D getPoint() {
		return point;
	}

	public void setPoint(Point3D point) {
		this.point = point;
	}
}
