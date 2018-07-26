package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.RiskGUI;
import model.Player;
import model.RiskGame;

/* -----------------------------------------------------
 * Class: SetPlayersGUI
 * Author: Alex Starks
 * Purpose: This class is responsible for getting the number and
 * type of players from the user via jpanel
 * ----------------------------------------------------- */
public class SetPlayerColor {
	
	private int width;
	private int height;
	private RiskGUI riskGUI;
	private final Color backgroundColor = Color.BLACK;
	
	// constructor: SetPLayersGUI(RiskGame)
	public SetPlayerColor(RiskGUI rg) 
	{
		riskGUI = rg;
		askUser();
	}
	
	
	// method: askUser()
	// purpose: to ask if user wants to play or watch ai
	private void askUser()
	{
		JLabel previewLabel = new JLabel("Player Color", JLabel.CENTER);
	    previewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48));
	    previewLabel.setSize(previewLabel.getPreferredSize());
	    previewLabel.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
	    JColorChooser colorChooser = new JColorChooser();
	    colorChooser.setPreviewPanel(previewLabel);
		JDialog d = colorChooser.createDialog(null,"",true,colorChooser,null,null); 
		d.setVisible(true);
		
		Player p = new Player(colorChooser.getColor());
		riskGUI.setPlayer(p);
	    riskGUI.setView(riskGUI.getMainMenuView());
	    	
	} // constructor


}// class

