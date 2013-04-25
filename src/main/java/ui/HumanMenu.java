package main.java.ui;
/**
 * HumanMenu is presented to Human players. It extends JPanel.
 *
 * HumanMenu uses ActionListener to take events from the Dice JButtons, the
 * Take Dice and Roll Dice Buttons, and the JRadioButtons showing available
 * scores to facilitate gameplay.
 *
 * @author Cory Dissinger
 * @version $Id: $Id
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Player;
import main.java.game.SpriteManager;
import main.java.game.Turn;
import main.java.game.Yahtzee;
public class HumanMenu extends JPanel implements ActionListener
{
  private Yahtzee controller;
  private SpriteManager spriteManager;
  private boolean isSingle, dieCheat, scoreCheat;
  private Turn theTurn;
  private Player thePlayer;
  private GridBagConstraints gbConst = new GridBagConstraints();
  private JLabel activeName, filler, currentScore, rollsLeft, lifeOne, lifeTwo;
  private Border raisedBevel, blueLine, redLine, compoundOut, compoundDieHeld;
  private Border etched, loweredBevel; 
  private JPanel titlePanel, playPanel, displayPanel, scoreSelectionPanel, scoreTitlePanel, bottomPanel, commandPanel, dicePanel, diceTitle;
  private ButtonGroup comboGroup;
  private Vector<ImageIcon> downDice;
  private Vector<ImageIcon> upDice;
  private JRadioButton [] comboSelectors;
  private JButton rollDice, takeDice, cheatDice, cheatScore;
  private JButton [] dieButtons;
  private boolean [] activeDice = {true, true, true, true, true};
  private int [] cheatedDice;
  private int cheatedScore;

  /**
   * Constructor for the HumanMenu class.
   *
   * Initializes all components. Uses BoxLayout on the top level container.
   * titlePanel, playPanel, and scoreTitlePanel use GridBagLayout's default
   * properties to center themselves. comboPanel, which contains all
   * JRadioButtons that represent possible scores, uses GridLayout.
   *
   * @param jApp The JApplet controller.
   * @param turn Newly created turn to control gameplay.
   * @param player Reference to active player object.
   * @param spriteManager SpriteManager containing image information.
   * @param isSingle Flag for game status.
   */
  public HumanMenu(Yahtzee jApp, Turn turn, Player player, SpriteManager spriteManager, boolean isSingle)
  {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setSize(new Dimension(600, 560));
    setMaximumSize(new Dimension(600, 560));                 
    setMinimumSize(new Dimension(600, 560));
    setPreferredSize(new Dimension(600, 560));
    controller = jApp;
    this.spriteManager = spriteManager;
    theTurn = turn;
    this.isSingle = isSingle;
    thePlayer = player;
    thePlayer.setDiceValues(theTurn.getDiceValues());
      
    if(!isSingle)
    {
      sendMessageToServer("newTurn", "");
      sendMessageToServer("roll", Arrays.toString(theTurn.getDiceValues()));
    }
 
    //Setup borders.
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    blueLine = BorderFactory.createLineBorder(Color.blue);
    redLine = BorderFactory.createLineBorder(Color.red);
    loweredBevel = BorderFactory.createLoweredBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(blueLine, raisedBevel);
    compoundDieHeld = BorderFactory.createCompoundBorder(redLine, loweredBevel);
    
    //Cheat Tiles
    cheatDice = new JButton();
    cheatDice.setSize(new Dimension(10, 35));
    cheatDice.setPreferredSize(new Dimension(140, 35));
    cheatDice.setMaximumSize(new Dimension(140, 35));
    cheatDice.setMinimumSize(new Dimension(140, 35));
    cheatDice.setActionCommand("cheatdice");
    cheatDice.addActionListener(this);
    cheatDice.setOpaque(false);
    cheatDice.setContentAreaFilled(false);
    cheatDice.setBorderPainted(false);
    
    cheatScore = new JButton();
    cheatScore.setSize(new Dimension(140, 35));
    cheatScore.setPreferredSize(new Dimension(140, 35));
    cheatScore.setMaximumSize(new Dimension(140, 35));
    cheatScore.setMinimumSize(new Dimension(140, 35));
    cheatScore.setActionCommand("cheatscore");
    cheatScore.addActionListener(this);
    cheatScore.setOpaque(false);
    cheatScore.setContentAreaFilled(false);
    cheatScore.setBorderPainted(false);

    int turnsLeft = thePlayer.getTurnsLeft();
    String pName = new String(thePlayer.getName());
    activeName = new JLabel("Current Player: " + pName );
    activeName.setSize(new Dimension (300, 35));
    activeName.setPreferredSize(new Dimension (300, 35));
    activeName.setMinimumSize(new Dimension (300, 35));
    activeName.setMaximumSize(new Dimension (300, 35));
    activeName.setFont(new Font("century gothic", Font.BOLD, 20));
    currentScore = new JLabel("Current Total Score: " + thePlayer.getGrandTotal());
    currentScore.setSize(new Dimension(300, 35));
    currentScore.setMinimumSize(new Dimension(300, 35));
    currentScore.setMaximumSize(new Dimension(300, 35));
    currentScore.setPreferredSize(new Dimension(300, 35));
    
    //Northern Content
    titlePanel = new JPanel(new GridBagLayout());
    titlePanel.setSize(new Dimension(580,35));
    titlePanel.setPreferredSize(new Dimension(580,35));
    titlePanel.setMaximumSize(new Dimension(580, 35)); 
     
    gbConst.gridy = 0;
    gbConst.gridx = 0;
    titlePanel.add(cheatDice, gbConst);
    gbConst.gridy = 0;
    gbConst.gridx = 1;   
    titlePanel.add(activeName, gbConst);
    gbConst.gridx = 2;
    titlePanel.add(cheatScore, gbConst);
    titlePanel.setBorder(etched);
    
    //Southern Content 
    
    playPanel = new JPanel(new GridBagLayout());
    playPanel.setSize(new Dimension(300, 70)); 
    playPanel.setPreferredSize(new Dimension(300, 70));
    playPanel.setMaximumSize(new Dimension(300, 70));
    playPanel.setMinimumSize(new Dimension(300, 70));
    
    rollDice = new JButton("Roll Dice");
    takeDice = new JButton("Take Dice");
    rollDice.setSize(new Dimension(70,40));
    takeDice.setSize(new Dimension(70,40));
    rollDice.addActionListener(this);
    takeDice.addActionListener(this);
    
    
    //gbConst.fill = GridBagConstraints.NONE;
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(0, 70, 0, 10);  
    playPanel.add(rollDice, gbConst);
    gbConst.gridx = 1;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(0, 10, 0, 70);
    playPanel.add(takeDice, gbConst);
    //playPanel.add(takeDice, gbConst);    
    playPanel.setBorder(raisedBevel);   
    
    //Center Content
  
    scoreSelectionPanel = new JPanel();
    scoreSelectionPanel.setLayout(new GridBagLayout());
    scoreSelectionPanel.setSize(new Dimension(300, 340));
    scoreSelectionPanel.setMinimumSize(new Dimension(300, 340));
    scoreSelectionPanel.setMaximumSize(new Dimension(300, 340));
    scoreSelectionPanel.setBorder(raisedBevel);
    updateScorePanel();
    
    displayPanel = new JPanel(new GridBagLayout());
    displayPanel.setSize(new Dimension(230, 420));
    
    //Initialize Dice Lables & Images
    dieButtons = new JButton[5];
    upDice = new Vector<ImageIcon>(spriteManager.getUpDiceImages());
    downDice = new Vector<ImageIcon>(spriteManager.getDownDiceImages());
    
    dieButtons[0] = new JButton(upDice.elementAt(0));
    dieButtons[1] = new JButton(upDice.elementAt(1));
    dieButtons[2] = new JButton(upDice.elementAt(2));
    dieButtons[3] = new JButton(upDice.elementAt(3));
    dieButtons[4] = new JButton(upDice.elementAt(4));
    dieButtons[0].setSize(new Dimension(90, 100));
    dieButtons[1].setSize(new Dimension(100, 100));
    dieButtons[2].setSize(new Dimension(100, 100));
    dieButtons[3].setSize(new Dimension(100, 100));
    dieButtons[4].setSize(new Dimension(100, 100));
    dieButtons[0].setBorder(raisedBevel);
    dieButtons[1].setBorder(raisedBevel);
    dieButtons[2].setBorder(raisedBevel);
    dieButtons[3].setBorder(raisedBevel);
    dieButtons[4].setBorder(raisedBevel);
    dieButtons[0].setActionCommand("Die1");
    dieButtons[1].setActionCommand("Die2");
    dieButtons[2].setActionCommand("Die3");
    dieButtons[3].setActionCommand("Die4");
    dieButtons[4].setActionCommand("Die5");
    dieButtons[0].addActionListener(this);
    dieButtons[1].addActionListener(this);
    dieButtons[2].addActionListener(this);
    dieButtons[3].addActionListener(this);
    dieButtons[4].addActionListener(this);
    
    diceTitle = new JPanel(new GridBagLayout());
    diceTitle.setSize(new Dimension(230, 50));
    diceTitle.setMinimumSize(new Dimension(230, 50));
    diceTitle.setPreferredSize(new Dimension(230, 50));
    diceTitle.setMaximumSize(new Dimension(230, 50));
    diceTitle.setBorder(raisedBevel);
    
    rollsLeft = new JLabel("Rolls Left: ");
    rollsLeft.setFont(new Font("serif", Font.BOLD, 12));
    rollsLeft.setSize(new Dimension(70, 30));
    rollsLeft.setPreferredSize(new Dimension(70, 30));
    rollsLeft.setMinimumSize(new Dimension(70, 30));
    rollsLeft.setMaximumSize(new Dimension(70, 30));
    gbConst.gridx = 0;
    gbConst.anchor = GridBagConstraints.LINE_START;
    gbConst.insets = new Insets(10, 10, 10, 30);
    diceTitle.add(rollsLeft, gbConst);
    
    lifeOne = new JLabel(spriteManager.getLife());
    gbConst.gridx = 1;
    gbConst.insets = new Insets(10, 10, 10, 10);
    gbConst.anchor = GridBagConstraints.CENTER;
    diceTitle.add(lifeOne, gbConst);
    lifeTwo = new JLabel(spriteManager.getLife());
    gbConst.gridx = 2;
    gbConst.insets = new Insets(10, 0, 10, 10);
    diceTitle.add(lifeTwo, gbConst);
    
    //Modify GridBagConstraints and add components.
    gbConst.anchor = GridBagConstraints.CENTER;
    gbConst.insets = new Insets(10,10,5,5);
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    displayPanel.add(dieButtons[0], gbConst);
    gbConst.insets = new Insets(5,10,5,5);
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    displayPanel.add(dieButtons[1], gbConst);
    gbConst.insets = new Insets(10,5,5,10);
    gbConst.gridx = 1;
    gbConst.gridy = 0;
    displayPanel.add(dieButtons[2], gbConst);
    gbConst.insets = new Insets(5,5,5,10);
    gbConst.gridx = 1;
    gbConst.gridy = 1;
    displayPanel.add(dieButtons[3], gbConst);
    gbConst.insets = new Insets(5,10,10,10);
    gbConst.gridx = 0;
    gbConst.gridy = 2;
    gbConst.gridwidth = 2;
    displayPanel.add(dieButtons[4], gbConst);
    
    //Add all content together.

    dicePanel = new JPanel();
    dicePanel.setLayout(new GridBagLayout());
    dicePanel.setSize(new Dimension(250, 485));
    dicePanel.setMinimumSize(new Dimension(250, 485));
    dicePanel.setMaximumSize(new Dimension(250, 485));
    dicePanel.setPreferredSize(new Dimension(250, 485));
    gbConst.gridy = 0;
    gbConst.insets = new Insets(10, 10, 10, 10);
    dicePanel.add(diceTitle, gbConst);
    gbConst.gridy = 1;
    gbConst.insets = new Insets(0, 10, 10, 10);
    dicePanel.add(displayPanel, gbConst);
    dicePanel.setBorder(etched);

    commandPanel = new JPanel(new GridBagLayout());
    commandPanel.setSize(new Dimension(320, 485));
    commandPanel.setPreferredSize(new Dimension(320, 485));
    commandPanel.setMinimumSize(new Dimension(320, 485));
    commandPanel.setMaximumSize(new Dimension(320, 485));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(10, 10, 10, 10);
    commandPanel.add(currentScore, gbConst);
    gbConst.gridx = 0;
    gbConst.gridy = 1;
    gbConst.insets = new Insets(0, 10, 10, 10);
    commandPanel.add(scoreSelectionPanel, gbConst);
    gbConst.gridx = 0;
    gbConst.gridy = 2;
    gbConst.insets = new Insets(0, 10, 10, 10);
    commandPanel.add(playPanel, gbConst);
    commandPanel.setBorder(etched);

    bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
    bottomPanel.setSize(new Dimension(580, 485));
    bottomPanel.setMaximumSize(new Dimension(580, 485));
    bottomPanel.setPreferredSize(new Dimension(580, 485));
    bottomPanel.setMinimumSize(new Dimension(580, 485));
    //bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    bottomPanel.add(dicePanel);
    bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    bottomPanel.add(commandPanel);
    bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

    add(Box.createRigidArea(new Dimension(0, 10)));
    add(titlePanel);
    add(Box.createRigidArea(new Dimension(0, 10)));
    add(bottomPanel);
    add(Box.createRigidArea(new Dimension(0, 10)));
 
    setBorder(compoundOut);   
    validate();
    setVisible(true);  
  }

  /**
   * updateComboPanel refreshes the JRadioButtons holding possible scores.
   *
   * updateComboPanel receives a TreeMap containing scoring information from
   * the active player, and iterates through each entry setting up a button for
   * each one.
   */
  public void updateScorePanel()
  {
    TreeMap<String, Integer> possiblePoints = thePlayer.getPossibleScores();
    TreeMap<String, Integer> setPoints = thePlayer.getSetScores();
    
    int index = 0;
    comboSelectors = new JRadioButton[possiblePoints.size()];
    comboGroup = new ButtonGroup();
    
    scoreSelectionPanel.removeAll();    
    
    JLabel title = new JLabel("Upper Section");
    title.setFont(new Font("serif", Font.BOLD, 20));
    title.setSize(new Dimension(135, 30));
    title.setMaximumSize(new Dimension(135, 30));
    title.setMinimumSize(new Dimension(135, 30));
    title.setPreferredSize(new Dimension(135, 30));
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(5, 10, 5, 10);
    scoreSelectionPanel.add(title, gbConst);
    
    if(setPoints.containsKey("Aces"))
    { 
      int score = setPoints.get("Aces");
      makeLabelScore("Aces", 1, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Aces");
      makeRadioScore("Aces", 1, 0, score, index++);   
    }

    if(setPoints.containsKey("Twos"))
    { 
      int score = setPoints.get("Twos");
      makeLabelScore("Twos", 2, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Twos");
      makeRadioScore("Twos", 2, 0, score, index++);   
    }
    
    if(setPoints.containsKey("Threes"))
    { 
      int score = setPoints.get("Threes");
      makeLabelScore("Threes", 3, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Threes");
      makeRadioScore("Threes", 3, 0, score, index++);   
    }
    
    if(setPoints.containsKey("Fours"))
    { 
      int score = setPoints.get("Fours");
      makeLabelScore("Fours", 4, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Fours");
      makeRadioScore("Fours", 4, 0, score, index++);   
    }
    
    if(setPoints.containsKey("Fives"))
    { 
      int score = setPoints.get("Fives");
      makeLabelScore("Fives", 5, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Fives");
      makeRadioScore("Fives", 5, 0, score, index++);   
    }
    
    if(setPoints.containsKey("Sixes"))
    { 
      int score = setPoints.get("Sixes");
      makeLabelScore("Sixes", 6, 0, score);    
    }
    else
    {
      int score = possiblePoints.get("Sixes");
      makeRadioScore("Sixes", 6, 0, score, index++);   
    }
    
    //End Upper Section, begin Lower Section.
    title = new JLabel("Lower Section");
    title.setFont(new Font("serif", Font.BOLD, 20));
    title.setSize(new Dimension(135, 30));
    title.setMaximumSize(new Dimension(135, 30));
    title.setMinimumSize(new Dimension(135, 30));
    title.setPreferredSize(new Dimension(135, 30));
    gbConst.gridx = 2;
    gbConst.gridy = 0;
    gbConst.insets = new Insets(5, 10, 5, 10);
    scoreSelectionPanel.add(title, gbConst);
    
    if(setPoints.containsKey("3 of a Kind"))
    { 
      int score = setPoints.get("3 of a Kind");
      makeLabelScore("3 of a Kind", 1, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("3 of a Kind");
      makeRadioScore("3 of a Kind", 1, 2, score, index++);   
    }
    
    if(setPoints.containsKey("4 of a Kind"))
    { 
      int score = setPoints.get("4 of a Kind");
      makeLabelScore("4 of a Kind", 2, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("4 of a Kind");
      makeRadioScore("4 of a Kind", 2, 2, score, index++);   
    }

    if(setPoints.containsKey("Full House"))
    { 
      int score = setPoints.get("Full House");
      makeLabelScore("Full House", 3, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("Full House");
      makeRadioScore("Full House", 3, 2, score, index++);   
    }
    
    if(setPoints.containsKey("Small Straight"))
    { 
      int score = setPoints.get("Small Straight");
      makeLabelScore("Small Straight", 4, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("Small Straight");
      makeRadioScore("Small Straight", 4, 2, score, index++);   
    }
    
    if(setPoints.containsKey("Large Straight"))
    { 
      int score = setPoints.get("Large Straight");
      makeLabelScore("Large Straight", 5, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("Large Straight");
      makeRadioScore("Large Straight", 5, 2, score, index++);   
    }
    
    if(setPoints.containsKey("YAHTZEE"))
    { 
      int score = setPoints.get("YAHTZEE");
      makeLabelScore("YAHTZEE", 6, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("YAHTZEE");
      makeRadioScore("YAHTZEE", 6, 2, score, index++);   
    }
    
    if(setPoints.containsKey("Chance"))
    { 
      int score = setPoints.get("Chance");
      makeLabelScore("Chance", 7, 2, score);    
    }
    else
    {
      int score = possiblePoints.get("Chance");
      makeRadioScore("Chance", 7, 2, score, index++);   
    }
  }
  
  /**
   * makeRadioScore accepts various parameters to create a category scoring JRadioButton.
   *
   * @param cat The name of the button to be made.
   * @param pos X Position
   * @param col Y Position
   * @param score Points received.
   * @param index Index to be inserted in the comboGroup.
   */
  public void makeRadioScore(String cat, int pos, int col, int score, int index)
  {
      JRadioButton temp = new JRadioButton(cat + ": " + score);
      temp.setSize(new Dimension(135, 25));
      temp.setPreferredSize(new Dimension(135, 25));
      temp.setMaximumSize(new Dimension(135, 25));
      temp.setMinimumSize(new Dimension(135, 25));
      temp.setSelected(false);
      temp.setActionCommand(cat);
      comboGroup.add(temp);
      comboSelectors[index] = new JRadioButton();
      comboSelectors[index] = temp;    
      
      gbConst.gridx = col;
      gbConst.gridy = pos;
      gbConst.insets = new Insets(0, 10, 5, 10);
      scoreSelectionPanel.add(temp, gbConst);    
  }
  
  /**
   * Creates a new JLabel object and displays it accordingly. It represents
   * a previously selected score.
   *
   * @param cat a {@link java.lang.String} object.
   * @param pos a int.
   * @param col a int.
   * @param score a int.
   */
  public void makeLabelScore(String cat, int pos, int col, int score)
  {
      JLabel temp = new JLabel(cat + "= " + score);
      temp.setSize(new Dimension(135, 25));
      temp.setPreferredSize(new Dimension(135, 25));
      temp.setMaximumSize(new Dimension(135, 25));
      temp.setMinimumSize(new Dimension(135, 25));
      temp.setBorder(loweredBevel);
      gbConst.gridx = col;
      gbConst.gridy = pos;
      gbConst.insets = new Insets(0, 10, 5, 10);
      scoreSelectionPanel.add(temp, gbConst);  
  }
  
  /**
   * Sets the multiplayer flag to true.
   *
   * @param value a boolean.
   */
  public void setGameToSingle(boolean value)
  {
    isSingle = value;
  }
  
  /**
   * updateDice repaints the Dice images to match their values.
   */
  public void updateDice()
  {
    int [] newValues = theTurn.getDiceValues();
  
    //Use ternary operator with the setIcon
    for(int i = 0; i < 5; i++)
    {
      switch(newValues[i])
      {
        case 1: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(0));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(0));
                break;
                
        case 2: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(1));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(1));
                break;
                
        case 3: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(2));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(2));
                break; 
                
        case 4: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(3));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(3));
                break;
                
        case 5: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(4));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(4));
                break;
                
        case 6: if(activeDice[i])
                  dieButtons[i].setIcon(upDice.elementAt(5));
                else 
                  dieButtons[i].setIcon(downDice.elementAt(5));
                break;
      }
    }
    
    validate();  
  }
  
  /**
   * Whenever a player makes an action this sends details to the server.
   *
   * @param type The type of action taken.
   * @param message A brief list of details.
   */
  public void sendMessageToServer(String type, String message)
  {
    String temp;
    
    if(type.equals("roll"))
    {
      temp = new String(thePlayer.getName() + ": has rolled " + message);
      controller.client.setRecentMessage(temp);
    }
    else if(type.equals("hold"))
    {
      temp = new String(thePlayer.getName() + ": has held a die with value = " + message);
      controller.client.setRecentMessage(temp);
    }
    else if(type.equals("newTurn"))
    {
      temp = new String("----------NEW TURN----------\n");
      controller.client.setRecentMessage(temp);
      temp = new String("Player: " + thePlayer.getName() + " has " + thePlayer.getTurnsLeft() + " turns left!\n");
      controller.client.setRecentMessage(temp);
    }  
    else if(type.equals("endTurn"))
    {
      temp = new String(thePlayer.getName() + " has chosen: " + getSelectedScore() + "=" + thePlayer.getScore(getSelectedScore()) + "\n\n");
      controller.client.setRecentMessage(temp);
    } 
  }
  
  /**
   * updateThrows changes the displayed information to match current throws left
   */
  public void updateThrows()
  {
    int throwsLeft = theTurn.getThrows();
  
    if(throwsLeft == 1)
    {
      lifeTwo.setIcon(null);
      lifeTwo.setText("X");
      lifeTwo.setFont(new Font("serif", Font.BOLD, 12));
      lifeTwo.setForeground(Color.RED);
    }
    else if(throwsLeft == 0)
    {
      lifeOne.setIcon(null);
      lifeOne.setText("X");
      lifeOne.setFont(new Font("serif", Font.BOLD, 12));
      lifeOne.setForeground(Color.RED);
    }
     
    validate();
  }
  
  /**
   * getSelectedScore checks each JRadioButton and returns the selected text.
   *
   * @return String representing selected score.
   */
  public String getSelectedScore()
  {
    String selection = new String("");
  
    for(int i = 0; i < comboSelectors.length; i++)
    {
      if(comboSelectors[i].isSelected())
        selection = new String(comboSelectors[i].getActionCommand());      
    }
    
    return selection;
  }
  
  /**
   * Toggles held/unheld for a clicked die.
   * 
   * @param dieCommand The die number that was clicked.
   */           
  private void dieHeld(String dieCommand)
  {
    int dieNo = Integer.parseInt(dieCommand.substring(3));
    dieNo--; //Prevents out of bounds. 

    int [] values = theTurn.getDiceValues();

    if(!isSingle)
      sendMessageToServer("hold", Integer.toString(values[dieNo]));

    if(activeDice[dieNo])
    {
      dieButtons[dieNo].setIcon(downDice.elementAt(values[dieNo]-1));
      activeDice[dieNo] = false; 
      theTurn.holdADie(dieNo);
    }
    else
    {
      dieButtons[dieNo].setIcon(upDice.elementAt(values[dieNo]-1));
      activeDice[dieNo] = true;
      theTurn.holdADie(dieNo);
    }
  }
  
  /**
   * Creates a string detailing the category and points received this turn.
   *
   * @return A message with the category and points received this turn.
   */
  public String getTurnInfo()
  {
    String temp = new String();
    
    temp += "Category Chosen: " + getSelectedScore() + "\n";
    temp += "Total Score: " + thePlayer.getGrandTotal() + "\n";
    
    return temp; 
  }
  
  /**
   * Activates the dice cheat, providing the JApplet a chance to listen for keys.
   */
  public void activateDiceCheat()
  {
    JOptionPane.showMessageDialog(controller, "Enter a series of five integers, 1 through 6" , "Dice Cheat", JOptionPane.QUESTION_MESSAGE);
    dieCheat = true;
    cheatedDice = new int[5];
    controller.getKeys();
    for(int i = 0; i < 5; i++)
      cheatedDice[i] = 0;
  }
  
  /**
   * Executes the dice cheat. Modifies the dice values.
   */     
  private void executeDiceCheat()
  { 
    theTurn.setDiceValues(cheatedDice);
    thePlayer.cheated();
    updateDice();
    updateScorePanel();
    controller.removeKeyListeners();
    dieCheat = false;
  }
  
  /**
   * Activates the score cheat, providing the JApplet a chance to listen for keys.
   */
  public void activateScoreCheat()
  {
    JOptionPane.showMessageDialog(controller, "Enter two integers for a new score (tens digit then ones digit), then click the radio button of the category to place it in. Click a button on the screen to see results.", "Score Cheat", JOptionPane.QUESTION_MESSAGE);
    scoreCheat = true;
    cheatedScore = 0;
    controller.getKeys();     
  }
  
  /**
   * Executes the score cheat using user entered keys.
   *
   * @param category a {@link java.lang.String} object.
   */
  public void executeScoreCheat(String category)
  {
    thePlayer.cheated();
    thePlayer.cheatScore(category, cheatedScore);
    updateScorePanel();
    scoreCheat = false;  
  }
  
  /**
   * Checks to see if the user has selected a score, returns true if so.
   *
   * @return True if a score is selected, false otherwise.
   */
  public boolean scoreSelected()
  {
    for(int i = 0; i < comboSelectors.length; i++)
    {
      if(comboSelectors[i].isSelected())
        return true;      
    }
    
    return false;
  }
  
  /**
   * {@inheritDoc}
   *
   * Overloaded actionPerformed method from ActionListener interface.
   *
   * This overloaded actionPerformed method has three possible results. The
   * human user could click the Roll Dice button to invoke a die roll, the
   * Take Dice button which sets the currently selected score into that players
   * score card, or holds a die.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(scoreCheat && scoreSelected() && (cheatedScore != 0))
      executeScoreCheat(getSelectedScore());  
    
    if(command.equals("Roll Dice"))
    { 
      theTurn.rollDice();
      thePlayer.setDiceValues(theTurn.getDiceValues());
      if(!isSingle)
        sendMessageToServer("roll", Arrays.toString(theTurn.getDiceValues()));
      updateDice();
      updateScorePanel();
      updateThrows();  
    }
    else if(command.equals("Take Dice"))
    {
      if(!scoreSelected())
      {
        controller.displayError("Please select a score before continuing!", "Game Error");
        return;
      }    
    
      thePlayer.setScore(getSelectedScore());
        
      if(!isSingle)
      {
        sendMessageToServer("endTurn", "");
        controller.meterPanel.subtractTurn();
        controller.client.updateServerScoreInfo(getSelectedScore(), thePlayer.getScore(getSelectedScore()));
        controller.client.updateTurn(); 
      }
      else
      {
        controller.meterPanel.subtractTurn();
        controller.updateSingleIndex();
        controller.updateSingleLog(getTurnInfo(), thePlayer.getName());
      }
      
      controller.updatePlayer(thePlayer); 
      controller.showAnimation("score");   
    }
    else if(command.equals("cheatdice"))
      activateDiceCheat(); 
    else if(command.equals("cheatscore"))
      activateScoreCheat();
    else
      dieHeld(command);
  }
  
  /**
   * Determines what cheat was activated.
   *
   * @param e The KeyEvent information.
   */
  public void handleCheat(KeyEvent e)
  {
    int input = Character.getNumericValue(e.getKeyChar());
    System.out.println(input);
    System.out.println(Arrays.toString(cheatedDice));
    if(dieCheat)
    {
      int count = 0;
      while(cheatedDice[count] != 0)
        count++;
      
      if((0 < input) && (input < 7))
        cheatedDice[count] = input;
      else
        JOptionPane.showMessageDialog(controller, "Input must be 1-6!", "Input Error", JOptionPane.ERROR_MESSAGE);
                
      if(cheatedDice[4] != 0)
        executeDiceCheat();
    }
    else if(scoreCheat && isSingle)
    { 
      if((-1 < input) && (input < 10))
      {
        if(cheatedScore == 0)
          cheatedScore = (input * 10);
        else
        {
          cheatedScore += input;
          controller.removeKeyListeners();
        }
      }
      else
        JOptionPane.showMessageDialog(controller, "Input must be 0-9!", "Input Error", JOptionPane.ERROR_MESSAGE);   
    }
  }
}
