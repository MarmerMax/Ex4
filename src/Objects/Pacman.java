package Objects;

/**
 * Class for represent pacman in project.
 * @author Max Marmer
 *
 */
public class Pacman extends GameObject{
	
	private double score = 0;

	/**
	 * Constructor
	 * @param lat latitude
	 * @param lon longitude
	 * @param id index
	 */
	public Pacman(String lat, String lon, String id) {
		super(lat, lon, id);
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
