package error;
/**
 * @author Kenny Kong
 * @version 0
 */
public class ValueOverflowException extends Exception
{
	public ValueOverflowException()
	{
		super("Current value exceeds maximum value.");
	}
}
