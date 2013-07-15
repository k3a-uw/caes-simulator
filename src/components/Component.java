package components;

/**
 * Component class
 * 
 * @author Kenny Kong
 * @modifier Kevin E. Anderson
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
	protected final String name;

	/**
	 * The ID of this component.
	 */
	protected final String id;

	/**
	 * This type.
	 */
	protected int type;
	
	/**
	 * The previous value of this component hold.
	 */
	protected double previous_value;

	/**
	 * The current value of this component hold.
	 */
	protected double current_value;

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
	public Component(final String the_name, final String the_id, final int the_type) {
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
	 * Set the type of this component.
	 */
	protected void setType(final int the_type) {
		type = the_type;
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
	 */
	public void setCurrentValue(final double the_value) {
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
	 * Allow an object to explicitly add to the current value.
	 * Primarily used for flows to move values around.
	 * @param value
	 */
	protected void add(final double value)
	{
		setCurrentValue(current_value+value);
	}
	
	/**
	 * Allow an object to explicitly subtract from the current value.
	 * Primarily used for flows to move values around.
	 * @param value
	 */
	
	protected void subtract(final double value)
	{
		setCurrentValue(current_value-value);
	}

	//TODO CREATE AN ENUMAERATION FOR COMPONENT TYPING
	/**
	 * Accepts a string and converts it to the proper integer value 
	 * that relates to component type.  It is NOT case sensitive.
	 * @param the_name The string name of the component. 
	 * @return The related Static int of that component.
	 * @throws IllegalArgumentException When the string provided is not a valid component name.
	 */
	public static int convertName(String the_name)
	{
		
		if (the_name.toUpperCase().equals("FLOW"))
		{
			return TYPE_FLOW;
		} else if (the_name.toUpperCase().equals("STOCK"))
		{
			return TYPE_STOCK;
		} else if (the_name.toUpperCase().equals("SYSTEM"))
		{
			return TYPE_SUBSYSTEM;
		} else if (the_name.toUpperCase().equals("CONTROL"))
		{
			return TYPE_CONTROL;
		} else if (the_name.toUpperCase().equals("SENSOR"))
		{
			return TYPE_SENSOR;
		} else if (the_name.toUpperCase().equals("CLOUD"))
		{
			return TYPE_CLOUD;
		} else
		{
			throw new IllegalArgumentException(the_name + " is not a valid Component Type");
		}
	
		
		
	}
	
	
	
	/**
	 * Calculate new value.
	 */
	public void calcNewValue()
	{
		// should be override by subclass if they need to calculate new value.
	}
}
