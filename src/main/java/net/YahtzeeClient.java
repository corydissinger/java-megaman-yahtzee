package main.java.net;
/**
 * Client class. Each user that opens this applet receives a Client class to themselves.
 *
 * @author Cory
 * @version $Id: $Id
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import main.java.game.Player;
import main.java.game.Yahtzee;
public class YahtzeeClient
{
  private Yahtzee controller;
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  /**
   * Constructor, accepts a Yahtzee controller.
   *
   * @param jApp The Yahtzee controller.
   */
  public YahtzeeClient(Yahtzee jApp)
  {
    controller = jApp;
  }

  /**
   * Initializes the client's socket connection.
   */
  public void initSocket()
  {
     try{
       socket = new Socket("acad.kutztown.edu", 15003);
       out = new PrintWriter(socket.getOutputStream(), true);
       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
     } catch (UnknownHostException e) {
       controller.displayError("Could not connect to host.", "Host Error");
     } catch  (IOException e) {
       controller.displayError("Could not recive I/O", "I/O Error");
     }
  }
  
  /**
   * Asks the server if the multiplayer game can continue.
   *
   * @return True if so, false otherwise.
   */
  public boolean continueMultiGame()
  {
    System.out.println("Check if game can continue.");
    out.println("canContinue");
    try{
      String response = new String(in.readLine());
      System.out.println("Server: " + response);
      
      if(response.equals("yes"))
        return true; 
    }
    catch(IOException e){
      controller.displayError("I/O Error checking server continue game status.", "I/O Error");
    }
    
    return false;
  }  

  /**
   * Asks the server for all scores for a target player.
   *
   * @param targetPlayer The player who's scores are requested.
   */
  public void updateClientScores(String targetPlayer)
  {
    out.println("getScores");
    System.out.println("Retrieving scores from server.");
    out.println(targetPlayer);

    int playerIndex = 0;
             
    for(int i = 0; i < controller.allPlayers.size(); i++)
    {
      if(controller.allPlayers.elementAt(i).getName().equals(targetPlayer))
      {
        playerIndex = i;
        break; 
      }
    }
        
    while(true)
    {
      try{
        String cat = new String(in.readLine());
          
        if(cat.equals("quit"))
          break;
            
        String score = new String(in.readLine());
        int theScore = Integer.parseInt(score);
     
        System.out.println("Cat and score received: " + cat + score);
     
        controller.allPlayers.elementAt(playerIndex).cheatScore(cat, theScore);
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }  
    }
  }
  
  /**
   * Adds a player to the server.
   */
  public void addPlayerToServer()
  {
    out.println("addPlayer");
    out.println(controller.allPlayers.elementAt(0).getName());
  }
  
  /**
   * Determines if the turn has changed.
   *
   * @return True if the turn changed, false otherwise.
   */
  public boolean isNextMultiTurn()
  {
    out.println("turnChange?");
    System.out.println("Request if Turn Changed");
    out.println(controller.activePlayer);
    
    try{
      String status = new String(in.readLine());
      if(status.equals("yes"))
        return true;
    }
    catch(IOException e)
    {
      controller.displayError("I/O Error check turn changing", "I/O Error");
    }
    
    return false;
  }
  
  /**
   * Gets the index of the active player that the server has stored.
   *
   * @return The integer index location of the active player.
   */
  public int getServerIndex()
  {
    out.println("serverIndex");
    System.out.println("Server Index Request");
    try{
      String response = new String(in.readLine());
      System.out.println("Client: " + response);
      
      int newIndex = Integer.parseInt(response);
      return newIndex;
    }
    catch(IOException e){
      controller.displayError("I/O Error receiving turn index from server.", "I/O Error");
    }
    
    return 0;
  }
  
  /**
   * Gets all players from the server.
   */
  public void getPlayersFromServer()
  {
    out.println("getPlayers");
    System.out.println("Getting Players from Server");
    
    controller.allPlayers = new Vector<Player>();
    
    try{
      while(true)
      {
        String name = new String(in.readLine());
        if(name.equals("endLoop"))
          break;
        Player p = new Player(name);
        System.out.println(p);
        controller.allPlayers.add(p);
      }
    }
    catch(IOException e)
    {
      controller.displayError("I/O Error when receiving player info.", "I/O Error");
    }
    
    System.out.println(controller.allPlayers);
  }
  
  /**
   * Sends a ready message to the server.
   */
  public void sendReadyMessage()
  {
    System.out.println("Send ready message to server.");
    out.println("ready");
  }  
  
  /**
   * Checks to see if the server is busy.
   *
   * @return True if busy, false otherwise.
   */
  public boolean checkAvailable()
  {
    out.println("available");
    String response;
    try{
      response = new String(in.readLine());
      System.out.println("Server response=" + response);
      if(response.equals("yes"))
        return true;
    }
    catch(IOException e){
      controller.displayError("Error checking server availability", "I/O Error");  
    }
    
    return false;
  }
  
  /**
   * Gets a recent message from the server.
   *
   * @return The recently set message.
   */
  public String getRecentMessage()
  {
    out.println("getRecent");
    String response;
    try{
      response = new String(in.readLine());
      System.out.println("Server response = " + response);
      return response;
    }
    catch(IOException e){
      controller.displayError("Error checking for recent messages", "I/O Error");
    }
    
    response = new String("No messages found...");
    return response;
  }
  
  /**
   * Sends a new recent message to the server.
   *
   * @param message The message to be stored.
   */
  public void setRecentMessage(String message)
  {
    out.println("setRecent");
    System.out.println("Message sent to server: " + message);
    out.println(message);
  }
  
  /**
   * Checks to see if other players are ready.
   *
   * @return True if all players are ready, false otherwise.
   */
  public boolean readyCheck()
  {
    System.out.println("Check server ready status.");
    out.println("readyCheck");
    try{
      String temp = new String(in.readLine());
      System.out.println("Response: " + temp);
      
      if(temp.equals("go"))
        return true;
    }
    catch(IOException e)
    {
      controller.displayError("Error checking ready status!", "I/O Error");
    }
    
    return false;  
  }
  
  /**
   * Sends a string of chatted text to the server.
   *
   * @param text The new chat message.
   */
  public void chatOverSocket(String text)
  {
    out.println("chatSend");
    text = "[" + controller.thisPlayer + "]: " + text;
    System.out.println(text);
    out.println(text);
  }
  
  /**
   * Gets the entire chat log from the server.
   *
   * @return String containing the entire chat log.
   */
  public String getChatLog()
  {
    out.println("chatGet");
    String chatLog = new String("");
    
    try{
      while(true)
      {
        String temp = new String(in.readLine());
        if(temp.equals("endLoop"))
          break;
        chatLog += temp;
        chatLog += "\n";  
      }
    }
    catch(IOException e){
      controller.displayError("Error receiving chat log.", "I/O Error");
    }
      
    System.out.println(chatLog);
      
    return chatLog;  
  }
  
  /**
   * Determines if it is the client's turn.
   *
   * @return True if so, false otherwise.
   */
  public boolean clientsTurn()
  {
    System.out.println(controller.thisPlayer + " is checking to see if it's their turn.");
    out.println("myTurn");
    out.println(controller.thisPlayer);
    
    try{
      String temp = new String(in.readLine());
      System.out.println("Server: " + temp);
      if(temp.equals("yes"))
        return true;
      else
        return false;
    }
    catch(IOException e)
    {
      controller.displayError("Error receiving turn information.", "I/O Error");
    }
        
    return false;
  }
  
  /**
   * Gets the name of the active player from the server.
   *
   * @return String containing the name of the active player.
   */
  public String getActiveFromServer()
  {
    System.out.println("Get active player from server.");
    out.println("getActive");
    try{
      String response = new String(in.readLine());
      System.out.println("Server: " + response);
      
      return response;
    }
    catch(IOException e){
      controller.displayError("I/O Error receiving active player from server.", "I/O Error");
    }
    
    return "";
  }
  
  /**
   * Updates the server scoring information with a new score.
   *
   * @param cat Name of the category.
   * @param score Points earned.
   */
  public void updateServerScoreInfo(String cat, int score)
  {
    out.println("updateScores");
    out.println(cat);
    out.println(score);    
  }
  
  /**
   * Changes the turn on the server.
   */
  public void updateTurn()
  {
    out.println("endTurn");
  }
  
  /**
   * Fetches the entire game log from the server.
   *
   * @param log The log to store the game log in.
   */
  public void getGameLog(Vector<String> log)
  {
    out.println("getGameLog");
    
    try{
      while(true)
      {
        String temp = new String(in.readLine());
            
        if(temp.equals("endLoop"))
          break;
          
        log.add(temp);
      }
    }
    catch(IOException e){
      controller.displayError("I/O Error receiving game log from server.", "I/O Error");
    }
  }
  
  /**
   * Closes the socket from future communication.
   */
  public void closeSocket()
  {
    try{
      socket.close();
    }
    catch(Exception e){
      controller.displayError("Socket close failed!", "I/O Error");
    }
  }
}
