package main.java.game;
/**
 * Abstract Player base class. Human and Artificial are derived from this.
 *
 * Player contains method that a player playing Yahtzee would likely need to
 * do. Rolling dice, checking scores, holding dice, and others are methods
 * defined in this class.
 *
 * @author Cory Dissinger
 * @version $Id: $Id
 */


import java.util.TreeMap;
public class Player
{
  String playerName, recentCat;
  YahtzeeScoreCard scoreCard;
  boolean cheated;
  int turnsTaken, recentScore;                                                            
  
  /**
   * Constructor for a human Player. Takes in their name, initializes data.
   *
   * @param name The name of the player to be created.
   */
  public Player(String name)
  {
    playerName = name;
    turnsTaken = 0;
    cheated = false;
    scoreCard = new YahtzeeScoreCard();
  }
  
  /**
   * Default constructor.
   */
  public Player(){}
  
  /**
   * setScore takes in a player chosen score and locks it into YahtzeeScoreCard.
   *
   * @param scoreName The name of the scoring category.
   */
  public void setScore(String scoreName)
  {
    if(scoreCard.attemptSet(scoreName))
    {
      recentCat = new String(scoreName);
      recentScore = getScore(scoreName);
    }
      
    turnsTaken++; 
  }
  
  /**
   * getGrandTotal returns the this player's upper, lower, and bonus score.
   *
   * @return The integer value of the total.
   */
  public int getGrandTotal()
  {
    return scoreCard.getGrandTotal();
  }
  
  /**
   * <p>cheatScore.</p>
   *
   * @param category a {@link java.lang.String} object.
   * @param score a int.
   */
  public void cheatScore(String category, int score)
  {
    scoreCard.cheatSet(category, score);
  }
  
  /**
   * getPossibleScores returns a mapping of categories to points.
   *
   * @return The TreeMap is used within the HumanMenu class mostly.
   */
  public TreeMap<String, Integer> getPossibleScores()
  {
    return scoreCard.getPossiblePoints();
  }
  
  /**
   * Returns the set scores of the player only.
   *
   * @return The TreeMap with set score name to point mapping.
   */
  public TreeMap<String, Integer> getSetScores()
  {
    return scoreCard.getSetPoints();
  }           
  
  /**
   * getActualScores returns a mapping of used categories to points.
   *
   * @return The TreeMap is used within ScoresMenu to display scores.
   */
  public TreeMap<String, Integer> getActualScores()
  {
    return scoreCard.getActualScores();
  }

  /**
   * getTurnsLeft returns the number of turns the player has left.
   *
   * @return The number of turns the player has left.
   */
  public int getTurnsLeft()
  {
    return (13 - turnsTaken);  
  }
  
  /**
   * setDiceValues overwrites the current dice values with the presented ones.
   *
   * @param theDice The array of ints representing dice values.
   */
  public void setDiceValues(int [] theDice)
  {
    scoreCard.setWorkingValues(theDice);  
  }
  
  /**
   * turnFinished increments the number of turns taken by one.
   */
  public void turnFinished()
  {
    turnsTaken++;
  }
  
  /**
   * Returns the most recently set category.
   *
   * @return String representing most recently chosen category.
   */
  public String getRecentCat()
  {
    return recentCat;
  }
 
  /**
   * Returns the most recently set score.
   *
   * @return Integer represting most recently chosen category's score.
   */
  public int getRecentScore()
  {
    return recentScore;
  }
  
  /**
   * Returns the player's assigned name.
   *
   * @return The player's name.
   */
  public String getName()
  {
    return playerName;
  }

  /**
   * Returns the score value for a category.
   *
   * @param name The name of the category
   * @return The points in that category.
   */
  public int getScore(String name)
  {
    return scoreCard.getCategoryScore(name);
  }

   /**
    * Changes the player's cheated flag to true.
    */
   public void cheated()
   {
    cheated = true;
   }    

  /**
   * Returns the player's ability to continue the turn.
   *
   * @return Returns true if 13 turns have been taken, false otherwise.
   */
  public boolean canContinue()
  {
    if(turnsTaken == 12)
      return false;
    else if(scoreCard.hasCategoriesLeft())
      return true; 
    else
      return false;  
  }
}
