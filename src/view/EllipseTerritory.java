package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary, Alex Starks
 * 
 * Description:
 * This class is the ellispe territory that extends Territory Shape so it may be drawn. 
 */
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import model.Territory;

public class EllipseTerritory extends TerritoryShape {

	private int borderSize = 15;
	
	public EllipseTerritory(Territory territory, Color border, Point p1, Point p2) {
		super(territory, border, p1, p2);
		addShape(new Ellipse2D.Double(getP1().x,getP1().y,getP2().x,getP2().y));
		addShape(new Ellipse2D.Double(getP1().x+borderSize/2,getP1().y+borderSize/2,getP2().x-borderSize,getP2().y-borderSize));	
	}



}
