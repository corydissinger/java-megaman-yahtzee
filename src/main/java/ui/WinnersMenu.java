package main.java.ui;
/**
 * WinnersMenu extends JPanel and presents a ranked list of the players when
 * the game is over.
 *
 * @author Cory
 * @version $Id: $Id
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;
public class WinnersMenu extends JPanel implements ActionListener
{
  private Yahtzee controller;
  private JTextArea winners;
  private JLabel title;
  private JButton reset;
  private Border blueLine, etched, raisedBevel, compoundOut;
  private GridBagConstraints gbConst = new GridBagConstraints();
  
  /**
   * Constructor for WinnersMenu class. Takes in a mapping of players.
   *
   * @param finalScores The mapping of players to grand total scores.
   * @param jApp The controlling JApplet.
   */
  public WinnersMenu(TreeMap<String, Integer> finalScores, Yahtzee jApp)
  {
    setLayout(new GridBagLayout());
    
    controller = jApp;
    //setSize(new Dimension(250, 350));
    //setMinimumSize(new Dimension(250, 350));
    //setMaximumSize(new Dimension(250, 350));
    //setPreferredSize(new Dimension(250, 350));
    
    blueLine = BorderFactory.createLineBorder(Color.blue);
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(blueLine, raisedBevel); 
    
    title = new JLabel("Final Standings");
    title.setFont(new Font("serif", Font.BOLD, 20));
    
    winners = new JTextArea();
    winners.setEditable(false);
    winners.setBorder(raisedBevel);
    setupWinners(finalScores);
    
    reset = new JButton("Reset?");
    reset.addActionListener(this);
    
    gbConst.gridy = 0;
    gbConst.anchor = GridBagConstraints.PAGE_START;
    gbConst.insets = new Insets(10,10,10,10);
    add(title, gbConst);
    gbConst.gridy = 1;
    add(winners, gbConst);
    
    gbConst.gridy = 2;
    add(reset, gbConst);
    
    setBorder(compoundOut);
    setVisible(true);
    validate();
  }
  
  /**
   * setupWinners pulls the highest to lowest player scores and writes them.
   *
   * @param scores The mapping of player names to grand totals.
   */
  public void setupWinners(TreeMap<String, Integer> scores)
  {
    String name = new String();
    int theScore = 0;
    int place = 1;
    
    while(scores.size() != 0)
    {
      for(Map.Entry<String, Integer> entry : scores.entrySet())
      {
        if(theScore < entry.getValue())
        {
          name = new String(entry.getKey());
          theScore = entry.getValue();
        }        
      }
      
      scores.remove(name);
      
      winners.append("Number " + place + ": " + name + ", with " + theScore + " points!\n");
      name = new String();
      theScore = 0;
      place++;
    }
  }
  
  /** {@inheritDoc} */
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(command.equals("Reset?"))
      controller.resetGame();
  }
  
}
