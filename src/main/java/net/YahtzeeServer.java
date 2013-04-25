package main.java.net;
/**
 * Server program for the Yahtzee project 3. Contains various methods for communicating
 * with clients.
 *
 * @author Cory
 * @version $Id: $Id
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import main.java.game.Player;
public class YahtzeeServer
{
  private Thread server;
  boolean allowingConnections;
  boolean handlingRequest;
  Vector<String> chatLog;
  int scoresHistoryKey;
  int logHistoryKey;
  Vector<String> gameLog;
  ServerSocket serverSocket = null;
  String activePlayer;
  String recentMessage;
  int turnIndex;
  Vector<Player> allPlayers;
  Vector<ConnectedClient> connectedClients;
  
  /**
   * YahtzeeServer constructor. Starts the server thread.
   */
  public YahtzeeServer()
  {
    server = new Thread(new Server());
    server.start();
  }
  
  /**
   * Inner server class, implements runnable so it can be Threaded.
   */     
  private class Server implements Runnable
  { 
    /**
     * Constructor for server, initializes all information and sets up the server socket.
     */         
    public Server()
    {
      allPlayers = new Vector<Player>();
      connectedClients = new Vector<ConnectedClient>();
      handlingRequest = true;
      allowingConnections = true;
      chatLog = new Vector<String>();
      scoresHistoryKey = 0;
      logHistoryKey = 0;
      gameLog = new Vector<String>();
      turnIndex = 0;
      activePlayer = new String();     
      recentMessage = new String("");
    
      try{
        serverSocket = new ServerSocket(15003);
      }
      catch (IOException e) {
        e.printStackTrace();
      }     
    }
  
    /**
     * Overloaded server run method. Keeps accepting clients until at capacity.
     */         
    public void run()
    {
      while(server != null)
      {
        ConnectedClient c;
        try{
          Thread.sleep(100);
          c = new ConnectedClient(serverSocket.accept(), this);
          if(!allowingConnections)
            server.interrupt();
          connectedClients.add(c);
          Thread t = new Thread(c);
          t.start();
        }   
        catch (IOException e) {
          System.out.println("Client accept connection failed!");
        } 
        catch (InterruptedException e){
          server.interrupt();
        }
      }
    }
  
  /**
   * Checks to see if the game has started.
   * 
   * @return True if so, false otherwise.      
   */     
    public boolean gameRunning()
    {
      if(true)
        return true;
      else
        return false;
    }
  
    /**
     * Checks to see if other players are ready.
     * 
     * @return True if so, false otherwise.          
     */         
    public boolean checkOtherPlayers()
    {
      for(ConnectedClient c : connectedClients)
      {
        if(!c.ready())
          return false;
      }
    
      activePlayer = allPlayers.elementAt(turnIndex).getName();
      return true;
    }
  
    /**
    * Stops the server, interrupts it's thread.
    */     
    public void stopServer()
    {
      allowingConnections = false;
      server.interrupt();  
    }
  
    /**
     * Updates the server's stored index.
     */         
    public void updateTurnIndex()
    {
      if(turnIndex == (allPlayers.size() - 1))
        turnIndex = 0;
      else
        turnIndex++;  
    }
  
    protected void finalize()
    {
      try{
        serverSocket.close();
      } 
      catch (IOException e) {
        System.out.println("Could not close socket");
        System.exit(-1);
      }
    }
  }
  
  /**
   * Inner connected client class. Implements runnable to continually check for requests.
   */     
  private class ConnectedClient implements Runnable
  {
    private Server server;
    private Thread client;
    private Socket clientSocket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean stayAlive;
    private boolean isReady;
    private String name;
    
    /**
     * Constructor. Sets up the socket.
     * 
     * @param socket The client's socket.
     * @param server The server thread controlling this.               
     */         
    public ConnectedClient(Socket socket, Server server)
    {
      this.server = server;
      clientSocket = socket;
      stayAlive = true;
      isReady = false;
    }
    
    /**
     * Overloaded run. Keeps handling requests until dead.
     */         
    public void run()
    {
      String message;
      
      try{
        try{
          in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          out = new PrintWriter(clientSocket.getOutputStream(), true);
        } 
        catch (IOException e) {
          System.out.println("I/O Failed");
          e.printStackTrace();
          System.exit(-1);
        }
      
        while(stayAlive)
        {  
          try{
            handleRequest(in.readLine());    
          } 
	       catch (IOException e) {
            System.out.println("Read failed");
            e.printStackTrace();
            System.exit(-1);
          }
          
          Thread.sleep(100);
          Thread.yield();
        }
      }
      catch(InterruptedException e)
      {
        stayAlive = false;
        if(client != null)
          client.interrupt();
      }            
    }
    
    /**
     * Handle request control method. Determines which method to call.
     * 
     * @param type The type of message received from the client.          
     */         
    public void handleRequest(String type)
    {
    
      /*if(type.equals("available"))
        if(checkAvailable())
          return;  */
      
     // handlingRequest = true;
      
      if(type.equals("addPlayer"))
        addPlayer();
      else if(type.equals("getPlayers"))
        getPlayers();
      else if(type.equals("ready"))
        readyPlayer();
      else if(type.equals("readyCheck"))
        readyCheck();
      else if(type.equals("chatSend"))
        addToChat();
      else if(type.equals("chatGet"))
        getChatLog();
      else if(type.equals("myTurn"))
        checkTurnStatus(); 
      else if(type.equals("updateScores"))
        updateScores();
      else if(type.equals("turnChange?"))
        checkTurnChange();
      else if(type.equals("serverIndex"))
        getServerIndex();
      else if(type.equals("getActive"))
        getActivePlayer();
      else if(type.equals("canContinue"))
        getGameContinueStatus();
      else if(type.equals("endTurn"))
        updateTurn();
      else if(type.equals("startGame"))
        startGame();
      else if(type.equals("getScores"))
        getScores();
      else if(type.equals("getGameLog"))
        getGameLog();
      else if(type.equals("setRecent"))
        setRecent();
      else if(type.equals("getRecent"))
        getRecent();
        
      Thread.yield();
      //handlingRequest = false;
    }
    
    /**
     * Starts the game by assigning an active player.
     */         
    public void startGame()
    {
      activePlayer = allPlayers.elementAt(turnIndex).getName();
    }
    
    /**
     * Sets the recent message.
     */         
    public void setRecent()
    {
      String message;
      try{
        message = new String(in.readLine());
        recentMessage = new String(message);
        gameLog.add(message);
      }    
      catch(IOException e){
        e.printStackTrace();
      }
    }
    
    /**
     * Retrieves the recent message from the server.
     */         
    public void getRecent()
    {
      out.println(recentMessage);
    }
    /*
    public boolean checkAvailable()
    {
      if(handlingRequest)
      {
        out.println("no");
        return false; 
      }
      else
      {
        out.println("yes");
        return true; 
      }
    } */
    
    /**
     * Retrieves all players from the server.
     */         
    public void getPlayers()
    {
      for(Player p : allPlayers)
        out.println(p.getName());

      out.println("endLoop");
    }
    
    /**
     * Gets the game log from the server.
     */         
    public void getGameLog()
    {
      for(String s : gameLog)
        out.println(s);
        
      out.println("endLoop");
    }
    
    /**
     * Retrieves the name of the active player from the server.
     */         
    public void getActivePlayer()
    {
      out.println(activePlayer);
    }
    
    /**
     * Returns the multiplayer game's status - if all players can continue.
     */         
    public void getGameContinueStatus()
    {
      for(int i = 0; i < allPlayers.size(); i++)
      {
        if(allPlayers.elementAt(i).canContinue())
        {
          out.println("yes");
          return;
        }
      }
    
      out.println("no");
    }
    
    /**
     * Adds a player to the server.
     */         
    public void addPlayer()
    {
      try{
        Player p = new Player(in.readLine());
        allPlayers.add(p);
        name = new String(p.getName()); 
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }  
    }
    
    /**
     *  Returns the current turn index of the server to the client.
     */          
    public void getServerIndex()
    {         
      out.println(Integer.toString(turnIndex));
    }
    
    /**
     * Toggles this client's ready flag.
     */         
    public void readyPlayer()
    {
      isReady = !isReady;     
    }
    
    /**
     * Tells the client if it can start the game, and stops the server.
     */         
    public void readyCheck()
    {
      if(server.checkOtherPlayers())
      {
        out.println("go");
        server.stopServer();
      }
      else
        out.println("wait");
    }
    
    /**
     * Returns the client's ready status.
     */         
    public boolean ready()
    {
      return isReady;
    }
    
    
    /**
     * Adds a message to the chat.
     */         
    public void addToChat()
    {
      try{
        String temp = new String(in.readLine());
        chatLog.add(temp);
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
        
    /**
     * Gets the server's chat log.
     */                 
    public void getChatLog()
    {         
      for(String s : chatLog)
        out.println(s);
        
      out.println("endLoop");
    }

    /**
     * Gets scores from the server for a selected player.
     */         
    public void getScores()
    {
      String thePlayer = new String("");
      Player temp = null;
      
      try{
        thePlayer = new String(in.readLine());
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
             
      for(int i = 0; i < allPlayers.size(); i++)
      {
        if(allPlayers.elementAt(i).getName().equals(thePlayer))
        {
          temp = allPlayers.elementAt(i);
          break; 
        }
      }
      
      System.out.println("Retreiving scores for player: " + thePlayer);
      
      if(temp == null)
      {
        out.println("quit");
        return;
      }
      
      TreeMap<String, Integer> scores = temp.getActualScores();
      
      for(Map.Entry<String, Integer> entry : scores.entrySet())
      {
        System.out.println("DEBUGGING SERVER: " + entry.getKey() + " " + entry.getValue());
        out.println(entry.getKey());
        out.println(entry.getValue());  
      }
      
      out.println("quit");
    }

    /**
     * Assigns new scores to the server.
     */         
    public void updateScores()
    {
      try{
        String cat = new String(in.readLine());
        String scoreNum = new String(in.readLine());
        int theScore = Integer.parseInt(scoreNum);
        
        allPlayers.elementAt(turnIndex).cheatScore(cat, theScore);
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }   
    }
    
    /**
     * Updates the player's number of turns taken.
     */         
    public void updateTurn()
    {
      allPlayers.elementAt(turnIndex).turnFinished(); 
      server.updateTurnIndex();
      activePlayer = new String(allPlayers.elementAt(turnIndex).getName());   
    }
    
    /**
     * Checks to see if the turn has changed.
     */         
    public void checkTurnStatus()
    {
      try{
        String clName = new String(in.readLine());
        
        boolean test = activePlayer.equals(clName);
        
        if(test)
          out.println("yes");
        else
          out.println("no");
      }
      catch(IOException e)
      {
        e.printStackTrace();
      } 
    }
    
    public void checkTurnChange()
    {
      try{
        String clMess = new String(in.readLine());
        
        if(clMess.equals(activePlayer))
          out.println("no");
        else
          out.println("yes");
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
    
  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   */
  public static void main(String [] args)
  {
    YahtzeeServer y = new YahtzeeServer();
  }
}
