package Logic.Objects;

/**
 * this exception is meant for times when something illegal is done in the gamelogic
 */
public class IllegalException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param text the string describing the exception
	 */
	public IllegalException(String text)
	{
        super(text);
    }
}
