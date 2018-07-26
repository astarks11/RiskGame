package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Steven Cleary, Alex Starks
 * 
 * Description:
 * This class updates the screen when the game is over, it is a possible
 * screen that shows up when the game is finished
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.RiskGame;

public class GameOver extends JPanel implements Observer {

	private RiskGame risk;
	private Point start, size;
	public GameOver(RiskGame risk, Point start, Point size){
		this.risk = risk;
		this.start = start;
		this.size = size;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.RED);
		Font font = new Font("Arial Black", Font.PLAIN, 64);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getHeight();
		if(!risk.isGameOver()){
			g.setColor(Color.WHITE);
			int width = fm.stringWidth("Game ended in tie, no winners");
			g.drawString("Game ended in tie, no winners", start.x + size.x/2 - width/2, start.y + size.y/2 - height/2);
		}else{
			String winner = risk.getWinner() + " won the game!";
			int width = fm.stringWidth(winner);
			g.drawString(winner, start.x + size.x/2 - width/2, start.y + size.y/2 - height/2);
		}
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
