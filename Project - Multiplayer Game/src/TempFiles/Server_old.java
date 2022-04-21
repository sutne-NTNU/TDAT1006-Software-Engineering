package TempFiles;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import static javax.swing.JOptionPane.*;
import Logic.Game;
import Logic.Municipality;
import Logic.Party;
import Logic.User;
import serverclasses.Cleanup;
import serverclasses.Database;

public class Server_old {
	protected static Database db;
	private LoginServer loginServer;
	private GameServer gameServer;
	private Thread loginThread, gameThread;
	public static final String IP = "192.168.137.127" /*"10.22.159.223"*/;

	//Runs the Server
	public static void main(String[] args) {
		Server_old run = new Server_old();
		run.shutDown();
	}

	public Server_old() {
		//creates a database and connection pools to SQL
		db = new Database();

		//creates and starts a tread looking for players trying to log in
		loginServer = new LoginServer();
		loginThread = new Thread(loginServer);
		loginThread.start();

		//creates and starts a thread for game synchronization
		gameServer = new GameServer();
		gameThread = new Thread(gameServer);
		gameThread.start();

		//halts execution of the main thread until window is closed
		showMessageDialog(null, "Closing this shuts down the server!");
	}


	public void shutDown() {
		try {
			//logs all active users off
			System.out.println(Database.update_LogoffAllUsers() + " users were logged off\n\n");

			System.out.println("Stopping LoginServer Thread");
			loginServer.stop();
			loginThread.interrupt();

			System.out.println("Stopping GameServer Thread");
			gameServer.stop();
			gameThread.interrupt();

			//gives a small break for threads to finish before closing all connections to MySQL
			System.out.println("Disconnecting from Database");
			Thread.sleep(1000); 
			db.shutDown();
		}catch(Exception e) {
			Cleanup.writeMessage(e, "shutDown() Server");
		}
		System.out.println("\n\nServer is no longer running!");
	}
}


















/*
 * Handles users connectiong to the server
 * registers new users, logs them in and logs them off
 */

class LoginServer implements Runnable{
	private volatile boolean exit; //used to stop the LoginServer Thread

	private final int[] userPorts = {3737, 7373};
	ServerSocket serverSocket;
	private ServerSocket[] boundSockets = new ServerSocket[userPorts.length];
	Socket in;
	Socket out;
	ObjectInputStream inputStream; //Total inputStreams.
	ObjectOutputStream outputStream; //Total outputStreams
	ArrayList<User> users = new ArrayList<>();
	Object read;


	public LoginServer() {
		try {
			//ip = /*"10.22.151.50"*/;
			//ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(InetAddress.getLocalHost());
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "userServer() - cannot find IP");
		}

		for(int i =  0; i < userPorts.length; i++) {
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(Server.IP, userPorts[i]));
				boundSockets[i] = serverSocket;
			}catch(Exception e) {
				Cleanup.writeMessage(e,  "userServer() - socketbinding");
			}
		}
	}


	//what is run by the main Server
	public void run() {
		exit = false;
		System.out.println("LoginServer is running...");

		while(!exit) {
			searchForUsers(); 
			performAction(); //either registers, logs in or logs out a user
			closeStreams();
		}
		Cleanup.closeServerSocket(serverSocket);
	} 

	public void stop() { //used to stop the Thread
		exit = true;
	}

	private void closeStreams() { 
		Cleanup.closeResources(inputStream, outputStream);
	}



	private void searchForUsers() {
		try {
			in = boundSockets[0].accept();
			out = boundSockets[1].accept();
			inputStream = new ObjectInputStream(in.getInputStream());
			outputStream = new ObjectOutputStream(out.getOutputStream());
			read = inputStream.readObject();
			return;
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "search()");
			resetStreams();
		}
	}


	private void performAction () {
		if(read instanceof Integer){
			int userID = (int)read;
			logoff(userID);
		}else if(!register()){
			login();
		}
	}

	private void resetStreams() {
		try {
			inputStream.close();
			in.close();
			outputStream.close();
			out.close();
			inputStream = null;
			outputStream = null;
			in = null;
			out = null;
		}catch(Exception e){
			Cleanup.writeMessage(e, "resetStreams()");
		}
	}

	private boolean register() {
		String readInput = (String)read;
		String[] input = readInput.split(",");
		
		if(!input[0].contentEquals("register")) {
			return false;
		}
		String username = input[1];
		String password = input[2];
		String eMail = input[3];

		try {
			if(hashAndRegister(username, password, eMail)) {
				User newUser = new User(Database.select_UserID(username), username);
				outputStream.writeObject(newUser);
				System.out.println("User registered:\nUsername: "+username+ "\nPassword: " + password + "\nEmail: " + eMail);
				return true;
			}else {
				outputStream.writeObject("Registration error");
				System.out.println("A user failed to register");
				return false;
			}
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "register()");
			return false;
		}finally {
			resetStreams();
		}
	}

	private boolean hashAndRegister(String username, String password, String email){
		try{
			System.out.println("username: " + username + " password: " +password + "email: " + email);
			//generating a bytearray
			byte [] bytes = gensalt();
			System.out.println(Arrays.toString(bytes));
			String saltstring = "";
			//making the salt into a string
			for(byte abyte:bytes){
				saltstring += abyte;
			}
			System.out.println(saltstring);
			//removing all - from the string
			//saltstring = saltstring.replace("-","");
			//changing the saltstring into a bytearray according to the ISO-8859-1 standard
			bytes = saltstring.getBytes(StandardCharsets.ISO_8859_1);
			//using the new bytearray as salt, while saving the saltstring with the password using ! as a divisor
			password = SHA_256Password(password, bytes) + "!" + saltstring;

			Database.insert_User(username, password, email); //return true if registration is successfull
			return true;
		}catch (Exception e){
			Cleanup.writeMessage(e, "registerNewUser() - salting and hashing");
			return false;
		}
	}

	private void login() {
		String username = "";
		String password = "";
		int i = 0;
		for(String word: ((String)read).split(",")) {
			if(i == 0) {
				username = word;
				i++;
			}else if(i == 1) {
				password = word;
			}
		}
		try {
			if(passwordOK(username, password)) {
				User logged = new User(Database.select_UserID(username), username);
				outputStream.reset();
				outputStream.writeObject(logged);
				System.out.println(username + " logged in!");
			}else {
				System.out.println("Someone failed to login");
				outputStream.writeObject("Wrong username/Password");
			}
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "login()");
		}finally {
			resetStreams();
		}
	}

	private boolean passwordOK(String username, String password){
		try {
			//calling the password + salt string navalbattle because it's awesome
			String navalbattle = Database.select_UserPassword(username);
			if(navalbattle == null) {
				System.out.println("Cannot find Username in Database");
				return false;
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
			password = SHA_256Password(password, salt);
			//adding the salt as string + divisor
			password += "!" + saltword;

			//if the password hash+saltstring matches, the User will login
			Database.update_UserLogin(username, password); //returns false if information doesnt match
			return true;
		} catch (Exception e){
			Cleanup.writeMessage(e, "login() - hashing and salting");
			return false;
		}
	}

	private boolean logoff(int userID){
		String username = Database.select_Username(userID);

		if(Database.select_UserOnline(username)) {
			return Database.update_UserLogoff(username);
		}
		return false;
	}



	/*
	 * EXTERNAL CODE
	 * source: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#sha 
	 */
	public String SHA_256Password(String passwordToHash, byte[] salt)
	{
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			Cleanup.writeMessage(e, "SHA_256Password()");
		}
		return generatedPassword;
	}

	private byte[] gensalt(){
		SecureRandom sr;
		byte[] salt = new byte[16];
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			sr.nextBytes(salt);
		}catch(Exception e){
			Cleanup.writeMessage(e, "gensalt()");
			return null;
		}
		return salt;
	}
}
/* 
 * END OF EXTERNAL CODE
 */
















/*
 * Handles updates from the game itself, recieving an updated arraylist of Municipalities
 * and sends the new arraylist to the other user
 * when endturn is called the class sends the arraylist to the database for storage
 */


class GameServer implements Runnable{
	private volatile boolean exit; //used to stop the GameServer Thread

	protected String ip;
	private final int[] gamePorts = {4444, 9999, 5555, 6666, 4949, 9494, 5656, 6565};
	private ServerSocket serverSocket;
	private ServerSocket[] boundSockets = new ServerSocket[gamePorts.length];
	private ArrayList<ObjectInputStream> inputStream = new ArrayList<ObjectInputStream>(); //Total inputStreams.
	private ArrayList<ObjectOutputStream> outputStream = new ArrayList<ObjectOutputStream>(); //Total outputStreams
	private ArrayList<User> users = new ArrayList<User>();
	private Game game = null;

	public GameServer() {
		try {
			ip = /*"192.168.137.212"*/"192.168.137.40";
		}catch(Exception e) {
			Cleanup.writeMessage(e, "GameServer() - cant find IP");
		}
		for(int i =  0; i < gamePorts.length; i++) {
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(ip, gamePorts[i]));
				boundSockets[i] = serverSocket;
			}catch(Exception e) {
				Cleanup.writeMessage(e, "gameServer() - socket trouble");
			}
		}
	}

	public void run() {
		System.out.println("GameServer running...");
		exit = false;
		while(!exit) {
			runLobby();
			startGame();
			runGame();
			closeStreams();
		}
		Cleanup.closeServerSocket(serverSocket);

	}

	public void stop() {
		exit = true;
	}

	private void closeStreams() {
		for(int i = 0; i < inputStream.size(); i++) {
			try {
				inputStream.get(i).close();
			}catch(Exception e) {
				Cleanup.writeMessage(e, "GameServer - end() inputstream");
			}
		}
		for(int i = 0; i < outputStream.size(); i++) {
			try {
				outputStream.get(i).close();
			}catch(Exception e) {
				Cleanup.writeMessage(e, "GameServer - end() outputstream");
			}
		}
		System.out.println("\nGameServer stopped");
	}

	private synchronized void runLobby() {
		//		int MAX_PLAYERS = 2;
		User curUser = null;
		boolean searchForPlayer1, searchForPlayer2, newErrorMessage;
		searchForPlayer1 = searchForPlayer2 = newErrorMessage = true;

		//probably only works if all users connect to the same sockets
		//		for(int i = 0; i < MAX_PLAYERS; i++) {
		//			
		//		}
		System.out.println("     Looking for player1...");
		while(searchForPlayer1) {
			try {
				Socket socketIn = boundSockets[0].accept();
				Socket socketOut = boundSockets[1].accept();

				ObjectInputStream reader = new ObjectInputStream(socketIn.getInputStream());
				ObjectOutputStream writer = new ObjectOutputStream(socketOut.getOutputStream());

				curUser = (User)reader.readObject();

				if(Database.select_UserOnline(curUser.getUsername())) {
					System.out.println("Player 1 Found!");
					searchForPlayer1 = false;
					users.add(curUser);

					inputStream.add(reader);
					outputStream.add(writer);
					/*reader.reset();
					writer.reset();*/
				}else {
					reader.close();
					writer.close();
				}
			}catch(Exception e) {
				if(newErrorMessage) {
					Cleanup.writeMessage(e, "runlobby()");
					newErrorMessage = false;
				}

			}
		}

		newErrorMessage = true;
		System.out.println("     Looking for player2...");
		while(searchForPlayer2) {
			try {
				Socket socketIn = boundSockets[2].accept();
				Socket socketOut = boundSockets[3].accept();

				ObjectOutputStream writer = new ObjectOutputStream(socketOut.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(socketIn.getInputStream());

				curUser = (User)reader.readObject();
				if(Database.select_UserOnline(curUser.getUsername())) {
					System.out.println("Player 2 found!");
					searchForPlayer2 = false;
					users.add(curUser);

					writer.writeObject(users.get(0));
					outputStream.get(0).writeObject(users.get(1));
					outputStream.get(0).flush();
					inputStream.add(reader);
					outputStream.add(writer);
					/*reader.reset();
					writer.reset();*/
				}else {
					reader.close();
					writer.close();
				}
			}catch(Exception e) {
				if(newErrorMessage) {
					Cleanup.writeMessage(e, "runlobby() - cannot find player 2");
					newErrorMessage = false;
				}
			}
		} 
	}
	
	private void startGame() {
		System.out.println("Creating game...");
		User[] gameUsers = new User[users.size()];

		//assigns each player a party when the game starts
		boolean liberal = true;
		
		users.get(0).setParty(new Party("004E8B", "Høyre", 1));
		gameUsers[0]=users.get(0);
		
		users.get(1).setParty(new Party("FF0000", "Arbeiderpartiet", 0));
		gameUsers[1]=users.get(1);
		
		//creating game object
		game = new Game(gameUsers);
		System.out.println("player 1: "+ game.getUsers()[0].getParty().getPartyID());
		System.out.println("player 2: "+ game.getUsers()[1].getParty().getPartyID());

		//inserts a new row in the Game table
		game.setGameId(Database.insert_NewGame(gameUsers.length));

		//inserts the startmap into mySQL
		Database.insert_StartMap(game.getMuns(), game.getGameId());

		for(int i = 0; i < gameUsers.length; i++) {
			//Adds the game setup values to the UserGame table
			Database.insert_UserGame(game.getUsers()[i].getUserid(), game.getGameId(), game.getUsers()[i].getParty().getPartyID());

			//increase gameCount for player
			Database.update_Gamecount(game.getUsers()[i].getUserid());
		}

		System.out.println("\nGame Made: \n" + game);

		game.setCurUser(users.get(0));

		//		if/when players can choose their own party
		//		Database.update_PartyForUser(gameUsers[0].getUserid(), game.getGameId(), "Arbeiderpartiet");
		//		Database.update_PartyForUser(gameUsers[1].getUserid(), game.getGameId(), "Høyere");
	}

	@SuppressWarnings("unchecked")
	private void runGame() {
		System.out.println("\n\nGame running!");

		ArrayList<Municipality> current = new ArrayList<Municipality>();
		ArrayList<Municipality> update = new ArrayList<Municipality>();
		Object readFromUser = null;

		try {
			//Sends the game to both players
			outputStream.get(1).writeObject(game);
			outputStream.get(0).writeObject(game);
			System.out.println("Game sent to players");

			int roundNR = 0;
			boolean play = true;

			//loops while the game is active
			while(play && !Thread.interrupted()) {

				//Updates RoundNumber
				roundNR++;
				Database.update_RoundNumber(roundNR, game.getGameId());
				System.out.println("Round " + roundNR + " has started");

				//for each round all players makes their moves 
				for(int i = 0; i < users.size(); i++) {

					//Sends updated map to player then reads input from that player 
					//(unless it is the first round for player1 as he/she already has an updated map)
					if(!(roundNR == 1 && i == 0)) {outputStream.get(i).writeObject(update);}
					System.out.println("reading from player " + i + " ...");
					while(update.equals(current) && !Thread.interrupted()) {
						readFromUser = inputStream.get(i).readObject();
						if(readFromUser != null) {
							update = (ArrayList<Municipality>)readFromUser;
						}
					}
					System.out.println("Updating gamestate");
					updateDatabase(update);
					current = update;
				}
			}
		}catch(Exception e){
			Cleanup.writeMessage(e, "rungame()");
		}
	}




	



	private void updateDatabase(ArrayList<Municipality> fromUser) {
		//retrieves the registered gameState from the Database
		ArrayList<Municipality> fromDatabase = Database.select_AllMunicipalities(game.getGameId());

		//Finds the municipalities that needs to update in the Database
		ArrayList<Municipality> toUpdate = findChanges(fromDatabase, fromUser);

		//Updates Database with the changes if there are any
		if(!toUpdate.isEmpty()) {
			Database.update_Municipalities(toUpdate, game.getGameId());
		}

		//		ArrayList<Municipality> toDelete = mToDelete(inDb, update);
		//		ArrayList<Municipality> toInsert = mToUpdate(inDb, update);
		//		for(int i = 0; i < toDelete.size(); i++) {
		//			Database.delete_Municipality(toDelete.get(i).getId());
		//		}
		//		for(int i = 0; i < toInsert.size(); i++) {
		//			Database.insert_MergedMunicipality(toInsert.get(i), game.getGameId());
		//		}

	}

	/*	
	//Checks what rows to delete from table
	private ArrayList<Municipality> mToDelete(ArrayList<Logic.Municipality> inDb, ArrayList<Logic.Municipality> update){
		ArrayList<Municipality> changedM = new ArrayList<>();
		boolean[] changes = new boolean[inDb.size()];

		for(int z = 0; z < changes.length; z++) {
			changes[z] = true;
		}
		for(int i = 0; i < inDb.size(); i++) {
			for(int y = 0; y < update.size(); y++) {
				if(inDb.get(y).getId() == inDb.get(i).getId()) {
					changes[i] = false;
				}
			}
		}

		for(int x = 0; x < changes.length; x++) {
			if(changes[x] == true) {
				changedM.add(update.get(x));
			}
		}
		return changedM;

	}
	 */
	/*
	private ArrayList<Municipality> mToInsert(ArrayList<Municipality> inDb, ArrayList<Municipality> update){
		ArrayList<Municipality> changedM = new ArrayList<>();
		boolean[] changes = new boolean[update.size()];

		for(int z = 0; z < changes.length; z++) {
			changes[z] = true;
		}
		for(int i = 0; i < update.size(); i++) {
			for(int y = 0; y < inDb.size(); y++) {
				if(update.get(i).getId() == inDb.get(y).getId()) {
					changes[i] = false;
				}
			}
		}

		for(int x = 0; x < changes.length; x++) {
			if(changes[x] == true) {
				changedM.add(update.get(x));
			}
		}
		return changedM;
	}
	 */
	//Checks what rows to update
	private ArrayList<Municipality> findChanges(ArrayList<Municipality> fromDatabase, ArrayList<Municipality> fromUser) {
		ArrayList<Municipality> changedMunicipalities = new ArrayList<Municipality>();
		//		ArrayList<Municipality> deletedMunicipalities = new ArrayList<Municipality>();

		//Nothing needs to be changed
		if(fromDatabase.equals(fromUser)) {
			System.out.println("found no changes between User and Database");
			return changedMunicipalities;
		}
		//someone has joined some municipalities
		if(fromDatabase.size() > fromUser.size()) {
			System.out.println("Database array was longer than user array");
		}
		//someone has split some municipalities
		else if(fromDatabase.size() < fromUser.size()) {
			System.out.println("Database array was shorter than user array");
		}
		//Finds which municipalities hav changed
		else{
			for(int i = 0; i < fromDatabase.size(); i++) {
				if(!fromUser.get(i).equals(fromDatabase.get(i))) {
					System.out.println(fromUser.get(i).getName() + " user VS database " + fromDatabase.get(i).getName());
					changedMunicipalities.add(fromUser.get(i));
					System.out.println("found a changed municipoality:	" + fromUser.get(i));

				}
			}
		}


		/*
		boolean[] changes = new boolean[fromUser.size()];
		int[][] valuesTo = this.breakDown(update);
		int[][] valuesFrom = this.breakDown(fromTable);

		for(int z = 0; z < changes.length; z++) {
			changes[z] = true;
		}
		for(int i = 0; i < valuesTo.length; i++) {
			for(int y = 0; y < valuesFrom.length; y++) {
				if(valuesTo[i][2] == valuesFrom[y][2]) {
					changes[i] = false;
				}
			}
		}

		for(int i = 0; i < valuesTo.length; i++) {
			for(int y = 0; y < valuesFrom.length; y++) {
				if(valuesTo[i][3] == valuesFrom[y][3]) {
					changes[i] = false;
				}
			}
		}

		for(int i = 0; i < valuesTo.length; i++) {
			for(int y = 0; y < valuesFrom.length; y++) {
				if(valuesTo[i][4] == valuesFrom[y][4]) {
					changes[i] = false;
				}
			}
		}

		for(int x = 0; x < changes.length; x++) {
			if(changes[x] == true) {
				changedM.add(update.get(x));
			}
		}
		 */
		return changedMunicipalities;
	}
	/*
	private int[][] breakDown(ArrayList<Municipality> list) {
		int[][] simplified = new int[list.size()][5];
		for(int i = 0; i < list.size(); i++) {
			simplified[i][0] = list.get(i).getId();
			simplified[i][1] = list.get(i).getOwner().getPlayer();
			simplified[i][2] = game.getGameId();
			simplified[i][3] = list.get(i).getTroops();
			if(list.get(i).getCity()) {
				simplified[i][4] = 1;
			}else {
				simplified[i][4] = 0;
			}

		}
		return simplified;
	}
	 */

}























