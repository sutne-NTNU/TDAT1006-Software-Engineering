package client.controller;

import client.App;
import client.model.chat.ChatMessage;
import client.model.chat.MessageTransformer;
import client.model.game.Player;
import client.utils.SceneData;
import javafx.scene.text.Text;

public class ChatController
{
    public static Text[] convertToTexts(ChatMessage newMessage)
    {
        Text timestamp = new Text(String.format("%s ", newMessage.time));
        Text sender = new Text(String.format(
            "%s [%s]", newMessage.sender.username, newMessage.sender.party.abbreviation
        ));
        Text message = new Text(String.format(": %s\n", newMessage.message));

        // set CSS id used in theme.css
        timestamp.setId("chat-time");
        sender.setId("chat-sender");
        message.setId("chat-message");
        sender.setFill(newMessage.sender.party.color); // setting fill in css will override this

        return new Text[] { timestamp, sender, message };
    }

    /**
     * Get contents of the field and transform it into a ChatMessage. If content is not necessary to
     * send returns null. Also clears the field.
     */
    public static ChatMessage initMessage(String str)
    {
        String message = MessageTransformer.transform(str);
        if (message == null) return null;
        Player me = LobbyController.me;
        if (me == null) me = GameController.me;
        return new ChatMessage(me, message);
    }

    public static void handleReceiveChatMessage(ChatMessage chatMessage)
    {
        SceneData<?> scene = App.getCurrentScene();
        if (scene.equals(App.game))
        {
            App.game.controller.updateChat(chatMessage);
        }
        else if (scene.equals(App.lobby))
        {
            App.lobby.controller.updateChat(chatMessage);
        }
        AudioController.play(AudioController.newMessage);
    }
}
