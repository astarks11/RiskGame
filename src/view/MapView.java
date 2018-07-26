package view;
/*
 * Class: MapView
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class shows the map view for the current game.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import controller.RiskGUI;
import model.Map;
import model.Player;
import model.RiskGame;
import model.Territory;
/*
 * this class displays the current state of the game at all times
 */
public class MapView extends JPanel implements Observer {
	private int width, height;
	private RiskGUI riskGUI;
	private RiskGame risk;
	private Point originalArrowPoint = null;
	// private Arrow arrow = null;
	private Map map;
	private Player p1;
	private boolean battleMode = false;
	//private ArrayList<Arrow> attacks;
	private Arrow attack;
	private boolean addingArrow = false;

	public MapView(Point start, Point size, RiskGame risk) {
		this.width = size.x;
		this.height = size.y;
		this.risk = risk;
		this.map = risk.getMap();
		this.p1 = risk.getPlayer();
		//attacks = new ArrayList<Arrow>();
		System.out.println(width + "\n" + height);
		if (p1 != null) {
			MouseMotionListener mml = new MML();
			this.addMouseMotionListener(mml);
			MouseListener ml = new ML();
			this.addMouseListener(ml);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (risk != null){
			this.setBackground(risk.getMap().getBackGroundColor());
			map.drawMap(g);
		}
		if(attack != null)
			attack.draw(g);
	}

	private class ML implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Territory clicked = map.getTerritory(e.getX(), e.getY());
			if(risk.getStartingGame()){
				//Territory clicked = risk.getMap().getTerritory(new Point(e.getX(),e.getY()));
				if(risk.getPlayer().ownsTerritory(clicked)){
					clicked.addArmy(risk.getPlayer());
					repaint();
					risk.aiStart(0);
				}
				if(risk.getPlayer().getArmySize() == 0){
					risk.setStartingGame(false);
				}
			}else if (clicked.getOwner() == p1 && risk.getCanDeploy())
				risk.selectTerritory(clicked);
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (risk.getCanBattle() || risk.getCanFortify()) {
				System.out.println(1);
				originalArrowPoint = new Point(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// arrow = null;
			if (risk.getCanBattle()) {
				System.out.println(" i can removing arrow");
				if (!risk.canAttack(attack.getAttackingPoint(),attack.getDefendingPoint()))
					attack = null; //remove arrow if attack cant be made
				originalArrowPoint = null;
				addingArrow = false;
				repaint();
			}
			if(risk.getCanFortify()){
				
				
				if (!risk.canFortify(attack.getAttackingPoint(),attack.getDefendingPoint())){
					System.out.println("removing arrow");
					attack = null; //remove arrow if fortify cant be made
				}
				originalArrowPoint = null;
				addingArrow = false;
				repaint();
			}

		}

	}

	private class MML implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			boolean shouldRepaint = false;
			if (risk.getCanBattle() || risk.getCanFortify()) {
				System.out.println(3);
				if (addingArrow) {
					if(attack == null)
						System.exit(1);
					attack.setEndPoint(new Point(e.getX(), e.getY()));
					shouldRepaint = true;
				} else {
					attack = new Arrow(new Point(originalArrowPoint), new Point(e.getX(), e.getY()));
					addingArrow = true;
				}
			}
			if (mouseClick(e.getX(), e.getY())) {
				shouldRepaint = true;
			}
			if (shouldRepaint == true) {
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (mouseClick(e.getX(), e.getY())) {
				repaint();
			}

		}
		

		private Color attackColor = new Color(0, 255, 0, 120);
		private Color cantAttack = new Color(255, 0, 0, 120);
		private Color highlight = new Color(0, 0, 255, 120);

		public boolean mouseClick(int x, int y) {
			boolean shouldRepaint = false;
			risk.cursorIsOnMap();
			if (map.contains(x, y)) {
				Territory selected = map.getTerritory(x, y);
				if (risk.getCanBattle()) {
					//System.out.println("can attack" + player.canAttack(selected));
					if (p1.canAttack(selected)) {
						if (!map.isColor(selected, attackColor)) {
							map.setNoHighlightAll();
							map.setTerritoryColor(selected, attackColor);
							shouldRepaint = true;
						}
					} else if (!map.isColor(selected, cantAttack)) {
						map.setNoHighlightAll();
						map.setTerritoryColor(selected, cantAttack);
						shouldRepaint = true;
					}
				} else {
					if (!map.isColor(selected, highlight)) {
						map.setNoHighlightAll();
						map.setTerritoryColor(selected, highlight);
						shouldRepaint = true;
					}
				}
			}
			return shouldRepaint;
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if((risk.attackInProgress() && risk.getCanBattle()) || (risk.fortifyInProgress() && risk.getCanFortify())){
			System.out.println("update");
			attack = null;
		}
		if(risk.shouldRemoveArrow()){
			System.out.println(" i am removing arrow");
			attack = null;
			originalArrowPoint = null;
			addingArrow = false;
			risk.removeArrow(false);
			repaint();
		}
		
		repaint();

	}
}
