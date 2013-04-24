package main.java.ui;
/**
 * This is the menu the player first sees, and allows them to setup initial game information.
 */ 


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;

public class StartMenu extends JPanel implements ActionListener
{
  private int numPlayers;
  private Yahtzee controller; 
  private GridBagConstraints gbConst = new GridBagConstraints();
  private JTextArea playerNames;
  private JTextField entryName;
  private JButton start, next;
  private JPanel titlePanel, buttonPanel, entryPanel, radioPanel; 
  private JRadioButton single, multi;
  private ButtonGroup radioGroup;
  private JScrollPane scrollText;
  private Border raisedBevel, blueLine, compound, etched, loweredBevel;  

  /**
   * Constructor. Accepts a Yahtzee controller as a parameter.
   * 
   * @param jApp The Yahtzee controller.      
   */     
  public StartMenu(Yahtzee jApp)
  {   
    super(new BorderLayout(5,5)); 
    setSize(new Dimension(300,200));
    setMinimumSize(new Dimension(300,200));
    setMaximumSize(new Dimension(300,200));
    setPreferredSize(new Dimension(300,200));
    
    numPlayers = 0;
    controller = jApp; 
    
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    blueLine = BorderFactory.createLineBorder(Color.blue);
    loweredBevel = BorderFactory.createLoweredBevelBorder();
    compound = BorderFactory.createCompoundBorder(blueLine, raisedBevel);

    titlePanel = new JPanel(new GridBagLayout());
    JLabel title = new JLabel("Welcome to Yahtzee!");
  
    title.setFont(new Font("calibri", Font.BOLD, 24));

    titlePanel.setBorder(etched);
    titlePanel.setSize(new Dimension(250,50));
    
    titlePanel.add(title, gbConst);
    
    buttonPanel = new JPanel();
    next = new JButton("Continue"); 
    
    buttonPanel.setSize(new Dimension(250,50));
    
    buttonPanel.setBorder(etched);
    
    next.addActionListener(this);
    
    buttonPanel.add(next, gbConst);    
        
    entryPanel = new JPanel();
    entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.PAGE_AXIS));
    entryPanel.setSize(new Dimension(110,80));
    entryPanel.setBorder(loweredBevel);
    entryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    radioPanel = new JPanel();
    radioGroup = new ButtonGroup();
    single = new JRadioButton("Single Player");
    multi = new JRadioButton("Multiplayer");
    entryName = new JTextField("Enter your name!");
    
    entryName.setActionCommand("Entry");
    
    entryName.setSize(new Dimension(120,30));
    entryName.setPreferredSize(new Dimension(120,30));
    entryName.setMinimumSize(new Dimension(120,30));
    entryName.setMaximumSize(new Dimension(120,30));
  
    
    //Radio Buttons + Panel
    single.setSelected(true);
    single.setAlignmentX(Component.CENTER_ALIGNMENT);
    multi.setAlignmentX(Component.CENTER_ALIGNMENT);
    radioGroup.add(single);
    radioGroup.add(multi);
    radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.PAGE_AXIS));
    radioPanel.add(single);
    radioPanel.add(multi);
    
    //Add text field and radio group
    entryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    entryPanel.add(entryName);
    entryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    entryPanel.add(radioPanel);
    //entryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
    //End Western Content
    
    add(buttonPanel, BorderLayout.SOUTH);
    add(titlePanel, BorderLayout.NORTH);
    add(entryPanel, BorderLayout.CENTER);
    
    setBorder(compound);
    validate();
    setVisible(true);
  }
     
  public boolean gameReady()
  {
    return false;
  }

  /**
   * Overloaded actionPerformed method. Waits for a continue command, then starts the game.
   * 
   * @param e The triggered ActionEvent.      
   */     
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    System.out.println("Start Menu Action Performed.");
    
    if(command.equals("Continue"))
    {
      if(single.isSelected())
      {
        controller.addPlayer(entryName.getText());
        controller.startSingleGame();
      }
      if(multi.isSelected())
      {
        controller.addPlayer(entryName.getText());
        controller.startMultiGame();
      }
    }
  }
}