package Logic.Stats;

import java.io.Serializable;

/**
 * a class representing a formerly played game
 */
public class ArchivedGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final int roomsize;
	private final String played_as_party;
	private final int rounds;
	private final String winner;
	private final String date;

	/**
	 *
	 * @param roomsize how many players were in the game
	 * @param played_as_party the party this player played as
	 * @param rounds how many round this player played
	 * @param winner who won the game
	 * @param date when this game was played
	 */
	public ArchivedGame(int roomsize, String played_as_party, int rounds, String winner, String date) {
		this.roomsize = roomsize;
		this.played_as_party = played_as_party;
		this.rounds = rounds;
		this.winner = winner;
		this.date = date;
	}

	/**
	 *
	 * @return how many people were in the game
	 */
	public int getRoomsize() {
		return roomsize;
	}

	/**
	 *
	 * @return who this player played as
	 */
	public String getPlayed_as_party() {
		return played_as_party;
	}

	/**
	 *
	 * @return how many rounds the game lasted
	 */
	public int getRounds() {
		return rounds;
	}

	/**
	 *
	 * @return who won the game
	 */
	public String getWinner() {
		return winner;
	}

	/**
	 *
	 * @return when the game was played
	 */
	public String getDate() {
		return date;
	}
}
