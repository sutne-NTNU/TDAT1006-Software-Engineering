package serverclasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import Logic.Cleanup;
import Logic.Objects.Municipality;
import Logic.Objects.Party;
import Logic.Objects.User;
import Logic.Stats.Stats;

/*
 * Handles all communication bewteen the server and the MySQL database
 * The class Is organised by what table each of the methods affects
 */


public class Database {
	private static ConnectionPool pool;
	private static final int MAX_PLAYERS_PER_GAME = 5;
	
	public Database() {
		
		try {
			String password = Cleanup.getPassword();
			String username = Cleanup.getProperty("username");
			String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/";
			
			//Creates a new connectionPool 
			System.out.println("Setting up Database Connection to - " + url + username);
			pool = new ConnectionPool(username, password, url + username);

		} catch (Exception e) {
			Cleanup.writeMessage(e,  "Database()");
		}
	}

	/**
	 * shuts down the connectionpool
	 */
	public void shutDown() { 
		pool.close(); 
	}





















	/* 
	 * 
	 * User (UserID, Username, Password, email, Gamecount, Gameswon, online, date_registered)
	 * 
	 */



	/*
	 * Single user
	 */


	/**This method inserts a user into the database, given the following parameters, returns true if successful
	 * @param username the name of the user
	 * @param password the passwordhash of the user
	 * @param email the email of the user
	 * @return boolean true if successful
	 */
	public static boolean insert_User(String username, String password, String email) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("INSERT INTO User (username, password, email) VALUES ( ?, ?, ? );");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			try {
				pstmt.executeUpdate();
			}
			catch(Exception e)
			{
				return false;
			}

			return true;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "insert_User()");
			return false;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
	}
	
	/**Select a hashed password from the user-table, given the username. Retuns null if the password is not found
	 * @param username user to select passwordhash of
	 * @return String, null if passwordhash not found
	 */
	public static String select_UserPassword(String username) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("SELECT password FROM User WHERE username = ? ;");
			pstmt.setString(1, username);
			res = pstmt.executeQuery();
			
			if(res.next()){
				return res.getString("password");
			}
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "select_UserPassword()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return null;
	}

	/**Sets a user to online, if the user is in the User table. Returns false if username/password do not corrolate.
	 * @param username username of user to login
	 * @param password passwordhash of users password to login
	 * @return boolean true if successful
	 */
	@SuppressWarnings("resource")
	public static User update_UserLogin(String username, String password) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			con.setAutoCommit(false);
			con.setSavepoint();

			//makes sure user isnt already logged in
			pstmt = con.prepareStatement("SELECT online, UserID FROM User WHERE username = ? ;");
			pstmt.setString(1, username);
			res = pstmt.executeQuery();
			if(!res.next() || res.getBoolean("online")) {
				return null;
			}
		
			pstmt = con.prepareStatement("UPDATE User SET online = true WHERE username = ? AND password = ? ;");
			pstmt.setString(1,username);
			pstmt.setString(2,password);
			if(pstmt.executeUpdate() != 1) 
			{ 
				//Either password is wrong or more than one user is affected (an error)
				con.rollback();
				return null; 
			}
			con.commit();
			
			return new User(res.getInt("UserID"), username);
		}catch(SQLException e) {
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "update_UserLogin()");
			return null;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			Cleanup.autoCommit(con, true);
			pool.releaseConnection(con);
		}
	}

	/**Switches user to offline, returns false if several users are updated or username isnt found
	 * @param username user to logoff
	 * @return boolean true if successful
	 */
	public static boolean update_UserLogoff(String username) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE User SET online = false WHERE username = ? ;");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
			return true;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_UserLogoff()");
			return false;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**
	 * adds 1 to the Gamecount for the users when they start a game (quitting a game counts as a loss)
	 *
	 * @param userID the id of the user that has started a game
	 * @return true if successful
	 */
	public static boolean update_Gamecount(int userID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE User SET Gamecount = Gamecount + 1 WHERE UserID = ? ;");
			pstmt.setInt(1, userID);
			int playersUpdated = pstmt.executeUpdate();
			
			return playersUpdated == 1;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_Gamecount()");
			return false;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**
	 * adds 1 to the Gameswon for the user that wins a game
	 *
	 * @param userID the id of the user to update won games of
	 * @return true if successful
	 */
	public static boolean update_Gameswon(int userID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE User SET Gameswon = Gameswon + 1 WHERE UserID = ? ;");
			pstmt.setInt(1, userID);
			int playersUpdated = pstmt.executeUpdate();
			
			return playersUpdated == 1;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_Gameswon()");
			return false;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**Fetches data from DB and creates a stats-object given a username.
	 * @param userID the id of the user to fetch data of
	 * @return Stats the stats object of the user
	 */
	@SuppressWarnings("resource")
	public static Stats select_Stats(int userID) {
		Connection con = pool.fetchConnection();
		ResultSet res = null;
		PreparedStatement pstmt = null;
		try {		
			pstmt = con.prepareStatement("SELECT Username, Gamecount, Gameswon, FORMAT( ((Gameswon * 1.0)/(Gamecount * 1.0)) , 2 ) AS WinRatio, Date_registered FROM User WHERE UserID = ? ");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
			Stats stats = new Stats();
			if(res.next()) 
			{
				stats.setPersonalStats(res.getString("username"), res.getInt("gamecount"), res.getInt("gameswon"), res.getString("WinRatio"), res.getString("Date_registered"));
			}
			
			pstmt = con.prepareStatement("SELECT Username, Gamecount, Gameswon, FORMAT( ((Gameswon * 1.0)/(Gamecount * 1.0)) , 2 ) AS WinRatio, Date_registered FROM User ORDER BY `WinRatio`DESC, Gamecount ASC");
			res = pstmt.executeQuery();
			while(res.next())
			{
					stats.addToPlayerStats(res.getString("username"), res.getInt("gamecount"), res.getInt("gameswon"), res.getString("WinRatio"), res.getString("Date_registered"));
			}
			
			pstmt = con.prepareStatement("SELECT Game.Roomsize, Partylist.Party, Game.Rounds, Game.Winner, Game.Time FROM Game JOIN User ON Winner = UserID JOIN UserGame ON UserGame.GameID = Game.GameID JOIN Partylist ON Partylist.PartyID = UserGame.PartyID WHERE UserGame.UserID = ? ;");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
			while(res.next())
			{
				stats.addToGameHistory(res.getInt("Roomsize"), res.getString("Party"), res.getInt("Rounds"), res.getString("Winner"), res.getString("Time"));
			}
			
			return stats;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "select_Stats()");
			return null;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
	}


	/**
	 * logs of all the users
	 * @return the number of updated tables if everything is fine
	 */
	public static int update_LogoffAllUsers() {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE User SET online = false WHERE online = true;");
			return pstmt.executeUpdate();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_LogoffAllUsers()");
			return -1;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}





















	/*
	 * 
	 * UserGame_has_Municipalities (MunicipalityID, Name, UserID, Troops, City GameID)
	 * 
	 */


	/**Inserts an entire arrayList of municipalities into the DB
	 * @param municipalities arraylist of municipalities to insert
	 * @param gameID game to insert municipalities into
	 */
	//Updates an entire array of municipalities (preferably only the ones that actually needs updates)
	public static void insert_StartMap(ArrayList<Municipality> municipalities, int gameID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			con.setSavepoint();
			pstmt = con.prepareStatement("INSERT INTO UserGame_has_Municipalities(MunicipalityID, Name, UserID, Troops, City, GameID) VALUES ( ?, ?, ?, ?, ?, ? );");
			pstmt.setInt(6, gameID);
			for(Municipality municipality : municipalities) {
				pstmt.setInt(1,  municipality.getID());
				pstmt.setString(2, municipality.getName());
				pstmt.setInt(3,  municipality.getOwner().getUserID());
				pstmt.setInt(4,  municipality.getTroops());
				pstmt.setBoolean(5, municipality.getCity());
				pstmt.addBatch();
			}
			pstmt.executeLargeBatch();
			con.commit();
		}catch(SQLException e) {
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "insert_StartMap()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.autoCommit(con, true);
			pool.releaseConnection(con);
		}
	}

	/**Updates a given game in the DB in accordance to Municipalities given in the ArrayList argument.
	 * @param municipalities the municipality arraylist to update game from
	 * @param gameID the id of the game to update
	 */
	
	public static void update_Municipalities(ArrayList<Municipality> municipalities, int gameID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE UserGame_has_Municipalities SET UserID = ?, Troops = ?  WHERE MunicipalityID = ? AND GameID = ? ;");
			pstmt.setInt(4, gameID);
			for(Municipality municipality : municipalities) {
				pstmt.setInt(1,  municipality.getOwner().getUserID());
				pstmt.setInt(2,  municipality.getTroops());
				pstmt.setInt(3,  municipality.getID());
				pstmt.addBatch();
			}
			pstmt.executeLargeBatch();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_Municipalities(ArrayList)");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.autoCommit(con, true);
			pool.releaseConnection(con);
		}
	}
	

	/** selects all the municipalities from a given game
	 * @param gameID the id of the game to check
	 * @return ArrayList an arraylist of all municipalities in the game
	 */

	public static ArrayList<Municipality> select_AllMunicipalities(int gameID) {
		Connection con = pool.fetchConnection();
		ResultSet res = null;
		PreparedStatement pstmt = null;
		ArrayList<Municipality> municipalities = new ArrayList<Municipality>();
		try {
			pstmt = con.prepareStatement("SELECT MunicipalityID, Name, Username, UserID, GameID, Troops, City FROM UserGame_has_Municipalities NATURAL JOIN User WHERE GameID = ? ORDER BY MunicipalityID;");
			pstmt.setInt(1, gameID);
			res = pstmt.executeQuery();
			
			while(res.next()) {
				int municipalityID = res.getInt("MunicipalityID");
				int userID = res.getInt("UserID");
				String name = res.getString("Name");
				String username = res.getString("Username");
				int troops = res.getInt("Troops");
				boolean city = res.getBoolean("City");
				municipalities.add(new Municipality(municipalityID, name, new User(userID,username), city, troops));
			}
			return municipalities;
			
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "select_AllMunicipalities()");
			return null;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
	}



















	/*
	 * 
	 * UserGame (GameID, UserID, PartyID)
	 * 
	 */


	/**Inserts data into the usergame table.
	 * @param userID the id of the user to insert
	 * @param gameID the id of the game the user is in
	 * @param PartyID the id of the party the user has
	 */

	public static void insert_UserGame(int userID, int gameID, int PartyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("INSERT INTO UserGame(UserID, GameID, PartyID) VALUES ( ?, ?, ?);");
			pstmt.setInt(1,  userID);
			pstmt.setInt(2,  gameID);
			pstmt.setInt(3,  PartyID);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "insert_UserGame()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


/*
 * 
 * 
 * Lobby (LobbyID, UserID, PartyID)
 * 
 */


	/**Selects a lobby with one or more free spots, returns the LobbyID
	 * @return int the id of the lobby
	 */
	public static int select_LobbyWithFreeSpot() {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		
		try {
			pstmt = con.prepareStatement("SELECT LobbyID FROM Lobby GROUP BY LobbyID HAVING COUNT(LobbyID) < ? ;");
			pstmt.setInt(1, MAX_PLAYERS_PER_GAME);
			res = pstmt.executeQuery();
			if(res.next()) {
				return res.getInt("LobbyID");
			}
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "select_LobbyWithFreeSpot()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return -1;
	}

	/**Selects a random party that has not been selected yet, returns the Party Object
	 * @param lobbyID the id of the lobby to check selected parties in
	 * @return Party the selected party
	 */
	public static Party select_FreeRandomPartyInLobby(int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("SELECT PartyID, Party, Colour FROM Partylist WHERE PartyID >= 0 AND PartyID NOT IN (SELECT PartyID FROM Lobby WHERE LobbyID = ? );");
			pstmt.setInt(1, lobbyID);
			res = pstmt.executeQuery();
			ArrayList<Party> parties = new ArrayList<Party>();
			while(res.next()) {
				parties.add(new Party(res.getString("Colour"), res.getString("Party"), res.getInt("PartyID")));
			}
			Random random = new Random();
			int party = random.nextInt(parties.size());
			return parties.get(party);
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "select_FreeRandomPartyInLobby()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return null;
	}

	/**Inserts a new user into a given lobby, and returns the users given party.
	 * @param lobbyID id of the lobby to insert the user into
	 * @param userID the id of the user to insert
	 * @return Party the party the user got in the lobby
	 */
	public static Party insert_JoinLobby(int lobbyID, int userID){
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			//finds a free random Party for the player joining an already opened lobby
			Party party = Database.select_FreeRandomPartyInLobby(lobbyID);
			int partyID = party.getPartyID();
			pstmt = con.prepareStatement("INSERT INTO Lobby(LobbyID , UserID, PartyID) VALUES ( ? , ? , ? ) ;");
			pstmt.setInt(1, lobbyID);
			pstmt.setInt(2, userID);
			pstmt.setInt(3, partyID);
			pstmt.executeUpdate();
			
			return party;
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "insert_JoinLobby()");
			return null;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**Inserts a new lobby into the lobby table, given a user that has no lobby. Returns the LobbyID
	 * @param userID the id of the user that created the lobby
	 * @return int the lobbyid
	 */
	@SuppressWarnings("resource")
	public static int insert_NewLobby(int userID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			//When starting a new lobby the player is assigned a random party
			Random random = new Random();
			ArrayList<Party> parties = Database.select_AllParties();
			int partyNumber = random.nextInt(parties.size());
			int partyID = parties.get(partyNumber).getPartyID();
			
			pstmt = con.prepareStatement("INSERT INTO Lobby(UserID, PartyID) VALUES ( ? , ? ) ;");
			pstmt.setInt(1, userID);
			pstmt.setInt(2, partyID);
			pstmt.executeUpdate();
			
			pstmt = con.prepareStatement("SELECT LAST_INSERT_ID() AS LobbyID;");
			res = pstmt.executeQuery();
			if(res.next()) {
				return res.getInt("LobbyID");
			}
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "insert_NewLobby()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return -1;
	}

	/**Selectsthe users already in the lobby
	 * @param lobbyID the id of the lobby the users are in
	 * @return ArrayList the arraylist of users in the lobby
	 */

	public static ArrayList<User> select_UsersInLobby(int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<User> usersInLobby = new ArrayList<User>();
		try {
			pstmt = con.prepareStatement("SELECT UserID, Username, PartyID, Colour, Party FROM Lobby NATURAL JOIN User NATURAL JOIN Partylist WHERE LobbyID = ? ;");
			pstmt.setInt(1, lobbyID);
			res = pstmt.executeQuery();
			while(res.next()){
				usersInLobby.add(new User(res.getInt("UserID"), res.getString("Username"), new Party(res.getString("Colour"), res.getString("Party"), res.getInt("PartyID"))));
			}
			return usersInLobby;
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "select_UsersInLobby()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return null;
	}

	/**Updates the party of a given user, in a given lobby
	 * @param userID the id the user to update party of
	 * @param lobbyID the id of the lobby the user is in
	 * @param party the party to change to
	 */

	public static void update_PartyForUser(int userID, int lobbyID, Party party) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE Lobby SET PartyID = ? WHERE LobbyID = ? AND UserID = ? ;");
			pstmt.setInt(1, party.getPartyID());
			pstmt.setInt(2, lobbyID);
			pstmt.setInt(3, userID);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_PartyForUser()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**Returns whether or not a given user in a given lobby is ready
	 * @param userID the id of the user to check
	 * @param lobbyID the id of the lobby the user is in
	 * @return boolean true if player is ready
	 */
	/*
	public static boolean select_PlayerReady(int userID, int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("SELECT Ready FROM Lobby WHERE LobbyID = ? AND UserID = ? ;");
			pstmt.setInt(1, lobbyID);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
			if(res.next()) {
				return res.getBoolean("Ready");
			}
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_PartyForUser()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return false;
	}
	*/

	/**change the ready state of a given user in a given lobby, returns whether or not a player is ready
	 * @param userID the ID of the user to change
	 * @param lobbyID the id of the lobby the user is in
	 * @return boolean true if players ready state is changed
	 */
	@SuppressWarnings("resource")
	public static boolean update_TogglePlayerReady(int userID, int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			boolean ready = false;
			pstmt = con.prepareStatement("SELECT Ready FROM Lobby WHERE LobbyID = ? AND UserID = ? ;");
			pstmt.setInt(1, lobbyID);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
			if(res.next()) {
				ready = res.getBoolean("Ready");
			}
		
			pstmt = con.prepareStatement("UPDATE Lobby SET Ready = ? WHERE LobbyID = ? AND UserID = ? ;");
			pstmt.setBoolean(1, !ready);
			pstmt.setInt(2, lobbyID);
			pstmt.setInt(3, userID);
			pstmt.executeUpdate();
			return ready;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_TogglePlayerReady()");
			return false;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
	}

	/**Selects the number of players ready in a given lobby
	 * @param lobbyID id of lobby where to check ready users
 	 * @return int number of ready users
	 */
	public static int[] select_PlayersReady(int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("SELECT Ready FROM Lobby WHERE LobbyID = ? ;");
			pstmt.setInt(1, lobbyID);
			res = pstmt.executeQuery();
			int[] players = new int[2];
			int ready = 1;
			int not_ready = 0;
			while(res.next()) {
				if(res.getBoolean("Ready")) {
					players[ready]++;
				}else{
					players[not_ready]++;
				}
			}
			return players;
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "select_NumberOfPlayersready()");
			return null;
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
	}

	/**Remove a given user from its corresponding lobby.
	 * @param userID id of user to remove from lobby
	 */

	public static void delete_LeaveLobby(int userID){
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("DELETE FROM Lobby WHERE UserID = ? ;");
			pstmt.setInt(1, userID);
			pstmt.executeUpdate();
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "delete_LeaveLobby()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/** Deletes a given lobby
	 * @param lobbyID id of lobby to delete
	 */

	public static void delete_Lobby(int lobbyID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("DELETE FROM Lobby WHERE LobbyID = ?");
			pstmt.setInt(1, lobbyID);
			pstmt.executeUpdate();
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "delete_Lobby()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**Deletes all of the lobbies in the lobby table.
	 *
	 */

	public static void delete_AllLobbies() {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("DELETE FROM Lobby;");
			pstmt.executeUpdate();
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "delete_AllLobbies()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * Partylist (PartyID, Party, Colour)
	 * 
	 */


	/**Fetches all of the parties currently registered to the database.
	 * @return ArrayList an arraylist of all parties
	 */
	public static ArrayList<Party> select_AllParties() {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Party> parties = new ArrayList<Party>();
		try {
			pstmt = con.prepareStatement("SELECT Colour, Party, PartyID FROM Partylist where PartyID >= 0;");
			res = pstmt.executeQuery();
			while(res.next()){
				parties.add(new Party(res.getString("Colour"), res.getString("Party"), res.getInt("PartyID")));
			}
			return parties;
		}catch(Exception e) {
			Cleanup.writeMessage(e,  "select_AllParties()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return null;
	}

	
	
	
	
	
	
	





	/*
	 * 
	 * Game (GameID, Roomsize, Rounds, News, Currentevent, Time_started)
	 * 
	 */


	/**Inserts a new game in the game table, given the players in the game, returns the gameID.
	 * @param players the number of players in the game
	 * @return int -1 if it was successful
	 */
	@SuppressWarnings("resource")
	public static int insert_NewGame(int players) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			pstmt = con.prepareStatement("INSERT INTO Game(Roomsize) VALUES (?);");
			pstmt.setInt(1, players);
			pstmt.executeUpdate();
			
			//retrieves and returns the last GameID inserted
			pstmt = con.prepareStatement("SELECT LAST_INSERT_ID() AS GameID;");
			res = pstmt.executeQuery();
			if(res.next()) {
				return res.getInt("GameID");
			}
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "insert_NewGame()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			Cleanup.closeResultSet(res);
			pool.releaseConnection(con);
		}
		return -1;
	}

	/**Update the round number to a given game.
	 * @param roundNumber the round the game is on
	 * @param gameID the id of the game
	 */

	public static void update_RoundNumber(int roundNumber, int gameID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE Game SET Rounds = ? WHERE GameID = ? ;");
			pstmt.setInt(1, roundNumber);
			pstmt.setInt(2, gameID);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_RoundNumber()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}

	/**Updates the game winner in the game table, to the given player.
	 * @param username the username of the winner
	 * @param gameID the id of the game
	 */
	public static void update_PlayerWon(String username, int gameID) {
		Connection con = pool.fetchConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE Game SET Winner = ? WHERE GameID = ? ;");
			pstmt.setString(1, username);
			pstmt.setInt(2, gameID);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			Cleanup.writeMessage(e,  "update_RoundNumber()");
		}finally {
			Cleanup.closePreparedStatement(pstmt);
			pool.releaseConnection(con);
		}
	}
}


