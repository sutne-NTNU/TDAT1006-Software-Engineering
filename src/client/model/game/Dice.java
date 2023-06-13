package client.model.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Utility class that originally only handled dice rolls, but has since grown to include other
 * convenient function for randomness.
 */
public class Dice
{
    private static Random random = new Random();

    /**
     * Rolls a single dice and returns its value.
     * @return a random int in the range {@code [1, 6]} inclusive.
     */
    public static int roll()
    {
        return random.nextInt(6) + 1;
    }

    /**
     * Rolls {@code numDice} number of dice and returns the sum of the rolls.
     */
    public static int roll(int numDice)
    {
        if (numDice < 0)
            throw new IllegalArgumentException("numDice must be greater than or equal to 0");
        int sum = 0;
        for (int i = 0; i < numDice; i++) sum += roll();
        return sum;
    }

    /**
     * Returns a random int in the range {@code [min, max]} inclusive.
     */
    public static int getRandom(int min, int max)
    {
        if (min > max) throw new IllegalArgumentException("min must be less than or equal to max");
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Returns a random int in the range {@code [0, max]} inclusive.
     */
    public static int getRandom(int max)
    {
        if (max < 0) throw new IllegalArgumentException("max must be greater than or equal to 0");
        return getRandom(0, max);
    }

    /**
     * Selects a random element from the given array.
     */
    public static <T> T selectRandom(T[] array)
    {
        if (array.length == 0) throw new IllegalArgumentException("array must not be empty");
        return array[random.nextInt(array.length)];
    }

    /**
     * Selects a random element from the given ArrayList.
     */
    public static <T> T selectRandom(ArrayList<T> array)
    {
        if (array.isEmpty()) throw new IllegalArgumentException("array must not be empty");
        return array.get(random.nextInt(array.size()));
    }
}
