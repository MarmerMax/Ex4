package Objects;

import Geom.Point3D;

public abstract class GameObject {
	
	private Point3D point;
	private int id;
	
	public GameObject(String lat, String lon, String id) {
		setPoint(new Point3D(Double.parseDouble(lat),Double.parseDouble(lon),0));
		this.id = Integer.parseInt(id);
	}

	public Point3D getPoint() {
		return point;
	}

	public void setPoint(Point3D point) {
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
