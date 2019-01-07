package Objects;

public class Player extends GameObject{
	
	private double score;
	
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
