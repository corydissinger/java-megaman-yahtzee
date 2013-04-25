package main.java.ui;
/**
 * InstructionsMenu class implementation. Displays basic instructions.
 *
 * @author Cory Dissinger
 * @version $Id: $Id
 */


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;
public class InstructionsMenu extends JPanel implements ActionListener
{
  private Yahtzee controller;
  private JTextArea instructions;
  private JLabel title;
  private Border purpleLine, raisedBevel, compoundOut, etched;
  private JButton back;
  
  /**
   * Constructor. Initializes and configures all components.
   *
   * @param jApp The JApplet controller.
   */
  public InstructionsMenu(Yahtzee jApp)
  {
    controller = jApp;
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setSize(new Dimension(400, 350));
    setMaximumSize(new Dimension(400, 350));
    setMinimumSize(new Dimension(400, 350));
    setPreferredSize(new Dimension(400, 350));
    
    purpleLine = BorderFactory.createLineBorder(Color.magenta);
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    compoundOut = BorderFactory.createCompoundBorder(purpleLine, raisedBevel); 
    
    title = new JLabel("Instructions");
    title.setFont(new Font("serif", Font.BOLD, 22));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    instructions = new JTextArea();
    instructions.setMaximumSize(new Dimension(360, 200));
    instructions.append("                        ----------How To Play----------\n");
    instructions.append("The goal is to get the highest score possible.\n");
    instructions.append("Select a radio button with a category, then Take Dice to score.\n");
    instructions.append("Click a die image to hold it. Click again to unhold.\n");
    instructions.append("Click Options->View Score Cards to see all score cards.\n");
    instructions.append("                        ----------Turn Execution----------\n");
    instructions.append("1. Dice are randomized.\n");
    instructions.append("2. You can hold/unhold any number of dice, or roll again.\n");
    instructions.append("3. Repeat step 2 until you roll 3 times OR see a score you like.\n");
    instructions.append("4. Select a category from Possible Scores, then click Take Dice.\n");
    instructions.setEditable(false);
    instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
    instructions.setBorder(etched);
    
    back = new JButton("Back");
    back.setAlignmentX(Component.CENTER_ALIGNMENT);
    back.addActionListener(this);
    
    add(Box.createRigidArea(new Dimension(400, 30)));
    add(title);    
    add(Box.createRigidArea(new Dimension(400, 30)));
    add(instructions);
    add(Box.createRigidArea(new Dimension(400, 30)));
    add(back);
    add(Box.createRigidArea(new Dimension(400, 30)));
    
    setBorder(compoundOut);
    setVisible(true);
    validate();
  }
  
  /**
   * {@inheritDoc}
   *
   * Overloaded actionPerformed method from ActionListener interface.
   *
   * This performs one operation which is returning to the previous menu.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    System.out.println(command);
    
    if(command.equals("Back"))
      controller.toGame();
  }
}
