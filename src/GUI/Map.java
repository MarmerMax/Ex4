package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class for create map.
 * @author Max Marmer
 *
 */
public class Map {

	private BufferedImage image;

	private Double  widthPercent, heightPercent;
	private Integer width, height, startHeight, startWidth;

	/**
	 * Class constructor.
	 */
	public Map() {
		try {
			image = ImageIO.read(new File("data\\Ariel1.png"));
			initialization();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function create map and check right size of window.
	 */
	private void initialization() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(screenSize.getWidth() < image.getWidth()) {
			widthPercent = screenSize.getWidth() /  image.getWidth();
			startWidth = (int)screenSize.getWidth();
		}
		else {
			widthPercent = 1.0;
			startWidth = image.getWidth();
		}
		if(screenSize.getHeight() < image.getHeight()) {
			heightPercent = screenSize.getHeight() / image.getHeight();
			startHeight = (int)screenSize.getHeight() + 50;
		}
		else {
			heightPercent = 1.0;
			startHeight = image.getHeight();
		}
		
		height = startHeight;
		width = startWidth;
	}

//	public void setStartSize(int height, int width) {
//		if(startHeight == null & startWidth == null) {
//			startHeight = height;
//			startWidth = width;
//		}
//	}
	/**
	 * This function check percent of after change frame window size.
	 * @param width
	 * @param height
	 */
	public void windowResize(int width, int height) {
		widthPercent = (double)width / startWidth;
		heightPercent = (double)height / startHeight;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public Double getWidthPercent() {
		return widthPercent;
	}

	public void setWidthPercent(Double widthPercent) {
		this.widthPercent = widthPercent;
	}

	public Double getHeightPercent() {
		return heightPercent;
	}

	public void setHeightPercent(Double heightPercent) {
		this.heightPercent = heightPercent;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(Integer startHeight) {
		this.startHeight = startHeight;
	}

	public Integer getStartWidth() {
		return startWidth;
	}

	public void setStartWidth(Integer startWidth) {
		this.startWidth = startWidth;
	}

	/**
	 * Get image.
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * This method convert from lat, lon to pixel x,y for this map.
	 * @param lat
	 * @param lon
	 * @return array of x, y in pixel
	 */
	public int[] fromLatLonToPixel(double lat, double lon) {
		double mapHeight = startHeight;
		double mapWidth = startWidth;
		double mapLatBottom = 32.101898;
		double mapLngLeft = 35.202369;
		double mapLngRight = 35.212416;
		double mapLatBottomRad = mapLatBottom * Math.PI / 180;
		double latitudeRad = lat * Math.PI / 180;
		double mapLngDelta = (mapLngRight - mapLngLeft);
		double worldMapWidth = ((mapWidth / mapLngDelta) * 360) / (2 * Math.PI);
		double mapOffsetY = (worldMapWidth / 2 * Math.log((1 + Math.sin(mapLatBottomRad))
				/ (1 - Math.sin(mapLatBottomRad))));
		double x = (lon - mapLngLeft) * (mapWidth / mapLngDelta);
		double y = mapHeight - ((worldMapWidth / 2 * Math.log((1 + Math.sin(latitudeRad)) 
				/ (1 - Math.sin(latitudeRad)))) - mapOffsetY);
		int [] pixelXY = new int[2];
		pixelXY[0] = (int)x;
		pixelXY[1] = (int)y;
		return pixelXY;
	}

	/**
	 * This method convert from x,y in pixel to lat, lon in degrees for this map.
	 * @param x x in pixel
	 * @param y y in pixel
	 * @return
	 */
	public double[] fromPixelToLatLon(int x, int y) {
		double [] degreesLatlon = new double[2];
		double xStep = (35.212416 - 35.202369) / startWidth; 
		double yStep = (32.105728 - 32.101898) / startHeight;
		degreesLatlon[0] = 32.105728 - (yStep * y);
		degreesLatlon[1] = 35.202369 + (xStep * x);
		return degreesLatlon;
	}

}
