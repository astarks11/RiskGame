package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class controlls how the user sees the game, it sets the game and arrows.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import controller.RiskGUI;
import model.RiskGame;
/*
 * this class holds the side panel and the map view
 */
public class GameView extends JPanel implements Observer{

	private int width, height;
	private Point start;
	private RiskGUI riskGui;
	private RiskGame rg;
	private Point originalArrowPoint = null;
	private Arrow arrow = null;
	private MapView mv;
	private SidePanel sp;
	private int sidePanelSize;
	private static int sidePanelSizeFactor = 5;
	
	public GameView(Point start,Point size, RiskGame risk,RiskGUI riskGui) {
		this.start = start;
		this.width = size.x;
		this.height = size.y;
		this.riskGui = riskGui;
		rg = risk;
		sidePanelSize = (int)width/sidePanelSizeFactor;
		setLayout(null);
		mv = new MapView(new Point(start.x + sidePanelSize,start.y),new Point(width-sidePanelSize,height),rg);
		mv.setBounds(start.x + sidePanelSize,start.y,width-sidePanelSize,height);
		risk.addObserver(mv);
		add(mv);
		mv.repaint();
		mv.validate();
		sp = new SidePanel(new Point(start.x,start.y),new Point(sidePanelSize,height),rg,riskGui);
		sp.setBounds(start.x,start.y,sidePanelSize,height);
		risk.addObserver(sp);
		add(sp);
		sp.repaint();
		sp.validate();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.GREEN);
		mv.repaint();
		//sp.repaint();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(rg.isGameOver())
			riskGui.endGame();
	}

	public int getSizeW() {
		return width;
	}

	public static int sidePanelSizeFactor(){
		return sidePanelSizeFactor;
	}
	
}
