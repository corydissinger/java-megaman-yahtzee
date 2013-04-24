package main.java.game;

/**
 * Implementation for the Turn class. Turn represents one Yahtzee turn.
 * 
 * Turn contains an object of YahtzeeDice and keeps track of the number of times
 * they have been rolled. 
 * 
 * @author Cory Dissinger
 */      


public class Turn
{
  private YahtzeeDice theDice;
  private int throwsLeft;
  private boolean turnStatus;
  
  /**
   * Constructor for the Turn class. Initializees all private data members.
   */     
  public Turn()
  {
    theDice = new YahtzeeDice();
    throwsLeft = 2;
    turnStatus = true;
    theDice.roll(); //Initial randomization.
  }
  
  /**
   * getThrows returns the number of throws the player currently has left.
   * 
   * @return The int number of throws the player has left.
   */           
  public int getThrows()
  {
    return throwsLeft;
  }
  
  /**
   * endTurn sets turnStatus to false
   */     
  public void endTurn()
  {
    turnStatus = false;
  }
  
  /**
   * holdADie holds a user specified die number.
   * 
   * @param dieNo Index of the die to be held.
   */           
  public void holdADie(int dieNo)
  {
    theDice.holdDie(dieNo);
  }
  
  /**
   * rollDice calls the roll() method on each Die unless there are no throws left.
   */        
  public void rollDice()
  {
    if(throwsLeft != 0)
    {
      theDice.roll();  
      throwsLeft--;
    }
  }
  
  /**
   * continueTurn returns the current turn status.
   * 
   * @return True if the turn can continue, false otherwise.
   */           
  public boolean continueTurn()
  {
    return turnStatus;
  }
  
  /**
   * getDiceValues returns an array of ints representing Dice values.
   * 
   * @return The array containing dice values.
   */           
  public int [] getDiceValues()
  {
    return theDice.getDiceValues();
  }
  
  /**
   * Changes the die values according to the nefarious user.
   * 
   * @param newDice The int array of new dice values.      
   */     
  public void setDiceValues(int [] newDice)
  {
    theDice.setDiceValues(newDice);
  }
}