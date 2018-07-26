package tests;
/*
 * Class: RiskGUI
 * 
 * Authors: Matthew Drake
 * 
 * Description:
 * This class runs 1000 games of risk with 2 beginner AI, 2 intermediate AI, and 2 expert AI
 * It will report how many games each AI won.
 */
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.BeginnerAI;
import model.ExpertAI;
import model.IntermediateAI;
import model.Map;
import model.Player;
import model.RiskGame;
import view.GameView;

public class Run6Bots
{ 
	public static void main(String[] args)
	{
		int beginnerWins = 0;
		int intermediateWins = 0;
		int expertWins = 0;
		for(int i = 0; i < 1000; i++)
		{
			System.out.println("Game #" + (i+1));
			Player p1 = null;
			ArrayList<Player> p2 = new ArrayList<Player>();
			p2.add(new Player(Color.BLUE,new BeginnerAI()));
			
			
			//p2.add(new Player(Color.RED,new BeginnerAI()));
			p2.add(new Player(Color.RED,new IntermediateAI()));
			p2.add(new Player(Color.GREEN,new ExpertAI()));
			p2.add(new Player(Color.YELLOW,new IntermediateAI()));
			p2.add(new Player(Color.ORANGE,new BeginnerAI()));
			p2.add(new Player(Color.BLACK,new ExpertAI()));
			Map gm = new Map(new Point(0,0),new Point(400 - 400/GameView.sidePanelSizeFactor(),400),1);

			RiskGame risk = new RiskGame(gm,p1,p2);
			gm.setUpUsers(p1, p2);

			
			risk.startGameAI(0);
			risk.runAiLoop(0);


			if(risk.getWinner() == p2.get(0) || risk.getWinner() == p2.get(4))beginnerWins++;
			else if(risk.getWinner() == p2.get(1) || risk.getWinner() == p2.get(3))intermediateWins++;
			else expertWins++;
			System.out.println("Beginners Won: " + beginnerWins + "\nIntermediates Won: " + intermediateWins + "\nExperts Won: " + expertWins);
		}

	}
}
