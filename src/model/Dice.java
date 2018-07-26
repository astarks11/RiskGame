package model;
/*
 * Class: Dice
 * 
 * Authors: Alex Starks
 * 
 * Description:
 * This class represents dice, and will return what each dice rolled.
 */

import java.util.Random;

public class Dice {
	
	private int dice1; // represents a dice
	private int dice2; // represents a dice
	private int dice3; // represents a dice
	private int dice4; // represents a dice
	private int dice5; // represents a dice
	private int dice6; // represents a dice
	private int playerDice; // represents a dice
	
	
	/* -------------------------------------------------------------
	 * Constructor: Dice
	 * Purpose: This constructor initializes the dice variables
	 * to zero.
	 * ----------------------------------------------------------- */
	public Dice()
	{
		dice1 = 0;
		dice2 = 0;
		dice3 = 0;
		dice4 = 0;
		dice5 = 0;
		dice6 = 0;
		playerDice = 0;
	}
	
	/* ------------------------------------------------------------
	 * Method: rollDie()
	 * Purpose: This method uses javas random class to set every
	 * dice to a random number.
	 * ------------------------------------------------------------ */
	public void rollDie()
	{
		Random rand = new Random();
		// reset dice integers from previous roll
		dice1 = 0;
		dice2 = 0;
		dice3 = 0;
		dice4 = 0;
		dice5 = 0;
		dice6 = 0;
		playerDice = 0;
		
		// set dice numbers
		dice1 = rand.nextInt(6)+1;
		dice2 = rand.nextInt(6)+1;
		dice3 = rand.nextInt(6)+1;
		dice4 = rand.nextInt(6)+1;
		dice5 = rand.nextInt(6)+1;
		dice6 = rand.nextInt(6)+1;
		playerDice = rand.nextInt(6)+1;
	}
	
	public int getDice1() { return dice1; }
	public int getDice2() { return dice2; }
	public int getDice3() { return dice3; }
	public int getDice4() { return dice4; }
	public int getDice5() { return dice5; }
	public int getDice6() { return dice6; }
	public int getPlayerDice() { return playerDice; }
	
	
	
	
	
	

}
