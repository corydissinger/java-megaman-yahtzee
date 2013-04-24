package main.java.ui;
/**
 * WaitMultiMenu has a chat and a recent messages area for players in an online game.
 */ 


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;

public class WaitMultiMenu extends JPanel implements ActionListener, KeyListener
{
  private Yahtzee controller;
  private GridBagConstraints gbConst;
  private JTextArea messageArea, chatArea;
  private JTextField chatEntry;
  private JLabel waitTitle;
  private Updater refresher;
  private JCheckBox ready;
  private JPanel titlePanel, messagePanel, buttonPanel, chatPanel;
  private JScrollPane scrollPane;
  private JLabel chatLabel;
  private Border raisedBevel, blueLine, compound, etched, loweredBevel;   
 
 /**
  * Constructor, accepts a Yahtzee controller as a parameter.
  * 
  * @param jApp The Yahtzee controller.    
  */     
  public WaitMultiMenu(Yahtzee jApp)
  {
    super(new GridBagLayout());
    controller = jApp;
    
    gbConst = new GridBagConstraints();
    raisedBevel = BorderFactory.createRaisedBevelBorder();
    etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    blueLine = BorderFactory.createLineBorder(Color.blue);
    loweredBevel = BorderFactory.createLoweredBevelBorder();
    compound = BorderFactory.createCompoundBorder(blueLine, raisedBevel);      

    setSize(new Dimension(320,320));
    setMinimumSize(new Dimension(320,360));
    setMaximumSize(new Dimension(320,380));
    setPreferredSize(new Dimension(320,380));
    
    //Setup Title
    waitTitle = new JLabel("Turn Intermission Lobby");
    titlePanel = new JPanel(new GridBagLayout());
    
    titlePanel.setSize(new Dimension(300, 25));
    titlePanel.setPreferredSize(new Dimension(300, 25));
    titlePanel.setMaximumSize(new Dimension(300, 25));
    titlePanel.setMinimumSize(new Dimension(300, 25));
    
    titlePanel.add(waitTitle, gbConst);
    titlePanel.setBorder(raisedBevel);
    
    //Setup Current Players
    messageArea = new JTextArea();

    messageArea.setSize(new Dimension(240,140));
    messageArea.setPreferredSize(new Dimension(240,140)); 
    messageArea.setMinimumSize(new Dimension(240,140));
    messageArea.setMaximumSize(new Dimension(240,140));
    messageArea.setBorder(etched);
    
    messageArea.setEditable(false);
    messageArea.append("---Current Message---\n");
    
    //Setup Buttons
    chatLabel = new JLabel("Chat", JLabel.CENTER);
    chatLabel.setBorder(etched);
    chatLabel.setFont(new Font("serif", Font.BOLD, 20));
    chatLabel.setHorizontalTextPosition(JLabel.CENTER);
    chatLabel.setSize(new Dimension(130, 30));
    chatLabel.setPreferredSize(new Dimension(130, 30));
    chatLabel.setMinimumSize(new Dimension(130, 30));
    chatLabel.setMaximumSize(new Dimension(130, 30));
    
    chatPanel = new JPanel(new GridBagLayout());
    chatArea = new JTextArea();
    chatArea.setEditable(false);
    chatArea.setText("--Welcome to Yahtzee, type in the box below then hit 'Enter' to chat.\n");
    scrollPane = new JScrollPane(chatArea);
    chatEntry = new JTextField();
    
    chatPanel.setSize(new Dimension(300, 140));
    chatPanel.setPreferredSize(new Dimension(300, 140));
    chatPanel.setMinimumSize(new Dimension(300, 140));
    chatPanel.setMaximumSize(new Dimension(300, 140));
    
    scrollPane.setSize(new Dimension(300, 100));
    scrollPane.setPreferredSize(new Dimension(300, 100));
    scrollPane.setMinimumSize(new Dimension(300, 100));
    scrollPane.setMaximumSize(new Dimension(300, 100));
    scrollPane.setBorder(etched);
    
    chatEntry.setSize(new Dimension(290, 30));
    chatEntry.setPreferredSize(new Dimension(290, 30));
    chatEntry.setMinimumSize(new Dimension(290, 30));
    chatEntry.setMaximumSize(new Dimension(290, 30));
    chatEntry.addKeyListener(this);
    
    gbConst.gridy = 0;
    chatPanel.add(scrollPane, gbConst);
    gbConst.gridy = 1;
    gbConst.insets = new Insets(5, 5, 5, 5);
    chatPanel.add(chatEntry, gbConst);
    
    gbConst.gridy = 0;
    gbConst.gridwidth = 2;
    gbConst.insets = new Insets(10, 10, 10, 10);
    add(titlePanel, gbConst);
    gbConst.gridy = 1;
    gbConst.gridwidth = 1;
    gbConst.insets = new Insets(0, 10, 10, 0);
    add(messageArea, gbConst);
    gbConst.gridy = 2;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(chatLabel, gbConst);
    gbConst.gridy = 3;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(chatPanel, gbConst);
  
    updateChatArea(controller.client.getChatLog());
    
    refresher = new Updater(this);
    Thread t = new Thread(refresher);
    t.start();
    
    setBorder(compound);
    validate();
    setVisible(true);
  }
  
  private class Updater implements Runnable
  {
    private Thread updater;
    private WaitMultiMenu controller;
    private boolean going;
  
    public Updater(WaitMultiMenu controller)
    {
      this.controller = controller;  
      going = true;
    }
  
    public void run()
    {
      updater = Thread.currentThread();
      going = true;
      
      while(going)
      {
        try{
          Thread.sleep(1000);
            
          controller.refreshAll();
        
          if(controller.getRefreshStatus())
          {
            going = false;
            controller.controller.tryNewTurn();
          }
        }
        catch(InterruptedException e)
        {
          Thread.currentThread().interrupt();
        }
      }
    }
  
    public void stopUpdater()
    {
      going = false;
      
      if(updater != null)
        updater.interrupt();   
    }
  }
 
 /**
  * Starts the inner Updater class which periodically gets server information.
  */   
  public void startUpdater()
  {
    refresher = new Updater(this);
    Thread t = new Thread(refresher);
    t.start();
  }
 
 /**
  * Stops the updater to prevent I/O interference.
  */   
  public void stopUpdater()
  {
    refresher.stopUpdater();
    try{
      Thread.sleep(1050);   
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
  }
  
  /**
   * Gets the game status from the server.
   * 
   * @return Returns true if its time for a new turn, false otherwise.      
   */     
  public boolean getRefreshStatus()
  {
    return controller.client.isNextMultiTurn();
  }
  
  /**
   * Sets the chat area's text to a new String.
   * 
   * @param chatText String containing chat log information.      
   */     
  public void updateChatArea(String chatText)
  {
    chatArea.setText("");
    chatArea.setText(chatText);
    
    validate();  
  }
  
  /**
   * Used with inner Updater class. Refreshes all components.
   */     
  public void refreshAll()
  {
    updateRecentMessage(controller.client.getRecentMessage());
    Thread.yield();
    updateChatArea(controller.client.getChatLog());
    Thread.yield();
  }
  
  /**
   * Updates the recent messages area.
   * 
   * @param message A recent message from the server.      
   */     
  public void updateRecentMessage(String message)
  {
    messageArea.setText("");
    messageArea.append("---Current Message---\n");
    messageArea.append(message);
    
    validate();  
  }
     
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(command.equals("Refresh"))
    {
      if(controller.client.isNextMultiTurn())
      {
        controller.tryNewTurn();
      }
      else
      {
        //updateMessages(controller.getMessage());
        updateChatArea(controller.client.getChatLog());      
      }
    }
  }
  
  /**
   * Overloaded keyPressed method. Waits for the user to press enter.
   * 
   * @param e The triggered KeyEvent.      
   */     
  public void keyPressed(KeyEvent e)
  {
    if(e.getKeyCode() == e.VK_ENTER)
    {
      String text = new String(chatEntry.getText());
      chatEntry.setText("");
      controller.client.chatOverSocket(text);
      updateChatArea(controller.client.getChatLog());  
    }
  }
    
  public void keyReleased(KeyEvent e)
  {}
  
  public void keyTyped(KeyEvent e)
  {} 
}