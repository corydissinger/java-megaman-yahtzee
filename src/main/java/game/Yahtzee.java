package main.java.game;
/**
 * Yahtzee extends JApplet and is the master of all components related to this
 * project. In its private data members it holds one of each of the menus, and
 * the Vector of all players currently playing.
 *
 * @author Cory
 * @version $Id: $Id
 */


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.net.YahtzeeClient;
import main.java.ui.AnimationMenu;
import main.java.ui.GameLog;
import main.java.ui.HumanMenu;
import main.java.ui.InstructionsMenu;
import main.java.ui.LobbyMenu;
import main.java.ui.MeterPanel;
import main.java.ui.PlayerOptions;
import main.java.ui.ScoresMenu;
import main.java.ui.StartMenu;
import main.java.ui.WaitMenu;
import main.java.ui.WaitMultiMenu;
import main.java.ui.WinnersMenu;
public class Yahtzee extends JApplet implements KeyListener
{
  private GridBagConstraints gbConst = new GridBagConstraints();
  private PlayerOptions playerOptions;
  private ScoresMenu scores;
  private AnimationMenu animationMenu;
  private StartMenu startMenu;
  private GameLog gameLog;
  private HumanMenu humanMenu;
  private LobbyMenu lobbyMenu;
  private WaitMultiMenu waitMultiMenu;
  private WaitMenu waitMenu;
  private WinnersMenu winnersMenu;
  private InstructionsMenu instructions;
  private SpriteManager spriteManager;
  private Turn theTurn;
  private JPanel previous;
  private String currentPanel, fromAnimation, recentCat;
  private int currentPlayerIndex = 0;
  private int recentScore;

  //Client working variablees.
  public Vector<Player> allPlayers;  
  public String thisPlayer, activePlayer;
  public YahtzeeClient client;
  public boolean isSingle;
  public MeterPanel meterPanel;

  /**
   * init is called when the JApplet is loaded into the browser.
   *
   * init Instantiates each menu, loads all of the dice images,
   * and displays the StartMenu to the user to setup the game.
   * It also forces the SpriteManager to load all images.
   */
  public void init()
  { 
    setLayout(new GridBagLayout());
    startMenu = new StartMenu(this);
    scores = new ScoresMenu(this);
    instructions = new InstructionsMenu(this);
    playerOptions = new PlayerOptions(this);
    gameLog = new GameLog(this);
    allPlayers = new Vector<Player>();
    currentPanel = new String("start");
    
    spriteManager = new SpriteManager(this);
    spriteManager.loadDiceImages();   
    spriteManager.loadReadyImages();
    spriteManager.loadMeterImages();
    spriteManager.loadScoreImages(); 
    spriteManager.loadFinishImages();
    spriteManager.loadLife();

    meterPanel = new MeterPanel(this, spriteManager);
    meterPanel.subtractTurn();

    getContentPane().add(startMenu, gbConst);
    setJMenuBar(playerOptions);
    
    setVisible(true);
    getContentPane().validate();
  }
  
  /**
   * runGame is the main method that drives gameplay.
   *
   * runGame is called each time a menu's lifespan is up. It determines
   * which player goes next, then displays the appropriate menu.
   * finishGame is called when no more players can take turns.
   */
  public void runSingleGame()
  {    
    if(continueSingleGame())
    {
      if(allPlayers.elementAt(currentPlayerIndex) instanceof Artificial)
        newArtificialTurn(allPlayers.elementAt(currentPlayerIndex));
      else
        showAnimation("ready"); 
    } 
    else   
      finishSingleGame();       
  }
  
  /**
   * Runs the game if it is a multiplayer game.
   */
  public void runMultiGame()
  {
    activePlayer = new String(client.getActiveFromServer());

    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    if(client.continueMultiGame())
    {
      if(client.clientsTurn())
      {
        showAnimation("ready");
      }
      else
      {
        newWaitTurn(allPlayers.elementAt(client.getServerIndex()));
      }
    }
    else
      finishMultiGame();
  }

  /**
   * updateIndex determines which position the currentPlayerIndex should be.
   *
   * currentPlayerIndex is incremented by one unless it would go out of bounds.
   * In which case, it is set to zero.
   */
  public void updateSingleIndex()
  {
    if(currentPlayerIndex == (allPlayers.size() - 1))
      currentPlayerIndex = 0;
    else
      currentPlayerIndex++;
  }
  
  /**
   * Stops all updaters to prevent I/O interference.
   */
  public void stopUpdaters()
  {
    if(currentPanel.equals("multiWait"))
      waitMultiMenu.stopUpdater();
  }
  
  /**
   * Resumes all updaters.
   */
  public void startUpdaters()
  {
    if(currentPanel.equals("multiWait"))
      waitMultiMenu.startUpdater();
  }
  
  /**
   * continueGame determines if another player can make a move or not.
   *
   * @return Returns true if a player has a move left, false otherwise.
   */
  public boolean continueSingleGame()
  {
    for(int i = 0; i < allPlayers.size(); i++)
    {
      if(allPlayers.elementAt(i).canContinue())
        return true;
    }
    
    return false;
  }
  
  /**
   * Method used in the animation menu to return the user to gameplay.
   */
  public void returnFromAnimation()
  {
    setLayout(new GridBagLayout());
    getContentPane().removeAll();
    setVisible(false);
    
    if(isSingle)
    {      
      if(fromAnimation.equals("ready"))
      {
        newHumanTurn(allPlayers.elementAt(currentPlayerIndex));  
      }
      else if(fromAnimation.equals("score"))
        tryNewTurn();
      else if(fromAnimation.equals("win") || fromAnimation.equals("lose"))
        showWinners();
      
    }
    else
    {
      if(fromAnimation.equals("ready"))
        newHumanTurn(allPlayers.elementAt(client.getServerIndex()));
      else if(fromAnimation.equals("score"))
        tryNewTurn();
      else if(fromAnimation.equals("win") || fromAnimation.equals("lose"))
        showWinners();
    } 
  }
  
  /**
   * newHumanTurn sets up the HumanMenu for the active Human player.
   *
   * @param player The active player.
   */
  public void newHumanTurn(Player player)
  {    
    theTurn = new Turn();
    currentPanel = new String("human");
    
    humanMenu = new HumanMenu(this, theTurn, player, spriteManager, isSingle);
    humanMenu.setOpaque(true);
    humanMenu.setGameToSingle(isSingle); 
    humanMenu.updateDice();
    meterPanel.updateMeter();
    
    gbConst.gridx = 0;
    gbConst.anchor = GridBagConstraints.PAGE_START;
    getContentPane().add(meterPanel, gbConst);
    gbConst.gridx = 1;
    gbConst.anchor = GridBagConstraints.CENTER;
    getContentPane().add(humanMenu, gbConst);
    setVisible(true);
    getContentPane().validate();
  }
  
  /**
   * If the single game option was selected, starts a single player game.
   */
  public void startSingleGame()
  {
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    isSingle = true;
    currentPlayerIndex = 0;
    
    Artificial computer = new Artificial("Computer");
    allPlayers.add(computer);
    
    runSingleGame();    
  }
  
  /**
   * If the multiplayer game optio nwas selected, starts a multiplayer game.
   */
  public void startMultiGame()
  {
    client = new YahtzeeClient(this);
    
    client.initSocket();
    client.addPlayerToServer();
    
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    isSingle = false;
    currentPanel = new String("lobby");
    thisPlayer = new String(allPlayers.elementAt(0).getName());
    
    lobbyMenu = new LobbyMenu(this, getPlayerNames());
    
    getContentPane().add(lobbyMenu, gbConst);
    getContentPane().validate();  
    setVisible(true);
  }
  /**
   * newArtificialTurn sets up the ArtificialMenu for the active Artificial player.
   *
   * @param player The active player.
   */
  public void newArtificialTurn(Player player)
  {
    theTurn = new Turn();
    currentPanel = new String("wait");
  
    Artificial artificial = (Artificial)player;
    
    String message = artificial.executeTurn(theTurn);
    
    waitMenu = new WaitMenu(this);
    waitMenu.setTextArea(message);
  
    gameLog.addTurnInfo(artificial.getTurnInfo(), artificial.getName());
    
    allPlayers.set(currentPlayerIndex, artificial); 
    
    getContentPane().add(waitMenu, gbConst);
    setVisible(true); 
    getContentPane().validate();
  }
  
  /**
   * Creates a new waiting menu for a multiplayer game.
   *
   * @param player The player to be used.
   */
  public void newWaitTurn(Player player)
  {
    currentPanel = new String("multiWait");
    
    waitMultiMenu = new WaitMultiMenu(this);
    
    getContentPane().add(waitMultiMenu, gbConst);
    setVisible(true);
    getContentPane().validate();
  }
  
  /**
   * Updates the game log in a single game.
   *
   * @param turnInfo a {@link java.lang.String} object.
   * @param pName a {@link java.lang.String} object.
   */
  public void updateSingleLog(String turnInfo, String pName)
  {
    gameLog.addTurnInfo(turnInfo, pName);
  }
  
  /**
   * optionsSelected is called when a player interacts with the JMenuBar.
   *
   * optionsSelected stores the current menu in previous, then displays
   * the menu the user asked for.
   *
   * @param option The menu the player has selected.
   */
  public void optionsSelected(String option)
  {
    previous = determineCurrentPanel();
    
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    if(option.equals("View Score Cards"))
    {
      scores.setupScorePanels(getScoreInfo());
      getContentPane().add(scores, gbConst);
      setVisible(true);
      getContentPane().validate();  
    }
    else if(option.equals("Instructions"))
    {
      getContentPane().add(instructions, gbConst);
      setVisible(true);
      getContentPane().validate();
    }
    else if(option.equals("Game Log"))
    {
      if(!isSingle)
        gameLog.addInfoFromServer();
      getContentPane().add(gameLog, gbConst);
      setVisible(true);
      getContentPane().validate();
    }
  }

  /**
   * determineCurrentPanel returns the currently active panel.
   *
   * @return The JPanel to be stored in previous.
   */
  public JPanel determineCurrentPanel()
  {
    if(currentPanel.equals("start"))
      return startMenu;
    else if(currentPanel.equals("human"))
      return humanMenu;
    else if(currentPanel.equals("wait"))
      return waitMenu;
    else if(currentPanel.equals("lobby"))
      return lobbyMenu;
    else if(currentPanel.equals("multiWait"))
      return waitMultiMenu;
    else
      return winnersMenu;
  }
  
  /**
   * initializePlayers is called when the user has finished setup in the StartMenu.
   *
   * @param name a {@link java.lang.String} object.
   */
  public void addPlayer(String name)
  {  
    if(thisPlayer == null)
      thisPlayer = new String(name);
    
    Player temp = new Player(name);     
    allPlayers.add(temp);
  }
  
  /**
   * Called whenever a player backs out of a JMenuBar spawned menu.
   */
  public void toGame()
  {
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    getContentPane().add(previous, gbConst);
    setVisible(true);
    getContentPane().validate();
  }

  /**
   * endTurn is called when a player takes in dice for their turn. Continues gameplay.
   */
  public void tryNewTurn()
  {    
    meterPanel.updateMeter();  
  
    if(isSingle)
    {
      getContentPane().removeAll();
      setVisible(false);
      getContentPane().validate();
      runSingleGame();
      updateSingleIndex();
    }
    else
    {
      if(client.isNextMultiTurn())
      {
        getContentPane().removeAll();
        setVisible(false);
        getContentPane().validate();
        runMultiGame();       
      }
    }
  }

  /**
   * Returns each player's score mapping.
   *
   * Maps a players name to a map of that player's scored categories.
   *
   * @return The TreeMap containing all players and their scoring info.
   */
  public TreeMap<String, TreeMap<String, Integer>> getScoreInfo()
  {
    TreeMap<String, TreeMap<String, Integer>> temp = new TreeMap<String, TreeMap<String, Integer>>();
    
    for(Player p : allPlayers)
    {
      String tempName = new String(p.getName());
      TreeMap<String, Integer> tempScores = p.getActualScores();
      
      temp.put(tempName, tempScores);
    }
    
    return temp;
  }
  
  /**
   * Returns a Vector containing all player names.
   *
   * @return A Vector containing the names of all current players.
   */
  public Vector<String> getPlayerNames()
  {
    Vector<String> temp = new Vector<String>();
    
    for(Player p : allPlayers)
      temp.add(p.getName());    
    
    return temp;
  }
  
  /**
   * getPlayerStandings returns a mapping of player names to grand total scores.
   *
   * @return The mapping of players to scores.
   */
  public TreeMap<String, Integer> getPlayerStandings()
  {
    TreeMap<String, Integer> temp = new TreeMap<String, Integer>();
    
    for(Player p : allPlayers)
      temp.put(p.getName(), p.getGrandTotal());
    
    return temp;
  }
  
  /**
   * Wraps up a multiplayer game with an appropriate animation.
   */
  public void finishMultiGame()
  {
    currentPanel = new String("winners");
    fromAnimation = new String("end");
    
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    for(Player p : allPlayers)
    {
      if(p.getName().equals(thisPlayer))
        continue;
      else
        client.updateClientScores(p.getName());
    }
    
    if(thisPlayerWon())
      showAnimation("win");
    else
      showAnimation("lose");
  }
  
  /**
   * finishSingleGame is called last and displays the WinnersMenu.
   */
  public void finishSingleGame()
  {
    currentPanel = new String("winners");
    fromAnimation = new String("end");
  
    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();
    
    if(thisPlayerWon())
      showAnimation("win");
     else
      showAnimation("lose");  
  } 
  
  /**
   * Determines if this client won.
   *
   * @return True if this player won, false otherwise.
   */
  public boolean thisPlayerWon()
  {
    int highScore = 0;
    String highName = new String("");
    
    for(Player p : allPlayers)
    {
      if(p.getGrandTotal() > highScore)
      {
        highScore = p.getGrandTotal();
        highName = new String(p.getName());
      }
    }  
    
    if(highName.equals(thisPlayer))
      return true;
    else
      return false;
  }
  
  /**
   * Brings up the winners menu with final standings.
   */
  public void showWinners()
  {
    winnersMenu = new WinnersMenu(getPlayerStandings(), this);
    
    getContentPane().add(winnersMenu, gbConst);
    setVisible(true);
    getContentPane().validate();  
  }
  
  /**
   * Forces the browser to reload.
   */
  public void resetGame()
  {
    try{
      getAppletContext().showDocument(new URL("javascript:doLoad()"));
    }  
    catch(MalformedURLException e){
      e.printStackTrace();
    }
  }
  /**
   * Displays a popup error message.
   *
   * @param message The error message to be displayed.
   * @param title Title of error.
   */
  public void displayError(String message, String title)
  {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Removes all key listeners after a cheat has been used.
   */
  public void removeKeyListeners()
  {
    KeyListener [] listener = getKeyListeners();
    removeKeyListener(listener[0]); 
  }
  
  /**
   * Adds a key listener temporarily for use with cheats.
   */
  public void getKeys()
  {
    addKeyListener(this);
    requestFocus();
  } 
  
  /**
   * Updates the current player to avoid loss of information.
   *
   * @param play The player to be updated.
   */
  public void updatePlayer(Player play)
  { 
    spriteManager.setBlastInfo(play.getRecentCat(), play.getRecentScore()); 
  }
  
  /**
   * Accepts a string, and prepares the AnimationMenu for that animation.
   *
   * @param name The name of the animation to be displayed.
   */
  public void showAnimation(String name)
  {  
    fromAnimation = new String(name);
    
    if(name.equals("ready"))
      spriteManager.setAnimation("ready");
    else if(name.equals("score"))
    {
      spriteManager.setAnimation("score");
    }
    else if(name.equals("win"))
      spriteManager.setAnimation("win");
    else if(name.equals("lose"))
      spriteManager.setAnimation("lose");
    else
      return;

    getContentPane().removeAll();
    setVisible(false);
    getContentPane().validate();

    animationMenu = new AnimationMenu(this, spriteManager);
        
    getContentPane().add(animationMenu, gbConst);
    setVisible(true);
    getContentPane().validate();

    animationMenu.startAnimation();
  }
  
  /** {@inheritDoc} */
  public void keyPressed(KeyEvent e)
  {return;}
  
  /** {@inheritDoc} */
  public void keyTyped(KeyEvent e)
  {humanMenu.handleCheat(e);}
  
  /** {@inheritDoc} */
  public void keyReleased(KeyEvent e)
  {return;}
}   
