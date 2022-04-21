package Logic.Stats;

import java.io.Serializable;


/**
 * a class representing the stats of a player
 */
public class PlayerStat implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final String username;
	private final int gamecount;
	private final int gamesWon;
	private final String win_loss;
	private final String joinDate;

	/**
	 *
	 * @param username the username of the player this PlayerStat instance represents
	 * @param gamecount how many games the player has played
	 * @param victories how many times this player has won
	 * @param win_loss2 the win to loss ratio of this player
	 * @param joinDate when the player created this account
	 */
	public PlayerStat(String username, int gamecount, int victories, String win_loss2, String joinDate)
	{
		this.username = username;
		this.gamecount = gamecount;
		this.gamesWon = victories;
		this.win_loss = win_loss2;
		this.joinDate = joinDate;
	}

	/**
	 *
	 * @return the username of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 *
	 * @return how many games the player has played
	 */
	public int getGamecount() {
		return gamecount;
	}

	/**
	 *
	 * @return how many games the player has won
	 */
	public int getGamesWon() {
		return gamesWon;
	}

	/**
	 *
	 * @return the win to loss ratio of the player
	 */
	public String getWin_loss() {
		return win_loss;
	}

	/**
	 *
	 * @return the date the account of the player was created
	 */
	public String getJoinDate()	{
		return joinDate;
	}

	/**
	 *
	 * @return this object represented as a string
	 */
	@Override
	public String toString()
	{
		return username + " - has played " + gamecount + " games and won " + gamesWon + " W/L: " + win_loss + " Joined: " + getJoinDate();
	}
}

