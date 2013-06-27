package components;
import error.ValueOverflowException;

/**
 * Component class
 * 
 * @author Kenny Kong
 *
 */

public class Component {
	public static final int TYPE_FLOW = 0;
	public static final int TYPE_STOCK = 1;
	public static final int TYPE_SUBSYSTEM = 2;
	public static final int TYPE_CONTROL = 3;
	public static final int TYPE_SENSOR = 4;
	public static final int TYPE_CLOUD = 5;

	/**
	 * The name of this component.
	 */
	private final String name;

	/**
	 * The ID of this component.
	 */
	private final String id;

	/**
	 * This type.
	 */
	private final int type;
	
	/**
	 * The previous value of this component hold.
	 */
	private double previous_value;

	/**
	 * The current value of this component hold.
	 */
	private double current_value;

	/**
	 * Instantiate this component.
	 * 
	 * @param the_name
	 *            name of this component.
	 * @param the_id
	 *            ID of this component.
	 * @param the_type
	 *            type of this component.
	 */
	public Component(final String the_name, final String the_id,
			final int the_type) {
		name = the_name;
		id = the_id;
		type = the_type;
	}

	/**
	 * Get the namee of this component.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the ID of this component.
	 * 
	 * @return the ID.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Get the type of this component.
	 * 
	 * @return the type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Set previous value of this component.
	 * 
	 * @param the_value
	 *            the value to set.
	 */
	public void setPreviousValue(final double the_value) {
		previous_value = the_value;
	}

	/**
	 * Get the previous value of this component.
	 * 
	 * @return the value.
	 */
	public double getPreviousValue() {
		return previous_value;
	}

	/**
	 * Set current value of this component.
	 * 
	 * @param the_value
	 *            the value to set.
	 * @throws ValueOverflowException 
	 */
	public void setCurrentValue(final double the_value) throws ValueOverflowException {
		current_value = the_value;
	}

	/**
	 * Get the current value of this component.
	 * 
	 * @return the value.
	 */
	public double getCurrentValue() {
		return current_value;
	}
	
	/**
	 * Set the previous value to current value.
	 */
	public void backup() {
		setPreviousValue(getCurrentValue());
	}
	
	/**
	 * Calculate new value.
	 */
	public void calcNewValue()
	{
		// should be override by subclass if they need to calculate new value.
	}
}
