package main.java.game;
/**
 * Solely responsible for loading, drawing, and mainting images for the program. 
 */ 

import java.awt.Image;
import java.net.URL;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ImageIcon;

public class SpriteManager
{
  private static final String IMG_DIR = "/images/"; 
	
  private Vector<ImageIcon> upDiceImages;
  private Vector<ImageIcon> downDiceImages;
  private Vector<ImageIcon> transitionImages;
  private ImageIcon megaManLife;
  private Vector<Image> readyImages;
  private Vector<Image> scoreImages;
  private Vector<Image> blastImages;
  private Image theBlast;
  private ImageIcon pellet, meter;
  private TreeMap<String, Integer> scoreCombo;
  private Yahtzee controller;
  private String currentAnimation, cat;
  private Image currentImage;
  private Image win;
  private Image lose;
  private int currentIndex;
  private int readyCounter;
  private int blastCount;
  private int score;
  private int waitTime;
  private int blastX, blastY;
  private boolean blastNotOffScreen, showBlast;
  
  /**
   * Constructor. Accepts a Yahtzee controller as an argument.
   * 
   * @param jApp The Yahtzee controller.      
   */     
  public SpriteManager(Yahtzee jApp)
  {
    controller = jApp;
    currentAnimation = new String("none");
    currentIndex = 0;
    readyCounter = 0;
  }
  
  /**
   * Loads the winning and losing images.
   */     
  public void loadFinishImages()
  {
    URL url = controller.getClass().getResource(IMG_DIR + "win.png");
    win = controller.getImage(url);
    
    url = controller.getClass().getResource(IMG_DIR + "lose.png");
    lose = controller.getImage(url);
  }
  
  /**
   * Loads each image involved in the score animation.
   */     
  public void loadScoreImages()
  {
    scoreImages = new Vector<Image>();
    
    for(int i = 1; i < 26; i++)
    {
      URL url = controller.getClass().getResource(IMG_DIR + "score" + i + ".png");
      Image img = controller.getImage(url);
      scoreImages.add(img);
    } 
    
    blastImages = new Vector<Image>();
    
    for(int i = 1; i < 4; i++)
    {
      URL url = controller.getClass().getResource(IMG_DIR + "blast" + i + ".png");
      Image img = controller.getImage(url);
      blastImages.add(img);
    }
  }
 
 /**
  * Returns the meter image.
  * 
  * @return The meter Image.    
  */    
  public ImageIcon getMeter()
  {
    return meter;
  }
  
  /**
   * Returns the pellet image.
   * 
   * @return The pellet Image.      
   */     
  public ImageIcon getPellet()
  {
    return pellet;
  }
  
  /**
   * Loads the meter and pellet image.
   */     
  public void loadMeterImages()
  {
    URL url = controller.getClass().getResource(IMG_DIR + "meter.png");
    Image img = controller.getImage(url);
    meter = new ImageIcon(img); 
    
    url = controller.getClass().getResource(IMG_DIR + "pellet.png");
    img = controller.getImage(url);
    pellet = new ImageIcon(img);    
  }
  
  /**
   * Loads the 1UP symbol for tracking rolls left.
   */     
  public void loadLife()
  {
    URL url = controller.getClass().getResource(IMG_DIR + "megaman1up.png");
    Image img = controller.getImage(url);
    megaManLife = new ImageIcon(img);    
  }
  
  /**
   * Returns the megaManLife 1UP ImageIcon.
   */     
  public ImageIcon getLife()
  {
    return megaManLife;
  }
  
  /**
   * Loads both sets of custom made dice.
   */     
  public void loadDiceImages()
  {
    upDiceImages = new Vector<ImageIcon>(6);
    downDiceImages = new Vector<ImageIcon>(6);
    
    for(int i = 0; i < 6; i++)
    {
      URL url = controller.getClass().getResource(IMG_DIR + "dieUp" + (i+1) + ".png");
      System.out.println(url);
      Image img = controller.getImage(url);
      upDiceImages.insertElementAt(new ImageIcon(img), i);
    }
    
    for(int i = 0; i < 6; i++)
    {
      URL url = controller.getClass().getResource(IMG_DIR + "dieDown" + (i+1) + ".png");
      Image img = controller.getImage(url);
      downDiceImages.insertElementAt(new ImageIcon(img), i);
    }
  }
  
  /**
   * Loads each image that controls the ready animation.
   */     
  public void loadReadyImages()
  {
    readyImages = new Vector<Image>();
    URL url;
    Image img;
    
    url = controller.getClass().getResource(IMG_DIR + "readyGo.png");
    img = controller.getImage(url);
    readyImages.add(img);
    
    url = controller.getClass().getResource(IMG_DIR + "readyNo.png");
    img = controller.getImage(url);
    readyImages.add(img);
    
    for(int i = 1; i <= 15; i++)
    {
      url = controller.getClass().getResource(IMG_DIR + "tele" + i + ".png");
      img = controller.getImage(url);
      readyImages.add(img);
    }
  }
 
 /**
  * Checks to see if the current animation can continue.
  * 
  * @return True if the animation can continue, false otherwise.    
  */   
  public boolean canAnimateMore()
  {
    if(currentAnimation.equals("ready"))
    {
      if(currentIndex == readyImages.size() - 1)
        return false;
    }
    else if(currentAnimation.equals("score"))
    {
      if(blastNotOffScreen)
        return true;
      else
        return false;
    }
    else if(currentAnimation.equals("win") || currentAnimation.equals("lose"))
      return false;
    
    return true;
  }
  
  /**
   * Modifies the blast animation based on what score was received.
   * 
   * @param message Name of the category to be displayed.
   * 
   * @param number The number of points received.            
   */     
  public void setBlastInfo(String message, int number)
  {
    cat = new String(message);
    score = number;
    blastCount = 0;
    
    if(score <= 20)
      theBlast = blastImages.elementAt(0);
    else if(score >= 21 && score < 40)
      theBlast = blastImages.elementAt(1);
    else
      theBlast = blastImages.elementAt(2);
      
    blastNotOffScreen = true;
    blastX = 220;
    blastY = 290 - (theBlast.getHeight(controller)/2); 
  }
  
  /**
   * Prepares an animation for display.
   * 
   * @param name The name of the animation to be prepared.      
   */     
  public void setAnimation(String name)
  {
    readyCounter = 0;
    currentIndex = 0;
    currentAnimation = new String(name);
    
    if(name.equals("ready"))
    {
      currentImage = readyImages.elementAt(0);
      waitTime = 200;
    }
    else if(name.equals("score"))
    {
      currentImage = scoreImages.elementAt(0);
      waitTime = 75;
      showBlast = false;
    }
    else if(name.equals("win"))
    {
      currentImage = win;
    }
    else if(name.equals("lose"))
    {
      currentImage = lose;
    }
  }
 
  /**
   * Returns the x coordinate of the blast.
   * 
   * @return The blast's x coordinate.      
   */      
  public int getBlastX()
  {
    return blastX;
  }
  
  /**
   * Returns the y coordinate of the blast.
   * 
   * @return The blast's y coordinate.      
   */     
  public int getBlastY()
  {
    return blastY;
  }
  
  /**
   * Returns the current image, used with animations.
   * 
   * @return The current frame in animation.      
   */     
  public Image getCurrentImage()
  {
    return currentImage;
  }
  
  /**
   * If a specific wait time is used, this returns it.
   * 
   * @return The wait time, in milliseconds.      
   */     
  public int getWaitTime()
  {
    return waitTime;
  }
  
  /**
   * Returns a string describing what type of animation is currently set.
   * 
   * @return String describing the current type.      
   */     
  public String getType()
  {
    return currentAnimation;
  }
 
 /**
  * Updates the current image, if possible.
  */    
  public void updateImage()
  {  
    if(currentAnimation.equals("ready"))
      updateReadyImage();
    else if(currentAnimation.equals("score"))
      updateScoreImage();
  }
  
  /**
   * Custom-tailored to update the readyImages Vector.
   */     
  public void updateReadyImage()
  {    
    if(currentIndex == 0)
    {
      currentIndex = 1;
      currentImage = readyImages.elementAt(currentIndex);
      waitTime = 300;
      return;
    }
    else if(currentIndex == 1 && readyCounter != 5)
    {
      currentIndex = 0;
      readyCounter++;
      currentImage = readyImages.elementAt(currentIndex);
      waitTime = 300;
      return;  
    }
    
    waitTime = 100;
    if(canAnimateMore())
    {
      currentIndex++;
      currentImage = readyImages.elementAt(currentIndex);    
    }
  }
  
  /**
   * Custom tailored to update the scoreImages Vector.
   */     
  public void updateScoreImage()
  {
    if(currentIndex != scoreImages.size() - 1) 
    {
      currentIndex++;
      currentImage = scoreImages.elementAt(currentIndex);
      return;       
    }
    else if(canAnimateMore())
    {
      showBlast = true; 
      currentImage = scoreImages.elementAt(scoreImages.size()-1);
      //Blast starts at 220, 290 on the last image.
      blastX = 220 + (blastCount * 10);
      blastCount++;
      if(blastX > 600)
        blastNotOffScreen = false;
    }
  }
  
  /**
   * Returns the blast's status flag.
   * 
   * @return True if the blast can be seen, false otherwise.      
   */        
  public boolean showBlast()
  {
    if(showBlast)
      return true;
    else
      return false;
  }
  
  /**
   * Returns the blast's current image.
   * 
   * @return Image object of the blast.
   */           
  public Image getBlast()
  {
    return theBlast;
  }
  
  /**
   * Returns the stored category name for the score animation.
   * 
   * @return Category name.      
   */     
  public String getCategory()
  {
    return cat;
  }
  
  /**
   * Returns the stored score for the category in the score animation.
   * 
   * @return The points valley of the category.      
   */     
  public int getScore()
  {
    return score;
  }
  
  /**
   * Returns the Vector of ImageIcons related to the unheld Dice.
   * 
   * @return The upDice Vector of ImageIcons      
   */     
  public Vector<ImageIcon> getUpDiceImages()
  {
    return upDiceImages;
  }
  
  /**
   * Returns the Vector of ImageIcons related to the held dice.
   * 
   * @return The downDice Vector of ImageIcons.      
   */     
  public Vector<ImageIcon> getDownDiceImages()
  {
    return downDiceImages;
  }
}