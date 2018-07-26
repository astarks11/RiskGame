package view;
/*
 * Class: RiskGUI
 * 
 * Authors: Alex Starks
 * 
 * Description:
 * This class will allow the user to set the AI for the game they would like
 * to play
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controller.RiskGUI;
import model.BeginnerAI;
import model.ExpertAI;
import model.IntermediateAI;
import model.Player;

public class SetArtificialPlayers {
	
	private RiskGUI riskGUI;
	private int numberOfAI;
	private JPanel panel;
	private JFrame frame;
	public static final String[] diff = { "Beginner", "Intermediate", "Expert"	};
	private JButton relative;
	private boolean player;

	public SetArtificialPlayers(RiskGUI rg, JButton relativeButton, boolean p) 
	{
		relative = relativeButton;
		riskGUI = rg;
		player = p;
		askUser();
	}
	
	
	// method: askUser()
	// purpose: ask user for number of ai
	private void askUser()
	{
		SpinnerModel model;
		// create frame
		frame = new JFrame();
		frame.setLocationRelativeTo(relative);		
		// create panel
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		// create label
		JLabel label = new JLabel("Number of AI: ");
		label.setFont(new Font("Verdana", Font.BOLD, 20));
		panel.add(label);
		// create spinner model and jspinner		
		if (player)
			model = new SpinnerNumberModel(2,2,5,1); // (initial,min,max,step)
		else
			model = new SpinnerNumberModel(3,3,6,1); // (initial,min,max,step)

		JSpinner number = new JSpinner(model);
		panel.add(number);
		// create button
		JButton pick = new JButton("pick");
		// add button listener
		pick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				numberOfAI = (int)number.getValue();
				askForDifficulties(numberOfAI);
			}
		});
		panel.add(pick);
	
		
		// add panel to frame
	    frame.add(panel);
	    frame.pack();
	    frame.setVisible(true);
	    
	    
	    
	
	   // riskGUI.setView(riskGUI.getMainMenuView());
	    	
	} // constructor
	
	private void askForDifficulties(int num){
		ArrayList<Player> ai = new ArrayList<Player>();
		for (int i = 0; i < num; i++)
		{
			String response = (String) JOptionPane.showInputDialog(panel,"Choose AI #"+(i+1)	+"'s difficulty","Choose Difficulty",JOptionPane.QUESTION_MESSAGE, null, diff, diff[0]);
			
			if (response!=null)
			{
				if (response.equals("Intermediate"))
				{
					ai.add(new Player(getRandColor(), new IntermediateAI()));
				}
				if (response.equals("Expert"))
				{
					ai.add(new Player(getRandColor(), new ExpertAI()));
				}
				if (response.equals("Beginner"))
				{
					ai.add(new Player(getRandColor(), new BeginnerAI()));
				}
			} else {
				ai.add(new Player(getRandColor(), new BeginnerAI()));
			}
		}
		riskGUI.setAI(ai);
		frame.dispose();
	}
	
	// returns a random color
	private Color getRandColor()
	{
		Random rand = new Random();
		int rVal = rand.nextInt(255);
		int gVal = rand.nextInt(255);
		int bVal = rand.nextInt(255);
		Color randCol = new Color(rVal,gVal,bVal);
		return randCol;
	}
}
