package main.java.ui;
/**
 * LobbyMenu provides the players an opportunity to chat and declare readiness.
 * Uses an inner updater Thread class to periodically receive chat information.
 *
 * @author Cory
 * @version $Id: $Id
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import main.java.game.Yahtzee;
public class LobbyMenu extends JPanel implements ActionListener, KeyListener
{
  private Yahtzee controller;
  private GridBagConstraints gbConst;
  private JTextArea playerNames, chatArea;
  private JTextField chatEntry;
  private JLabel lobbyTitle, chatLabel;
  private JButton ready;
  private JPanel titlePanel, playerPanel, buttonPanel, chatPanel;
  private JScrollPane scrollPane;
  private Border raisedBevel, blueLine, compound, etched, loweredBevel;   
   
  /**
   * Constructor for LobbyMenu. Takes in a controller and Vector of players.
   *
   * @param jApp Yahtzee controler.
   * @param players Vector of currently signed up players.
   */
  public LobbyMenu(Yahtzee jApp, Vector<String> players)
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
    lobbyTitle = new JLabel("Player Lobby");
    titlePanel = new JPanel(new GridBagLayout());
    
    titlePanel.setSize(new Dimension(300, 25));
    titlePanel.setPreferredSize(new Dimension(300, 25));
    titlePanel.setMaximumSize(new Dimension(300, 25));
    titlePanel.setMinimumSize(new Dimension(300, 25));
    
    titlePanel.add(lobbyTitle, gbConst);
    titlePanel.setBorder(raisedBevel);
    
    //Setup Current Players
    playerPanel = new JPanel(new GridBagLayout());
    playerNames = new JTextArea();

    playerPanel.setSize(new Dimension(150,145));

    playerNames.setSize(new Dimension(130,125));
    playerNames.setPreferredSize(new Dimension(130,125)); 
    playerNames.setMinimumSize(new Dimension(130,125));
    playerNames.setMaximumSize(new Dimension(130,125));
    playerNames.setBorder(etched);
    
    playerNames.setEditable(false);
    playerNames.append("---Current Players---\n");
    playerNames.append("1. " + players.elementAt(0) + "\n");
    
    gbConst.anchor = GridBagConstraints.CENTER;
    playerPanel.add(playerNames, gbConst);
    
    //Setup Buttons
    buttonPanel = new JPanel(new GridBagLayout());
    ready = new JButton("I'm Ready!");
    
    buttonPanel.setSize(new Dimension(150,145));
    buttonPanel.setPreferredSize(new Dimension(150,145));
    buttonPanel.setMinimumSize(new Dimension(150,145));
    buttonPanel.setMaximumSize(new Dimension(150,145));
    
    ready.addActionListener(this);
    ready.setActionCommand("ready");
    
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
    
    chatLabel = new JLabel("Chat", JLabel.CENTER);
    chatLabel.setBorder(etched);
    chatLabel.setFont(new Font("serif", Font.BOLD, 20));
    chatLabel.setHorizontalTextPosition(JLabel.CENTER);
    chatLabel.setSize(new Dimension(130, 30));
    chatLabel.setPreferredSize(new Dimension(130, 30));
    chatLabel.setMinimumSize(new Dimension(130, 30));
    chatLabel.setMaximumSize(new Dimension(130, 30));
    
    gbConst.gridy = 0;
    chatPanel.add(scrollPane, gbConst);
    gbConst.gridy = 1;
    gbConst.insets = new Insets(5, 5, 5, 5);
    chatPanel.add(chatEntry, gbConst);
    
    gbConst.gridx = 0;
    gbConst.gridy = 0;
    buttonPanel.add(ready, gbConst);
    
    gbConst.gridy = 0;
    gbConst.gridx = 0;
    gbConst.gridwidth = 2;
    gbConst.insets = new Insets(10, 10, 10, 10);
    add(titlePanel, gbConst);
    gbConst.gridy = 1;
    gbConst.gridx = 0;
    gbConst.gridwidth = 1;
    gbConst.insets = new Insets(0, 10, 10, 0);
    add(playerPanel, gbConst);
    gbConst.gridy = 1;
    gbConst.gridx = 1;
    gbConst.insets = new Insets(0, 0, 10, 10);
    add(buttonPanel, gbConst);
    gbConst.gridy = 2;
    gbConst.gridx = 0;
    gbConst.gridwidth = 2;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(chatLabel, gbConst);
    gbConst.gridy = 3;
    gbConst.gridx = 0;
    gbConst.insets = new Insets(0, 10, 10, 10);
    add(chatPanel, gbConst);
  
    updateChatArea(controller.client.getChatLog());
    controller.client.getPlayersFromServer();
    updatePlayerNames(controller.getPlayerNames()); 
    
    Updater u = new Updater(this);
    Updater refresher = new Updater(this);
    refresher.start();
    
    setBorder(compound);
    validate();
    setVisible(true);
  }
  
  private class Updater extends Thread
  {
    private LobbyMenu controller;
    private boolean keepGoing = true;
  
    public Updater(LobbyMenu lobMenu)
    {
      controller = lobMenu;  
    }
  
    public void run()
    {
      while(keepGoing)
      {
        try{
          sleep(1000);
        }
        catch(InterruptedException e)
        {
          e.printStackTrace();
        }
        
        controller.refreshAll();
        
        
        if(controller.controller.client.readyCheck())
        {
          keepGoing = false;
          controller.controller.runMultiGame(); 
        }
      }     
    }
  }
   
  /**
   * <p>readyCheck.</p>
   *
   * @return a boolean.
   */
  public boolean readyCheck()
  {
    return true;
  }
  
  /**
   * updatePlayerNames checks the server for any additional players.
   *
   * @param players The players received from the server.
   */
  public void updatePlayerNames(Vector<String> players)
  {
    int count = 1;
    
    playerNames.setText(null);
  
    playerNames.append("---Current Players---\n");
  
    for(String name : players)
    {
      playerNames.append(count + ". " + name + "\n");  
      count++;  
    } 
    
    validate();
  }
  
  /**
   * Updates the chat area with a new String of text.
   *
   * @param chatText String containing the chat log.
   */
  public void updateChatArea(String chatText)
  {
    chatArea.setText("");
    chatArea.setText(chatText);
    
    validate();  
  }
 
  /**
   * Called by updater. Refreshes all components with server information.
   */
  public void refreshAll()
  {
    controller.client.getPlayersFromServer();
    Thread.yield();
    updatePlayerNames(controller.getPlayerNames());
    Thread.yield();
    updateChatArea(controller.client.getChatLog());
    Thread.yield();
    controller.client.readyCheck();
    Thread.yield();
  }
  
  /**
   * {@inheritDoc}
   *
   * Overloaded actionPerformed, works with ready button.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command = new String(e.getActionCommand());
    
    if(command.equals("ready"))
      controller.client.sendReadyMessage();
  }
  
  /**
   * {@inheritDoc}
   *
   * Overloaded keyPressed, waits for enter then sends entered text to server.
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
    
  /** {@inheritDoc} */
  public void keyReleased(KeyEvent e)
  {}
  
  /** {@inheritDoc} */
  public void keyTyped(KeyEvent e)
  {} 
}
