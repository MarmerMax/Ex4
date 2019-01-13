package Algo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Game.Game;

class GameAlgoTest {
	
	Game game;
	GameAlgo algo;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		game = new Game();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGameAlgo() {
		boolean flag = true;
		algo = new GameAlgo(game);
		if(algo == null) {
			flag = false;
		}
		assertTrue(flag);
	}

	@Test
	void testGetDir() {
		algo = new GameAlgo(game);
		double a;
		a = algo.getDir();
		assertEquals(a, 0);
	}

}
