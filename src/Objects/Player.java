package Objects;

/**
 * Class for represent player in project.
 * @author Max Marmer
 *
 */
public class Player extends GameObject{
	
	private double score;
	
	/**
	 * Constructor.
	 * @param lat latitude
	 * @param lon longitude
	 * @param id index
	 */
	public Player(String lat, String lon, String id) {
		super(lat,lon, id);
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	public void addScore() {
		score++;
	}
}
