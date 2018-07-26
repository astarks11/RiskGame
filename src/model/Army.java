package model;
/*
 * Class: Army
 * 
 * Authors: Alex Starks
 * 
 * Description:
 * This class is an enum that returns the types of cards
 */

public enum Army {
	// declare enums
	INFANTRY, CALVARY, ARTILLERY, WILDCARD;
	
	public String toString(){
		if(this == INFANTRY)
			return "Infantry";
		if(this == CALVARY)
			return "Calvary";
		if(this == ARTILLERY)
			return "Artillery";
		else
			return "Wildcard";
	}
};
