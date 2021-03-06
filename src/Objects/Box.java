package Objects;

import Geom.Point3D;
/**
 * Class for represent box in project.
 * @author Mar Marmer
 *
 */
public class Box {
	
	private Point3D p1, p2;
	
	/**
	 * Constructor.
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 */
	public Box(String lat1, String lon1, String lat2, String lon2) {
		p1 = new Point3D(Double.parseDouble(lat1),Double.parseDouble(lon1),0);
		p2 = new Point3D(Double.parseDouble(lat2),Double.parseDouble(lon2),0);
	}

	public Point3D getP1() {
		return p1;
	}
	
	public Point3D getP2() {
		return p2;
	}

}
