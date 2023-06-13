package client.model.game;

import client.utils.Bot;
import client.utils.Config;

public class Game
{
    public Municipality[] municipalities; // the 'Map'
    public Player[] remainingPlayers;
    public Player currentPlayer;

    public Game(Player[] players)
    {
        this.remainingPlayers = players;
        this.currentPlayer = Dice.selectRandom(players);
        this.municipalities = MapCreator.createMunicipalities(players);
    }

    /**
     * Verifies that the attack is valid, than then perform the attack updating both municipalities
     * according to the result.
     *
     * @param attacker the municipality that attacks
     * @param defender the municipality that defends itself
     * @return whether player won or lost as string, or why you can't attack
     */
    public boolean performAttack(Municipality attacker, Municipality defender)
    {
        if (!attacker.isOwner(this.currentPlayer))
            throw new RuntimeException("Current player does not own" + attacker);

        boolean won = attacker.attack(defender);
        if (won) this.removeEliminatedPlayers();
        return won;
    }

    /**
     * End the current players turn, updating the number of dice for their own municipalities and
     * changing the current player to the next player in the list.
     */
    public void endTurn()
    {
        // Add dice randomly to current players municipalities
        for (Municipality municipality : this.municipalities)
        {
            if (!this.currentPlayer.equals(municipality.owner)) continue;
            int numDangerousNeighbors = 0;
            Municipality[] neighbors = municipality.getNeighbors(this.municipalities);
            for (Municipality neighbor : neighbors)
            {
                if (this.currentPlayer.equals(neighbor.owner)) continue;
                numDangerousNeighbors++;
            }
            // Make municipality has very few hostile neighbors, reduce chance it gets new dice
            if (Dice.roll() > numDangerousNeighbors + 1) continue;
            int newDice = Dice.getRandom(Config.MIN_DICE_PER_TURN, Config.MAX_DICE_PER_TURN);
            municipality.numDice = Math.min(Config.MAX_DICE, municipality.numDice + newDice);
        }
        // update current player
        this.newCurrentPlayer();
    }

    /**
     * Check if a player has won the game.
     *
     * @return True if there is only 1 player left in the game.
     */
    public boolean isOver()
    {
        return this.remainingPlayers.length == 1;
    }

    /**
     * @return The last remaining player in the game, or null if there are more than 1 player left.
     */
    public Player getWinner()
    {
        if (this.remainingPlayers.length == 1) return this.remainingPlayers[0];
        return null;
    }

    /**
     * Loop over all municipalities, any player still in the player array that does not own any
     * municipalities are removed.
     */
    private void removeEliminatedPlayers()
    {
        // Check which players are still in the game (have at least one municipality)
        boolean[] stillInGame = new boolean[this.remainingPlayers.length];
        for (Municipality municipality : this.municipalities)
        {
            for (int playerIndex = 0; playerIndex < this.remainingPlayers.length; playerIndex++)
            {
                if (this.remainingPlayers[playerIndex].equals(municipality.owner))
                {
                    stillInGame[playerIndex] = true;
                    break;
                }
            }
        }
        // Remove players who are no longer in the game
        for (int playerIndex = 0; playerIndex < this.remainingPlayers.length; playerIndex++)
        {
            if (stillInGame[playerIndex]) continue;
            this.removePlayer(this.remainingPlayers[playerIndex]);
        }
    }

    /**
     * Remove a player from the game so they can no longer play. If the player is the current
     * player, the turn is ended. If the player owns any municipalities, they are set to neutral
     * (unowned).
     * @param player the player to remove
     */
    public void removePlayer(Player player)
    {
        if (player.isBot)
        {
            Bot.sendLoseResponse(player);
        }
        Player[] updatedPlayers = new Player[this.remainingPlayers.length - 1];
        int updatedIndex = 0;
        for (int i = 0; i < this.remainingPlayers.length; i++)
        {
            if (this.remainingPlayers[i].equals(player)) continue;
            updatedPlayers[updatedIndex] = this.remainingPlayers[i];
            updatedIndex++;
        }
        for (Municipality municipality : this.municipalities)
        {
            if (!player.equals(municipality.owner)) continue;
            municipality.owner = null;
        }
        if (this.currentPlayer.equals(player)) this.newCurrentPlayer();
        this.remainingPlayers = updatedPlayers;
    }

    /**
     * Changes the current player to the next player in the list.
     */
    private void newCurrentPlayer()
    {
        int currentIndex = -1;
        for (int i = 0; i < this.remainingPlayers.length; i++)
        {
            if (this.remainingPlayers[i].equals(this.currentPlayer))
            {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex == -1)
            throw new RuntimeException("Current player not found in player list");
        this.currentPlayer
            = this.remainingPlayers[(currentIndex + 1) % this.remainingPlayers.length];
    }
}
