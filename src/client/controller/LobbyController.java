package client.controller;

import client.App;
import client.model.game.Dice;
import client.model.game.Player;
import client.model.game.PoliticalParty;
import client.utils.Bot;
import java.util.ArrayList;

public class LobbyController
{
    public static Player me;
    private static ArrayList<Player> playersInLobby = new ArrayList<Player>();
    private static int numPlayersReady = 0;

    public static Player[] getPlayersInLobby()
    {
        return playersInLobby.toArray(new Player[playersInLobby.size()]);
    }

    public static void clearLobby()
    {
        me = null;
        playersInLobby.clear();
        numPlayersReady = 0;
    }

    public static void joinLobby()
    {
        LobbyController.me = addPlayer(UserController.username);
        App.lobby.controller.updateReadyCount(numPlayersReady, 2);
        App.setScene(App.lobby);
        MusicController.start(MusicController.grieg);
    }

    public static void readyUp()
    {
        numPlayersReady++;
        int numRequiredPlayersReady = Math.max(2, playersInLobby.size());
        App.lobby.controller.updateReadyCount(numPlayersReady, numRequiredPlayersReady);
        if (numPlayersReady < numRequiredPlayersReady) return;
        GameController.initGame(me, getPlayersInLobby());
        App.setScene(App.game);
        GameController.startGame();
    }

    public static void leaveLobby()
    {
        clearLobby();
        App.setScene(App.menu);
        MusicController.stop();
    }

    public static void giveMeANewParty()
    {
        me.party = getFreeParty();
        App.lobby.controller.updatePlayerTable(getPlayersInLobby());
    }

    public static void addBot()
    {
        Player bot = addPlayer(Bot.getName());
        bot.isBot = true;
        readyUp();
        Bot.promptForMessage(bot);
    }

    public static Player addPlayer(String username)
    {
        Player player = new Player(username, getFreeParty());
        playersInLobby.add(player);
        if (playersInLobby.size() == PoliticalParty.PARTIES.length)
        {
            App.lobby.controller.onLobbyFilledChange(true);
        }
        App.lobby.controller.updatePlayerTable(getPlayersInLobby());
        return player;
    }

    /**
     * @return the next free available party
     */
    private static PoliticalParty getFreeParty()
    {
        ArrayList<PoliticalParty> parties = new ArrayList<PoliticalParty>();
        for (PoliticalParty party : PoliticalParty.PARTIES)
        {
            boolean isFree = true;
            for (Player player : playersInLobby)
            {
                if (party.equals(player.party))
                {
                    isFree = false;
                    break;
                }
            }
            if (isFree) parties.add(party);
        }
        return Dice.selectRandom(parties);
    }
}
