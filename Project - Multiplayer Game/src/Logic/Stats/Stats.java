package Logic.Stats;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * a class representing the Stats of a player, their gamehistory and the highscore of all players
 */
public class Stats implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<PlayerStat> allPlayerStats;
	private ArrayList<ArchivedGame> gameHistory;
	private PlayerStat personalStats;

	/**
	 * the constructor creates new arraylists to hold the stats of other players and the gamehistory
	 */
	public Stats(){
		allPlayerStats = new ArrayList<PlayerStat>();
		gameHistory = new ArrayList<ArchivedGame>();
	}

	/**
	 *
	 * @return the arraylist of the highscores
	 */
	public ArrayList<PlayerStat> getAllPlayerStats() {
		return allPlayerStats;
	}

	/**
	 *
	 * @return arraylist of archived games, the game history
	 */
	public ArrayList<ArchivedGame> getGameHistory() {
		return gameHistory;
	}

	/**
	 *
	 * @return the players own stats
	 */
	public PlayerStat getPersonalStats() {
		return personalStats;
	}

	/**
	 * adds another players stats to the highscore
	 * @param username the players username
	 * @param gamecount how many games the player has played
	 * @param victories how many game the player has won
	 * @param win_loss the ratio of wins to losses of the player
	 * @param joinDate when the player created their account
	 */
	public void addToPlayerStats(String username, int gamecount, int victories, String win_loss, String joinDate) 
	{
		this.allPlayerStats.add(new PlayerStat(username, gamecount, victories, win_loss, joinDate));
	}

	/**
	 * adds a game to the gamehistory arraylist
	 * @param roomsize how many players that were initially in the game
	 * @param played_as_party the party the user played as
	 * @param rounds how many rounds the game lasted
	 * @param winner who won the game
	 * @param date when the game was played
	 */
	public void addToGameHistory(int roomsize, String played_as_party, int rounds, String winner, String date) 
	{
		this.gameHistory.add(new ArchivedGame(roomsize, played_as_party, rounds, winner, date));
	}

	/**
	 *  sets the stats of the player
	 * @param username the username of the player
	 * @param gamecount how many games the player has played
	 * @param victories how many times the player has won
	 * @param win_loss the win to loss ratio of the player
	 * @param joinDate when the player joined the game
	 */
	public void setPersonalStats(String username, int gamecount, int victories, String win_loss, String joinDate) 
	{
		this.personalStats = new PlayerStat(username, gamecount, victories, win_loss, joinDate);
	}

	/**
	 *
	 * @return the players personal stats
	 */
	@Override
	public String toString()
	{
		return personalStats.toString();
	}
}

