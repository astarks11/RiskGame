package model;
/*
 * Class: RiskGUI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class is used for the AI's to be able to talk to the game, about which territory it wants to 
 * attack and from which territory it would like to attack 
 */
public class Move
{

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private Territory attackingTerritory, defendingTerritory; // territories needed for moves
	
	
	
	/* ---------------------------------------------------------------------------------------
	 * Constructor: Move(Terr, int, Terr, int)
	 * Purpose: This constructor takes an attacking territory, the number of dice it will attack with
	 * and defending territory, the number of defending dice and sets the instance variables.
	 * ------------------------------------------------------------------------------------- */

	public Move(Territory attackingTerritory, Territory defendingTerritory)
	{
		this.attackingTerritory = attackingTerritory;
		this.defendingTerritory = defendingTerritory;
		
	}
	
	
	// getters
	public Territory getAttackingTerritory() { return attackingTerritory; }
	public Territory getDefendingTerritory() { return defendingTerritory; }
	
}
