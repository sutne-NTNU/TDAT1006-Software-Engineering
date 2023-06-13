package client.model.game;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import org.junit.Test;

public class DiceTest
{
    @Test
    public void testRoll()
    {
        for (int i = 0; i < 100; i++)
        {
            int roll = Dice.roll();
            assertTrue(1 <= roll && roll <= 6);
        }
    }

    @Test
    public void testRollN()
    {
        for (int i = 0; i < 100; i++)
        {
            int roll = Dice.roll(3);
            assertTrue(3 <= roll && roll <= 18);
        }
    }

    @Test
    public void testGetRandom()
    {
        for (int i = 0; i < 100; i++)
        {
            int random = Dice.getRandom(3, 6);
            assertTrue(3 <= random && random <= 6);
        }
        for (int i = 0; i < 100; i++)
        {
            int random = Dice.getRandom(6);
            assertTrue(0 <= random && random <= 6);
        }
    }

    @Test
    public void selectRandomFromArrayTest()
    {
        Integer[] array = { 0, 1, 2, 3, 4, 5 };
        boolean[] found = new boolean[array.length];
        for (int i = 0; i < 100; i++)
        {
            int random = Dice.selectRandom(array);
            assert (0 <= random && random <= 5);
            found[random] = true;
        }
        for (int i = 0; i < 5; i++)
        {
            assertTrue(found[i]);
        }
    }

    @Test
    public void selectRandomFromArrayListTest()
    {
        ArrayList<Integer> array = new ArrayList<Integer>();
        for (int i = 0; i <= 5; i++)
        {
            array.add(i);
        }
        boolean[] found = new boolean[array.size()];
        for (int i = 0; i < 100; i++)
        {
            int random = Dice.selectRandom(array);
            assert (0 <= random && random <= 5);
            found[random] = true;
        }
        for (int i = 0; i < 5; i++)
        {
            assertTrue(found[i]);
        }
    }
}
