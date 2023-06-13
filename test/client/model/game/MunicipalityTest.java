package client.model.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MunicipalityTest
{
    private Municipality[] municipalities;

    @Before
    public void setUp()
    {
        Player[] temp = new Player[] {
            new Player("temp1", PoliticalParty.AP),
            new Player("temp2", PoliticalParty.H),
        };
        this.municipalities = new Game(temp).municipalities;
    }

    @Test
    public void isOwnerTest()
    {
        Player owner1 = new Player("owner 1", PoliticalParty.AP);
        Player owner2 = new Player("owner 2", PoliticalParty.H);
        Municipality trondheim = this.municipalities[Municipality.TRONDHEIM];
        trondheim.owner = null;
        assertFalse(trondheim.isOwner(owner1));
        trondheim.owner = owner1;
        assertFalse(trondheim.isOwner(null));
        assertTrue(trondheim.isOwner(owner1));
        assertFalse(trondheim.isOwner(owner2));
    }

    @Test
    public void isNeighborTest()
    {
        Municipality trondheim = this.municipalities[Municipality.TRONDHEIM];
        assertTrue(trondheim.isNeighbor(Municipality.MELHUS));
        assertTrue(trondheim.isNeighbor(Municipality.MALVIK));
        assertTrue(trondheim.isNeighbor(Municipality.KLAEBU));
        assertTrue(trondheim.isNeighbor(Municipality.SELBU));

        assertFalse(trondheim.isNeighbor(Municipality.HITRA));
        assertFalse(trondheim.isNeighbor(Municipality.SKAUN));
        assertFalse(trondheim.isNeighbor(Municipality.OPPDAL));
    }

    @Test
    public void getNeighborsTest()
    {
        Municipality trondheim = this.municipalities[Municipality.TRONDHEIM];
        Municipality[] neighbors = trondheim.getNeighbors(this.municipalities);
        assertTrue(neighbors.length == 4);
        for (Municipality neighbor : neighbors)
        {
            assertTrue(
                neighbor.index == Municipality.MELHUS || neighbor.index == Municipality.MALVIK
                || neighbor.index == Municipality.KLAEBU || neighbor.index == Municipality.SELBU
            );
        }
    }

    @Test
    public void canAttackTest()
    {
        Player owner1 = new Player("owner 1", PoliticalParty.AP);
        Player owner2 = new Player("owner 2", PoliticalParty.H);
        Municipality attacker = this.municipalities[Municipality.TRONDHEIM];
        Municipality defender = this.municipalities[Municipality.MELHUS];
        attacker.owner = owner1;
        defender.owner = owner2;
        attacker.numDice = 1;
        assertFalse(attacker.canAttack(defender));
        attacker.numDice = 3;
        assertTrue(attacker.canAttack(defender));

        attacker.owner = owner1;
        defender.owner = owner1;
        assertFalse(attacker.canAttack(defender));
        assertFalse(attacker.canAttack(this.municipalities[Municipality.OPPDAL]));
    }

    @Test
    public void attackTest()
    {
        Municipality trondheim = this.municipalities[Municipality.TRONDHEIM];
        Municipality melhus = this.municipalities[Municipality.MELHUS];
        Player winner = new Player("new", PoliticalParty.AP);
        trondheim.owner = winner;
        trondheim.numDice = 7;
        melhus.numDice = 1;
        assertTrue(trondheim.attack(melhus));
        assertTrue(melhus.owner.equals(winner));
        assertTrue(melhus.numDice == 6);
        assertTrue(trondheim.numDice == 1);
    }

    @Test
    public void attackExceptionTest()
    {
        Municipality trondheim = this.municipalities[Municipality.TRONDHEIM];
        Municipality oppdal = this.municipalities[Municipality.OPPDAL];
        try
        {
            trondheim.attack(oppdal);
            assertTrue(false);
        }
        catch (Exception e)
        {
        }
    }
}
