package model;
/*
 * Class: RiskGUI
 * 
 * Authors: Matthew Drake, Alex Starks
 * 
 * Description:
 * This class holds information for each territory. It lets the user know who 
 * the neighbours are, the owner color and territory ID of the current object.
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import view.*;

/*
 * holds info about territories
 */
public class Territory {

	// -------------------------------------
	// instance variables
	// -------------------------------------
	private int territoryID; // the ID of this territory obect
	private Color ownerColor; // owner of the territory
	private int armySize; // number of armies on territory
	private Player owner; // owner player
	private List<Territory> neighbors; // list of territories neighbors

	/* --------------------------------------------------- 
	 * Constructor: Territory 
	 * Purpose: Set Id to argument. Set owner to null. Set armySize to 0. 
	 * --------------------------------------------------- */
	public Territory(int ID) {
		// set instance variables
		this.territoryID = ID;
		this.owner = null;
		this.armySize = 0;
		this.neighbors = new ArrayList<Territory>();
	}


	public Territory(int ID, Color ownerColor) {
		// set instance variables
		this.ownerColor = ownerColor;
		this.territoryID = ID;
		this.owner = null;
		this.armySize = 0;
		this.neighbors = new ArrayList<Territory>();
	}

	/* --------------------------------------------------- 
	 * Method: addArmy()
	 * Purpose: This method increments the size of the army by one if the Color
	 * of the argument is the same Color of the owner. A True return value means
	 * a successful add. A false means the colors were not the same.
	 * ------------------------------------------------- */
	public boolean addArmy(Player player) {

		// check if player matches owner or if null

		if (owner == player) {

			// decrement players army
			if (player.decrementArmy()) {
				/*if (ownerColor == null) {
					player.addControlledTerritory(this);
					owner = player;
				}*/
				// if decremented successfully, increment territory size
				armySize++;
				ownerColor = player.getColor();
				return true;
			}
			return false;
		}
		return false;
	}



	public void decrementArmy() {
		// decrement army size

		armySize--;
		//owner.addArmy();
		// when army size hits zero, set owner to null
		// set armySize to 0 to avoid negative values
		if (armySize <= 0) {
			owner.removeControlledTerritory(this);
			ownerColor = Map.getDefaultColor();
			owner = null;
			armySize = 0;
		}
	}
	

	/*
	 * --------------------------------------------------- Method: isNeighbor()
	 * Purpose: returns true if arguments territory ID is the same as any of the
	 * ID's in the neighbors list
	 * --------------------------------------------------
	 */
	public boolean isNeighbor(Territory terr) {
		for (Territory t : neighbors) {
			if (t.getID() == terr.getID())
				return true;
		}
		return false;
	}

	// ----------------------------------------------------
	// setters
	// ----------------------------------------------------

	public void addNeighbor(Territory terr) {
		neighbors.add(terr);
	}

	// ----------------------------------------------------
	// getters
	// ----------------------------------------------------
	public int getID() {
		return territoryID;
	}

	public Color getOwnerColor() {
		if(owner != null)
			return owner.getColor();
		return ownerColor;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player p) {
		owner = p;
		//p.addControlledTerritory(this); do not uncomment, don't forget to add this when switching owners
	}

	public int getArmySize() {
		return armySize;
	}

	public List<Territory> getNeighbors() {
		return neighbors;
	}

	public void subArmies(int armiesToSub){
		armySize -= armiesToSub;
	}
	
	public void addArmies(int armiesToAdd) {
		armySize += armiesToAdd;
		
	}
	
	public String toString(){
		return "owened by: " + owner.getColor() + ", armysize: "+ armySize + ", id: " + territoryID;
	}
	

}
