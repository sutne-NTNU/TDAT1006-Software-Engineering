/**
 * this class keeps track of all game and user related values connected to a player
 *
 */
package Logic.Objects;

import java.io.Serializable;


public class User implements Serializable{
    private static final long serialVersionUID = 1L;
	
	private int userID;
    private String username;


    /**
     * is chosen in the lobby and made as an object from the Database
     *
     */
    private Party party;

    /**
     * used to identify spot in Game array
     */
    private int playerNR;

    /**
     *
     * @param userID identifying number of user
     * @param username the nickname of the user
     */
    public User(int userID, String username){
        this.userID = userID;
        this.username = username;
        this.party = new Party("#FFFFFF", "nøytral", -1); 
    }

    /**
     *
     * @param userID identifying number of the user
     * @param username the nickname of the user
     * @param party the party of the user
     */
    public User(int userID, String username, Party party){
        this.userID = userID;
        this.username = username;
        this.party = party;
    }
    
    /*
     * setters
     */

    /**
     *
     * @param userID sets the id of the user
     */
    public void setUserID		(int userID) 		{this.userID = userID;}

    /**
     *
     * @param username sets the username of the user
     */
    public void setUsername		(String username) 	{this.username = username;}

    /**
     *
     * @param party sets the party of the user
     */
    public void setParty		(Party party) 		{this.party = party;}

    /**
     *
     * @param playerNR the the nr of the player
     */
    public void setPlayerNR		(int playerNR) 		{this.playerNR = playerNR;}

    
    /*
     * getters
     */

    /**
     *
     * @return the Userid of the user
     */
    public int getUserID() 		{return userID;}

    /**
     *
     * @return the username of the user
     */
    public String getUsername() {return username;}

    /**
     *
     * @return the party the user plays as
     */
    public Party getParty() 	{return party;}

    /**
     *
     * @return an int corresponding to the users place in the game array of userlist
     */
    public int getPlayerNR() 	{return playerNR;}

    /**
     *
     * @return a string representation of the object
     */
    public String toString(){
        return "ID: " + userID + ", username: " + username;
    }

    /**
     * the equals just checks userid as it should be unique
     * @param obj another object
     * @return the equality of objects
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof User)){
            return false;
        }
        return (userID == ((User) obj).getUserID());
    } 
}

