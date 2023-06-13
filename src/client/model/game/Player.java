package client.model.game;

public class Player
{
    public final String username;
    public PoliticalParty party;
    public boolean isBot = false;

    public Player(String username, PoliticalParty party)
    {
        this.username = username;
        this.party = party;
    }

    // Getters in order for PropertyValueFactory in Lobby to work.
    public String getUsername()
    {
        return this.username;
    }
    public String getParty()
    {
        return this.party.name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Player)) return false;
        Player other = (Player)obj;
        return this.username.equals(other.username);
    }

    @Override
    public String toString()
    {
        return String.format("Player(%s, %s)", this.username, this.party);
    }
}
