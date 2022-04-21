package serverclasses.serverObjects;
/**
 * represents a user which is either in a lobby or a game
 */

import Logic.Objects.Party;
import Logic.Objects.User;

public class ConnectedPlayer extends ConnectedUser
{
	/**
	 * the lobby the player belongs to
	 */
	private int lobbyID;
	/**
	 * the game the player is in
	 */
	private int gameID;
	/**
	 * the party the player plays as
	 */
	private Party party;
	/**
	 * the place in the game userarray the player has
	 */
	private int playerNumber;

	/**
	 * the constructor only has lobbyID and not gameID due to a user needing to enter the lobby to play a game
	 * @param user the user which is logged in
	 * @param lobbyID the lobby the user is in
	 */
	public ConnectedPlayer(ConnectedUser user, int lobbyID) 
	{
		super(user);
		this.lobbyID = lobbyID;
	}

	/**
	 *
	 * @return the gameID
	 */
	public int getGameID() 							{return gameID;}

	/**
	 *
	 * @return the playerNumber
	 */
	public int getPlayerNumber() 					{return playerNumber;}

	/**
	 *
	 * @return the players party
	 */
	public Party getParty() 						{return party;}

	/**
	 *
	 * @return the lobbyID
	 */
	public int getLobbyID() 						{return lobbyID;}

	/**
	 * sets the game the player is in
	 * @param gameID the id of the game the player has joined
	 */
	public void setGameID(int gameID) 				{this.gameID = gameID;}

	/**
	 * sets the int corresponding to the players place in the games userarray
	 * @param playerNumber the number corresponding to the place in the games userarray
	 */
	public void setPlayerNumber(int playerNumber) 	{this.playerNumber = playerNumber;}

	/**
	 * sets the players party
	 * @param party the party the player is set to
	 */
	public void setParty(Party party) 				{this.party = party;}

	/**
	 *
	 * @return an User object representative of the player
	 */
	public User getUser()
	{
		return new User(this.getUserid(), this.getUsername(), this.getParty());
	}

	/**
	 *
	 * @param player another player to check the equality of
	 * @return whether the players are equal
	 */
	public boolean equals(ConnectedPlayer player) 
	{
		return (this.getUserid() == player.getUserid());
	}

	/**
	 *
	 * @return the important attributes as a string
	 */
	public String toString() 
	{
		return super.toString() + " Lobby: " + lobbyID + " Game: " + gameID + " Party " + party.getPartyName();
	}
	
}
