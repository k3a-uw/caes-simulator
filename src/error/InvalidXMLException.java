package error;

public class InvalidXMLException extends Exception
{
	/**
	 * Serializable for portability if needed in future.
	 */
	private static final long serialVersionUID = 1L;

	public InvalidXMLException(final String message)
	{
		super(message);
	}

}
