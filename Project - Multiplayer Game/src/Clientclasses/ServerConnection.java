package Clientclasses;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Logic.Cleanup;
import Logic.Objects.*;
import Logic.Stats.Stats;

public class ServerConnection implements Runnable{

	/**The ip of the server to connect to
	 *
	 */
	private final String IP = Cleanup.getProperty("IP");
	/**The port of the connected server
	 *
	 */
	private final int PORT = Integer.parseInt(Cleanup.getProperty("Port"));

	/**boolean value for whether or not the server should exit
	 *
	 */
	private static volatile boolean exit;
	/**The socket for the connection
	 *
	 */
	private static Socket socket;
	/**OutputStream for object sent to server
	 *
	 */
	private static ObjectOutputStream toServer;
	/**InputStream for objects sent from server
	 *
	 */
	private static ObjectInputStream fromServer;

	/**Standard constructor, sets up the in/out streams
	 *
	 */
	public ServerConnection() {
		setupStreams();
	}

	/**Test constructor, does not set up in/out streams
	 * @param stuff an object of any kind to call this constructor
	 */
	public ServerConnection(Object stuff) {
	}

	/**Sets up the in/out streams to and from server.
	 *
	 */
	public void setupStreams()
	{
		try 
		{
			socket = new Socket(IP, PORT); 
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
			System.out.println("Connected to " + socket.getInetAddress().getHostName());
		} 
		catch (IOException e) 
		{
			Cleanup.writeMessage(e, "ServerConnection - setupStreams()");
		}
	}

	/**Closes the in/out streams
	 *
	 */
	public void closeStreams()
	{
		try 
		{
			socket.close();
			toServer.close();
			fromServer.close();
		} 
		catch (IOException e) 
		{
			Cleanup.writeMessage(e, "ServerConnection - setupStreams()");
		}
	}
	
	private void sendToServer(Object obj)
	{ 
		if(toServer != null) {
			try
			{
				toServer.writeObject(obj);
				toServer.flush();
				toServer.reset();
			}
			catch(Exception e) 
			{
				Cleanup.writeMessage(e, "ServerConnection - sendToServer()");
			}
		}
	}
	
	private Object getObjectFromServer()
	{
		try
		{
			Object objectFromServer = fromServer.readObject();
			return objectFromServer;
		}
		catch(Exception e) 
		{
			Cleanup.writeMessage(e, "ServerConnection - getObjectFromServer()");
			return null;
		}
	}
	
	
	
	
	
	/*
	 * 
	 * Methods from outside a game/lobby
	 * 
	 */

	/**Ends the connection
	 *
	 */
	public void quit()
	{
		sendToServer(-66); 	//lets server know user is no longer running the program
		stop();
	}

	/**Registers a new user given the following parameters, returns true if the registration was successful
	 * @param username the username given by user
	 * @param password the password given by user
	 * @param email the email given by user
	 * @return boolean the result
	 */
	public boolean register(String username, String password, String email)
	{
		sendToServer("register," + username + "," + password + "," + email);
		Object objectFromServer = getObjectFromServer();
		if(objectFromServer instanceof String) {
			String feedback = (String)objectFromServer;
			if(feedback.contentEquals("Registration OK"))
			{
				return true;
			}
			else if(feedback.contentEquals("Registration FAILED"))
			{
				return false;
			}
		}
		System.err.println("feedback from server not recognized in - register(): " + objectFromServer);
		return false;
	}

	/**Logs in a user with the given parameters, returns true if the login is successful
	 * @param username the users username
	 * @param password the users password
	 * @return boolean the result of the login
	 */
	public boolean login(String username, String password)
	{
		sendToServer(username + "," + password);
		Object objectFromServer = getObjectFromServer();
		if(objectFromServer instanceof String) {
			String feedback = (String)objectFromServer;
			if(feedback.contentEquals("Wrong Username or Password"))
			{
				return false;
			}
		}
		else if(objectFromServer instanceof User)
		{
			User loggedInAs = (User)objectFromServer;
			Client.setUser(loggedInAs);
			return true;
		}
		System.err.println("feedback from server not recognized in - login(): " + objectFromServer);
		return false;
	}


	/**fetches a Stats object from the server.
	 * @return Stats the stats that have been fetched from the server
	 */
	public Stats getStats()
	{
		sendToServer(1002);
		Object objectFromServer = getObjectFromServer();
		if(objectFromServer instanceof Stats) 
		{
			Stats stats = (Stats)objectFromServer; 
			return stats;
		}
		else
		{
			System.err.println("feedback from server not recognized in - getStats(): " + objectFromServer);
			return null;
		}
	}
	
	public void logoff()
	{
		sendToServer(-1003);
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<User> joinLobby()
	{
		if(!exit) 
		{
			sendToServer(-3001);
		}
		sendToServer(1001);
		Object objectFromServer = getObjectFromServer();
		if(objectFromServer instanceof ArrayList)
		{
			ArrayList<Object> check = (ArrayList<Object>)objectFromServer;
			if(!check.isEmpty() && check.get(0) instanceof User) 
			{
				//starts the thread constantly checking for updates within the game/lobby from other players 
				Runnable gameThread = new ServerConnection("this String as parameter keeps the thread from creating new input/output Streams");
				new Thread(gameThread).start();
				return (ArrayList<User>)objectFromServer;
			}
			else
			{
				return null;
			}
		}
		System.err.println("feedback from server not recognized in - joinLobby(): " + objectFromServer);
		return null;
	}

	
	
	
	
	
	
	/*
	 * Thread that constantly checks for updates sent by the Server after logging in
	 * then updates the appropriate local values and uses appropriate methods
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run() 
	{
		exit = false;
		while(!exit)
		{

			try {
				Thread.sleep(20);
			}catch (Exception e){
				System.out.println(e);
			}
			Object objectFromServer = getObjectFromServer();
			System.out.println(objectFromServer);
			
			
			if(objectFromServer instanceof Game)
				//someone started the game
			{
				Game game = (Game)objectFromServer;
				Client.setGame(game);
				Client.launchGame();
			}
			else if(objectFromServer instanceof User)
				//someone joined this players lobby
			{
				User lobbyUser = (User)objectFromServer;
				Client.addLobbyUser(lobbyUser);
			}
			else if(objectFromServer instanceof Municipality)
				//A player has lost an attack
			{
				Municipality municipality = (Municipality)objectFromServer;
				Client.mergeChange(municipality);
				Client.updateGame();
			}
			else if(objectFromServer instanceof Municipality[])
				//A player has won a municipality after an attack
			{
				Municipality[] municipalities = (Municipality[])objectFromServer;
				Client.mergeChanges(municipalities);
				Client.updateGame();
			}
			else if(objectFromServer instanceof ArrayList<?>)
			{
				ArrayList<Object> check = (ArrayList<Object>)objectFromServer;
				if(!check.isEmpty() && check.get(0) instanceof Municipality)
					//A player has ended their round, this is the new map
				{
					ArrayList<Municipality> municipalities = (ArrayList<Municipality>)objectFromServer;
					Client.getGame().setMunicipalities(municipalities);
					Client.getGame().newCurrentPlayer();
					Client.updateGame();
				}
				else
				{
					System.err.println("ArrayList recieved was empty, or contains a wierd object type :( ");
				}
			}
			else if(objectFromServer instanceof Party)
				//the server has assigned you a new party
			{
				Party newParty = (Party)objectFromServer;
				Client.setUserParty(newParty);
			}
			else if(objectFromServer instanceof ChatMessage)
			{
				ChatMessage chatMessage = (ChatMessage) objectFromServer;
				Client.receiveMessage(chatMessage);
			}
			else if(objectFromServer instanceof String)
			{
				String messageFromServer = (String)objectFromServer;
				if(messageFromServer.contentEquals("SERVER SHUTTING DOWN"))
				{
					stop();
				}
				else if(messageFromServer.contains("PlayersReady-"))
					//A player is ready in the lobby
				{
					Client.updatePlayersReady(messageFromServer);
				}
				else if(messageFromServer.contains("WINNER-"))
					//A player is ready in the lobby
				{
					Client.endGame(messageFromServer);
				}
				else if(messageFromServer.contentEquals("YOU LEFT"))
				{
					stop();
				}
			}
			else
			{
				System.err.println("Object from server not recognized - " + objectFromServer);
			}
		}
	}
	
	//stops the thread reading from server
	public static void stop()
	{
		exit = true;
	}
	
	/*
	 * 
	 * methods called while user is in game, feedback is handled in the Thread below
	 * 
	 */

	/**Send a given message to the server
	 * @param message the massage object given by user
	 */
	public void sendMessage(ChatMessage message)
	{
		sendToServer(message);
	}

	/**Tell the server that the current user is ready to play the game
	 *
	 */
	public void ready()
	{
		sendToServer(2001);
	}


	/**Change the current users party
	 *
	 */
	public void changeToRandomParty()
	{
		sendToServer(2002);
	}

	/**Tell the server to let you leave the lobby.
	 *
	 */
	public void leaveLobby()
	{
		sendToServer(-2003);
	}

	/**Sends the server the updated municipality after a loss
	 * @param changedMunicipality the municipality in question
	 */
	public void updateLostAttack(Municipality changedMunicipality)
	{
		sendToServer(changedMunicipality);
	}

	/**Sends an array of update municipalities to the server
	 * @param changedMunicipalities the municipalities in question
	 */
	public void updateWonAttack(Municipality[] changedMunicipalities)
	{
		sendToServer(changedMunicipalities);
	}

	/**sends the map to the server
	 * @param newMap The current state of the whole map
	 */
	public void endTurn(ArrayList<Municipality> newMap)
	{
		sendToServer(newMap);
		sendToServer("RoundNumber-" + Client.getGame().getRoundNumber());
	}

	/**Sends the server a winner object to end game
	 * @param winner the User object that won the game
	 */
	public void endGame(User winner)
	{
		sendToServer(winner);
	}

	/**Tells the server that you have left a game
	 *
	 */
	public void leaveGame()
	{
		sendToServer(-3001);
	}
	
}

