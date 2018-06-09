package com.invaliddomain.jircbot.toys;

import java.util.Random;

public class FlipCoin {
	/*
	 * Coin flip
	 */
	public static String main() {

		String[] coin = {
				"heads",
				"tails"
		};

		Random rand = new Random();

		return String.format("It's %s", coin[(rand.nextInt(coin.length))]);

	}
}
