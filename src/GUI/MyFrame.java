package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Algo.GameAlgo;
import Coords.MyCoords;
import Game.Game;
import Geom.Point3D;
import Objects.Pacman;
import Objects.Ghost;
import Objects.Fruit;
import Objects.Box;
import Objects.Player;
import Robot.Play;



public class MyFrame extends JFrame implements MouseListener{

	private Image dbImage;
	private Graphics dbg;
	public BufferedImage background;
	private int height, width;
//	private int height, width, startHeight, startWidth;
//	private double heightPercent, widthPercent;

	private Game game;
	private Play play;
	private Map map;

	private boolean createPacman = false;
	private int readyToStart = 0; //0 - need to chose file and create player, 1 = need to create player, 2 - ready
	private boolean gameRunning = false;
	
	private int x, y;
	private boolean doRotate = false;
	private double dir = 0;
	

	

	public MyFrame() {
		createGUI();
		this.addMouseListener(this);
	}

	public void createGUI(){
		checkBackground();

		setVisible(true);
		setSize(map.getStartWidth(), map.getStartHeight() + 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
	}

//	private void windowResize() {
//		widthPercent = (double)width / startWidth;
//		heightPercent = (double)height / startHeight;
//	}
	
	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg =dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

	private void paintComponent(Graphics g) {
		g.drawImage(background, 8, 50, width, height, this);
		if(game != null) {
			if(game.getPacmanList() != null) {
				drawPacman(g);
			}
			if(game.getGhostList() != null) {
				drawGhost(g);
			}
			if(game.getFruitList() != null) {
				drawFruit(g);
			}
			if(game.getBoxList() != null) {
				drawBox(g);
			}
			if(game.getPlayer() != null) {
				drawPlayer(g);
			}
		}
		repaint();
	}

	private void drawPacman(Graphics g) {
		int index = 0;
		while(index < game.getPacmanList().size()) {
			Pacman temp = game.getPacmanList().get(index);
			int [] coor = map.fromLatLonToPixel(temp.getPoint().x(), temp.getPoint().y());
			int xP = (int)((double)coor[0] * map.getWidthPercent());
			int yP = (int)((double)coor[1] * map.getHeightPercent());
			int dX = (int)(10.0 * map.getWidthPercent());
			int dY = (int)(10.0 * map.getHeightPercent());
			int imgSize = (int)(20.0 * ((map.getWidthPercent() + map.getHeightPercent()) / 2));
			g.setColor(Color.YELLOW);
			g.fillOval(8 + xP - dX, 50 + yP - dY, imgSize, imgSize);
			index++;
		}
	}

	private void drawGhost(Graphics g) {
		int index = 0;
		while(index < game.getGhostList().size()) {
			Ghost temp = game.getGhostList().get(index);
			int [] coor = map.fromLatLonToPixel(temp.getPoint().x(), temp.getPoint().y());
			int xP = (int)((double)coor[0] * map.getWidthPercent());
			int yP = (int)((double)coor[1] * map.getHeightPercent());
			int dX = (int)(15.0 * map.getWidthPercent());
			int dY = (int)(15.0 * map.getHeightPercent());
			int imgSize = (int)(30.0 * ((map.getWidthPercent() + map.getHeightPercent()) / 2));
//			int xP = (int)((double)coor[0] * widthPercent);
//			int yP = (int)((double)coor[1] * heightPercent);
//			int dX = (int)(15.0 * widthPercent);
//			int dY = (int)(15.0 * heightPercent);
//			int imgSize = (int)(30.0 * ((widthPercent + heightPercent) / 2));
			g.setColor(Color.RED);
			g.fillOval(8 + xP - dX, 50 + yP - dY, imgSize, imgSize);
			index++;
		}
	}

	private void drawFruit(Graphics g) {
		int index = 0;
		while(index < game.getFruitList().size()) {
			Fruit temp = game.getFruitList().get(index);
			int [] coor = map.fromLatLonToPixel(temp.getPoint().x(), temp.getPoint().y());
			int xP = (int)((double)coor[0] * map.getWidthPercent());
			int yP = (int)((double)coor[1] * map.getHeightPercent());
			int dX = (int)(5.0 * map.getWidthPercent());
			int dY = (int)(5.0 * map.getHeightPercent());
			int imgSize = (int)(10.0 * ((map.getWidthPercent() + map.getHeightPercent()) / 2));
//			int xP = (int)((double)coor[0] * widthPercent);
//			int yP = (int)((double)coor[1] * heightPercent);
//			int dX = (int)(5.0 * widthPercent);
//			int dY = (int)(5.0 * heightPercent);
//			int imgSize = (int)(10.0 * ((widthPercent + heightPercent) / 2));
			g.setColor(Color.GREEN);
			g.fillOval(8 + xP - dX, 50 + yP - dY, imgSize, imgSize);
			index++;
		}
	}

	private void drawPlayer(Graphics g) {
		if(game.getPlayer() != null) {
			Player temp = game.getPlayer();
			int [] coor = map.fromLatLonToPixel(temp.getPoint().x(), temp.getPoint().y());
			int xP = (int)((double)coor[0] * map.getWidthPercent());
			int yP = (int)((double)coor[1] * map.getHeightPercent());
			int dX = (int)(17.5 * map.getWidthPercent());
			int dY = (int)(17.5 * map.getHeightPercent());
			int imgSize = (int)(35.0 * ((map.getWidthPercent() + map.getHeightPercent()) / 2));
//			int xP = (int)((double)coor[0] * widthPercent);
//			int yP = (int)((double)coor[1] * heightPercent);
//			int dX = (int)(17.5 * widthPercent);
//			int dY = (int)(17.5 * heightPercent);
//			int imgSize = (int)(35.0 * ((widthPercent + heightPercent) / 2));
			g.setColor(Color.PINK);
			g.fillOval(8  + xP - dX, yP - dY + 50, imgSize, imgSize);
		}
	}

	private void drawBox(Graphics g) {
		int index = 0;
		while(index < game.getBoxList().size()) {
			Box temp = game.getBoxList().get(index);
			int [] coor1 = map.fromLatLonToPixel(temp.getP1().x(), temp.getP1().y());
			int [] coor2 = map.fromLatLonToPixel(temp.getP2().x(), temp.getP2().y());

			int xP = (int)((double)coor1[0] * map.getWidthPercent());//start x
			int yP = (int)((double)coor2[1] * map.getHeightPercent());//start y

			int widthRect = (int)((double)Math.abs(coor1[0] - coor2[0]) * map.getWidthPercent());//width length
			int heightRect = (int)((double)Math.abs(coor1[1] - coor2[1]) * map.getHeightPercent());//height length

			g.setColor(Color.BLACK);
			g.fillRect(8 + xP, 50 + yP, widthRect, heightRect);
			index++;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		if(readyToStart == 2 && createPacman) {
			x = (int)(arg.getX());//x by click
			y = (int)(arg.getY());//y by click
			boolean notBox = checkPlace(x, y);
			if(notBox) {
				int xP = (int)((double)x * (Math.pow(map.getWidthPercent(), -1))); //change x to actually size
				int yP = (int)((double)(y - 50) * (Math.pow(map.getHeightPercent(), -1))); //change y to actually size

				double [] playerCoordinates = map.fromPixelToLatLon(xP, yP);

				//create player
				game.createPlayer(playerCoordinates);

				//add player to game
				play.setInitLocation(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y());

				repaint();
			}
			else {
				String msg = "Wrong place! Please try again!";
				JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (doRotate) {
			x = (int)(arg.getX());//x by click
			y = (int)(arg.getY());//y by click
			int xP = (int)((double)x * (Math.pow(map.getHeightPercent(), -1))); //change x to actually size
			int yP = (int)((double)(y - 50) * (Math.pow(map.getWidthPercent(), -1))); //change y to actually size

			double [] toGoCoordinates = map.fromPixelToLatLon(xP, yP);
			Point3D temp = new Point3D(toGoCoordinates[0], toGoCoordinates[1], 0);
			MyCoords azimuth = new MyCoords();
			double [] AED = azimuth.azimuth_elevation_dist(game.getPlayer().getPoint(), temp);
			System.out.println(AED[0]);
			dir = AED[0];
			play.rotate(dir); 
		}
		else if (readyToStart == 0) {
			String msg = "Please first choose the game file";
			JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String msg = "Please choose object to create";
			JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean checkPlace(int x, int y) {


		return true;
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	private String readFileDialog() {
		//try read from the file
		FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
		fd.setFile("*.csv");
		fd.setDirectory("data\\");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});
		fd.setVisible(true);
		String folder = fd.getDirectory();
		String fileName = fd.getFile();
		return folder + fileName;
	}

//	private double[] fromPixelToLatLon(int x, int y) {
//		double [] xy = new double[2];
//		double xStep = (35.212416 - 35.202369) / startWidth; 
//		double yStep = (32.105728 - 32.101898) / startHeight;
//		xy[0] = 32.105728 - (yStep * y);
//		xy[1] = 35.202369 + (xStep * x);
//		return xy;
//	}
//
//	private int[] fromLatLonToPixel(double latitude, double longitude) {
//		double mapHeight = startHeight;
//		double mapWidth = startWidth;
//		double mapLatBottom = 32.101898;
//		double mapLngLeft = 35.202369;
//		double mapLngRight = 35.212416;
//		double mapLatBottomRad = mapLatBottom * Math.PI / 180;
//		double latitudeRad = latitude * Math.PI / 180;
//		double mapLngDelta = (mapLngRight - mapLngLeft);
//		double worldMapWidth = ((mapWidth / mapLngDelta) * 360) / (2 * Math.PI);
//		double mapOffsetY = (worldMapWidth / 2 * Math.log((1 + Math.sin(mapLatBottomRad))
//				/ (1 - Math.sin(mapLatBottomRad))));
//		double x = (longitude - mapLngLeft) * (mapWidth / mapLngDelta);
//		double y = mapHeight - ((worldMapWidth / 2 * Math.log((1 + Math.sin(latitudeRad)) 
//				/ (1 - Math.sin(latitudeRad)))) - mapOffsetY);
//		int [] cc = new int[2];
//		cc[0] = (int)x;
//		cc[1] = (int)y;
//		return cc;
//	}
	
//	class RepaintThread implements Runnable{
//		@Override
//		public void run() {
//			while(gameRunning) {
//				try {
//					repaint();
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}	
//	}
	
	private void checkBackground() {
		map = new Map();
		background = map.getImage();
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		if(screenSize.getWidth() < background.getWidth()) {
//			widthPercent = screenSize.getWidth() /  background.getWidth();
//			startWidth = (int)screenSize.getWidth();
//		}
//		else {
//			widthPercent = 1;
//			startWidth = background.getWidth();
//		}
//		if(screenSize.getHeight() < background.getHeight()) {
//			heightPercent = screenSize.getHeight() / background.getHeight();
//			startHeight = (int)screenSize.getHeight() + 50;
//		}
//		else {
//			heightPercent = 1;
//			startHeight = background.getHeight();
//		}
		
//		map.setStartSize(startHeight, startWidth);
		
		
//		height = startHeight;
//		width = startWidth;
	}
	
	private void createMenu() {
		MenuBar menuBar = new MenuBar();
		//file menu 
		Menu file = new Menu("File");
		MenuItem fileItemOpen = new MenuItem("Open");
		MenuItem fileItemSave = new MenuItem("Save");
		MenuItem fileItemExit = new MenuItem("Exit");
		file.add(fileItemOpen);
		file.add(fileItemSave);
		file.add(fileItemExit);
		menuBar.add(file);

		//game menu
		Menu create = new Menu("Create");
		MenuItem createItemPlayer = new MenuItem("Player");
		create.add(createItemPlayer);
		menuBar.add(create);

		Menu game1 = new Menu("Game");
		MenuItem game1ItemStart = new MenuItem("Start");
		MenuItem game1ItemStartAuto = new MenuItem("Auto Start");
		game1.add(game1ItemStart);
		game1.add(game1ItemStartAuto);
		menuBar.add(game1);	

		this.setMenuBar(menuBar);


		/////////////////////file menu///////////////////////
		fileItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = readFileDialog();
				game = new Game();
				game.setMap(map);
				play = new Play(fileName);
				play.setIDs(1);
				game.initialization(play.getBoard());				
				repaint();
				readyToStart = 1;
			}
		});

		fileItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});

		/////////////////////create menu/////////////////////
		createItemPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(readyToStart == 1) {
					createPacman = true;
					readyToStart = 2;
				}
				else if (readyToStart == 0){
					String msg = "Please first choose the game file";
					JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		/////////////////////game menu///////////////////////
		game1ItemStartAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(readyToStart == 1) {
					createPacman=false;
					
					int xP = (int)((double)1 * (Math.pow(map.getWidthPercent(), -1))); //change x to actually size
					int yP = (int)((double)1 * (Math.pow(map.getHeightPercent(), -1))); //change y to actually size

					double [] playerCoordinates = map.fromPixelToLatLon(xP, yP);

					//create player
					game.createPlayer(playerCoordinates);

					//add player to game
					play.setInitLocation(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y());
					
					play.start();
					Thread gamePlay = new Thread(new Runnable() {
						@Override
						public void run() {
							play();
						}
						private void play() {
							GameAlgo algo = new GameAlgo(game);
							while(play.isRuning()) {
								play.setInitLocation(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y());
								
								game.initialization(play.getBoard());
								
								algo.initialization();
								
								play.rotate(algo.getDir());
								try {
									Thread.sleep(30);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								repaint();
							}
							gameRunning = false;
							String info = play.getStatistics();
							System.out.println(info);
						}
					});
					gamePlay.start();
					readyToStart = 0;
				}
				else {
					String msg = "Please first choose the game file and create Player";
					JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		game1ItemStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(game.getPlayer() != null) {
					createPacman=false;
					doRotate = true;
					play.start();
					Thread gamePlay = new Thread(new Runnable() {
						@Override
						public void run() {
							play();
						}
						private void play() {
							while(play.isRuning()) {
								game.initialization(play.getBoard());
								play.setInitLocation(game.getPlayer().getPoint().x(), game.getPlayer().getPoint().y());
								play.rotate(dir);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
//								ArrayList<String> board_data = new ArrayList<>();
//								board_data = play.getBoard();
								//System.out.println(board_data.get(0));
								repaint();
							}
							gameRunning = false;
							String info = play.getStatistics();
							System.out.println(info);
						}
					});
					gamePlay.start();
					readyToStart = 0;
				}
				else {
					String msg = "Please first choose the game file and create Player";
					JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		//////////////////////////window frame///////////////////////
		getContentPane().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Component c = (Component)e.getComponent();
				width = c.getWidth();
				height = c.getHeight();
	//			windowResize();
				map.windowResize(width, height);
				repaint();
			}
		});	
	}
}