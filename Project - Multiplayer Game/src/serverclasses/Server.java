package serverclasses;

import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static javax.swing.JOptionPane.*;

//created by us

import external_code.HashAndSalt;
import serverclasses.serverObjects.*;
import Logic.Cleanup;
import Logic.Logic;
import Logic.Stats.*;
import Logic.Objects.*;

/**
 * the server which connects users to database
 */

public class Server implements Runnable
{
	/**
	 * the IP-address of the server
	 */
	public static final String IP = Cleanup.getProperty("IP");
	/**
	 * the port the server has opened
	 */
	public static final int PORT = Integer.parseInt(Cleanup.getProperty("Port"));

	private static volatile boolean exit;
	private static ServerSocket serverSocket;
	private static Database database;


	/**
	 * server startup
	 * @param args standard main param
	 */

	public static void main(String[] args) 
	{
		Runnable server = new Server();
		new Thread(server).start();
		
		showMessageDialog(null, "The surver is running!\nClosing this window shuts it down");
		
		Server.shutDown();
	}


	/**
	 * creates a new server instance by creating sockets, connecting to Database and binding the IP
	 */
	public Server() 
	{
		try
		{
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, PORT));
			database = new Database();
		}
		catch(Exception e)
		{
			Cleanup.writeMessage(e, "Server()");
		}
	}

	/**
	 * Constantly looks for new connections then puts them in their own thread
	 *
	 */
	@Override
	public void run()
	{
		exit = false;
		try 
		{
			while(!exit) 
			{
				Socket clientSocket = serverSocket.accept();

				System.out.println("New Client connected: " + clientSocket.getInetAddress().getHostName());
				Runnable chatHandler = new UserHandler(clientSocket);
				new Thread(chatHandler).start();
			}
		}
		catch(Exception e) 
		{
			if(!e.getMessage().contentEquals("Socket closed"))
			{
				Cleanup.writeMessage(e, "Server - run()");
			}
		}
		finally 
		{
			if(!serverSocket.isClosed())
			{
				Cleanup.closeServerSocket(serverSocket);
			}

			//logs off all users and shuts down the database
			System.out.println(Database.update_LogoffAllUsers() + " Users were logged off");
			Database.delete_AllLobbies();
			database.shutDown();

			System.out.println("Server stopped");
			System.exit(ABORT);
		}
	}

	/**
	 * shuts down the server and logs off all users
	 */
	public static void shutDown() 
	{
		exit = true;
		
		//Sends a message to all connected users that the server is shutting down and stops their threads
		UserHandler.broadcastMessage("SERVER HAS STOPPED");
		UserHandler.stopAll();
		Cleanup.closeServerSocket(serverSocket);
	}
}

















/**
 *
 * 
 * Each myUser has their own UserHandler Thread
 * 
 */

class UserHandler implements Runnable
{
	/**
	 * A user of the program can be in either of these three states
	 */
	/**
	 * a Connected Client
	 */
	private ConnectedClient 	myClient;
	/**
	 * Keeps tabs on all clients connected but not logged in
	 *
	 */
	private static ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();
	/**
	 * a Connected User
	 */
	private ConnectedUser 		myUser;
	/**
	 * All Users are kept this arraylist when they havent entered a game/lobby yet
	 *
	 */
	private static ArrayList<ConnectedUser> connectedUsers = new ArrayList<ConnectedUser>();
	/**
	 * a connected player
	 */
	private ConnectedPlayer 	myPlayer;
	/**
	 * users in a game/lobby are kept in this arraylist
	 *
	 */
	private static ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<ConnectedPlayer>();

	private static volatile boolean serverRunning = true; 	//stops all threads of this class
	private volatile boolean quit = false; 					//stops this thread

	/**
	 * constructor for a new client
	 * @param clientSocket the socket of the userhandlers client
	 */
	UserHandler(Socket clientSocket)
	{
		myClient = new ConnectedClient(clientSocket);
		connectedClients.add(myClient);
	}
	



	/*
	 * 
	 * Each Client has their own thread 
	 * 
	 */

	/**
	 * waits for input from user, and reacts accordingly
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run() 
	{
		//loops until the connected myClient manages to login 
		while(serverRunning && !quit)
		{
			while((myUser == null && myPlayer == null) && serverRunning && !quit)
			{
				Object objectFromClient = myClient.recieve();
				if(objectFromClient instanceof String) 
				{
					//Tries to log myUser in
					String inputString = (String)objectFromClient;
					login_or_register(inputString);
				}
			}
			while(myClient == null && (myUser != null || myPlayer != null) && serverRunning && !quit) 
			{
				try
				{
					Object objectFromUser;
					if(myUser != null)
					{
						objectFromUser = myUser.recieve();
					}
					else
					{
						objectFromUser = myPlayer.recieve();
					}

				

					if(objectFromUser instanceof ChatMessage) 
					{
						//broadcasts message to all other players in same game as this myUser
						ChatMessage message = (ChatMessage)objectFromUser;
						sendChatMessage(message);
					}
					else if(objectFromUser instanceof Municipality) 
					{
						//sent to server when a myUser has made a move in the game, doesnt need feedback
						Municipality changedMunicipality = (Municipality)objectFromUser;
						updateMunicipality(changedMunicipality);
					}
					
					else if(objectFromUser instanceof Municipality[]) 
					{
						//sent to server when a myUser has made a move in the game, doesnt need feedback
						Municipality[] changedMunicipalities = (Municipality[])objectFromUser;
						updateMunicipalities(changedMunicipalities);
					}
					else if(objectFromUser instanceof ArrayList<?>) 
					{
						//a myUser has finished their turn
						ArrayList<Object> check = (ArrayList<Object>)objectFromUser;
						if(!check.isEmpty() && check.get(0) instanceof Municipality)
						{
							ArrayList<Municipality> newMap = (ArrayList<Municipality>)objectFromUser;
							endTurn(newMap);
						}
					}
					else if(objectFromUser instanceof User) 
					{
						//this is the winner of the game
						User winner = (User)objectFromUser;
						endGame(winner);
					}
					else if(objectFromUser instanceof String) 
					{
						//a myUser has finished their turn
						String messageFromUser = (String)objectFromUser;
						if(messageFromUser.contains("RoundNumber-"))
						{
							int roundNumber = Integer.parseInt(messageFromUser.replace("RoundNumber-", ""));
							Database.update_RoundNumber(roundNumber, myPlayer.getGameID());
						}
					}

					//can use different int values for different myUser actions that dont require special input
					else if(objectFromUser instanceof Integer) 
					{
						int input = (int)objectFromUser;
						switch(input)
						{
						//Main Menu
						case 1001:
							joinLobby();
							break;
						case 1002:
							viewStats();
							break;
						case -1003:
							logoff();
							break;

							//in Lobby
						case 2001:
							ready();
							break;
						case 2002:
							changeToRandomParty();
							break;
						case -2003:
							leaveLobby();
							break;

							//in Game
						case -3001:
							leaveGame();
							break;	

							//user closes the program
						case -66:
							quit();
							break;

						default:
							System.err.println("No suitable action for: " + input + " from " + myUser.getUsername());
							break;
						}
					}
					else 
					{
						if(objectFromUser == null) 
						{
							stop();
						}
						else 
						{
							System.err.println("Could not understand " + objectFromUser + "	sent by " + myUser.getUsername());
						}
					}
				}
				catch(Exception e) 
				{
					Cleanup.writeMessage(e, "UserHandler - run()");
				}
			}
		}
	}





	
	
	
	

	/**
	 * 
	 * Methods that affects all connected users of the program
	 * 
	 */


	/**
	 * broadcasts a message to all connections
	 *
	 * @param message the message which is broadcasted
	 */
	public static void broadcastMessage(String message) 
	{ 
		for(ConnectedClient client: connectedClients) 
		{
			client.send(message);
		}
		for(ConnectedUser user: connectedUsers) 
		{
			user.send(message);
		}
		for(ConnectedPlayer player: connectedPlayers) 
		{
			player.send(message);
		}
	}

	/**
	 * stops all Threads running in this class
	 *
	 */
	public static void stopAll()
	{
		serverRunning = false;
		for(ConnectedClient client: connectedClients) 
		{
			client.closeStreams();
		}
		for(ConnectedUser user: connectedUsers) 
		{
			user.closeStreams();
		}
		for(ConnectedPlayer player: connectedPlayers) 
		{
			player.closeStreams();
		}
	}




/**
 * 
 * Switching between Client, User and Player:
 * When starting the game a player is automatically a Client
 * 
 */

	
		private void fromClient_toUser(User loggedInAs)
			//logging in
		{
			myClient.resetStreams();
			myUser = new ConnectedUser(loggedInAs.getUserID(), loggedInAs.getUsername(), myClient);
			connectedClients.remove(myClient);
			System.out.println("	" + myUser.getUsername() + " logged in");
			myClient = null;
			
			
			connectedUsers.add(myUser);
		}
		private void fromUser_toPlayer(int lobbyID)
			//joining game/lobby
		{
			myUser.resetStreams();
			myPlayer = new ConnectedPlayer(myUser, lobbyID);
			connectedUsers.remove(myUser);
			System.out.println("	" + myPlayer.getUsername() + " joined lobby " + myPlayer.getLobbyID());
			myUser = null;
			
			connectedPlayers.add(myPlayer);
		}
		private void fromPlayer_toUser() 
			//leaving lobby/game
		{
			myPlayer.resetStreams();
			myUser = new ConnectedUser(myPlayer);
			connectedPlayers.remove(myPlayer);
			if(myPlayer.getGameID() > 0)
			{
				System.err.println("	" + myUser.getUsername() + " left game " + myPlayer.getGameID());
			}
			else
			{
				System.err.println("	" + myUser.getUsername() + " left lobby " + myPlayer.getLobbyID());
			}
			myPlayer = null;
			
			connectedUsers.add(myUser);
		}
		private void fromUser_toClient() 
			//logging off
		{
			myUser.resetStreams();
			myClient = new ConnectedClient(myUser);
			connectedUsers.remove(myUser);
			System.err.println("	" + myUser.getUsername() + " signed out");
			myUser = null;
			
			connectedClients.add(myClient);
		}
		
		
		
		
		
		
		/*
		 * 
		 * Communication withing a players game/lobby
		 * 
		 */
		
		private void sendToGame_OrLobby(Object obj)
		{
			for(ConnectedPlayer player: connectedPlayers)
			{
				if((myPlayer.getGameID() == player.getGameID() || myPlayer.getLobbyID() == player.getLobbyID()) && !(myPlayer.getUserid() == player.getUserid())) 
				{
					try 
					{
						player.send(obj);
					}
					catch(Exception e) 
					{
						Cleanup.writeMessage(e, "sendtoGame_OrLobby()");
					}
				}
			}
		}

		private void sendToLobby(Object obj)
		{
			for(ConnectedPlayer player: connectedPlayers)
			{
				if(myPlayer.getLobbyID() == player.getLobbyID() && !(myPlayer.getUserid() == player.getUserid())) 
				{
					try 
					{
						player.send(obj);
					}
					catch(Exception e) 
					{
						Cleanup.writeMessage(e, "sendtoLobby()");
					}
				}
			}
		}
		
		private void sendToGame(Object obj)
		{
			for(ConnectedPlayer player: connectedPlayers)
			{
				if(myPlayer.getGameID() == player.getGameID() && !(myPlayer.getUserid() == player.getUserid())) 
				{
					try 
					{
						player.send(obj);
					}
					catch(Exception e) 
					{
						Cleanup.writeMessage(e, "sendtoGame()");
					}
				}
			}
		}
		
		
		
		
		
		
		
		
		
		

	/*
	 * 
	 * Connected client is in the ConnectedClient state (login menu)
	 * 
	 */



	private void login_or_register(String input) 
	{
		String[] inputVariables = input.split(",");
		if(inputVariables.length < 2) {
			return;
		}
		if(inputVariables[0].contentEquals("register")) 
			//myClient wants to register
		{
			String username = inputVariables[1];
			String inputPassword = inputVariables[2];
			String email = inputVariables[3];

			//hashing password for security
			String password = hashPassword(inputPassword);
			try 
			{
				if(Database.insert_User(username, password, email))
				{
					myClient.send("Registration OK");
					System.out.println("NEW REGISTRATION: " + username + "	Email: " + email);
				}
				else
				{
					myClient.send("Registration FAILED");
				}
			}
			catch(Exception e) 
			{
				myClient.send("Registration FAILED");
				Cleanup.writeMessage(e,  "login_or_register()");
			}
			
		}

		else    //ConnectedClient wants to log in

		{
			String username = inputVariables[0];
			String password = inputVariables[1];

			User loggedInAs = login_OK(username, password);
			if(loggedInAs != null) 
			{
				//return the myClient object to the myClient
				myClient.send(loggedInAs);

				//Changes this threads client to a logged in ConnectedUser
				fromClient_toUser(loggedInAs);
			}
			else 
			{
				myClient.send("Wrong Username or Password");
			}
		}
	}
	
	//stops this instance of the class
	private void stop() 
	{
		if(myClient != null)
		{
			System.err.println(myClient.getIP().getHostName() + " is no longer running the application"); 
		}
		quit = true;
	}
	
	
	//User has closed their application
	private void quit() 
	{
		logoff();
		stop();
	}





	
	

		










	/*
	 * 
	 * Connection is in the ConnectedUser state (main menu)
	 * 
	 */


	private void joinLobby() 
	{
		int lobbyID = Database.select_LobbyWithFreeSpot();
		if(lobbyID != -1) 
		{
			if(myPlayer != null){
				fromPlayer_toUser();
			}
			fromUser_toPlayer(lobbyID);
			
			//Joins the lobby (and is assigned a random party)
			Party myPlayersParty = Database.insert_JoinLobby(lobbyID, myPlayer.getUserid());
			myPlayer.setParty(myPlayersParty);
			
			//sends an array of the names of people in the same lobby to the player joining
			ArrayList<User> usersInLobby = Database.select_UsersInLobby(lobbyID);
			
			//sends info to myPlayer
			myPlayer.send(usersInLobby);
			myPlayer.send(myPlayer.getParty());
			
			//sends this users info and Party to the players in the lobby
			sendToLobby(new User(myPlayer.getUserid(), myPlayer.getUsername(), myPlayer.getParty()));
		}
		else
		{
			lobbyID = Database.insert_NewLobby(myUser.getUserid());
			if(myPlayer != null){
				fromPlayer_toUser();
			}
			fromUser_toPlayer(lobbyID);
			myPlayer.send(Database.select_UsersInLobby(lobbyID));
		}
		myPlayer.send("PlayersReady-" + Database.select_PlayersReady(myPlayer.getLobbyID())[1]);
	}


	private void viewStats() 
	{
		Stats myUserStats = Database.select_Stats(myUser.getUserid());
		myUser.send(myUserStats);
	}

	private void logoff() 
	{
		if(myUser != null)
		{
			Database.update_UserLogoff(myUser.getUsername());

			//switching back from User to anonymous Client
			fromUser_toClient();
		}
		else if(myPlayer != null)
		{
			if(myPlayer.getGameID() > 0)
			{
				leaveGame();
			}
			else 
			{
				leaveLobby();
			}
			
			Database.update_UserLogoff(myUser.getUsername());

			//switching back from Player to anonymous Client
			fromUser_toClient();
		}
	}



	
	
	
	


	/*
	 * 
	 * 
	 * In a Lobby
	 * 
	 * 
	 */


	/**
	 * sends a chatmessage to all users in the same game/lobby as this player
	 *
	 * @param chatMessage the message to send
	 */
	private void sendChatMessage(ChatMessage chatMessage)
	{
		sendToGame_OrLobby(chatMessage);
	}

	private void changeToRandomParty() 
	{
		Party newParty = Database.select_FreeRandomPartyInLobby(myPlayer.getLobbyID());
		myPlayer.setParty(newParty);
		Database.update_PartyForUser(myPlayer.getUserid(), myPlayer.getLobbyID(), myPlayer.getParty());
		
		myPlayer.send(myPlayer.getParty());
		sendToLobby(myPlayer.getUser());
	}


	private void ready() 
	{
		Database.update_TogglePlayerReady(myPlayer.getUserid(), myPlayer.getLobbyID());
		String playersReady = "PlayersReady-" + Database.select_PlayersReady(myPlayer.getLobbyID())[1];
		sendToLobby(playersReady);
		myPlayer.send(playersReady);
		if(Database.select_PlayersReady(myPlayer.getLobbyID())[0] == 0)
		{
			startGame();
		}
	}
	
	//Takes all players in the same lobby as this myPlayer and crates a game object then sends it to all the playres in the lobby
	private void startGame() 
	{
		ArrayList<User> players = Database.select_UsersInLobby(myPlayer.getLobbyID());
		User[] gameUsers = new User[players.size()];

		//Inserts the users into a regular Array and assigns what playernumber they are
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).setPlayerNR(i);
			gameUsers[i] = players.get(i);
		}

		//creating game object
		Game game = null;
		try {
			game = new Game(gameUsers, Logic.createMap(gameUsers));
		}catch (IllegalException e){}
		if(game == null){
			return;
		}

		//inserts a new row in the Game table and sets the gameID for the game object
		game.setGameID(Database.insert_NewGame(gameUsers.length));
		
		for(int i = 0; i < gameUsers.length; i++) 
		{
			//Adds the game setup values to the UserGame table
			Database.insert_UserGame(game.getUsers()[i].getUserID(), game.getGameID(), game.getUsers()[i].getParty().getPartyID());

			//increase gameCount for player
			Database.update_Gamecount(game.getUsers()[i].getUserID());
		}
		//inserts the startmap into mySQL
		Database.insert_StartMap(game.getMunicipalities(), game.getGameID());
		Database.update_RoundNumber(game.getRoundNumber(), game.getGameID());

		System.out.println("\nGame Made: " + game + "\n");

		//sends the game object to all the users in the lobby (including myPlayer)
		for(ConnectedPlayer player : connectedPlayers)
		{
			if(player.getLobbyID() == myPlayer.getLobbyID()) 
			{
				player.send(game);
				player.setGameID(game.getGameID());
				System.out.println("	" + player.getUsername() + " has joined game " + player.getGameID());
			}
		}
		Database.delete_Lobby(myPlayer.getLobbyID());
	}


	private void leaveLobby() 
	{
		//sends a chatMesssage to the game that the player has left
		myPlayer.send("YOU LEFT");
		ChatMessage signoff = new ChatMessage("ATTENTION", myPlayer.getUser(), "LEFT\n");
		sendToLobby(signoff);
		Database.delete_LeaveLobby(myPlayer.getUserid());
		fromPlayer_toUser();
	}



	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * 
	 * 
	 * In game actions
	 * 
	 * 
	 */



	private void updateMunicipality(Municipality update) 
	{
		//sends the municipality to the pther players in the game
		sendToGame(update);
	}
	
	private void updateMunicipalities(Municipality[] update) 
	{
		//sends the two changed municipalities to the other polayers in the game
		sendToGame(update);
	}

	private void endTurn(ArrayList<Municipality> gameMap) 
	{
		//Sends the updated final arrayList to all users in the same game as this myPlayer
		sendToGame(gameMap);

		//updates the database with the changes
		updateDatabase(gameMap);
	}
	
	private void endGame(User winner) {
		Database.update_PlayerWon(winner.getUsername(), myPlayer.getGameID());
		Database.update_Gameswon(winner.getUserID());
		if(winner.getPlayerNR() == -1) {
			String winnerFeedback = "WINNER-" + winner.getUsername()+"-valget er over";
			sendToGame(winnerFeedback);
			myPlayer.send(winnerFeedback);
		}else {
			String winnerFeedback = "WINNER-" + winner.getUsername()+"- ";
			sendToGame(winnerFeedback);
			myPlayer.send(winnerFeedback);
		}
	}

	private void leaveGame() 
	{
		//sends a chatMesssage to the game that the player has left
		myPlayer.send("YOU LEFT");
		ChatMessage signoff = new ChatMessage("ATTENTION" , myPlayer.getUser() , " LEFT\n");
		sendToGame(signoff);
		
		fromPlayer_toUser();
	}






	
	
	
	
/*
 * 
 * Real MVP methods
 * 
 */




	private void updateDatabase(ArrayList<Municipality> fromUser) 
	{
		//retrieves the registered gameState from the Database
		ArrayList<Municipality> fromDatabase = Database.select_AllMunicipalities(myPlayer.getGameID());

		//Finds the municipalities that needs to update in the Database
		ArrayList<Municipality> toUpdate = findChanges(fromDatabase, fromUser);

		//Updates Database with the changes if there are any
		if(!toUpdate.isEmpty()) 
		{
			Database.update_Municipalities(toUpdate, myPlayer.getGameID());
		}
	}


	private ArrayList<Municipality> findChanges(ArrayList<Municipality> fromDatabase, ArrayList<Municipality> fromUser) 
	{
		ArrayList<Municipality> changedMunicipalities = new ArrayList<Municipality>();


		if(fromDatabase.equals(fromUser)) 
		{
			//Nothing needs to be changed
			System.out.println("found no changes between ConnectedUser and Database Municipalities");
			return changedMunicipalities;
		}
		else if(fromDatabase.size() > fromUser.size()) 
		{
			//someone has joined some municipalities
			System.out.println("Database array was LONGER than myPlayer array");
		}
		else if(fromDatabase.size() < fromUser.size()) 
		{
			//someone has split some municipalities
			System.out.println("Database array was SHORTER than myPlayer array");
		}
		else  //Finds which municipalities that has changed
		{
			for(int i = 0; i < fromDatabase.size(); i++) 
			{
				if(!fromUser.get(i).equals(fromDatabase.get(i))) 
				{
					changedMunicipalities.add(fromUser.get(i));
				}
			}
		}
		return changedMunicipalities;
	}


	private User login_OK(String username, String password){
		try {
			//calling the password + salt string navalbattle because it's awesome
			String navalbattle = Database.select_UserPassword(username);
			if(navalbattle == null) {
				System.out.println("Cannot find " + username + " in Database");
				return null;
			}
			//splitting hash and salt 
			String[] strings = navalbattle.split("!");
			//hash as string
			//String hashword = strings[0];
			//salt as string
			String saltword = strings[1];
			//creating a byte array from saltstring to get the same salt as in registeruser
			byte[] salt = saltword.getBytes(StandardCharsets.ISO_8859_1);
			//generating a hash from the given password
			password = HashAndSalt.SHA_256Password(password, salt);
			//adding the salt as string + divisor
			password += "!" + saltword;

			//if the password hash+saltstring matches, the ConnectedUser will login
			return Database.update_UserLogin(username, password); //returns ConnectedUser object player logged in as (null if something goes wrong)
		} catch (Exception e){
			Cleanup.writeMessage(e, "login() - hashing and salting");
			return null;
		}
	}

	private String hashPassword(String password){
		try{
			//generating a bytearray
			byte [] bytes = HashAndSalt.gensalt();

			//making the salt into a string
			String saltstring = "";
			for(byte abyte:bytes){
				saltstring += abyte;
			}
			//removing all - from the string
			//saltstring = saltstring.replace("-","");
			//changing the saltstring into a bytearray according to the ISO-8859-1 standard
			bytes = saltstring.getBytes(StandardCharsets.ISO_8859_1);
			//using the new bytearray as salt, while saving the saltstring with the password using ! as a divisor
			return HashAndSalt.SHA_256Password(password, bytes) + "!" + saltstring;
		}catch (Exception e){
			Cleanup.writeMessage(e, "registerNewUser() - salting and hashing");
			return null;
		}
	}

}


	
