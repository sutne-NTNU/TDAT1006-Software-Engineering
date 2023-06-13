package client.controller;

import client.App;
import client.model.game.Game;
import client.model.game.Municipality;
import client.model.game.Player;
import client.model.game.PoliticalParty;
import client.utils.Bot;

public class GameController
{
    public static Player me;
    public static Game game;
    private static Municipality selectedMunicipality;

    public static void initGame(Player me, Player[] players)
    {
        GameController.me = me;
        game = new Game(players);
        LobbyController.clearLobby();
        // make sure first player to play is not a bot
        if (game.currentPlayer.isBot)
        {
            for (Player player : game.remainingPlayers)
            {
                if (player.isBot) continue;
                game.currentPlayer = player;
                break;
            }
        }
    }

    private static boolean isMyTurn()
    {
        return game.currentPlayer.equals(me);
    }

    public static void startGame()
    {
        App.game.controller.updateCurrentPlayer(game.currentPlayer, game.currentPlayer.equals(me));
        App.game.controller.updateMap(game.municipalities);
    }

    public static void leaveGame()
    {
        me = null;
        game = null;
        selectedMunicipality = null;
        App.setScene(App.menu);
        MusicController.stop();
    }

    public static void onSelectMunicipality(int index)
    {
        if (!isMyTurn()) return;
        if (game.isOver()) return;
        Municipality newMunicipality = game.municipalities[index];
        // Reset visual selection
        App.game.controller.updateSelectedMunicipality(selectedMunicipality, false);
        for (Municipality mun : game.municipalities)
        {
            App.game.controller.updateFocus(mun, true);
        }
        // Handle Game Logic
        if (newMunicipality.equals(selectedMunicipality))
        {
            // User unselected the selected municipality
            selectedMunicipality = null;
            return;
        }
        if (selectedMunicipality == null || me.equals(newMunicipality.owner))
        {
            if (!me.equals(newMunicipality.owner)) return;
            // User updated their selection
            selectedMunicipality = newMunicipality;
        }
        else
        {
            // User tried attacking a municipality
            if (selectedMunicipality.isOwner(game.currentPlayer)
                && selectedMunicipality.canAttack(newMunicipality))
            {
                boolean wonAttack = game.performAttack(selectedMunicipality, newMunicipality);
                App.game.controller.updateMap(game.municipalities);
                {
                    // This line causes lag !!! no idea why, all it does is change one text, but as
                    // long as that text is set the interface lags
                    /*
                    App.game.controller.updateAttackResult(
                        selectedMunicipality, newMunicipality, won
                    );
                    */
                }
                AudioController.play(wonAttack ? AudioController.click : AudioController.error);
                if (wonAttack && game.isOver()) endTurn();
            }
            selectedMunicipality = null;
            return;
        }

        // Update visual selection
        Municipality[] neighbors = selectedMunicipality.getNeighbors(game.municipalities);
        for (Municipality mun : game.municipalities)
        {
            App.game.controller.updateFocus(mun, false);
        }
        for (Municipality neighbor : neighbors)
        {
            if (me.equals(neighbor.owner)) continue;
            App.game.controller.updateFocus(neighbor, true);
        }
        App.game.controller.updateSelectedMunicipality(selectedMunicipality, true);
    }

    public static void onHoverMunicipality(int index, boolean isHovering)
    {
        if (!isMyTurn()) return;
        if (game.isOver()) return;
        Municipality municipality = game.municipalities[index];
        if (selectedMunicipality == null)
        {
            // No municipality is selected
            if (!me.equals(municipality.owner)) return;
            App.game.controller.updateSelectedMunicipality(municipality, isHovering);
        }
        else
        {
            if (me.equals(municipality.owner)) return;
            if (selectedMunicipality.isNeighbor(index))
            {
                App.game.controller.updateSelectedMunicipality(municipality, isHovering);
            }
        }
    }

    public static void endTurn()
    {
        game.endTurn();
        if (game.isOver())
        {
            handleWin();
            return;
        }
        if (game.currentPlayer.isBot)
        {
            Bot.performTurn(game);
        }
        boolean isMyTurn = game.currentPlayer.equals(me);
        if (isMyTurn)
        {
            AudioController.play(AudioController.error);
        }
        App.game.controller.updateSelectedMunicipality(selectedMunicipality, false);
        selectedMunicipality = null;
        App.game.controller.updateAttackResult("");
        App.game.controller.updateCurrentPlayer(game.currentPlayer, isMyTurn);
        App.game.controller.updateMap(game.municipalities);
    }

    private static boolean handleWin()
    {
        Player winner = game.getWinner();
        if (winner == null) return false;
        if (winner.isBot)
        {
            Bot.sendWinResponse(winner);
        }
        App.game.controller.updateWinner(winner);
        if (winner.party.equals(PoliticalParty.KRF))
        {
            AudioController.play(AudioController.blunk);
        }
        MusicController.start(MusicController.metal);
        return true;
    }
}
