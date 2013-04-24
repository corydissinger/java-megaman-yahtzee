package main.java.ui;
/**
 * MeterPanel displays the number of turns the player has left, Megaman life style.
 */ 


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.java.game.SpriteManager;
import main.java.game.Yahtzee;

public class MeterPanel extends JPanel
{
  private Yahtzee controller;
  private SpriteManager spriteManager;
  private ImageIcon meter;
  private ImageIcon pellet;
  private int turnsLeft;
  
  /**
   * Constructor. Takes a controller and spritemanager argument.
   *
   * @param jApp The controller.
   * @param sm The SpriteManager.
   */              
  public MeterPanel(Yahtzee jApp, SpriteManager sm)
  {
    controller = jApp;
    
    turnsLeft = 13;
    meter = sm.getMeter();
    pellet = sm.getPellet(); 
    
    setOpaque(true);
    setVisible(true);
    
    setSize(new Dimension(45, 170));
    setPreferredSize(new Dimension(45, 170));
    setMinimumSize(new Dimension(45, 170));
    setMaximumSize(new Dimension(45, 170));
  }
  
  /**
   * Decrements internal turn count by one.
   */     
  public void subtractTurn()
  {
    turnsLeft--;  
  }
  
  /**
   * Repaints the meter to reflect current turns left.
   */     
  public void updateMeter()
  {
    repaint();
    validate();
  }
  
  /**
   * Paints a number of pellets equal to the turns left.
   */     
  public void paint(Graphics g)
  {
    g.drawImage(meter.getImage(), 0, 0, this);
    
    for(int i = 0; i <= turnsLeft; i++)
      g.drawImage(pellet.getImage(), 8, 104-(8*i), this);
      
    g.setFont(new Font("serif", Font.BOLD, 14));
    g.setColor(Color.red);
    g.drawString(Integer.toString(turnsLeft+1), 12, 148);
  }
}