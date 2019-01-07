package Objects;

public class Pacman extends GameObject{
	
	private double score = 0;

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
