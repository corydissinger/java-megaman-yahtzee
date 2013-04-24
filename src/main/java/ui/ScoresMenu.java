package main.java.ui;
/**
 * ScoresMenu provides a way for the user to view other player's scores.
 */ 


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;

public class ScoresMenu extends JPanel implements ActionListener, ItemListener
{
  private Yahtzee controller;
  private GridBagConstraints gbConst = new GridBagConstraints();
  private Border blueLine, raisedBevel, compoundOut, etched, lowEtched, loweredBevel;
  private JLabel choosePlayer;
  private JComboBox playerNames;
  private JPanel comboBoxPanel, backPanel, buttonPanel, defaultPanel;
  private JPanel scoresPanel;
  private JButton back;
  
  /**
   * Constructor for ScoresMenu. Accepts a Yahtzee controller.
   * 
   * @param jApp The Yahtzee controller.
   */           
  public ScoresMenu(Yahtzee jApp)
  {
    controller = jApp;
  
    setLayout(new GridBagLayout());
    setSize(new Dimension(600, 560));
    setPreferredSize(new Dimension(600, 560));
    setMinimumSize(new Dimension(600, 560));
    setMaximumSize(new Dimension(600, 560));
    
    blueLine = BorderFactory.createLineBorder(Color.blue);
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    lowEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(blueLine, raisedBevel);  
    loweredBevel = BorderFactory.createLoweredBevelBorder();  
    
    playerNames = new JComboBox();
    playerNames.setEditable(false);
    playerNames.addItemListener(this);
    choosePlayer = new JLabel("Choose a Player:");
    
    playerNames.setSize(new Dimension(120, 30));
    playerNames.setMaximumSize(new Dimension(120, 30));
    playerNames.setMinimumSize(new Dimension(120, 30));
    playerNames.setPreferredSize(new Dimension(120, 30));
    
    choosePlayer.setSize(new Dimension(120, 30));
    choosePlayer.setPreferredSize(new Dimension(120, 30));
    choosePlayer.setMaximumSize(new Dimension(120, 30));
    choosePlayer.setMinimumSize(new Dimension(120, 30));
    
    comboBoxPanel = new JPanel(new GridBagLayout());
    comboBoxPanel.setSize(new Dimension(400, 40));
    comboBoxPanel.setMinimumSize(new Dimension(400, 40));
    comboBoxPanel.setMaximumSize(new Dimension(400, 40));
    gbConst.gridx = 0;
    gbConst.insets = new Insets(5, 10, 5, 10);
    comboBoxPanel.add(choosePlayer, gbConst);
    gbConst.gridx = 1;
    comboBoxPanel.add(playerNames, gbConst);
    
    comboBoxPanel.setBorder(raisedBevel);
    
    back = new JButton("Back");
    back.addActionListener(this);
    
    buttonPanel = new JPanel(new GridBagLayout());
    buttonPanel.setSize(new Dimension(580, 50));
    buttonPanel.setMinimumSize(new Dimension(580, 50));
    buttonPanel.setMaximumSize(new Dimension(580, 50));
    buttonPanel.add(back, gbConst);
    buttonPanel.setBorder(raisedBevel);
        
    defaultPanel = new JPanel();
  
    scoresPanel = new JPanel(new CardLayout());
    scoresPanel.add(defaultPanel, "Default");
    scoresPanel.setBorder(etched);
     
    gbConst.gridy = 0;
    gbConst.insets = new Insets(10, 120, 10, 80);
    add(comboBoxPanel, gbConst);
    gbConst.gridy = 1;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(scoresPanel, gbConst);
    gbConst.gridy = 2;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(buttonPanel, gbConst);
    setBorder(compoundOut);
    setVisible(true);
    validate();
  }
  
  /**
   * Creates a score panel for each player and adds them to the card layout.
   * 
   * @param scoreInfo A mapping of player names to score mappings.      
   */     
  public void setupScorePanels(TreeMap<String, TreeMap<String, Integer>> scoreInfo)
  {
    int index = 0;
    playerNames.removeAllItems();
    scoresPanel.removeAll();
     
    controller.stopUpdaters();
     
    for(Map.Entry<String, TreeMap<String, Integer>> entry : scoreInfo.entrySet())
    {
      String tempName = new String(entry.getKey());
      
      if(!controller.isSingle && (!tempName.equals(controller.thisPlayer)))
      {
        Thread.yield();
        controller.client.updateClientScores(tempName);
        Thread.yield();
      }
      
      TreeMap<String, Integer> comboPoints = entry.getValue();
      
      playerNames.addItem(tempName);
      scoresPanel.add(createScorePanel(tempName, comboPoints), tempName);    
           
      index++;
    } 
    
    controller.startUpdaters();
    
    validate(); 
  }
  
  /**
   * Creates a title label for each player, using their name.
   * 
   * @param name The player's name.
   * @return A JLabel with their name.         
   */     
  public JLabel createTitleLabel(String name)
  {
    JLabel title = new JLabel(name);
    
    title.setFont(new Font("serif", Font.BOLD, 20));
    title.setSize(new Dimension(580,40));
    title.setPreferredSize(new Dimension(580,40));
    title.setMaximumSize(new Dimension(580,40));
    title.setMinimumSize(new Dimension(580,40));
    
    return title;
  }
  
  /**
   * Using a player's scores, sets up the entire panel to show their scores.
   * 
   * @param comboPoints The player's score mapping.
   * @return JPanel with scoring information.            
   */     
  public JPanel createScorePanel(String name, TreeMap<String, Integer> comboPoints)
  {    
    JPanel upperScoresPanel = new JPanel(new GridBagLayout());
    upperScoresPanel.setSize(new Dimension(350, 195));
    upperScoresPanel.setMinimumSize(new Dimension(350, 195));
    upperScoresPanel.setMaximumSize(new Dimension(350, 195));
    upperScoresPanel.setPreferredSize(new Dimension(350, 195));
    upperScoresPanel.setBorder(loweredBevel);
    
    JPanel upperBonusPanel = new JPanel(new GridBagLayout());
    upperBonusPanel.setSize(new Dimension(200, 100));
    upperBonusPanel.setMinimumSize(new Dimension(200, 100));
    upperBonusPanel.setMaximumSize(new Dimension(200, 100));
    upperBonusPanel.setPreferredSize(new Dimension(200, 100));
    upperBonusPanel.setBorder(raisedBevel);
    
    JPanel lowerScoresPanel = new JPanel(new GridBagLayout());
    lowerScoresPanel.setSize(new Dimension(350, 195));
    lowerScoresPanel.setMinimumSize(new Dimension(350, 195));
    lowerScoresPanel.setMaximumSize(new Dimension(350, 195));
    lowerScoresPanel.setPreferredSize(new Dimension(350, 195));
    lowerScoresPanel.setBorder(loweredBevel); 
    
    JPanel lowerBonusPanel = new JPanel(new GridBagLayout());
    lowerBonusPanel.setSize(new Dimension(200, 100));
    lowerBonusPanel.setPreferredSize(new Dimension(200, 100));
    lowerBonusPanel.setMinimumSize(new Dimension(200, 100));
    lowerBonusPanel.setMaximumSize(new Dimension(200, 100));
    lowerBonusPanel.setBorder(raisedBevel);
        
    JLabel sectionHeader = new JLabel("Upper Section");
    JLabel tempLabel;
    sectionHeader.setFont(new Font("serif", Font.ITALIC, 20));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.gridwidth = 3;
    upperScoresPanel.add(sectionHeader, gbConst);
    tempLabel = new JLabel("Aces: " + comboPoints.get("Aces"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    gbConst.insets = new Insets(5, 5, 5, 5);
    gbConst.gridwidth = 1;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Twos: " + comboPoints.get("Twos"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 1;
    gbConst.gridy = 1;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Threes: " + comboPoints.get("Threes"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 2;
    gbConst.gridy = 1;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Fours: " + comboPoints.get("Fours"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 0;
    gbConst.gridy = 2;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Fives: " + comboPoints.get("Fives"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 1;
    gbConst.gridy = 2;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Sixes: " + comboPoints.get("Sixes"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 2;
    gbConst.gridy = 2;
    upperScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Upper Bonus: " + comboPoints.get("Upper Bonus"));
    tempLabel.setFont(new Font("serif", Font.BOLD, 14));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    upperBonusPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Upper Total: " + comboPoints.get("Total of Upper Section"));
    tempLabel.setFont(new Font("serif", Font.BOLD, 18));
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    upperBonusPanel.add(tempLabel, gbConst);
    
    //Lower Section
    sectionHeader = new JLabel("Lower Section");
    sectionHeader.setFont(new Font("serif", Font.ITALIC, 20));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.gridwidth = 3;
    lowerScoresPanel.add(sectionHeader, gbConst);
    tempLabel = new JLabel("3 of a Kind: " + comboPoints.get("3 of a Kind"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridwidth = 1;
    gbConst.insets = new Insets(5, 5, 5, 5);
    gbConst.gridx = 0;
    gbConst.gridy = 1;    
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("4 of a Kind: " + comboPoints.get("4 of a Kind"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 1;
    gbConst.gridy = 1;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Full House: " + comboPoints.get("Full House"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 2;
    gbConst.gridy = 1;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Small Straight: " + comboPoints.get("Small Straight"));
    tempLabel.setBorder(etched);
    tempLabel.setFont(new Font("serif", Font.PLAIN, 11));
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 0;
    gbConst.gridy = 2;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Large Straight: " + comboPoints.get("Large Straight"));
    tempLabel.setBorder(etched);
    tempLabel.setFont(new Font("serif", Font.PLAIN, 11));
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 1;
    gbConst.gridy = 2;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("YAHTZEE: " + comboPoints.get("YAHTZEE"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 0;
    gbConst.gridy = 3;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Chance :" + comboPoints.get("Chance"));
    tempLabel.setBorder(etched);
    setLabelSize(tempLabel, 100, 40);
    gbConst.gridx = 1;
    gbConst.gridy = 3;
    lowerScoresPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("Lower Total: " + comboPoints.get("Total of Lower Section"));
    tempLabel.setFont(new Font("serif", Font.BOLD, 14));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    lowerBonusPanel.add(tempLabel, gbConst);
    tempLabel = new JLabel("GRAND TOTAL: " + comboPoints.get("GRAND TOTAL"));
    tempLabel.setFont(new Font("serif", Font.BOLD, 18));
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    lowerBonusPanel.add(tempLabel, gbConst);
    
    JPanel temp = new JPanel(new GridBagLayout());
    temp.setSize(new Dimension(580,420));
    temp.setPreferredSize(new Dimension(580,420));
    temp.setMinimumSize(new Dimension(580,420));
    temp.setMaximumSize(new Dimension(580,420));
    
    gbConst.gridy = 0;
    gbConst.gridx = 0;
    gbConst.insets = new Insets(10,10,10,10);
    temp.add(upperScoresPanel, gbConst);
    gbConst.gridy = 0;
    gbConst.gridx = 1;
    gbConst.insets = new Insets(105,0,10,10);
    temp.add(upperBonusPanel, gbConst);
    gbConst.gridy = 1;
    gbConst.gridx = 0;
    gbConst.insets = new Insets(0,10,10,10);
    temp.add(lowerScoresPanel, gbConst);
    gbConst.gridy = 1;
    gbConst.gridx = 1;
    gbConst.insets = new Insets(95,0,10,10);
    temp.add(lowerBonusPanel, gbConst);
    temp.setVisible(true);
    temp.validate();
      
    return temp; 
  }
   
  /**
   * Saves lines of code by setting all label sizes at once.
   * 
   * @param label The JLabel to be resized.
   * @param width The width.
   * @param height The height.
   */                 
  public void setLabelSize(JLabel label, int width, int height)
  {
    label.setSize(new Dimension(width, height));
    label.setPreferredSize(new Dimension(width, height));
    label.setMinimumSize(new Dimension(width, height));
    label.setMaximumSize(new Dimension(width, height));
  }
  
  /**
   * Overloaded actionPerformed, returns to the main game when back is pressed.
   * 
   * @param e The triggered ActionEvent.      
   */     
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(command.equals("Back"))
      controller.toGame();
  }
  
  /**
   * Overloaded itemStateChanged, flips through the card layout based on what's chosen.
   * 
   * @param e Triggered ItemEvent.      
   */     
  public void itemStateChanged(ItemEvent e)
  {
    CardLayout scoresLayout = (CardLayout)(scoresPanel.getLayout());
    scoresLayout.show(scoresPanel, (String)e.getItem());
    System.out.println(e.getItem());
  }
}