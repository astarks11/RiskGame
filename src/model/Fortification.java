package model;
/*
 * Class: IntermediateAI
 * 
 * Authors: Matthew Drake, Alex Starks
 * 
 * Description:
 * This class contains the information necessary for fortifications to occur. AIs use
 * this to move from one territory to another and return the amount of armies it wants
 * to move
 */
public class Fortification
{

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private Player player; // player making move
	private Territory terr1; // territory that supplies army
	private Territory terr2; // territory that gets fortified
	private int numOfArmy; // number of army to fortify
	
	/* -----------------------------------
	 * constructor: Fortification
	 * Purpose: This constructor creates the fortification
	 * object and sets the instance variables.
	 * ----------------------------------- */
	public Fortification(Player player, Territory terr1, Territory terr2, int numOfArmy)
	{
		this.player = player;
		this.terr1 = terr1;
		this.terr2 = terr2;
		this.numOfArmy = numOfArmy;
	}
	
	/* --------------------------------------------------------
	 * Method: fortify()
	 * Purpose: This method moves the given amount of army from
	 * the first territory to the second territory. This
	 * is achieved if the first territory will have more than 
	 * 1 army after the move, if the two territories are owned by the player,
	 * and if the territories are neighbors. Otherwise false is returned.
	 * --------------------------------------------------------- */
	public boolean fortify2()
	{
		if (player.ownsTerritory(terr1) && player.ownsTerritory(terr2))
		{
			if ((terr1.getArmySize() - numOfArmy) >= 1)
			{
				if (terr1.isNeighbor(terr2)) {
					for (int i = 0; i < numOfArmy; i++)
					{
						terr1.decrementArmy();
						terr2.addArmy(player);
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean fortify(){
		terr1.subArmies(numOfArmy);
		terr2.addArmies(numOfArmy);
		return false;
	}
}