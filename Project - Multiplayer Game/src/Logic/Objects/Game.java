package Logic.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import Logic.Logic;

/**
 * this class represents the game instance, containing all the players and the map of the game
 */

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;

	//municipality map!
	private ArrayList<Municipality> municipalities;
	private User[] users;
	private User currentPlayer;
	private int gameID, roundNumber, turn_number, max_turns;

	public Game(User[] users, ArrayList<Municipality> municipalities){
		this.users = users;
		this.currentPlayer = users[new Random().nextInt(users.length)];
		this.municipalities = municipalities;
		this.roundNumber = 0;
		this.max_turns = users.length * 4;
		this.turn_number = 1;
	}

	/*
	 * Getters
	 */

	/**
	 *
	 * @return the array of Users in the game
	 */
	 public User[] getUsers() 					{return users;}

	/**
	 *
	 * @return the player who currently plays
	 */
	 public User getCurrentPlayer() 			{return currentPlayer;}

	/**
	 *
	 * @return which round the game is currently on
	 */
	 public int getRoundNumber() 				{return roundNumber;}

	/**
	 *
	 * @return an arraylist of all the municipalities on the map
	 */
	 public ArrayList<Municipality> getMunicipalities() 	{return municipalities;}

	/**
	 *
	 * @return the id of the game
	 */
	 public int getGameID() 					{return gameID;}

	 /*
	  * Setters
	  */

	/**
	 *
	 * @param gameID sets the ID of the game
	 */
	 public void setGameID(int gameID){
		 this.gameID = gameID;
	 }

	/**
	 *
	 * @param municipalities sets the municipalities to be represented on the map
	 */
	 public void setMunicipalities(ArrayList<Municipality> municipalities){
		 for(int i = 0; i < municipalities.size();i++){
			 this.municipalities.set(i, municipalities.get(i));
		 }
	 }

	/**
	 * sets current user by checking whether it is the same user
	 *
	 * @param currentPlayer the next player
	 */
	 public void setCurrentPlayer(User currentPlayer){
		 this.currentPlayer = currentPlayer;
	 }

	/**
	 * changes currentPlayer to the next player
	 *
	 */
	public void newCurrentPlayer() {
		 if(currentPlayer.getUserID() == users[users.length-1].getUserID()){
			 currentPlayer = users[0];
			 turn_number++;
		 }else{
		 	currentPlayer = users[currentPlayer.getPlayerNR() + 1];
		 }
	 }

	/**
	 * setOwner for municipality in arraylist and all references to it in the map
	 *
	 * @param attackingmun the municipality that has invaded another
	 * @param defendingmun the municipality which changes owner
	 */
	 public void setOwner(Municipality attackingmun, Municipality defendingmun){
		 int indexatk = municipalities.indexOf(attackingmun);
		 int indexdef = municipalities.indexOf(defendingmun);
		 municipalities.get(indexdef).setOwner(municipalities.get(indexatk).getOwner());
	 }

	/**
	 * ending the turn, all provinces owned by the new current user have their value added to their troop count
	 *
	 */
	 public void endTurn(){
		//checks if any player has been eliminated
		 for(User user: users){
			 int ownedmuns = 0;
			 for(Municipality mun: municipalities){
				 if(mun.getOwner().equals(user)) ownedmuns++;
			 }
			 if(ownedmuns == 0){ 
				 removePlayer(user);
			 }
		 }
		 
		 //Adding more troops to the municipalities
		 for(int i = 0; i < municipalities.size(); i++){
			 if(municipalities.get(i).getOwner().equals(currentPlayer))
				 municipalities.get(i).addTroops((int)(double)((municipalities.get(i).getValue()+3)/3));
		 }
		 newCurrentPlayer();
		 turn_number++;
		 if(turn_number % users.length == 0)roundNumber++;
	 }

	/**
	 * removes a player from the game
	 * @param player the player to remove
	 * @return User-object that is either winner or Null
	 */
	 public int removePlayer(User player) {
		 User[] newUsers = new User[users.length - 1];
		 int teller = 0;
		 for(int i = 0; i < users.length; i++)
		 {
			 if(users[i].getUserID() != player.getUserID())
			 {
				 newUsers[teller] = users[i];
				 teller++;
			 }
		 }
		 this.users = newUsers;
		 return users.length;
	 }

	/**
	 * finds a winner
	 * @return winner
	 */
	 public User findWinner() {
		 User winner = null;
		 if(users.length == 1) {
			 return users[0];
		 }
		 if(turn_number > max_turns) {
			//finds the points for all users left in the game
			 int[] points = new int[users.length];
			 for(int i = 0; i < users.length; i++){
				 for(Municipality mun: municipalities){
					 if(mun.getOwner().equals(users[i])){
						 points[i] += mun.getValue();
					 }
				 }
			 }
			 //finds the player with the most points
			 int winnerpoints = 0;
			 for(int i = 0; i < points.length; i++){
				 if(points[i] > winnerpoints) {
					 winnerpoints = points[i];
					 winner = users[i];
					 winner.setPlayerNR(-1);
				 }
			 }
		 }
		 System.out.println(winner);
		 return winner;
	 }


	/**
	 * checks whether you can attack or not, and whether you win or lose
	 *
	 * @param attackingmun the municipality that attacks another
	 * @param defendingmun the municipality that defends itself from the attack
	 * @return whether player won or lost as string, or why you can't attack
	 */
	 public Municipality[] attack(Municipality attackingmun, Municipality defendingmun){
		 Municipality[] result;
		 try{
			 if(!(Logic.isOwner(attackingmun, currentPlayer)))
			 {
				 return null;
			 }
			 else if(Logic.isOwner(defendingmun, attackingmun.getOwner()))
			 {
				 return null;
			 }
			 else if(attackingmun.getTroops() <= 1)
			 {
				 return null;
			 }
			 else
			 {
				 result = battle(attackingmun,defendingmun);

				 if(result[1] != null) //attacker won
				 {
					 setOwner(attackingmun,defendingmun);
					 result = new Municipality[]{attackingmun,defendingmun};
				 }
			 }
			 return result;
		 }
		 catch (IllegalException e)
		 {
			 System.out.println(e);
			 return null;
		 }

	 }


	/**
	 *calculates a municipality attack, and updates all references to the battling municipalities in the map
	 * if the current user doesnt match attacker an exception is thrown because the move isn't supposed to be possible
	 *
	 * @param attackingmun the municipality which attacks
	 * @param defendingmun the municipality which defends
	 * @return true if attacker wins
	 * @throws IllegalException thrown if the currentplayer doesn't own the municipality, this should be prevented in gui
	 */
	 public Municipality[] battle(Municipality attackingmun, Municipality defendingmun)throws IllegalException{
		 if(currentPlayer.getUserID() != attackingmun.getOwner().getUserID()) throw new IllegalException("You cannot attack from a municipality you do not own");
		 return attackingmun.attackMunicipality(defendingmun);
	 }


	/**
	 *
	 * @return represents the game and its players as a string
	 */
	public String toString() {
		 String game = "	gameID: " + this.getGameID() + "\n	Players: ";
		 for(int i = 0; i < users.length; i++) {
			 game += "\n	- " + users[i].getUsername() + " playing as " + users[i].getParty().getPartyName();
		 }
		 game += "\n" + getCurrentPlayer().getUsername() + " makes the first move";
		 return game;
	 }
}
