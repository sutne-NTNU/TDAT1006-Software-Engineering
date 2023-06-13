package client.model.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttackLogicTest
{
    @Test
    public void attackWonTest()
    {
        for (int i = 0; i < 100; i++)
        {
            assertTrue(AttackLogic.getAttackResult(7, 1));
        }
    }

    @Test
    public void attackLostTest()
    {
        for (int i = 0; i < 100; i++)
        {
            assertFalse(AttackLogic.getAttackResult(1, 6));
        }
    }

    @Test
    public void resultingDiceWonTest()
    {
        int[] result = AttackLogic.getDiceAfterAttack(7, 4, true);
        assertTrue(result[0] == 1 && result[1] == 6);
    }

    @Test
    public void resultingDiceLostTest()
    {
        int[] dice = AttackLogic.getDiceAfterAttack(7, 4, false);
        assertTrue(dice[0] == 1 && dice[1] == 4);
    }
}
