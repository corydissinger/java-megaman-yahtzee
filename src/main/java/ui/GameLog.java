package main.java.ui;
/**
 * GameLog is a class that stores recent actions of all players currently active.
 * A player can view this log at anytime by clicking on Options.
 */  


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;

public class GameLog extends JPanel implements ActionListener
{
  private Yahtzee controller;
  private GridBagConstraints gbConst = new GridBagConstraints();
  private Border blueLine, raisedBevel, compoundOut, etched, lowEtched;
  private JLabel title;
  private JButton back;
  private JTextArea entryText;
  private JScrollPane scrollPane;
  private String gameLog;
  private int turnCounter = 1;
  
  /**
   * GameLog constructor. Initializes components, takes in a Yahtzee controller.
   *
   * @param jApp The yahtzee controller.
   */           
  public GameLog(Yahtzee jApp)
  {
    controller = jApp;
    
    gameLog = new String();
    
    setSize(new Dimension(400, 500));
    setMinimumSize(new Dimension(400, 500));
    setPreferredSize(new Dimension(400, 500));
    setMaximumSize(new Dimension(400, 500));

    blueLine = BorderFactory.createLineBorder(Color.blue);
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    lowEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(blueLine, raisedBevel); 
    
    title = new JLabel("Game Log");
    title.setFont(new Font("serif", Font.BOLD, 20));
    title.setSize(new Dimension(380, 40));
    title.setMinimumSize(new Dimension(380, 40));
    title.setMaximumSize(new Dimension(380, 40));
    title.setPreferredSize(new Dimension(380, 40));
    title.setBorder(etched);

    entryText = new JTextArea();
    entryText.setColumns(30);
    entryText.setEditable(false);
    scrollPane = new JScrollPane(entryText);
    scrollPane.setSize(new Dimension(380, 380));
    scrollPane.setMaximumSize(new Dimension(380, 380));
    scrollPane.setPreferredSize(new Dimension(380, 380));
    scrollPane.setMinimumSize(new Dimension(380, 380));
    scrollPane.setBorder(raisedBevel);
    
    back = new JButton("Back");
    back.addActionListener(this);
    back.setSize(new Dimension(80,40));
    back.setMinimumSize(new Dimension(80,40));
    back.setSize(new Dimension(80,40));
    back.setSize(new Dimension(80,40));
    
    gbConst.gridy = 0;
    gbConst.insets = new Insets(20, 10, 10, 10);
    add(title, gbConst);
    gbConst.gridy = 1;
    gbConst.insets = new Insets(10, 10, 10, 10);
    add(scrollPane, gbConst);
    gbConst.gridy = 2;
    gbConst.insets = new Insets(10, 10, 10, 10);
    add(back, gbConst);
    
    setBorder(compoundOut);
    
    setVisible(true);
    validate();
  }
 
 /**
  * Used in single games. Takes the results of the turn and adds them to the messages.
  * 
  * @param turnResults A short message entailing turn history.
  * 
  * @param playerName The player who just went.
  */            
  public void addTurnInfo(String turnResults, String playerName)
  {
    entryText.append("Turn " + turnCounter + " - Player Name: " + playerName + "\n");
    turnCounter++;
    entryText.append(turnResults + "\n");
    entryText.append("---------------------------------------------------\n");  
  }
  
  /**
   * Used in multi games. Grabs all recent messages from the server.
   */        
  public void addInfoFromServer()
  { 
    Vector<String> temp = new Vector<String>();
    entryText.setText("");
    controller.stopUpdaters();
    controller.client.getGameLog(temp);
    
    for(String s : temp)
      entryText.append(s + "\n");
    
    controller.startUpdaters();
    
  }
  
  /**
   * Overloaded actionPerformed. Returns to the game when invoked.
   * 
   * @param e The ActionEvent triggered.
   */           
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(command.equals("Back"))
      controller.toGame();
  }
}