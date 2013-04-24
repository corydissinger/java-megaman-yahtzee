package main.java.ui;
/**
 * WaitMenu for single game use. Displays what the AI did.
 */ 


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;

public class WaitMenu extends JPanel implements ActionListener
{
  private Yahtzee controller;
  private JButton nextTurn;
  private JLabel title;
  private JTextArea turnInformation;
  private GridBagConstraints gbConst;
  private Border blueLine, raisedBevel, compoundOut, etched;
  
  /**
   * Constructor for WaitMenu, accepts a Yahtzee controller as a parameter.
   * 
   * @param jApp the Yahtzee controller.      
   */     
  public WaitMenu(Yahtzee jApp)
  {
    controller = jApp;
    gbConst = new GridBagConstraints();
  
    setLayout(new GridBagLayout()); 
    setSize(new Dimension(300, 200));
    setMinimumSize(new Dimension(300, 200));
    setMaximumSize(new Dimension(300, 200));
    setPreferredSize(new Dimension(300, 200));
    
    blueLine = BorderFactory.createLineBorder(Color.blue);
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(blueLine, raisedBevel);

    title = new JLabel("Turn Results");
    title.setFont(new Font("serif", Font.BOLD, 22));
    
    turnInformation = new JTextArea();
    
    nextTurn = new JButton("Next Turn");
    nextTurn.addActionListener(this);
    
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(15, 0, 0, 0);
    add(title, gbConst); 
    
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    gbConst.insets = new Insets(15, 0, 0, 0);
    add(turnInformation, gbConst);
    
    gbConst.gridx = 0;
    gbConst.gridy = 2;
    gbConst.insets = new Insets(15, 0, 15, 0);
    add(nextTurn, gbConst);
    
    setBorder(compoundOut);
    setVisible(true);
    validate();   
  }
  
  /**
   * Sets the text area with the AI's turn information.
   * 
   * @param message The AI's plays.      
   */     
  public void setTextArea(String message)
  {
    turnInformation.setText(null);
    turnInformation.append(message);
    
    validate();  
  }
 
 /**
  * Overloaded actionPerformed method. Waits for the player to click next turn.
  * 
  * @param e The triggered ActionEvent.    
  */    
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    System.out.println(command);
    //Spawn a dialog box if a player is currently taking a turn.
    //Have a Yahtzee method that checks if all players are ready before running game.
    //If a player is currently taking their turn, return the name of the player for the dialog box.
    if(command.equals("Next Turn"))
      controller.showAnimation("ready");
      //controller.tryNewTurn();
  }  
}