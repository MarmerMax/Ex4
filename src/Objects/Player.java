package Objects;

public class Player extends GameObject{
	
	private double score;
	
	public Player(String lat, String lon) {
		super(lat,lon);
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
