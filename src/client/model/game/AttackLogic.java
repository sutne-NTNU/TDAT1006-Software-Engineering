package client.model.game;

/**
 * Class containing the logic for the game.
 * This class is used by the client to determine the outcome of an attack.
 */
public class AttackLogic
{
    /**
     * Rolls attacker and defenders dice. Returns true if attacker wins.
     * Attacker wins if the sum of his dice is larger than the sum of the defenders dice.
     * If the sum is equal, the defender wins.
     */
    public static boolean getAttackResult(int numDiceAttacker, int numDiceDefender)
    {
        return Dice.roll(numDiceAttacker) > Dice.roll(numDiceDefender);
    }

    /**
     * Finds the resulting remaining dice for both the attacker and the defender, depending on if
     * the attacker won. If attacker won, all but 1 of their remaining dice are moved to the
     * defending municipality (as the attacker now owns it).
     * @returns {@code [attackerDice, defenderDice]}
     */
    public static int[]
    getDiceAfterAttack(int numDiceAttacker, int numDiceDefender, boolean attackerWon)
    {
        if (attackerWon)
        {
            return new int[] { 1, numDiceAttacker - 1 };
        }
        else
        {
            return new int[] { 1, numDiceDefender };
        }
    }
}
