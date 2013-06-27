package error;

public class ValueOverflowException extends Exception
{
	public ValueOverflowException()
	{
		super("Current value exceeds maximum value.");
	}
}
