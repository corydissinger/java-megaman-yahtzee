package main.java.game;

/**
 * YahtzeeDice extends the Dice class so that it can hold dice.
 *
 * @author Cory
 * @version $Id: $Id
 */
public class YahtzeeDice extends Dice
{
  private boolean [] activeDice = {true, true, true, true, true};
  
  /**
   * Constructor, it only calls the super class's constructor.
   */
  public YahtzeeDice()
  {
    super();
  }
  
  /**
   * Overloaded roll method from Dice. Only rolls active dice.
   */
  public void roll()
  {
    for(int i = 0; i < 5; i++)
    {
      if(activeDice[i])
        theDice[i].roll();
    }    
  }

  /**
   * holdDie holds a Die at a specific index.
   *
   * @param index The index in the activeDice array to toggle.
   * @return a boolean.
   */
  public boolean holdDie(int index)
  {
    if(index < 5)
    {
      activeDice[index] = !activeDice[index];
      return true;
    }
    else
      return false;
  }
}
