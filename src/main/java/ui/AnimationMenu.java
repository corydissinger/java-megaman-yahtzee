package main.java.ui;
/**
 * AnimationMenu acts as a canvas for any applet drawing. Uses mouse listener
 * to exit.
 */  


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.java.game.SpriteManager;
import main.java.game.Yahtzee;

public class AnimationMenu extends JPanel implements MouseListener
{
  private Yahtzee controller;
  private SpriteManager spriteManager;
  private boolean animating;
  
  /**
   * Constructor for AnimationMenu.
   * 
   * @param jApp The controlling JApplet.
   * 
   * @param sm SpriteManager responsible for providing images.
   */                 
  public AnimationMenu(Yahtzee jApp, SpriteManager sm) 
  {
    super(new BorderLayout());
    setOpaque(true);
    animating = true;
  
    controller = jApp;
    spriteManager = sm;
    
    setSize(new Dimension(800, 600));
    setPreferredSize(new Dimension(800, 600));
    setMinimumSize(new Dimension(800, 600));
    setMaximumSize(new Dimension(800, 600));
    addMouseListener(this);
  }
  
  /**
   * Creates a new thread that drives animation.
   */     
  public void startAnimation()
  {
    Thread t = new Thread(new Animator());
    t.start();
  }
  
  class Animator extends Thread
  {  
    private int waitTime;
    
    public void run()
    {   
      waitTime = spriteManager.getWaitTime();
       
      while(animating)
      {
        repaint();
        
        try{
          Thread.sleep(waitTime);
          controller.repaint();
        
          spriteManager.updateImage();
          waitTime = spriteManager.getWaitTime();
        }
        catch(InterruptedException e){
          e.printStackTrace();
        }
      }
    }

  }

  /**
   * Overloaded mouseClicked method, quits the animation menu.
   * 
   * @param e The moust event triggered.
   */           
  public void mouseClicked(MouseEvent e)
  {
    animating = false;
    controller.returnFromAnimation();    
  }
  
  public void mouseExited(MouseEvent e)
  {}
  
  public void mouseEntered(MouseEvent e)
  {}
  
  public void mouseReleased(MouseEvent e)
  {}
  
  public void mousePressed(MouseEvent e)
  {}
  
  /**
   * Overloaded paint method. Paints according to what is stored in SpriteManager.
   * 
   * @param g Graphics object.
   */           
  public void paint(Graphics g)
  {
    super.paint(g);
    
    ImageIcon iI = new ImageIcon(spriteManager.getCurrentImage());
    iI.paintIcon(this, g, 0, 0);

    if(spriteManager.getType().equals("ready"))
    {
      g.setColor(Color.white);
      g.fillRect(280, 515, 280, 60);
      g.setFont(new Font("serif", Font.BOLD, 32));
      g.setColor(Color.red);
      g.drawString("Click to Continue", 300, 550);
    }
    else if(spriteManager.getType().equals("score"))
    {
      if(spriteManager.canAnimateMore())
      {
        if(spriteManager.showBlast())
        {
          iI = new ImageIcon(spriteManager.getBlast());
          iI.paintIcon(this, g, spriteManager.getBlastX(), spriteManager.getBlastY());
        }
        else
        {
          g.setColor(Color.white);
          g.fillRect(280, 515, 280, 60);
          g.setFont(new Font("serif", Font.BOLD, 32));
          g.setColor(Color.red);
          g.drawString("Click to Continue", 300, 550);
        }
        
        String cat = spriteManager.getCategory();
        int score = spriteManager.getScore();
        
        String message = new String("You got: " + score + " points for " + cat);
        
        g.setFont(new Font("serif", Font.BOLD, 32));
        g.setColor(Color.black);
        g.drawString(message, 100, 100);
      }
    }
    else if(spriteManager.getType().equals("win") || spriteManager.getType().equals("lose"))
    {
      g.setFont(new Font("serif", Font.BOLD, 32));
      g.setColor(Color.red);
      g.drawString("Click to Continue", 250, 450);
    }
    
    g.finalize();
    validate();
  }
}