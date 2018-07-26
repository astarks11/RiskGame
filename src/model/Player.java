package model;
/*
 * Class: Player
 * 
 * Authors: Matthew Drake, Alex Starks
 * 
 * Description:
 * This class deals with all of the player interaction, it holds the 
 * AI type of the current player, it holds which territories the current
 * player holds and so on.
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {


	// -------------------------------------
	// instance variables
	// -------------------------------------
	private List<Territory> controlled; // territories controlled by player
	private int armyAmount; // number of army
	private AI ai;
	private Color color; // player color
	private List<Card> myCards; // players cards

	/* -------------------------------------------------
	 * Constructor: Player
	 * Purpose: This constructor sets the instance variables
	 * and sets the players color.
	 * ------------------------------------------------- */
	public Player(Color col,AI ai) {//if ai == null then player is human
		this.ai = ai;
		this.controlled = new ArrayList<Territory>();
		this.armyAmount = 0;
		this.color = col;
		this.myCards = new ArrayList<Card>();

	}
	/* -------------------------------------------------
	 * Constructor: Player
	 * Purpose: This constructor sets the instance variables
	 * and sets the players color.
	 * ------------------------------------------------- */
	public Player(Color col) {//if ai == null then player is human
		this.ai = null;
		this.controlled = new ArrayList<Territory>();
		this.armyAmount = 0;
		this.color = col;
		this.myCards = new ArrayList<Card>(); 
	}


	/* --------------------------------------------------
	 * Method: addControlledTerritory
	 * Purpose: This method adds a new territory to the players
	 * owned territories.
	 * -------------------------------------------------- */
	public void addControlledTerritory(Territory terr) {
		terr.setOwner(this);
		controlled.add(terr);
	}

	/* --------------------------------------------------
	 * Method: removeControlledTerritory
	 * Purpose: This method removes the territory from the players
	 * owned territories.
	 * -------------------------------------------------- */
	public void removeControlledTerritory(Territory terr) {
		terr.setOwner(null);
		controlled.remove(terr);
	}

	/* -------------------------------------------------
	 * Method: addCardToPlayersHand()
	 * Purpose: This method adds the card object to
	 * the players hand
	 * ----------------------------------------------- */
	public void addCardToPlayersHand(Card card) {
		myCards.add(card);
	}

	/* ------------------------------------------------
	 * Method: removeCardFromPlayersHand()
	 * Purpose: This method removes the card object from
	 * the players hand
	 * ------------------------------------------------ */
	public void removeCardFromPlayersHand(Card card) {
		myCards.remove(card);
	}

	
	public void claimCard(int index){
		Card card = myCards.get(index);
		if(card.getType() == Army.INFANTRY){
			
		}else if(card.getType() == Army.ARTILLERY){
			
		}else if(card.getType() == Army.CALVARY){
			
		}else{//wildcard
			
		}
		removeCardFromPlayersHand(card);
	}
	/* ----------------------------------------------------
	 * Method: decrementArmy()
	 * Purpose: This method decrements the number of armymen the
	 * player has.
	 * ---------------------------------------------------- */
	public boolean decrementArmy() {
		if (armyAmount > 0)
		{
			armyAmount--;
			return true;
		}
		return false;
	}


	/* ----------------------------------------------------
	 * Method: ownsTerritory()
	 * Purpose: checks if the player owns the given
	 * territory.
	 * -------------------------------------------------- */
	public boolean ownsTerritory(Territory terr)
	{
		for (Territory t : controlled)
		{
			if (t == terr)
				return true;
		}
		return false;
	}

	/* -------------------------------------------------
	 * Method: addArmy()
	 * Purpose: This method adds an army man to the players
	 * army size.
	 * ----------------------------------------------- */
	public void addArmy() 
	{
		armyAmount++;
	}

	/* ---------------------------------------------------
	 * Method: reinforce()
	 * Purpose: This method takes the territory and adds
	 * the amount of army to the territory and returns true if this
	 * has been successfully done. Returns false if owner of territory
	 * is not player in argument, returns false if amount is greater
	 * than players army size.
	 * --------------------------------------------------- */
	public boolean reinforce(Territory terr, int amount)
	{
		if(terr.getOwner() != null && terr.getOwner() != this)
			return false;

		if(amount > armyAmount)
			return false;
		for(int i = 0; i < amount; i++)

		{
			terr.addArmy(this);
			this.decrementArmy();
		}

		return true;
	}

	// setters
	public void setArmy(int num) { armyAmount = num; }
	// getters
	public List<Territory> getControlledTerritories() { return controlled; }
	public Color getColor() { return color; }	
	public int getArmySize() { return armyAmount; }
	public List<Card> getCards() { return myCards; }
	
	/*
	 * This method will need to iterate through the list of player controlled
	 * territories, and for each of those territories, check to see which neighbors
	 * are controlled by enemies, and add it to the list.
	 */
	public ArrayList<Move> getMoves() 
	{ 
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		for (int i = 0; i < controlled.size(); i++)
		{
			if(controlled.get(i).getArmySize() != 1)
			{
				for (int j = 0; j < controlled.get(i).getNeighbors().size(); j++)
				{
					if (controlled.get(i).getNeighbors().get(j).getOwner().getColor() != this.getColor())
					{
						possibleMoves.add(new Move(controlled.get(i),controlled.get(i).getNeighbors().get(j)));
						
					}	
				}
			}
		}
		return possibleMoves; 
	}

	/*
	 * return a boolean whether this player can attack the territory t
	 */
	public boolean canAttack(Territory t){

		for (int i = 0; i < controlled.size(); i++){
			for (int j = 0; j < controlled.get(i).getNeighbors().size(); j++){
				if (controlled.get(i).getNeighbors().get(j) == t && controlled.get(i).getNeighbors().get(j).getOwner() != this){
					return true;
				}	
			}
		}
		return false;
	}


	public AI getAI(){
		return ai;
	}
	public void calculateReinforcements()
	{
		armyAmount += controlled.size();
	}
	
	public String toString(){
		return color.toString();
	}

	public void deployed(){
		armyAmount = 0;
	}


}
