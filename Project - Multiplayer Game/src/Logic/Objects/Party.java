/**
 * the class which defines a political party
 */
package Logic.Objects;

import java.io.Serializable;

// a class to store all the information regarding a users party
public class Party implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String colour;
    private final String partyName;
    private final int partyID;

    /**
     * creates a temporary party, which isn't real
     * @param partyID identifying number of party
     */
    public Party(int partyID){
        this.partyID = partyID;
        colour = "0";
        partyName = "not ready";
    }

    /**
     * creates a real party
     * @param colour the colour of the party
     * @param partyName the name of the party
     * @param partyID identifying number of the party
     */
    public Party(String colour, String partyName, int partyID) {
        this.colour = colour;
        this.partyName = partyName;
        this.partyID = partyID;
    }

    /**
     *
     * @return the colour representing the party
     */
    public String getColour() {
        return colour;
    }

    /**
     *
     * @return the name of the party
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     *
     * @return the id of the party
     */
    public int getPartyID() {
        return partyID;
    }

    /**
     * checks whether this equals another object
     * @param obj the object to check the equality of
     * @return whether the object both have the same partyid as this party and is a Party object
     */
    public boolean equals(Object obj){
        return obj instanceof Party && ((Party) obj).getPartyID() == this.getPartyID();
    }

    /**
     *
     * @return a string representation of the object
     */
    public String toString(){
        return partyName;
    }
}