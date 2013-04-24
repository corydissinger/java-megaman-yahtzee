package main.java.game;
/**
 * YahtzeeScoreCard class implementation. Holds many methods for the storing
 * and manipulation of Yahtzee scores.
 */  
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class YahtzeeScoreCard
{
  private TreeMap<String,Integer> comboPoints;
  private TreeMap<String,Integer> possiblePoints;
  private TreeMap<String, Boolean> usedPoints;
  private boolean bonusTurn;
  private int [] workingValues;
  
  /**
   * Constructor. Creates an entry for each scoreable category.
   */     
  public YahtzeeScoreCard()
  {
    workingValues = new int[5];
    bonusTurn = false;
    comboPoints = new TreeMap<String, Integer>();
    
    //Upper Section
    comboPoints.put(new String("Aces"), 0);
    comboPoints.put(new String("Twos"), 0);
    comboPoints.put(new String("Threes"), 0);
    comboPoints.put(new String("Fours"), 0);
    comboPoints.put(new String("Fives"), 0);
    comboPoints.put(new String("Sixes"), 0);
    comboPoints.put(new String("Upper Bonus"), 0);
    
    //Lower Section
    comboPoints.put(new String("3 of a Kind"), 0);
    comboPoints.put(new String("4 of a Kind"), 0);
    comboPoints.put(new String("Full House"), 0);
    comboPoints.put(new String("Small Straight"), 0);
    comboPoints.put(new String("Large Straight"), 0);
    comboPoints.put(new String("YAHTZEE"), 0);
    comboPoints.put(new String("Chance"), 0);
    
    //Totals.
    comboPoints.put(new String("Total of Upper Section"), 0);
    comboPoints.put(new String("Total of Lower Section"), 0);
    comboPoints.put(new String("GRAND TOTAL"), 0);
       
    //Make the possiblePoint map because it is paralell to comboPoints
    possiblePoints = new TreeMap<String, Integer>(comboPoints); 
    
    //Remove extraneous categories from possiblePoints.
    possiblePoints.remove("Total of Upper Section");
    possiblePoints.remove("Total of Lower Section");
    possiblePoints.remove("GRAND TOTAL");
    possiblePoints.remove("Upper Bonus"); 
    
    usedPoints = new TreeMap<String, Boolean>();
    for(Map.Entry<String, Integer> entry: comboPoints.entrySet())
    {
      String name = entry.getKey();
      
      usedPoints.put(new String(name), false);
    }
    
    //Remove extraneous categories from usedPoints.
    usedPoints.remove("Total of Upper Section");
    usedPoints.remove("Total of Lower Section");
    usedPoints.remove("GRAND TOTAL");
    usedPoints.remove("Upper Bonus");  
  }
  
  public boolean hasCategoriesLeft()
  {
    for(Map.Entry<String, Boolean> entry : usedPoints.entrySet())
    {
      if(!entry.getValue())
        return true;
    }
    
    return false;
  }
  
  /**
   * attemptSet tries to set score into a category.
   * 
   * @param catName The name of the category to be set.
   * @return Returns true if sucessful, false otherwise.
   */              
  public boolean attemptSet(String catName)
  {
    if(categoryAlreadySet(catName))
    {
      setCategory(catName);
      return true;
    }
    else
      return false;
  }
  
  public void cheatSet(String catName, int catScore)
  {
    if(!usedPoints.containsKey(catName))
      return;  
    
    if(!usedPoints.get(catName))
    {
      calcYahtzeeBonus();
      comboPoints.put(catName, catScore);
      usedPoints.put(catName, true);
      calcUpperBonus();
      calcTotals();    
    }
  }
  
  /**
   * Used with attemptSet. Determines if a player has already set a category.
   * 
   * @param catName The name of the category to be checked.
   * @return Returns true if the category is already set, false otherwise.
   */              
  public boolean categoryAlreadySet(String catName)
  {
    if(usedPoints.containsKey(catName))
      if(usedPoints.get(catName))
        return false;
      else
        return true;
    else
      return false;
  }
  
  /**
   * getPossiblePoints returns a mapping of possible categories to scores.
   * 
   * @return The mapping of possible scoreable categories to scores.
   */           
  public TreeMap<String, Integer> getPossiblePoints()
  {
    TreeMap<String, Integer> onlyPossible = new TreeMap<String, Integer>();
    
    for(Map.Entry<String, Integer> entry : possiblePoints.entrySet())
    {
      if(usedPoints.get(entry.getKey()))
        continue;
    
      int tempPoints = entry.getValue();
      String tempName = new String(entry.getKey());
      onlyPossible.put(tempName, tempPoints);
    }
    
    return onlyPossible;  
  }
  
  /**
   * Grabs the scores the player has set previously.
   * 
   * @return The String to Integer pairs representing a category and that score.
   */
  public TreeMap<String, Integer> getSetPoints()
  {
    TreeMap<String, Integer> onlySet = new TreeMap<String, Integer>();
    
    for(Map.Entry<String, Integer> entry : comboPoints.entrySet())
    {
      if(usedPoints.containsKey(entry.getKey()))
      {
        if(usedPoints.get(entry.getKey()))
        {
          int tempPoints = entry.getValue();
          String tempName = new String(entry.getKey());
          onlySet.put(tempName, tempPoints);
        }
      }
    }
    
    return onlySet;
  }            
  
  /**
   * Called if attemptSet is successful. Sets a score into comboPoints.
   * 
   * @param catName The name of the category to be set.
   */           
  private void setCategory(String catName)
  {
    calcYahtzeeBonus();
    comboPoints.put(catName, possiblePoints.get(catName));
    usedPoints.put(catName, true);
    calcUpperBonus();
    calcTotals();
  }
  
  /**
   * Returns the int score of a specific category.
   * 
   * @param category The category's score to be returned.
   * @return The integer score of the category.
   */              
  public int getCategoryScore(String category)
  {
    return comboPoints.get(category);
  }
  
  /**
   * Used with Artificial. Determines if any of the lower categories are possible.
   * 
   * @return True if a lower section is scoreable, false otherwise.
   */           
  public boolean lowSectionPossible()
  {
    if(possiblePoints.get("Small Straight") != 0 && !usedPoints.get("Small Straight"))
      return true;
    if(possiblePoints.get("Large Straight") != 0 && !usedPoints.get("Large Straight"))
      return true;
    if(possiblePoints.get("3 of a Kind") != 0 && !usedPoints.get("3 of a Kind"))
      return true;
    if(possiblePoints.get("4 of a Kind") != 0 && !usedPoints.get("4 of a Kind"))
      return true;
    if(possiblePoints.get("Full House") != 0 && !usedPoints.get("Full House"))
      return true;
    if(possiblePoints.get("YAHTZEE") != 0 && !usedPoints.get("YAHTZEE"))
      return true;
    else
      return false;
  }
  
  /**
   * getHighestScorableCategory returns the name of the highest points category.
   * 
   * @return The name of the highest scoring category.
   */           
  public String getHighestScorableCategory()
  {
    int highestScore = 0;
    String category = new String();
  
    for(Map.Entry<String, Integer> entry: possiblePoints.entrySet())
    {
      String tempCat = new String(entry.getKey());
      int tempScore = entry.getValue();
      
      if(tempScore >= highestScore && !usedPoints.get(tempCat))
      {
        highestScore = tempScore;
        category = tempCat;   
      }
    }
    
    return category;
  }
  
  //Function Name: getGrandTotal
  //Description: Gets the Grand Total score category.
  //Parameters: None.
  //Return: int - The player's Grand Total score.
  public int getGrandTotal()
  {
    return comboPoints.get("GRAND TOTAL");
  }
  
  //Function Name: getBonusTurn
  //Description: Grants the player a bonus turn if true. Then, toggles the bonus
  //             appropriately.
  //Parameters: None.
  //Return: true - If the player deserves a bonus turn.
  //        false - Otherwise.
  public boolean getBonusTurn()
  {
    if(bonusTurn)
    {
      toggleBonusTurn();
      return true;
    }
    else
      return false;
  }
  
  //Function Name: setWorkingValues
  //Description: Takes in an array, sets "workingValues" to the same values
  //             as the array. workingValues represents the dice face values.
  //Parameters: int [] theValues - The new values to be set.
  //Return: None.
  public void setWorkingValues(int [] theValues)
  {
    for(int i = 0; i < 5; i++)
      workingValues[i] = theValues[i];
      
    calcAllPossible();
  }
  
  //Function Name: calcAllPossible
  //Description: Updates all possible scores based on current working
  //             values. These scores are not set to the players final
  //             values, however.
  //Parameters: None.
  //Return: None.
  private void calcAllPossible()
  {
    resetAllPossible();
  
    calcAces();
    calcTwos();
    calcThrees();
    calcFours();
    calcFives();
    calcSixes();  
    calcSmallStraight();   
    calcLargeStraight(); 
    calcThreeKind();
    calcFourKind();
    calcFullHouse();
    calcYahtzee();
    calcChance();
  }
  
  //Function Name: resetAllPossible
  //Description: Sets all possible scores back to 0.
  //Parameters: None.
  //Return: None.
  private void resetAllPossible()
  {
    for(Map.Entry<String, Integer> entry : possiblePoints.entrySet())
    {
      String key = entry.getKey();
      int val = 0;
      
      possiblePoints.put(new String(key), val);
    } 
  }
  
  //Function Name: calcOnes
  //Description: Determines the possible "Ones" category score.
  //Parameters: None.
  //Return: None.
  private void calcAces()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 1)
        temp++;
        
    possiblePoints.put(new String("Aces"), temp);
  }
  
  //Function Name: calcTwos
  //Description: Determines the possible "Twos" category score.
  //Parameters: None.
  //Return: None.
  private void calcTwos()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 2)
        temp++;
     
    temp = temp * 2;
        
    possiblePoints.put(new String("Twos"), temp);
  }  

  //Function Name: calcThrees
  //Description: Determines the possible "Threes" category score.
  //Parameters: None.
  //Return: None.
  private void calcThrees()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 3)
        temp++;
     
    temp = temp * 3;
        
    possiblePoints.put(new String("Threes"), temp);
  } 

  //Function Name: calcFours
  //Description: Determines the possible "Fours" category score.
  //Parameters: None.
  //Return: None.  
  private void calcFours()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 4)
        temp++;
     
    temp = temp * 4;
        
    possiblePoints.put(new String("Fours"), temp);
  } 

  //Function Name: calcFives
  //Description: Determines the possible "Fives" category score.
  //Parameters: None.
  //Return: None.  
  private void calcFives()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 5)
        temp++;
     
    temp = temp * 5;
        
    possiblePoints.put(new String("Fives"), temp);
  } 

  //Function Name: calcSixes
  //Description: Determines the possible "Sixes" category score.
  //Parameters: None.
  //Return: None.  
  private void calcSixes()
  {
    int temp = 0;
    
    for(int i = 0; i < 5; i++)
      if(workingValues[i] == 6)
        temp++;
     
    temp = temp * 6;
        
    possiblePoints.put(new String("Sixes"), temp);
  } 
  
  //Function Name: calcSmallStraight
  //Description: Determines if the player has a small straight, then adds 30.
  //Parameters. None.
  //Return: None.
  private void calcSmallStraight()
  {
    int [] temp = (int[])workingValues.clone();
    
    Arrays.sort(temp);
    
    if(determineSmallStraight(temp))
      possiblePoints.put(new String("Small Straight"), 30);
  }
  
  //Function Name: calcLargeStraight
  //Description: Determines if the player has a large straight, then adds 40.
  //Parameters: None.
  //Return: None.
  private void calcLargeStraight()
  {
    int [] temp = (int[])workingValues.clone();
    
    Arrays.sort(temp);
    
    if(determineLargeStraight(temp))
      possiblePoints.put(new String("Large Straight"), 40);
  }
  
  //Function Name: calcThreeKind
  //Description: Determines if the player has three of a kind, then adds the
  //             sum of all dice to the player's score in that category.
  //Parameters: None.
  //Return: None.
  private void calcThreeKind()
  {
    int [] temp = (int[])workingValues.clone();
    
    Arrays.sort(temp);
    
    if(determineThreeKind(temp))
      possiblePoints.put(new String("3 of a Kind"), getSum());
  }
  
  //Function Name: calcFourKind
  //Description: Determines if the player has four of a kind, then adds the
  //             sum of all dice to the player's score in that category.
  //Parameters: None.
  //Return: None.
  private void calcFourKind()
  {
    int [] temp = (int[])workingValues.clone();
    
    Arrays.sort(temp);
    
    if(determineFourKind(temp))
      possiblePoints.put(new String("4 of a Kind"), getSum());
  }
 
 //Function Name: calcFullHouse
 //Description: Determines if the player has a full house, then adds 25 to
 //             the players possible score in that category.
 //Parameters: None.
 //Return: None.
  private void calcFullHouse()
  {
    int [] temp = (int[])workingValues.clone();
    
    Arrays.sort(temp);
    
    if(determineFullHouse(temp))
      possiblePoints.put(new String("Full House"), 25);
  }
  
  //Function Name: calcYahtzee
  //Description: Determines if the player has 5 of a kind, then adds 50 to the
  //             players score in the Yahtzee category.
  //Parameters: None.
  //Return: None.
  private void calcYahtzee()
  {
    int theNumber = workingValues[0];
    int counter = 0;
    
    for(int i = 0; i < 5; i ++)
    {
      if(workingValues[i] == theNumber)
        counter++;
    }    
    
    if(counter == 5)
      possiblePoints.put(new String("YAHTZEE"), 50);
  }
  
  //Function Name: calcYahtzeeBonus
  //Description: If the player already has a Yahtzee, this gives them 100
  //             points in the Yahtzee Bonus as well as an additional turn.
  //Parameters: None.
  //Return: None.
  private void calcYahtzeeBonus()
  {
    if(comboPoints.get("YAHTZEE") == 50 && possiblePoints.get("YAHTZEE") == 50)
      toggleBonusTurn();
  }
  
  //Function Name: toggleBonusTurn
  //Description: Changes to bonusTurn data member to the inverse value, 
  //             true to false and false to true.
  //Parameters: None.
  //Return: None.
  private void toggleBonusTurn()
  {
    bonusTurn = !bonusTurn;
  }
  
  //Function Name: calcChance
  //Description: Adds the player's current die scores to the Chance category.
  //Parameters: None.
  //Return: None.
  private void calcChance()
  {
    possiblePoints.put(new String("Chance"), getSum());
  }
  
  //Function Name: calcTotals
  //Description: Calculates the total of the upper and lower section, as well
  //             as the grand total.
  //Parameters: None.
  //Return: None.
  private void calcTotals()
  {
    comboPoints.put(new String("Total of Upper Section"), getUpperSum());
    comboPoints.put(new String("Total of Lower Section"), getLowerSum());
    comboPoints.put(new String("GRAND TOTAL"), (getUpperSum() + getLowerSum()));    
  }
  
  //Function Name: calcUpperBonus
  //Description: If the player has 63 or more points in the upper section,
  //             they are awarded a 35 point bonus.
  //Parameters: None.
  //Return: None.
  private void calcUpperBonus()
  { 
    if(getUpperSum() > 63)
      comboPoints.put(new String("Upper Bonus"), 35); 
  }
  
  //Function Name: determineSmallStraight
  //Description: Determines if a small straight is present in the player's
  //             current working values.
  //Parameters: int [] temp - The sorted array of working values.
  //Return: true - If a small straight was found.
  //        false - Otherwise.
  private boolean determineSmallStraight(int [] temp)
  {
    int strCounter = 0;
    
    for(int i = 0; i < 4; i++)
    {
      if(temp[i] == (temp[i+1] - 1))
        strCounter++;
      else if(temp[i] == temp[i+1])
        strCounter = strCounter;
      else
        strCounter = 0;
       
      if(strCounter == 3)
        return true;
    }
    
    return false; 
  }
  
  //Function Name: determineLargeStraight
  //Description: Determine if a large straight is present in the player's
  //             current working values.
  //Parameters: int [] temp - The sorted array of working values.
  //Return: true - If a large straight was found.
  //        false - Otherwise.
  private boolean determineLargeStraight(int [] temp)
  {
    if(temp[0] == 1)
    {
      for(int i = 0; i < 5; i++)
      {
        if(temp[i] != i+1)
          return false;
      }
      return true;
    }
      
    if(temp[0] == 2)
    {
      for(int i = 0; i < 5; i++)
      {
        if(temp[i] != i+2)
          return false;
      }
      return true;
    }
    
    return false;
  }
  
  //Function Name: determineThreeKind
  //Description: Determine if a three of a kind is present.
  //Parameters: int [] temp - The sorted array of working values.
  //Return: true - If three of a kind was found.
  //        false - Otherwise.
  private boolean determineThreeKind(int [] temp)
  {
    int matchCounter = 0;
  
    for(int i = 0; i < 4; i++)
    {
      if(temp[i] == temp[i+1])
        matchCounter++;
      else
        matchCounter = 0;
        
      if(matchCounter == 2)
        return true;
    }
    
    return false;
  }
  
  //Function Name: determineFourKind
  //Description: Determine if a four of a kind is present.
  //Parameters: int [] temp - The sorted array of working values.
  //Return: true - If four of a kind was found.
  //        false - Otherwise.
  private boolean determineFourKind(int [] temp)
  {
    int matchCounter = 0;
    
    for(int i = 0; i < 4; i++)
    {
      if(temp[i] == temp[i+1])
        matchCounter++;
      else
        matchCounter = 0;
        
      if(matchCounter == 3)
        return true;
    }
    
    return false;
  }
  
  //Function Name: determineFullHouse
  //Description: Determine if a full house is present.
  //Parameters: int [] temp - The sorted array of working values.
  //Return: true - If a full house was found.
  //        false - Otherwise.
  private boolean determineFullHouse(int [] temp)
  {
    int firstVal = temp[0];
    int secondVal = 0;
    int firstCount = 0;
    int secondCount = 0;
    int counter = 0;
    
    //Count how many times the first value appears.
    while(counter < 5)
    {
      if(firstVal == temp[counter])
      {
        counter++;
        firstCount++;
      }
      else
      {
        secondVal = temp[counter];
        secondCount++;
        counter++;
        break;
      }
    }
    
    //If more than five OR the same as the second value, there is no full
    //house.
    if(counter == 5)
      return false;
    
    if(firstVal == secondVal)
      return false;
    
    //Count occurrence of second value.  
    while(counter < 5)
    {
      if(secondVal == temp[counter])
      {
        counter++;
        secondCount++;
      }
      else
        break;
    }
    
    //Final checks.
    if(firstCount == 2 && secondCount == 3)
      return true;
    if(firstCount == 3 && secondCount == 2)
      return true;
    else
      return false;
  }
  
  //Function Name: getSum
  //Description: Returns the sum of all the workingValues.
  //Parameters: None
  //Return: int sum - The sum of all working values.
  private int getSum()
  {
    int sum = 0;
    
    for(int i = 0; i < 5; i++)
      sum += workingValues[i];
      
    return sum;
  }
  
  //Function Name: getUpperSum
  //Description: Returns the sum of all upper section scores.
  //Parameters: None.
  //Return: int sum - The sum of all upper section scores.
  private int getUpperSum()
  {
    int sum = 0;
    
    sum += comboPoints.get("Aces");
    sum += comboPoints.get("Twos");
    sum += comboPoints.get("Threes");
    sum += comboPoints.get("Fours");  
    sum += comboPoints.get("Fives");
    sum += comboPoints.get("Sixes");
    sum += comboPoints.get("Upper Bonus");
    
    return sum;
  }
  
  //Function Name: getLowerSum
  //Description: Returns the sum of all the lower section scores.
  //Parameters: None.
  //Return: int sum - The sum of all lower section scores.
  private int getLowerSum()
  {
    int sum = 0;
    
    sum += comboPoints.get("3 of a Kind");
    sum += comboPoints.get("4 of a Kind");
    sum += comboPoints.get("Full House");   
    sum += comboPoints.get("Small Straight");
    sum += comboPoints.get("Large Straight");
    sum += comboPoints.get("YAHTZEE");
    sum += comboPoints.get("Chance");
    
    return sum;
  }
  
  //Function Name: scorableCategories
  //Description: Returns a string showing the names of scorable categories.
  //Parameters: None.
  //Return: String temp - The string with names of scorable categories.
  public String scorableCategories()
  {
    String temp = new String();
    
    temp = "You can enter a score in the following categories:\n";
    
    for(Map.Entry<String, Boolean> entry: usedPoints.entrySet())
    {
      String catName = entry.getKey();
      boolean isSet = entry.getValue();
      
      if(!isSet)
        temp += catName + "\n";
    }
    
    return temp;
  }
  
  //Function Name: getPossibleScores
  //Description: Returns a string showing what categories the player can
  //             receive a score for.
  //Parameters: None.
  //Return: String temp - The string containing all possible scores.
  public String getPossibleScores()
  { 
    String temp = new String();
   
    temp = "\n Showing all POSSIBLE scores. \n";
    temp += "------------------------------------\n";
    temp += "***        UPPER SECTION         ***\n";
    temp += "------------------------------------\n";
    temp += possiblePoints.floorEntry("Aces").toString() + "\n";
    temp += possiblePoints.floorEntry("Twos").toString() + "\n";
    temp += possiblePoints.floorEntry("Threes").toString() + "\n";
    temp += possiblePoints.floorEntry("Fours").toString() + "\n";
    temp += possiblePoints.floorEntry("Fives").toString() + "\n";
    temp += possiblePoints.floorEntry("Sixes").toString() + "\n";
    temp += "------------------------------------\n";
    temp += "***        LOWER SECTION         ***\n";
    temp += "------------------------------------\n";
    temp += possiblePoints.floorEntry("3 of a Kind").toString() + "\n";    
    temp += possiblePoints.floorEntry("4 of a Kind").toString() + "\n"; 
    temp += possiblePoints.floorEntry("Full House").toString() + "\n";  
    temp += possiblePoints.floorEntry("Small Straight").toString() + "\n"; 
    temp += possiblePoints.floorEntry("Large Straight").toString() + "\n";
    temp += possiblePoints.floorEntry("YAHTZEE").toString() + "\n"; 
    temp += possiblePoints.floorEntry("Chance").toString() + "\n";     
    temp += "------------------------------------\n";
        
    return temp;  
  }
  
  //Function Name: getActualScores
  //Description: This creates a string containing all of the player's current
  //             scores, and returns it.
  //Parameters: None.
  //Return: String temp - The string containing all scoring information.
  public TreeMap<String, Integer> getActualScores()
  {
    return comboPoints;
  }
}