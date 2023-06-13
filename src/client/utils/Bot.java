package client.utils;

import client.App;
import client.controller.ChatController;
import client.controller.GameController;
import client.model.chat.ChatMessage;
import client.model.chat.MessageTransformer;
import client.model.game.Dice;
import client.model.game.Game;
import client.model.game.Municipality;
import client.model.game.Player;
import client.model.game.PoliticalParty;
import java.util.ArrayList;
import javafx.application.Platform;

public class Bot
{
    private static int botCount = 0;
    public static String getName()
    {
        botCount++;
        return "Bot " + botCount;
    }

    /**
     * There is a chance the bot will respond with a random message.
     */
    public static void promptForMessage(Player player)
    {
        if (!player.isBot) return;
        new Thread(() -> {
            if (Dice.roll() < 4) return;
            think(200, 20_000);
            String str = getRandomMessage(player);
            String message = MessageTransformer.transform(str);
            ChatMessage chatMessage = new ChatMessage(player, message);
            runOnMain(() -> ChatController.handleReceiveChatMessage(chatMessage));
        }).start();
    }

    /**
     * Will send a random message from the list of random Win Messages.
     */
    public static void sendWinResponse(Player player)
    {
        new Thread(() -> {
            String str = getRandomWinMessage(player);
            String message = MessageTransformer.transform(str);
            ChatMessage chatMessage = new ChatMessage(player, message);
            runOnMain(() -> ChatController.handleReceiveChatMessage(chatMessage));
        }).start();
    }

    /**
     * Will send a random message from the list of random Lose Messages.
     */
    public static void sendLoseResponse(Player player)
    {
        new Thread(() -> {
            String str = getRandomLoseMessage(player);
            String message = MessageTransformer.transform(str);
            ChatMessage chatMessage = new ChatMessage(player, message);
            runOnMain(() -> ChatController.handleReceiveChatMessage(chatMessage));
        }).start();
    }

    /**
     * Let a bot perform the current players turn in the game.
     */
    public static void performTurn(Game game)
    {
        Player player = game.currentPlayer;
        new Thread(() -> {
            ArrayList<Municipality> owned = new ArrayList<>();
            for (Municipality municipality : game.municipalities)
            {
                if (!player.equals(municipality.owner)) continue;
                owned.add(municipality);
            }
            while (owned.size() > 0)
            {
                Municipality municipality = owned.remove(Dice.getRandom(owned.size() - 1));
                if (!municipality.isOwner(player)) continue;
                for (Municipality neighbor : municipality.getNeighbors(game.municipalities))
                {
                    if (!municipality.canAttack(neighbor)) continue;
                    if (municipality.numDice < neighbor.numDice) continue;
                    if (Dice.roll() < 2) continue;
                    think(130);
                    boolean won = game.performAttack(municipality, neighbor);
                    if (won) owned.add(neighbor);
                    runOnMain(() -> { App.game.controller.updateMap(game.municipalities); });
                    break;
                }
            }
            runOnMain(() -> GameController.endTurn());
        }).start();
    }

    /**
     * Simulate thinking by randomly sleeping the thread between min and max milliseconds.
     * Calls to this function must be done in a separate thread from the main thread.
     */
    private static void think(int min, int max)
    {
        think(Dice.getRandom(min, max));
    }
    private static void think(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            System.out.println("Bot was interrupted");
        }
    }

    /**
     * All changes to the GUI must be done on the main thread.
     */
    public static void runOnMain(Runnable runnable)
    {
        Platform.runLater(runnable);
    }

    private final static String getRandomMessage(Player bot)
    {
        return Dice.selectRandom(new String[] {
            "Hei, jeg heter " + bot.username,
            "beep boop, beep boop",
            "Jeg skjenner en bot som heter Anna",
            "/bigchung",
            "/sans",
            "/lenny",
            "/gang",
            "1011010101",
            "Fint vær vi har idag",
            "Hvilket parti støtter du?",
            "Dette spillet er utrolig morsomt :D",
        });
    }

    private final static String getRandomWinMessage(Player bot)
    {
        return Dice.selectRandom(new String[] {
            bot.party.name + " er det beste partiet!!",
            bot.party.name + " vant som fortjent!!!",
            "Du hadde ikke en sjanse",
            "/gnome",
            "/gang",
            "/bigchung",
        });
    }

    private final static String getRandomLoseMessage(Player bot)
    {
        return Dice.selectRandom(new String[] {
            "Jeg synes " + bot.party.name + " skulle vunnet :'(",
            "Jeg sladrer til Zuckerberg!",
            "Jeg prøvde mitt beste :(",
            (bot.party.equals(PoliticalParty.KRF) ? "Ukristent spill"
                                                  : "Jeg hadde nok gjort det bedre som KRF"),
            "/actually",
            "/lenny",
        });
    }
}
