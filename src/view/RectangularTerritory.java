package view;
/*
 * Class: RectangularTerritory
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class is the base class for rectangular classes. It extends TerritoryShape.
 */
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import model.Territory;

public class RectangularTerritory extends TerritoryShape{
	
	private int borderSize = 15;

	public RectangularTerritory(Territory territory, Color border, Point p1, Point p2){
		super(territory,border, p1, p2);
		addShape(new Rectangle(getP1().x,getP1().y,getP2().x,getP2().y));
		addShape(new Rectangle(getP1().x+borderSize/2,getP1().y+borderSize/2,getP2().x-borderSize,getP2().y-borderSize));	
	}
	

}
