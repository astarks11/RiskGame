package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary
 * 
 * Description:
 * This class draws the side panel for the game view menu of the game.
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.RiskGUI;
import model.*;


//Territory
//owner
//armies
//--------
//pcolor
//reinf
//terr owned
//cards
//-------
//reinf
//battle
//fortify
//trade
/*
 * this class is used to control anything that cant be controled in the map view about the game
 */
public class SidePanel extends JPanel implements Observer {

	private int width, height;
	private RiskGame risk;
	private Point s1start, s2start, s3start, buttonSize;
	private JButton next, deploy, battle, fortify, trade, add, sub, makeAttack, makeFortify, tradeCard, leftCard,
			rightCard, reset, endGame, addArmies, subArmies, maxArmies;
	private Player p1;
	private List<Player> p2;
	private int count = 0;
	private MapView mv;
	private int colorDiff = 30;
	private ArrayList<JButton> phases;
	private BL bl;
	private Font buttonFont;
	private static Color ButtonColor = new Color(176, 196, 222);
	private int mode = 1;
	private Map map;
	private boolean deploying = false, battling = false, fortifying = false, trading = false, enteringLoop = true;
	private int currentCard = 0, playerArmies;
	private ArrayList<Integer> selectedCards;
	private RiskGUI riskGui;
	private int fontSize = 22;

	public SidePanel(Point start, Point size, RiskGame rg, RiskGUI riskGui) {
		this.riskGui = riskGui;
		this.width = size.x;
		this.height = size.y / 3;
		buttonSize = new Point(width / 3, height / 6);
		this.s1start = start;
		this.s2start = new Point(start.x, start.y + height);
		this.s3start = new Point(start.x, start.y + 2 * height);
		this.risk = rg;
		MML mml = new MML();
		this.addMouseMotionListener(mml);
		selectedCards = new ArrayList<Integer>();
		buttonFont = new Font("Arial Black", Font.PLAIN, 22);
		p1 = risk.getPlayer();
		p2 = risk.getAiPlayers();
		phases = new ArrayList<JButton>();
		setLayout(null);
		next = new JButton("next");
		bl = new BL();
		// next.addActionListener(bl);
		// next.setBounds(start.x + buttonSize.x/2, start.y+10, buttonSize.x,
		// buttonSize.y);
		// next.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
		// Color.BLACK));
		// next.setBackground(new Color(255, 228, 225));
		// add(next);
		deploy = new JButton("Reinf");
		phases.add(deploy);
		battle = new JButton("Battle");
		phases.add(battle);
		fortify = new JButton("Fortify");
		phases.add(fortify);
		trade = new JButton("Trade");
		phases.add(trade);
		drawButtons();
		add = new JButton("+");
		sub = new JButton("-");
		makeAttack = new JButton("Make Attack!");
		add.addActionListener(bl);
		sub.addActionListener(bl);
		makeAttack.addActionListener(bl);
		makeAttack.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		makeAttack.setBackground(new Color(255, 228, 225));
		makeAttack.setFont(buttonFont);
		makeAttack.setFocusPainted(false);
		makeFortify = new JButton("Make Fortification!");
		makeFortify.addActionListener(bl);
		makeFortify.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		makeFortify.setBackground(new Color(255, 228, 225));
		makeFortify.setFont(buttonFont);
		makeFortify.setFocusPainted(false);
		initButtons();
		add.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GREEN));
		add.setBackground(new Color(255, 228, 225));
		add.setFont(buttonFont);
		add.setFocusPainted(false);
		sub.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.RED));
		sub.setBackground(new Color(255, 228, 225));
		sub.setFont(buttonFont);
		sub.setFocusPainted(false);
		addArmies = new JButton("+");
		subArmies = new JButton("-");
		maxArmies = new JButton("Max");
		
		addArmies.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GREEN));
		subArmies.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.RED));
		maxArmies.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		
		addArmies.setFont(buttonFont);
		subArmies.setFont(buttonFont);
		maxArmies.setFont(buttonFont);
		
		addArmies.setBackground(new Color(255, 228, 225));
		subArmies.setBackground(new Color(255, 228, 225));
		maxArmies.setBackground(new Color(255, 228, 225));
		
		addArmies.setFocusPainted(false);
		subArmies.setFocusPainted(false);
		maxArmies.setFocusPainted(false);
		
		addArmies.addActionListener(bl);
		subArmies.addActionListener(bl);
		maxArmies.addActionListener(bl);
		
		add(add);
		add(sub);
	}

	private void initButtons() {
		tradeCard = new JButton("Trade Card");
		tradeCard.addActionListener(bl);
		tradeCard.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		tradeCard.setBackground(new Color(255, 228, 225));
		tradeCard.setFont(buttonFont);
		tradeCard.setFocusPainted(false);
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(buttonFont);
		int stringWidth = fm.stringWidth("Trade Card");
		int stringHeight = fm.getHeight();
		tradeCard.setBounds(s3start.x, s3start.y, stringWidth + 30,
				stringHeight + 20);
		add(tradeCard);

		leftCard = new JButton("<");
		leftCard.addActionListener(bl);
		leftCard.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		leftCard.setBackground(new Color(255, 228, 225));
		leftCard.setFont(buttonFont);
		leftCard.setFocusPainted(false);
		stringWidth = fm.stringWidth("<");
		stringHeight = fm.getHeight();
		leftCard.setBounds(s3start.x, s3start.y + height / 2, stringWidth + 30, stringHeight + 20);
		add(leftCard);

		rightCard = new JButton(">");
		rightCard.addActionListener(bl);
		rightCard.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		rightCard.setBackground(new Color(255, 228, 225));
		rightCard.setFont(buttonFont);
		rightCard.setFocusPainted(false);
		stringWidth = fm.stringWidth(">");
		stringHeight = fm.getHeight();
		rightCard.setBounds(s3start.x + width - (stringWidth + 30), s3start.y + height / 2, stringWidth + 30,
				stringHeight + 20);
		add(rightCard);
	
		reset = new JButton("Reset");
		reset.addActionListener(bl);
		reset.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE));
		reset.setBackground(new Color(255, 228, 225));
		reset.setFont(buttonFont);
		reset.setFocusPainted(false);
		stringWidth = fm.stringWidth("reset");
		stringHeight = fm.getHeight();
		reset.setBounds(s3start.x + width - (stringWidth + 30), s3start.y, stringWidth + 30,
				stringHeight + 20);
		add(reset);
		
		endGame = new JButton("X");
		endGame.addActionListener(bl);
		endGame.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.RED));
		endGame.setBackground(new Color(255, 228, 225));
		endGame.setFont(buttonFont);
		endGame.setFocusPainted(false);
		stringWidth = fm.stringWidth("X");
		stringHeight = fm.getHeight();
		endGame.setBounds(s1start.x , s1start.y, stringWidth + 30,
				stringHeight + 20);
		add(endGame);
	}

	private void drawButtons() {
		int space = (height - 4 * buttonSize.y) / 5;
		for (int i = 0; i < phases.size(); i++) {
			phases.get(i).addActionListener(bl);
			phases.get(i).setBounds(s2start.x + 10, s2start.y + i * (buttonSize.y) + (i + 1) * space, buttonSize.x,
					buttonSize.y);
			phases.get(i).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
			phases.get(i).setBackground(SidePanel.getButtonColor());
			phases.get(i).setFont(buttonFont);
			phases.get(i).setFocusPainted(false);
			add(phases.get(i));
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(SidePanel.getButtonColor());
		g.setColor(new Color(176 + colorDiff, 196 + colorDiff, 222 + colorDiff));
		g.fillRect(s2start.x, s2start.y, width, height);
		g.setColor(new Color(176 - colorDiff, 196 - colorDiff, 222 - colorDiff));
		g.fillRect(s3start.x, s3start.y, width, height);
		g.setColor(new Color(176 + colorDiff, 196 + colorDiff, 222 + colorDiff));
		g.fillRect(s3start.x + width / 4 - 20, s3start.y + height / 4 - 20, width / 2 + 40, height);
		g.setColor(Color.BLACK);//
		if(risk.getStartingGame())
			drawStartDirections(g);
		if (deploying)
			drawArmiesPerRound(g);
		if (battling)
			drawAttack(g);
		if (fortifying)
			drawFortify(g);
		if (risk.getPlayer() != null)
			drawCards(g);
	}

	private void drawStartDirections(Graphics g) {
		Font font = new Font("Arial Black", Font.PLAIN, 24);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int stringHeight = fm.getHeight();
		String directions = "Place troops 1 at a time";
		int stringWidth = fm.stringWidth(directions);
		g.drawString(directions, s1start.x + width/2-stringWidth/2, s1start.y + height/2 - stringHeight/2);
	}

	private void drawCards(Graphics g) {
		if(selectedCards.contains(currentCard)){
			g.setColor(Color.GREEN);
			g.fillRect(s3start.x + width / 4 - 20, s3start.y + height / 4 - 20, width / 2 + 40, height);
		}
		if (risk.getPlayer().getCards().size() != 0) {
			if (risk.getPlayer().getCards().get(currentCard).getTerritoryID() == -1) {
				Font font = new Font("Arial Black", Font.PLAIN, 24);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				int stringHeight = fm.getHeight();
				String wildcard = "WildCard";
				int stringWidth = fm.stringWidth(wildcard);
				g.drawString(wildcard, s3start.x + width / 2 - stringWidth / 2,
						s3start.y + height / 2 + height / 4 + stringHeight + 10);
				return;
			}
			TerritoryShape c1 = risk.getMap().getTerrShape(risk.getPlayer().getCards().get(currentCard).getTerritoryID());
			Point p1 = new Point(s3start.x + width / 4, s3start.y + height / 4);
			Point p2 = new Point(width / 2, height / 2);

			TerritoryShape c2 = new TerritoryShape(c1, p1, p2);
			c2.draw(g);
			g.drawRect(p1.x, p1.y, p2.x, p2.y);
			Font font = new Font("Arial Black", Font.PLAIN, 24);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int stringHeight = fm.getHeight();
			String atype = risk.getPlayer().getCards().get(currentCard).getType().toString();
			int stringWidth = fm.stringWidth(atype);
			g.drawString(atype, s3start.x + width / 2 - stringWidth / 2,
					s3start.y + height / 2 + height / 4 + stringHeight + 10);
		} else {
			g.setColor(Color.BLACK);
			Font font = new Font("Arial Black", Font.PLAIN, 24);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int stringHeight = fm.getHeight();
			int stringWidth = fm.stringWidth("No Cards");
			g.setColor(Color.BLACK);
			g.fillRect(s3start.x + width / 2 - (stringWidth + 20) / 2, s3start.y + height / 2 - (stringHeight + 20) / 2,
					stringWidth + 20, stringHeight + 20);
			g.setColor(Color.WHITE);
			g.fillRect(s3start.x + width / 2 - (stringWidth + 10) / 2, s3start.y + height / 2 - (stringHeight + 10) / 2,
					stringWidth + 10, stringHeight + 10);
			g.setColor(Color.BLACK);
			g.drawString("No Cards", s3start.x + width / 2 - stringWidth / 2,
					s3start.y + height / 2 + stringHeight / 4);
		}
	}

	public void drawArmiesPerRound(Graphics g) {
		Font font = new Font("Arial Black", Font.PLAIN, fontSize);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		if (p1 == null) {
			return;
		}
		String armies = "Armies: " + p1.getArmySize() + "/" + playerArmies;
		int stringWidth = fm.stringWidth(armies);
		int stringHeight = fm.getHeight();
		g.setColor(Color.BLACK);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 2 - (stringHeight + 20) / 2,
				stringWidth + 20, stringHeight + 20);
		g.setColor(Color.WHITE);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 10) / 2, s1start.y + height / 2 - (stringHeight + 10) / 2,
				stringWidth + 10, stringHeight + 10);
		g.setColor(Color.BLACK);
		g.drawString(armies, s1start.x + width / 2 - stringWidth / 2, s1start.y + height / 2 + stringHeight / 4);
		sub.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height - stringHeight,
				stringWidth / 4, stringHeight);
		add.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2 + stringWidth + 20 - stringWidth / 4,
				s1start.y + height - stringHeight, stringWidth / 4, stringHeight);
		remove(makeAttack);
	}

	public void drawAttack(Graphics g) {
		Font font = new Font("Arial Black", Font.PLAIN, fontSize);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		if (p1 == null) {
			return;
		}
		String armies = "Dice roll: " + risk.getAttackAmount() + "/" + risk.possibleDiceRolls();// risk.getAttackingSize();
		int stringWidth = fm.stringWidth(armies);
		int stringHeight = fm.getHeight();
		g.setColor(Color.BLACK);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height - (int)add.getBounds().getHeight() - (stringHeight + 20),
				stringWidth + 20, stringHeight + 20);
		g.setColor(Color.WHITE);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 10) / 2, s1start.y + height - (int)add.getBounds().getHeight() - (stringHeight + 15),
				stringWidth + 10, stringHeight + 10);
		g.setColor(Color.BLACK);
		g.drawString(armies, s1start.x + width / 2 - stringWidth / 2, s1start.y + height - (int)add.getBounds().getHeight()-(stringHeight + 20)/4);
		//sub.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 2 + stringHeight,stringWidth / 4, stringHeight);
		//add.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2 + stringWidth + 20 - stringWidth / 4,s1start.y + height / 2 + stringHeight, stringWidth / 4, stringHeight);

		stringHeight = fm.getHeight();
		remove(makeFortify);
		add(makeAttack);
		
		
		font = new Font("Arial Black", Font.PLAIN, fontSize);
		g.setFont(font);
		fm = g.getFontMetrics(font);
		if (p1 == null) {
			return;
		}
		String armiesToAdd = "Armies To Add: " + risk.getArmiesToAdd() + "/" + risk.getArmiesToAddPossibly();// risk.getAttackingSize();
		stringWidth = fm.stringWidth(armiesToAdd);
		stringHeight = fm.getHeight();
		g.setColor(Color.BLACK);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + (int)endGame.getBounds().getHeight() + 10,
				stringWidth + 20, stringHeight + 20);
		g.setColor(Color.WHITE);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 10) / 2, s1start.y + (int)endGame.getBounds().getHeight() + 15,
				stringWidth + 10, stringHeight + 10);
		g.setColor(Color.BLACK);
		g.drawString(armiesToAdd, s1start.x + width / 2 - stringWidth / 2, s1start.y + (int)endGame.getBounds().getHeight() + 3*(stringHeight+20)/4);
		subArmies.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 4 + stringHeight,
				stringWidth / 4, stringHeight);
		addArmies.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2 + stringWidth + 20 - stringWidth / 4,
				s1start.y + height / 4 + stringHeight, stringWidth / 4, stringHeight);
		stringWidth = fm.stringWidth(maxArmies.getText());
		stringHeight = fm.getHeight();
		add(addArmies);
		add(subArmies);
		add(maxArmies);
		
		maxArmies.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 4 + stringHeight, stringWidth + 20, stringHeight);
		
		stringWidth = fm.stringWidth(makeAttack.getText());
		makeAttack.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2,s1start.y + (int)addArmies.getBounds().getMaxY() + 5, stringWidth + 20, stringHeight + 10);
	
	}

	public void drawFortify(Graphics g) {
		Font font = new Font("Arial Black", Font.PLAIN, fontSize);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		if (p1 == null) {
			return;
		}
		String armies = "Armies: " + risk.getFortifyAmount() + "/" + risk.getFortifySize();
		int stringWidth = fm.stringWidth(armies);
		int stringHeight = fm.getHeight();
		g.setColor(Color.BLACK);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 2 - (stringHeight + 20) / 2,
				stringWidth + 20, stringHeight + 20);
		g.setColor(Color.WHITE);
		g.fillRect(s1start.x + width / 2 - (stringWidth + 10) / 2, s1start.y + height / 2 - (stringHeight + 10) / 2,
				stringWidth + 10, stringHeight + 10);
		g.setColor(Color.BLACK);
		g.drawString(armies, s1start.x + width / 2 - stringWidth / 2, s1start.y + height / 2 + stringHeight / 4);
		//sub.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, s1start.y + height / 2 + stringHeight,stringWidth / 4, stringHeight);
		//add.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2 + stringWidth + 20 - stringWidth / 4,s1start.y + height / 2 + stringHeight, stringWidth / 4, stringHeight);
		stringWidth = fm.stringWidth(makeFortify.getText());
		stringHeight = fm.getHeight();
		remove(makeAttack);
		remove(addArmies);
		remove(subArmies);
		remove(maxArmies);
		add(makeFortify);
		makeFortify.setBounds(s1start.x + width / 2 - (stringWidth + 20) / 2, (int)add.getBounds().getMinY() - (stringHeight + 20) - 10, stringWidth + 20, stringHeight + 20);
	}

	public class BL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton buttonClicked = (JButton) e.getSource();
			if (buttonClicked == makeAttack) {
				risk.makeAttack();
			}
			if (buttonClicked == makeFortify) {
				risk.makeFortification();
			}
			if (buttonClicked == add) {
				if (deploying)
					risk.troopDeployed();
				if (battling)
					risk.addAttacker();
				if (fortifying)
					risk.addFortifier();
			}
			if (buttonClicked == sub) {
				if (deploying)
					risk.unDeploy();
				if (battling)
					risk.subAttacker();
				if (fortifying)
					risk.subFortifier();
			}
			if(buttonClicked == addArmies){
				System.out.println("adding army");
				risk.addArmieToAdd();
			}
			if(buttonClicked == subArmies){
				risk.subArmyToAdd();
			}
			if(buttonClicked == maxArmies){
				risk.addAllPossibleArmies();
			}
			if (buttonClicked == deploy && (trading || enteringLoop) && !risk.getStartingGame()) {
				if(enteringLoop == false){
					risk.aiTurns(0);
					//risk.getPlayer().setArmy(risk.getArmiesPerRound() + risk.getPlayer().getArmySize());
					risk.getPlayer().calculateReinforcements();
					
					add(add);
					add(sub);
				}
				playerArmies = risk.getPlayer().getArmySize();
				enteringLoop = false;
				trading = false;
				deploying = true;
				releaseButtons();
				deploy.setBackground(new Color(135, 206, 250));
				risk.setCanDeploy(true);
			}
			if (buttonClicked == battle && deploying) {
				deploying = false;
				battling = true;
				risk.getPlayer().deployed();
				releaseButtons();
				battle.setBackground(new Color(135, 206, 250));
				risk.setCanBattle(true);
			}
			if (buttonClicked == fortify && battling) {
				battling = false;
				fortifying = true;
				releaseButtons();
				fortify.setBackground(new Color(135, 206, 250));
				risk.setCanFortify(true);
			}
			if (buttonClicked == trade && fortifying) {
				fortifying = false;
				trading = true;
				remove(add);
				remove(sub);
				remove(makeFortify);
				releaseButtons();
				trade.setBackground(new Color(135, 206, 250));
				risk.setCanTrade(true);
			}
			if (trading) {
				if (buttonClicked == tradeCard && risk.getPlayer().getCards().size() > 0) {
					//risk.getPlayer().claimCard(currentCard);
					selectedCards.add(currentCard);
					if(selectedCards.size() == 3){
						List<Card> cards = risk.getPlayer().getCards();
						
						risk.tradeCards(risk.getPlayer(), cards.get(selectedCards.get(0)), cards.get(selectedCards.get(1)), cards.get(selectedCards.get(2)));
						selectedCards = null;
						selectedCards = new ArrayList<Integer>();
					}
					repaint();
				}
				if(buttonClicked == leftCard){
					if(currentCard > 0){
						currentCard --;
						repaint();
					}
				}
				if(buttonClicked == rightCard){
					if(currentCard < risk.getPlayer().getCards().size()-1){
						currentCard++;
						repaint();
					}
				}
				if(buttonClicked == reset){
					selectedCards = null;
					selectedCards = new ArrayList<Integer>();
					repaint();
				}
				
			}
			if(buttonClicked == endGame){
				riskGui.endGame();
			}
		}

		private void releaseButtons() {
			for (int i = 0; i < phases.size(); i++) {
				phases.get(i).setBackground(SidePanel.getButtonColor());
			}
			risk.deSelectAll();
			risk.setCanDeploy(false);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();

	}

	public static Color getButtonColor() {
		return ButtonColor;
	}
	
	private class MML implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(risk.isCursorOnMap()){
				risk.getMap().setNoHighlightAll();
				risk.cursorIsOffMap();
			}
			
		}
	}

	
}
