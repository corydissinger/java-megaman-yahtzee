package main.java.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import main.java.game.Yahtzee;

public class PlayerOptions extends JMenuBar implements ActionListener
{
  private Yahtzee controller;
  private JMenu menu;
  private JMenuItem scoreCards, instructions, gameLog;
  
  public PlayerOptions(Yahtzee jApp)
  {
    controller = jApp;
  
    menu = new JMenu("Options");
    menu.addActionListener(this);
    add(menu);
  
    //Score Cards item.
    scoreCards = new JMenuItem("View Score Cards", KeyEvent.VK_S);
    scoreCards.setAccelerator(KeyStroke.getKeyStroke(
              KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    scoreCards.addActionListener(this);
      
    //Instructions
    instructions = new JMenuItem("Instructions", KeyEvent.VK_I);
    instructions.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, ActionEvent.CTRL_MASK));
    instructions.addActionListener(this); 
             
    gameLog = new JMenuItem("Game Log", KeyEvent.VK_G);
    gameLog.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_G, ActionEvent.CTRL_MASK));
    gameLog.addActionListener(this);
             
    //Add Content On
    menu.add(scoreCards);
    menu.add(instructions);
    menu.add(gameLog);
    
    setVisible(true);
    validate();              
  }
  
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    controller.optionsSelected(command);
  }
}