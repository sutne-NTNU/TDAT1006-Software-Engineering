package Clientclasses;

import java.util.ArrayList;

import Logic.Objects.*;
import Logic.Stats.Stats;
import fx.Main;
import fx.controllers.Controller;


/**
 * Client is the link between the gui/game logic and the ServerConnection
 * And is the instance of the game being played by the user
 */
public class Client{
	
	private static ServerConnection con = new ServerConnection();
	
	private static User user;
    private static Game game = null;
    

    //private static FXMLLoader gamewinloader;
    private static Municipality curMun;
    private static ArrayList<User> lobbyUsers;
    private static boolean marked;
    private static ArrayList<String> markedMun;
    private static String rollDiceResults;
    public static int playersReady;
    /*
     * Setters
     */

    /** Method that sets the game.
     * @param game Game-object that is to be set
     */
    public static void setGame(Game game)							{Client.game = game;}

    /** Method that sets the user.
     * @param user User-object that is to be set
     */
    public static void setUser(User user)							{Client.user = user;}

    /** Method that sets a FXML-loader.
     * @param gamewinloader the desired FXML-loader for a FXML-document
     */
    //public static void setGamewinloader(FXMLLoader gamewinloader)	{Client.gamewinloader = gamewinloader;}

    /** Method that sets a municipality to marked or unmarked, depending on the input.
     * @param marked True if a municipality is marked (clicked on), false if not
     */
    public static void setMarked(boolean marked)					{Client.marked = marked;}

    /** Method that sets the result-text after a player has attacked  municipality.
     * @param rollDiceResults Result of an attack/ a defence
     */
    public static void setRollDiceResults(String rollDiceResults)	{Client.rollDiceResults = rollDiceResults;}

    /** Method that sets the municipalities a player is allowed to attack, after the player clicked on a valid municipality of theirs.
     * @param markedMun A list of municipalities that a player is allowed to attack.
     *                  The municipalities are set after the player has clicked on a municipality that they own.
     *                  THe marked municipalities are neighbours to the clicked municipality.
     */
    public static void setMarkedMun(ArrayList<String> markedMun)	{Client.markedMun = markedMun;}

    /** Method that sets a municipality
     * @param curMun Sets the municipality
     */
    public static void setCurMun(Municipality curMun)				{Client.curMun = curMun;}
    
    /*
     * Getters
     */

    /** Gets a Game-object
     * @return Returns a Game-object
     */
    public static Game getGame()						{return game;}

    /** Gets a User-object
     * @return Returns a User-object
     */
    public static User getUser()						{return user;}

    /** Gets a FXMLLoader-object
     * @return Returns a FXMLLoader-object
     */
    //public static FXMLLoader getGamewinloader()			{return gamewinloader;}

    /** Gets the value of marked
     * @return Returns a boolean value. True if municipality is marked, false if not.
     */
    public static boolean getMarked()					{return marked;}

    /** Gets the results-string from a dice roll
     * @return Returns a string with the result from a dice throw
     */
    public static String getRollDiceResults()			{return rollDiceResults;}

    /** Gets the arraylist of marked municipalities
     * @return Returns an ArrayList of marked municipalities
     */
    public static ArrayList<String> getMarkedMun()		{return markedMun;}

    /** Gets the current municipality
     * @return Returns the current municipality
     */
    public static Municipality getCurMun()				{return curMun;}

    /** Gets an ArrayList of lobby users
     * @return Returns an ArrayList of lobby users
     */
    public static ArrayList<User> getLobbyUsers()       {return lobbyUsers;}

    

    
    
    /*
     * 
     * GUI
     * 
     */

    /** Adds a lobby user to the lobby
     * @param newUser User-object of the user that is to join the lobby
     */
    public static void addLobbyUser(User newUser)
    {
        //if the user already exists, they have changed their party
    	for(int i = 0; i < lobbyUsers.size(); i++)
    	{
            if(lobbyUsers.get(i).getUserID() == newUser.getUserID()) 
            {
            	lobbyUsers.get(i).setParty(newUser.getParty());
            	Main.controller6.updateLobby();
            	return;
            }
        }
    	//otherwise they are added to the lobby list
        lobbyUsers.add(newUser);
        Main.controller6.updateLobby();
    }

    /** Updates number of ready players in lobby to new number of ready players
     * @param messageFromServer String containing a text and the new number of ready players
     */
    public static void updatePlayersReady(String messageFromServer) {
		String players_Ready = messageFromServer.replace("PlayersReady-","");
		Client.playersReady = Integer.parseInt(players_Ready);
		Main.controller6.updateLobby();
	}

    /**
     * Updates the map to map with changes after user has interacted with game
     */
    public static void updateGame()
    {
        Main.controller4.updateMap();
    }

    public static void launchGame(){
    	updateGame();
    	Client.lobbyUsers = null;
    	Client.playersReady = 0;
        Main.controller6.goToGameWindow();
    }

    /** Recieves a ChatMessage-object and sends it to the controller.
     *  If the timestamp of the message is "ATTENTION", this means that a player left.
     *  If the player is in a game, the player left the game.
     *  If the player is not in game, the player is in lobby, i.e the player left the lobby.
     *  If the timestamp is not "ATTENTION", a regular message has been sent, and will be received by all players.
     * @param message ChatMessage-object that all users will receive
     */
    public static void receiveMessage(ChatMessage message){
        if(message.getTimestamp().contentEquals("ATTENTION"))
        {
        	if(Client.game != null)
        	{
        		if(Client.game.getCurrentPlayer().equals(message.getUser()))
        		{
        			Client.game.newCurrentPlayer();
        		}
        		if(1 == Client.game.removePlayer(message.getUser())) 
        		{
        			con.endGame(Client.game.findWinner());
        		}
        	}
        	else
        	{
        		 Client.lobbyUsers.remove(message.getUser());
            	 Main.controller6.updateLobby();
        	}
        }
    	Controller.receivemessagetemp(message);
    }

    /** Merges changes on municipalities locally on users computer, with changes received from other players.
     *  Merge changes for when opponent lost an attack.
     * @param municipality ArrayList of municipalities
     */
    public static void mergeChange(Municipality municipality)
    {
    	for(int j = 0; j < game.getMunicipalities().size(); j++) 
		{
			if(game.getMunicipalities().get(j).getName().contentEquals(municipality.getName()))
			{
				game.getMunicipalities().set(j, municipality);
				return;
			}
		}
    }

    /**Merges changes on municipalities locally on users computer, with changes received from other players.
     *  Merge changes for when opponent won an attack
     * @param municipalities ArrayList of municipalities
     */
    public static void mergeChanges(Municipality[] municipalities)
    {
    	for(int i = 0; i < municipalities.length; i++)
    	{
    		for(int j = 0; j < game.getMunicipalities().size(); j++) 
    		{
    			if(game.getMunicipalities().get(j).getName().contentEquals(municipalities[i].getName()))
    			{
    				game.getMunicipalities().set(j, municipalities[i]);
    			}
    		}
    	}
    }

    /** Sets a new party for a user
     * @param newParty The new party-object that is to be set for a user
     */
    public static void setUserParty(Party newParty)
    {
    	user.setParty(newParty);
    	Main.controller6.updateLobby();
    }

    /** finds what player won the game by their username, and updates it in the gamewindow
     * @param winnerFeedback String that is the winner
     */
    public static void endGame(String winnerFeedback) {
    	User winner = null;
    	String[] winnerstate = winnerFeedback.split("-");
    	for(User user : game.getUsers())
    	{
    		if(user.getUsername().contentEquals(winnerstate[1])){
    			winner = user;
    		}
    	}
    	Main.controller4.updateWinner(winner, winnerstate[2]);
    	Client.game.setCurrentPlayer(null);
    }
    
    
    
    
    
    
    
    
    
    
    /*
     * 
     * From user to server
     * 
     */

    /** Clientside of registering a user
     * @param username String that is the username of the new user
     * @param password String that is the password of the new user
     * @param email String that is the email of the new user
     * @return returns a serverconnection metod call
     */
    public static boolean register(String username, String password, String email)
    {
    	return con.register(username, password, email);
    }

    /** Attempts to log in a user based on username and password input
     * @param username String that is the username of the person logging in
     * @param password String that is the password of the person logging in
     * @return Returns a serverconnection method call
     */
    public static boolean login(String username, String password)
    {
    	return con.login(username, password); 
    }

    /** Gets the stats of a player
     * @return Returns a serverconnection method call
     */
    public static Stats getStats()
    {
    	return con.getStats();
    }

    /** Puts a user in a lobby. If there are no users in a lobby or if all lobbies are full, a lobby is created for the user.
     */
    public static void joinLobby(){
    	ArrayList<User> users = con.joinLobby();
    	for(int i = 0; i < users.size(); i++)
    	{
    		if(user.getUserID() == users.get(i).getUserID())
    		{
    			user.setParty(users.get(i).getParty());
    			users.set(i, user);
    		}
    		
    	}
        Client.lobbyUsers = users;
        Main.controller6.updateLobby();
    }

    /**
     * Changes a users party to a random party.
     */
    public static void changeParty()
    {
    	con.changeToRandomParty();
    }

    /** Sends a message object to the server
     * @param message ChatMessage-object
     */
    public static void sendMessage(ChatMessage message)
    {
    	con.sendMessage(message);
    }

    /**
     * Calls a method in ServerConnection that attempts to remove the user from the lobby
     */
    public static void leaveLobby()
    {
    	con.leaveLobby();
    	Client.lobbyUsers = null;
    	Client.playersReady = 0;
    }
 
    /**
     * Calls a method in ServerConnection that tells the server that a player is ready
     */
    public static void ready()
	{
		con.ready();
	}

    /** Sends an ArrayList of changed municipalities after a successfull attack.
     *  Two municipalities are changed when an attack is successful
     *  (all but one troop is moved to newly won mun, and color is changed on newly won mun)
     * @param changedMunicipalities ArrayList of municipalities with changes done by users
     */
    public static void attackWon(Municipality[] changedMunicipalities)
    {
    	con.updateWonAttack(changedMunicipalities);
    } 

    /** Sends an ArrayList of changed municipalities after an unsuccessful attack.
     * One municipality is changed when an attack is unsuccessfull (remove all but one troop from attacking mun)
     * @param changedMunicipality ArrayList of municipalities with changes done by users
     */
    public static void attackLost(Municipality changedMunicipality)
    {
    	con.updateLostAttack(changedMunicipality);
    }

    /**
     * Sets a user as winner and sends this username back to other players in the game.
     * Stats for the winning player is also updated.
     */
    public static void endTurn() {
    	Client.game.endTurn();
    	User winner = Client.game.findWinner();
        if (winner != null) {
            con.endGame(winner);
            											endGame("Winner-"+winner.getUsername()+"-Valget er over");
        } else {
            con.endTurn(Client.game.getMunicipalities());
        }
    }

    /**
     * Calls a method in ServerConnection that removes a player from a game
     */
    public static void leaveGame()
    {
    	con.leaveGame();
    	Client.setGame(null);
    }

    /**
     * Calls a method in ServerConnection that logs a user of
     */
    public static void logoff()
    {
    	con.logoff();
    }

    /**
     * Calls a method in ServerConnection that handles when a user exits the primarystage (crosses out the game)
     */
    public static void quit()
    {
        if(Client.game !=null && Client.game.getCurrentPlayer().equals(Client.user))endTurn();
    	Client.game = null;
        con.quit();
    }
}

