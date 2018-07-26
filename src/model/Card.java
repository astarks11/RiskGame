package model;
/*
 * Class: Card
 * 
 * Authors: Alex Starks
 * 
 * Description:
 * This class represents a card object. It contians a territory
 * and piece type. 
 */
import java.util.List;

public class Card {
	
	// -------------------------------------
	// instance variables
	// -------------------------------------
	private int territoryID; // the id of the territory
	private Army piece; // the type of army piece
	
	
	/* -------------------------------------------
	 * Constructor: Card
	 * Purpose: Creates a Card object
	 * ------------------------------------------ */
	public Card(int territoryID, Army type)
	{
		this.territoryID = territoryID;
		this.piece = type;
	}
	
	// getter
	public Army getType() { return piece; }
	public int getTerritoryID() { return territoryID; }
	

}
