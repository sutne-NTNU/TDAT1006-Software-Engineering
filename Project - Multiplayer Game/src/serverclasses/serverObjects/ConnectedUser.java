package serverclasses.serverObjects;

import serverclasses.serverObjects.ConnectedClient;

/**
 * a class representing a client that is logged in but not in a lobby or a game
 */
public class ConnectedUser extends ConnectedClient
{
    /**
     * the ID of the user
     */
	private final int userID;
    /**
     * the username of the user
     */
    private final String username;

    /**
     * a constructor creating an original ConnectedUser instance
     * @param userID the ID of the user
     * @param username the username of the user
     * @param client the ConnectedClient which has the connection to the user
     */
    public ConnectedUser(int userID, String username, ConnectedClient client) 
    {
    	super(client);
    	this.userID = userID;
    	this.username = username;
    }

    /**
     * creates a new instance of ConnectedUser from an already existing instance
     * @param user the old instance which the attributes are transferred from
     */
    public ConnectedUser(ConnectedUser user)
    {
    	super(user);
    	this.userID = user.getUserid();
    	this.username = user.getUsername();
    }

    /**
     * creates a new ConnectedUser from an instance of ConnectedPlayer
     * this is needed when a player leaves a game or lobby, but doesn't log out
     * @param player the player to refactor into a user
     */
    public ConnectedUser(ConnectedPlayer player)
    {
    	super(player);
    	this.userID = player.getUserid();
    	this.username = player.getUsername();
    }

    /**
     *
     * @return the userID
     */
    public int getUserid()		{return userID;}

    /**
     *
     * @return the username
     */
    public String getUsername()	{return username;}

    /**
     *
     * @param user the other ConnectedUser to check whether equals
     * @return true if the ConnectedUsers are equal
     */
    public boolean equals(ConnectedUser user) 
    {
        return (userID == user.getUserid() && username.equals(user.getUsername()));
    }

    /**
     *
     * @return a string of the most important attributes
     */
    public String toString()
    {
        return super.toString() + "	ID: " + userID + ", username: " + username;
    }
    
    
}