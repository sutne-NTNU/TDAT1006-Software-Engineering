/**
 * municipality class represents a single tile in our game.
 */
package Logic.Objects;

import java.io.Serializable;

import Logic.Logic;

//a Municipality for our game
public class Municipality implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//our municipalities are very militant, so they have an attribute troops, they can also be cities, be valuable and have an owner!
    private final int id;
    private final String name;
    private User owner;
    private int troops;
    private boolean city;
    private int value = 1;

    /**
     *
     * @param id the id of the municipality
     * @param name the name of the municipality
     * @param owner the user who owns the municipality
     * @param city whether the municipality is a city or not
     * @param troops how many troops the municipality should have
     */
    public Municipality(int id, String name, User owner, boolean city, int troops){
        this.id = id;
        this.owner = owner;
        this.city = city;
        this.troops = troops;
        this.name = name;
    }
    
    //boring gets and sets

    /**
     *
     * @param owner set the user who owns the municipality
     */
    public void setOwner(User owner)	{this.owner = owner;}

    /**
     *
     * @param troops how many troops the municipality should have
     */
    public void setTroops(int troops) 	{this.troops = troops;}

    /**
     *
     * @return the owner of the municipality
     */
    public User getOwner()				{return owner;}

    /**
     *
     * @return the name of the municipality
     */
    public String getName() 			{return name;}

    /**
     *
     * @return the number of troops in the municipality
     */
    public int getTroops() 				{return troops;}

    /**
     *
     * @return the id of the municipality
     */
    public int getID() 					{return id;}

    /**
     *
     * @return whether the municipality is a city or not
     */
    public boolean getCity()			{return city;}

    /**
     *
     * @return the value of the municipality
     */
    public int getValue()				{return  value;}

    /**
     * troops will sometimes increase (for example at start of turn), max troops is limited by value of province and by the provinces city status.
     * @param recruits how many troops to add
     */
    public void addTroops(int recruits){
        int maxtroops =(int)(double)8 + value/5;
        if(city){
            maxtroops++;
        }
        troops += recruits;
        if(troops > maxtroops){
            troops = maxtroops;
        }
    }

    /**
     * attack another municipality
     *
     * @param defender the municipality which is attacked
     * @return true if attacker wins, and false if defender wins
     */
    public Municipality[] attackMunicipality(Municipality defender){
        Municipality[] result = null;
    	if(troops > 1){ //must have more than one troop to be able to attack
            boolean won = Logic.invasion(troops-1,defender.getTroops());
            if(won){
                defender.setTroops(troops-1);
                result = new Municipality[]{this, defender};
            }
            else if(!won)
            {
            	result = new Municipality[]{this, null};
            }
            troops = 1; //after attacking the attacking troops are always reduced to 1
        }
        return result;
    }

    /**
     * added an equals for simple comparison
     *
     * @param o another object
     * @return equality
     */
    public boolean equals(Object o){
    	if(o instanceof Municipality){
    		Municipality mun = (Municipality)o;
    		return(this.troops == mun.getTroops() && this.value == mun.getValue() && this.city == mun.getCity() && this.owner.equals(mun.getOwner()) && this.id == mun.getID());
    	}
    	return false;
    }

    /**
     * a little toString() which might come in handy, who really knows
     *
     * @return all the important information about the municipality
     */
    @Override
    public String toString(){
        return name + " - " +  "owner: " + owner.getUsername() + ", Troops: " + troops + ", city: " + city;
    }
    
}

