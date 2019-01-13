package Objects;

/**
 * Class for represent ghost in project.
 * @author Max Marmer
 *
 */
public class Ghost extends GameObject{

	/**
	 * Constructor
	 * @param lat latitude
	 * @param lon longitude
	 * @param id index
	 */
	public Ghost(String lat, String lon, String id) {
		super(lat, lon, id);
	}
}
