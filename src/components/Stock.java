package components;

/**
 * Stock class
 * 
 * @author Kenny Kong
 *
 */
public class Stock extends Component {

	/**
	 * The max value
	 */
	private double max_value;
	
	/**
	 * The unit
	 */
	private String unit;


	/**
	 * Instantiate the Stock
	 * 
	 * @param the_name the name of this stock
	 * @param the_id the ID of this stock
	 * @param the_max_value the maximum value allowed
	 * @param the_unit the measurement unit for this Stock
	 * @throws ValueOverflowException 
	 */
	public Stock(final String the_name, final String the_id,
		     final double the_max_value, final String the_unit) {
		super(the_name, the_id, Component.TYPE_STOCK);
		
		max_value = the_max_value;
		unit = the_unit;
	}

	/**
	 * Set the current value of this stock up to its maximum value.
	 * 
	 * @param the_current_value the value to be set.
	 */
	public void setCurrentValue(final double the_current_value) {
		if (Double.isInfinite(max_value)) {
			current_value = the_current_value;
		} else {
			current_value = Math.min(the_current_value, max_value);
		}
	}
		
	/**
	 * Get this measuring unit.
	 * 
	 * @return measuring unit.
	 */
	public String getUnit() {
		return unit;
	}
}
