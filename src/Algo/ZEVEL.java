package Algo;

import java.awt.geom.Line2D;

public class ZEVEL {
	
	private int[] intersectionPoint(Line2D line1, Line2D line2) {
		//System.out.println(line1.getP1().toString() + " ----> " + line1.getP2().toString());
		//System.out.println(line2.getP1().toString() + " ----> " + line2.getP2().toString());
		double p1x, p1y, p2x, p2y, q1x, q1y, q2x, q2y;
		p1x = line1.getX1();
		p1y = line1.getY1();
		p2x = line1.getX2();
		p2y = line1.getY2();

		q1x = line2.getX1();
		q1y = line2.getY1();
		q2x = line2.getX2();
		q2y = line2.getY2();

		double a1 = p2y - p1y;
		double b1 = p1x - p2x;
		double c1 = a1 * p1x + b1 * p1y;

		double a2 = q2y - q1y;
		double b2 = q1x - q2x;
		double c2 = a2 * q2x + b2 * q2y;

		double delta = a1 * b2 - a2 * b1;

		int [] arr = {(int)((b2 * c1 - b1 * c2) / delta), (int)((a1 * c2 - a2 * c1) / delta)};

		return arr;
	}
	

	//	private void findAnglesOutOffGame() {
	//		String [] arr = new String[anglesList.size()];
	//
	//		for(int i = 0; i < anglesList.size(); i++) {
	//			for(int j = 0; j < anglesList.size(); j++) {
	//				if(i != j) {
	//					int [] a = game.getMap().fromLatLonToPixel(anglesList.get(j).get(0).x(), anglesList.get(j).get(0).y());
	//					int [] b = game.getMap().fromLatLonToPixel(anglesList.get(j).get(1).x(), anglesList.get(j).get(1).y());
	//					int [] c = game.getMap().fromLatLonToPixel(anglesList.get(j).get(2).x(), anglesList.get(j).get(2).y());
	//					int [] d = game.getMap().fromLatLonToPixel(anglesList.get(j).get(3).x(), anglesList.get(j).get(3).y());
	//
	//					for(int k = 0; k < 4; k++) {
	//						int [] x = game.getMap().fromLatLonToPixel(anglesList.get(i).get(k).x(), anglesList.get(i).get(k).y());
	//
	//						boolean flag = thisPointInRectangle(a[0], a[1], b[0], b[1], c[0], c[1], d[0], d[1], x[0], x[1]);
	//						if(flag) {
	//							if(k == 3) {
	//								arr[i] = k + "";
	//							}
	//							else {
	//								arr[i] = k + ",";	
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}

	private float area(int x1, int y1, int x2, int y2, int x3, int y3) { 
		return (float)Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0); 
	} 


	private boolean thisPointInRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int x, int y) { 
		float A = area(x1, y1, x2, y2, x3, y3) + area(x1, y1, x4, y4, x3, y3); 
		float A1 = area(x, y, x1, y1, x2, y2);
		float A2 = area(x, y, x2, y2, x3, y3);
		float A3 = area(x, y, x3, y3, x4, y4); 
		float A4 = area(x, y, x1, y1, x4, y4); 

		return (A == A1 + A2 + A3 + A4); 
	}

}
