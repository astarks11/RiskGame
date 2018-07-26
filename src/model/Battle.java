package model;
/*
 * Class: Battle
 * 
 * Authors: Matthew Drake, Alex Starks
 * 
 * Description:
 * This class allows users to battle and take over territories.
 * Engage carnage needs to be called multiple times for one
 * battle.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private Territory attacker; // attacking territory
	private Territory defender; // defending territory
	private int attackerDice1; // attacker dice number 1
	private int attackerDice2; // attacker dice number 2
	private int attackerDice3; // attacker dice number 3
	private int defenderDice1; // defender dice number 1
	private int defenderDice2; // defender dice number 1
	private List<Integer> attackerDie; // list of attackerDie
	private List<Integer> defenderDie; // list of defenderDie
	private Player ownerA, ownerD;
	
	
	/* ------------------------------------------------
	 * Constructor: Battle()
	 * Purpose: To have the battle and initialize the instance variables.
	 * The dice are automatically set to 0, only the generator will replace value
	 * ------------------------------------------------- */
	public Battle(Territory a, Territory d)
	{
		attackerDie = new ArrayList<Integer>();
		defenderDie = new ArrayList<Integer>();
		attacker = a;
		ownerA = a.getOwner();
		defender = d;
		ownerD = d.getOwner();
		attackerDice1 = 0;
		attackerDice2 = 0;
		attackerDice3 = 0;
		defenderDice1 = 0;
		defenderDice2 = 0;
	}
	
	
	/* ------------------------------------------------
	 * Method: engageCarnage
	 * Purpose: This method generates results of a RISK game battle.
	 * 
	 * NOTES: returns false if territories are owned by same
	 * returns false if attacker has only 1
	 * ------------------------------------------------ */
	public boolean engageCarnage(int aDiceAmount, int dDiceAmount, int armiesToAdd) 
	{
		// reset dice rolls from previous roll
		attackerDice1 = 0;
		attackerDice2 = 0;
		attackerDice3 = 0;
		defenderDice1 = 0;
		defenderDice2 = 0;
		
		// check if battle is possible
		if (attacker.getOwner() == defender.getOwner())
		{
			//System.out.println(attacker.getOwner().getColor() + " - " + defender.getOwner().getColor());
			
			return false;
		}
		if (attacker.getArmySize() < 2)
		{
			return false;
		}
		Random rand = new Random(); // random number generator used to simulate dice rolls
		// ------------------------------ generate dice results ---------------------------------------
		for (int i = 0; i < aDiceAmount; i++)
		{
			int roll = rand.nextInt(6) + 1;
			attackerDie.add(roll);
			if (i == 0)
				attackerDice1 = roll;
			if (i == 1)
				attackerDice2 = roll;
			if (i == 2)
				attackerDice3 = roll;
		}
		
		for (int i = 0; i < dDiceAmount; i++) 
		{
			int roll = rand.nextInt(6) + 1;
			defenderDie.add(roll);
			if (i == 0)
				defenderDice1 = roll;
			if (i == 1)
				defenderDice2 = roll;
		}
		// ------------------------------ generate dice results ---------------------------------------

		
		
	

		// highest attacker dice
		Integer aHighest = new Integer(0);
		for (Integer in : attackerDie)
		{
			if (in.intValue() > aHighest.intValue())
				aHighest = in;
		}
		attackerDie.remove(aHighest);
		
		// get second highest dice
		Integer aSecondHighest = new Integer(0);
		if (attackerDie.size() > 0)
		{
			for (Integer in : attackerDie)
			{
				if (in.intValue() > aSecondHighest.intValue())
					aSecondHighest = in;
			}		
		}
		
		
		// highest defender dice
		Integer dHighest = new Integer(0);
		for (Integer in : defenderDie)
		{
			if (in.intValue() > dHighest.intValue())
				dHighest = in;
		}
		defenderDie.remove(dHighest);
		
		// get second highest dice
		Integer dSecondHighest = new Integer(0);
		if (defenderDie.size() > 0)
		{
			for (Integer in : defenderDie)
			{
				if (in.intValue() > dSecondHighest.intValue())
					dSecondHighest = in;
			}		
		}
		
		
		
		
		
		
		// ----------------------- determine winners & exchange ---------------------------

		if (aHighest.intValue() > dHighest.intValue()){//a won
			defender.decrementArmy();
		} else {//defender won
			attacker.decrementArmy();
		}
		
		if (dSecondHighest.intValue() != 0 && aSecondHighest.intValue() != 0)
		{
			if (aSecondHighest.intValue() > dSecondHighest.intValue()){
				defender.decrementArmy();
			} else {
				attacker.decrementArmy();
			}
		}
		// ----------------------- determine winners & exchange ---------------------------
		if(defender.getArmySize() == 0)
		{
			defender.setOwner(attacker.getOwner());
			attacker.getOwner().addControlledTerritory(defender);
			
			defender.addArmies(armiesToAdd);
			attacker.addArmies(-armiesToAdd);
			//defender.getOwner().addControlledTerritory(defender);
			
		}
		return true;
	}
	
	// getter
	public int getAttackerDice1() { return attackerDice1; }
	public int getAttackerDice2() { return attackerDice2; }
	public int getAttackerDice3() { return attackerDice3; }
	public int getDefenderDice1() { return defenderDice1; }
	public int getDefenderDice2() { return defenderDice2; }


}
