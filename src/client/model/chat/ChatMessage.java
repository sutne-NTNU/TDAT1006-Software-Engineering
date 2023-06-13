package client.model.chat;

import client.model.game.Player;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage
{
    public final Player sender;
    public final String time;
    public final String message;

    public ChatMessage(Player sender, String message)
    {
        this.message = message;
        this.sender = sender;
        this.time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString()
    {
        return String.format(
            "%s %s (%s):%s", this.time, this.sender.username, this.sender.party.name, this.message
        );
    }
}
