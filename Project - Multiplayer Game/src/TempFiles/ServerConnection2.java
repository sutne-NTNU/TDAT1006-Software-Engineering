package TempFiles;
import Logic.Game;
import Logic.User;
import Logic.Municipality;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import serverclasses.Cleanup;
import serverclasses.Server;
import Logic.Party;




public class ServerConnection2 {
	
	private final String IP = "";
	private final int PORT = 6969;
	
	private User user;
	private ArrayList<Municipality> municipalities;
	private Game game;
	
	private Socket socket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	
	public ServerConnection2() {
		setupStreams();
	}
	
	public void setupStreams(){
		try {
			socket = new Socket(IP, PORT);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			Cleanup.writeMessage(e, "ServerConnection - setupStreams()");
		}
	}
	public void closeStreams(){
		try {
			socket.close();
			toServer.close();
			fromServer.close();
		} catch (IOException e) {
			Cleanup.writeMessage(e, "ServerConnection - setupStreams()");
		}
	}
	
	public void send(Object obj) 
	{
		try
		{
			toServer.writeObject(obj);
		}
		catch(Exception e) 
		{
			Cleanup.writeMessage(e, "ServerConnection - send()");
		}
	}
	
	public Object recieve() 
	{
		try
		{
			return fromServer.readObject();
		}
		catch(Exception e) 
		{
			Cleanup.writeMessage(e, "ServerConnection - send()");
			return null;
		}
	}
	
	//methods used by client
//	endGame()
//	logOff()
//	startGame()
//	endTurn()
//	joinGame()
//	register()
//	login()
	
}

































class ServerConnectionxxx {
	private static int[] gamePorts = {4444, 9999, 5555, 6666, 4949, 9494, 5656, 6565};
	private static volatile Boolean initializer, yourTurn = false;
	private static User user;
	private Socket in = null;
	private Socket out = null;
	private static volatile Game curGame = null;
	private ActiveGame activeGame;
	private Thread activeGameThread;
	private volatile ArrayList<Municipality> munics = null;
	private volatile static boolean play = false;
	private String[] info = new String[3];
	private boolean register = false;
	private boolean logoff = false;
	private Object read = null;
	private Object prevRead = null;
	private ObjectInputStream reader = null;
	private ObjectOutputStream writer = null;

	public ServerConnectionxxx() {
		activeGame = new ActiveGame();
		activeGameThread = new Thread(activeGame);
	}


	public boolean register(String username, String password, String email) {
		info[0] = username;
		info[1] = password;
		info[2] = email;
		register = true;
		return userAction();
	}

	public boolean logIn(String username, String password) {
		info[0] = username;
		info[1] = password;
		register = false;
		return userAction();
	}

	public boolean logOff() {
		logoff = true;
		return userAction();
	}

	//Stops the active game thread and clears all variables.
	public void endGame() {
		play = false;

		activeGame.stop();
		activeGameThread.interrupt();

		munics.clear();
		try {
			writer.writeObject(munics);
		}catch(Exception e) {
			Cleanup.writeMessage(e, "endGame()");
		}
	}

	//Sets you to player two
	public boolean joinGame() {
		initializer = false;
		return connectToGame();
	}

	//Updates the municipalities and sends it to the server.
	public boolean endTurn(ArrayList<Municipality> update) {
		munics = update;
		activeGameThread.interrupt();
		System.out.println("Municipalities updated in game: "+curGame.getGameId());
		//while(update.equals(munics)) {
		//}
		return true;
	}


	public static User getUser() {
		return user;
	}

	public static Game getGame() {
		return curGame;
	}

	/*public void run() {
			run = true;
			while(run) {
				runLogin();
				while(initializer == null) {
					//System.out.println(initializer);
				}
				runGame();
				end();
			}
		}
		public void end() {
			run = false;
		}*/

	//Sets you to player one and initializez the lobby.
	public void startGame(){
		System.out.println("Starting lobby");
		initializer = true;
		connectToGame();
		activeGameThread.start();
	}

	//method for registering and logging in/out, this is sent to the server.
	public boolean userAction() {
		int[] ports = {3737, 7373};
		ObjectInputStream sIn = null;
		ObjectOutputStream sOut = null;
		//			Socket lin = null;
		//			Socket lout = null;
		boolean tryConnect = false;
		boolean runLogin = true;
		Object serverMessage = null;
		int i = 0;
		while(runLogin) {
			if(info[0] != null) {
				tryConnect = true;
			}
			while(tryConnect) {
				try {
					System.out.println("Trying to log in");

					out= new Socket(Server.IP, ports[0]);
					in= new Socket(Server.IP, ports[1]);
					System.out.println("Login: Ports set");
					System.out.println("Login: Connection successful");
					sOut = new ObjectOutputStream(out.getOutputStream());
					sIn = new ObjectInputStream(in.getInputStream());
					System.out.println("Login: Streams set");
					if(register) {
						sOut.writeObject("register,"+info[0]+","+info[1]+","+info[2]);
						tryConnect = false;
					}else if(logoff) {
						sOut.writeObject(user.getUserid());
					}else {
						sOut.writeObject(info[0]+","+info[1]);
						tryConnect = false;
					}
					sOut.close();

				}catch(Exception e) {
					Cleanup.writeMessage(e, "userAction()");
					i++;
					if(i > 4){
						return false;
					}
				}
			}while(serverMessage == null) {
				try {
					serverMessage = sIn.readObject();
					if(serverMessage instanceof User) {
						user = (User) serverMessage;
						Client.setUser(user);
						System.out.println(user.toString());
						runLogin = false;
						return true;
					}else if(serverMessage instanceof String || serverMessage.equals("Registration error" )  || serverMessage.equals("Wrong username/Password")){
						System.out.println(serverMessage);
						sIn.close();
						runLogin = false;
						return false;
					}else if(serverMessage instanceof String && serverMessage.equals("Logged off")){
						System.out.println(serverMessage);
						runLogin = false;
						return true;
					}
				}catch(Exception e) {
					Cleanup.writeMessage(e, "userAction()");
					return false;
				}
			}
		}
		return runLogin;
	}

	//Starts a lobby if player one, joins a lobby if player two.
	public boolean connectToGame() {
		//			write = null;
		read = null;
		boolean tryConnect = true;
		reader = null;
		writer = null;
		//chatThread.run();
		System.out.println("Connecting to game");
		while(tryConnect) {
			try {
				if(initializer) {
					out = new Socket(Server.IP, gamePorts[0]);
					in = new Socket(Server.IP, gamePorts[1]);
					writer = new  ObjectOutputStream(out.getOutputStream());
					reader = new ObjectInputStream(in.getInputStream());
					writer.writeObject(user);
					//writer.reset();
					System.out.println(reader.readObject().toString()+" - Joined the game");
					System.out.println("\nYou are player 1");
					play = true;
					tryConnect = false;
				}else {
					out = new Socket(Server.IP, gamePorts[2]);
					in = new Socket(Server.IP, gamePorts[3]);
					reader = new ObjectInputStream(in.getInputStream());
					writer = new  ObjectOutputStream(out.getOutputStream());
					writer.writeObject(user);
					//writer.reset();
					System.out.println(reader.readObject().toString()+" - Joined the game");
					System.out.println("You are player 2");
					play = true;
					System.out.println("play: " + play);
					tryConnect = false;
					activeGameThread.start();
				}
			}catch(Exception e) {
				Cleanup.writeMessage(e, "connectToGame()");
				return false;
			}
		}
		return true;
	}

	//A thread class reading/writing to and from the server.
	@SuppressWarnings("unchecked")
	class ActiveGame implements Runnable{
		public volatile boolean exit = false;

		public void stop() {
			exit = true;
		}
		//Runs the ActiveGame thread
		public void run() {
			//chatThread.run();
			prevRead = munics;
			System.out.println("Running game");


			//Controls whether or nor not the thread is still running.
			try {
				int counter = 0;
				while(play && !exit) {
					System.out.println(play + " " + exit);
					counter++;
					System.out.println(counter);

					//Reads a game object sent from the server.
					while(!exit && curGame == null) {
						try {
							System.out.println("hei " + curGame);
							Object ass = reader.readObject();
							//System.out.println("ActiveGame has read an object: " +ass);
							if(ass != null && ass instanceof Game) {
								curGame = (Game)ass;
								System.out.println(ass);
								System.out.println(curGame);
								for(User u : curGame.getUsers()) {
									System.out.println(u.getUsername());
									System.out.println("party: " + u.getParty().getPartyName());
								}
								prevRead = curGame.getMuns();
								System.out.println("Curgame: " + curGame);
								Client.updateGame(curGame);
								if(curGame.getUsers()[0].equals(user)) {
									yourTurn = true;
								}else if(curGame.getUsers()[1].equals(user)) {
									yourTurn = false;
								}
								System.out.println("1" + play + " " + exit + "yourTurn: " + yourTurn);

							}
						}catch(Exception e) {
							Cleanup.writeMessage(e, "curGame casting");
						}

					}

					System.out.println("4" + play + " " + exit);


					//Chunck of code that reads/writes an arraylist two and from server, based on whos turn it is.
					if(!yourTurn) {
						while(read == null || read.equals(prevRead)) {
							System.out.println("anglikaner");
							read = reader.readObject();
							System.out.println("Katolikk");
							//waiting for update...
						}
						prevRead = read;
						//System.out.println("Enemy wrote" + read.toString());
						munics = (ArrayList<Municipality>) read;
						System.out.println("munics " + munics);
						curGame.setMunicipalities(munics);
						yourTurn = true;
						if(user.equals(curGame.getUsers()[1])) {
							curGame.setCurUser(curGame.getUsers()[1]);
						}else if(user.equals(curGame.getUsers()[0])){
							curGame.setCurUser(curGame.getUsers()[0]);
						}
						curGame.setMunicipalities(munics);
						Client.updateGame(curGame);
						System.out.println(Client.getGame().getUsers()[0].getParty().getPartyID()+ " " + Client.getGame().getUsers()[1].getParty().getPartyID());
						System.out.println("municipalities " + Client.getGame().getMuns() + ", curgame " + curGame.getMuns());
						for(Municipality m: Client.getGame().getMap()) {
							System.out.println("map " + m);
						}
						System.out.println("2" + play + " " + exit);

					}else if(yourTurn) {

						while(!Thread.interrupted() && (munics == null || prevRead == null|| munics.equals(prevRead))){
							munics = Client.getGame().getMuns();
						}
						if(Thread.interrupted()) {
							System.out.println("Thread has been interrupted");
						}
						writer.writeObject(munics);
						writer.flush();
						//System.out.println("You wrote" + read.toString());
						yourTurn = false;
						if(user.equals(curGame.getUsers()[1])) {
							curGame.setCurUser(curGame.getUsers()[0]);
						}else if(user.equals(curGame.getUsers()[0])){
							curGame.setCurUser(curGame.getUsers()[1]);
						}
						System.out.println("3" + play + " " + exit);
						Client.updateGame(curGame);
					}
					System.out.println("5" + play + " " + exit);
				}

			}catch(Exception e) {
				Cleanup.writeMessage(e, "Activegame - run()");
			}
		}


	}
}
