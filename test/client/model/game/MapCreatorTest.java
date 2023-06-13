package client.model.game;

import static org.junit.Assert.assertTrue;

import client.utils.Config;
import org.junit.Before;
import org.junit.Test;

public class MapCreatorTest
{
    Municipality[] municipality;
    Player[] players;

    @Before
    public void setUp()
    {
        this.players = new Player[] {
            new Player("test1", PoliticalParty.AP),
            new Player("test2", PoliticalParty.H),
            new Player("test3", PoliticalParty.SP),
        };
        this.municipality = MapCreator.createMunicipalities(this.players);
    }

    @Test
    public void createMapOwnerTest()
    {
        int[] ownerCount = new int[this.players.length];
        int notOwned = 0;
        for (Municipality mun : this.municipality)
        {
            if (mun.owner == null)
            {
                notOwned++;
                continue;
            }
            for (int i = 0; i < this.players.length; i++)
            {
                if (mun.owner.equals(this.players[i]))
                {
                    ownerCount[i]++;
                }
            }
        }
        assertTrue(notOwned == Municipality.NAMES.length % this.players.length);
        assertTrue(ownerCount[0] == (int)(Municipality.NAMES.length / this.players.length));
        assertTrue(ownerCount[0] == ownerCount[1]);
        assertTrue(ownerCount[1] == ownerCount[2]);
    }

    @Test
    public void createMapDiceTest()
    {
        for (Municipality mun : this.municipality)
        {
            assertTrue(
                Config.MIN_START_DICE <= mun.numDice && mun.numDice <= Config.MAX_START_DICE
            );
        }
    }
}
