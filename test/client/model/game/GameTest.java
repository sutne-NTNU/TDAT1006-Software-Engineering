package client.model.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameTest
{
    private Game game;
    private Player[] players;

    @Before
    public void setUp()
    {
        this.players = new Player[] {
            new Player("Player 0", PoliticalParty.AP),
            new Player("Player 1", PoliticalParty.H),
            new Player("Player 2", PoliticalParty.SP),
        };
        this.game = new Game(this.players);
    }

    @Test
    public void attackTest()
    {
        this.game.currentPlayer = this.players[0];
        Municipality attacker = this.game.municipalities[Municipality.TRONDHEIM];
        Municipality defender = this.game.municipalities[Municipality.MELHUS];
        attacker.owner = this.players[0];
        defender.owner = this.players[1];
        attacker.numDice = 7;
        defender.numDice = 1;
        assertTrue(this.game.performAttack(attacker, defender));
        try
        {
            // owner should have changed, method throws error as player can't attack themselves
            this.game.performAttack(attacker, defender);
            assertTrue(false);
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void endTurnNewPlayerTest()
    {
        Player currentPlayer = this.game.currentPlayer;
        this.game.endTurn();
        assertFalse(this.game.currentPlayer.equals(currentPlayer));
    }

    @Test
    public void endTurnDiceTest()
    {
        int opponentDiceBefore = 0;
        int playerDiceBefore = 0;
        Player player = this.game.currentPlayer;
        for (Municipality municipality : this.game.municipalities)
        {
            if (municipality.isOwner(player))
                playerDiceBefore += municipality.numDice;
            else
                opponentDiceBefore += municipality.numDice;
        }
        this.game.endTurn();
        int opponentDiceAfter = 0;
        int playerDiceAfter = 0;
        for (Municipality municipality : this.game.municipalities)
        {
            if (municipality.isOwner(player))
                playerDiceAfter += municipality.numDice;
            else
                opponentDiceAfter += municipality.numDice;
        }
        assertTrue(opponentDiceBefore == opponentDiceAfter);
        assertTrue(playerDiceBefore < playerDiceAfter);
    }

    @Test
    public void removePlayerTest()
    {
        assertTrue(this.game.remainingPlayers.length == 3);
        this.game.removePlayer(this.players[0]);
        assertTrue(this.game.remainingPlayers.length == 2);
    }

    @Test
    public void isOverTest()
    {
        assertFalse(this.game.isOver());
        this.game.removePlayer(this.players[2]);
        assertFalse(this.game.isOver());
        this.game.removePlayer(this.players[0]);
        assertTrue(this.game.isOver());
    }

    @Test
    public void getWinnerTest()
    {
        assertTrue(this.game.getWinner() == null);
        this.game.removePlayer(this.players[2]);
        assertTrue(this.game.getWinner() == null);
        this.game.removePlayer(this.players[0]);
        assertTrue(this.game.getWinner().equals(this.players[1]));
    }
}
