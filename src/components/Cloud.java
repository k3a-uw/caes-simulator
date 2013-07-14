package components;

/**
 * Cloud class
 * 
 * @author Fuat
 * @modifier Kevin E. Anderson (k3a@uw.edu)
 * 
 */

public class Cloud extends Stock
{
	/**
	 * @param the_name
	 * @param the_id
	 * @param the_max_value
	 * @param the_unit
	 * @param id
	 */
	public Cloud(String the_name, String the_id, String the_unit)
	{
		super(the_name, the_id, Double.POSITIVE_INFINITY, the_unit);
		setType(Component.TYPE_CLOUD);
		setPreviousValue(Double.POSITIVE_INFINITY);
		setCurrentValue(Double.POSITIVE_INFINITY);
	}

}
