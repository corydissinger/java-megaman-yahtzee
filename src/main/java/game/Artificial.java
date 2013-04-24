package main.java.game;
/**
 * Artificial is one of the implementations of the abstract base class, Player.
 * It has a multitude of methods to make decisions and handle a Turn on it's
 * own. 
 *   
 * @author Cory Dissinger  
 */
 

public class Artificial extends Player
{
  private int rollsTaken;
  private int turnsTaken;
  private boolean keepGoing;
  private String chosenCat;
  private String turnInfo;
  private Turn theTurn;

  /**
   * Constructor for the Artificial class.
   * 
   * @param name The name to be assigned to this Player.
   */           
  public Artificial(String name)
  {
    super(name);
    rollsTaken = 0;
    turnsTaken = 0;
  }

  /**
   * Method that runs a turn for the artificial player.
   * 
   * executeTurn keeps track of the number of rolls that have been made, and
   * continues rolling all dice until a lower section score is available or
   *  
   * @param turn Turn object which contains the dice and other turn information.
   * 
   * @return A string describing the Artificial's actions during the turn. 
   */                        
  public String executeTurn(Turn turn)
  {
    String returnMessage = new String();
    theTurn = turn;
    resetValues();
    turnsTaken++;
    
    while(keepGoing)
    {
      scoreCard.setWorkingValues(theTurn.getDiceValues());
      determineChoice();     
    }
    
    setScore(chosenCat);  
    
    returnMessage += "Computer Name: " + playerName + "\n";
    returnMessage += "Category Chosen: " + chosenCat + "\n";
    returnMessage += "Total Score: " + getGrandTotal() + "\n";
    
    turnInfo = new String("Category Chosen: " + chosenCat + "\n");
    turnInfo += "Total Score: " + getGrandTotal() + "\n";
    
    return returnMessage;
  }

  /**
   * Returns the string containing turnInfo for use with the GameLog.
   * 
   * @return The string containing the turn information.
   */           
  public String getTurnInfo()
  {
    return turnInfo;
  }

  /**
   * Method that resets all private data members used in decision making.
   */     
  public void resetValues()
  {
    rollsTaken = 0;
    keepGoing = true;
    chosenCat = new String();    
  }

  /**
   * Determines what this artificial player should do.
   * 
   * First checks to see if a lower section score is possible, if so it takes
   * the greatest score available. Otherwise, it will roll the dice again if it
   * can. Otherwise, it will just choose the best score available.
   */                 
  private void determineChoice()
  {
    if(scoreCard.lowSectionPossible())
    {
      chosenCat = new String(getBestScore());
      keepGoing = false;
    }
    else if(canStillRoll())
      theTurn.rollDice();        
    else
    {
      chosenCat = new String(getBestScore());
      keepGoing = false;
    }
  }

  /**
   * Determines if the artificial player still can roll.
   * 
   * @return Returns true if there are rolls left, false otherwise.
   */           
  private boolean canStillRoll()
  {
    if(rollsTaken == 2)
    {
      rollsTaken = 0;
      return false;
    }
    else
    {
      rollsTaken++;
      return true;
    }
  }

  /**
   * Gets the best score the artificial player can take.
   * 
   * @return Returns the name of the category with highest score.
   */           
  private String getBestScore()
  {
    return scoreCard.getHighestScorableCategory();
  }
}