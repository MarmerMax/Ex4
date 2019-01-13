package Game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import GUI.Map;
import Geom.Point3D;
import Objects.Player;

class GameTest {
	
	Game game;
	Point3D point;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		game = new Game();
		point = new Point3D(10,10);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGame() {
		boolean flag = true;
		if(game == null) {
			flag = false;
		}
		assertTrue(flag);
	}

	@Test
	void testCreatePlayer() {
		boolean flag = true;
		game.createPlayer(point);
		if(game.getPlayer() == null) {
			flag = false;
		}
		assertTrue(flag);
	}

	@Test
	void testGetPlayer() {
		boolean flag = true;
		game.createPlayer(point);
		Player player = game.getPlayer();
		if(player == null) {
			flag = false;
		}
		assertTrue(flag);
	}

	@Test
	void testGetMap() {
		Map map = new Map();
		game.setMap(map);
		boolean flag = true;
		if(game.getMap() == null) {
			flag = false;
		}
		assertTrue(flag);
	}

}
