package main.java.game;
/**
 * The original Dice class. Contains an array of a private class Die.
 *
 * The Dice class contains various methods for rolling dice. It creates an array
 * of 5 Die(s) and randomizes each.
 *
 * @author Cory Dissinger
 * @version $Id: $Id
 */
import java.util.Random;
public class Dice
{
  protected Die [] theDice;
   
  /**
   * Dice class constructor. Takes no parameters, instantiates array of 5 Die.
   */
  public Dice()
  {
    theDice = new Die[5];
  
    for(int i = 0; i < 5; i++)
      theDice[i] = new Die();    
  }
  
  /**
   * Calls each Die's roll() method.
   */
  public void roll()
  {
    for(int i = 0; i < 5; i++)
      theDice[i].roll(); 
  }
  
  /**
   * Takes in an array of int to manually set the dice values. Used with cheats.
   *
   * @param newDice The array of new die values.
   */
  public void setDiceValues(int [] newDice)
  {
    for(int i = 0; i < 5; i++)
    {
      theDice[i].setValue(newDice[i]);
    }
  }
    
  /**
   * Returns an int array of values representing the current dice values.
   *
   * @return An array of the five dice face values.
   */
  public int [] getDiceValues()
  {
    int [] temp = new int[5];
    
    for(int i = 0; i < 5; i++)
      temp[i] = theDice[i].getValue();
    
    return temp;
  }
  
  protected class Die
  {
    private int value;      //The die's current value.
    private Random rng; //Random Number Generator.
    
    //Function Name: Constructor
    //Description: Instantiates an instance of the Die object. 
    //Parameters: None.
    //Return: None.
    public Die()
    {
      rng = new Random();
    }
  
    //Function Name: setValue
    //Description: Sets the face value of the die.
    //Parameters: int newVal - The new value.
    //Return: None.
    private void setValue(int newVal)
    {
      value = newVal;
    }
  
    //Function Name: getValue
    //Description: Gets the current face value.
    //Parameters: None
    //Return: int value - Current face value.
    public int getValue()
    {
      return value;
    }
    
    //Function Name: roll
    //Description: Uses the rng to assign a new face value.
    //Parameters: None.
    //Return: None.
    public void roll()
    {
      int randNum = rng.nextInt(6) + 1;
      setValue(randNum);      
    }  
  }
}

