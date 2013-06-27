package components;
import error.ValueOverflowException;

/**
 * Stock class
 * 
 * @author Kenny
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
	 * The previous value
	 */
	private double previous_value;


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
	 * Set the current value of this stock.
	 * 
	 * @param the_current_value the value to be set.
	 * @throws ValueOverflowException throw this exception if the current value exceed maximum allowed value.
	 */
	public void setCurrentValue(final double the_current_value)
			throws ValueOverflowException {
		if (the_current_value > max_value) {
			throw new ValueOverflowException();
		}
		super.setCurrentValue(the_current_value);
	}

	/**
	 * Set the previous value.
	 * 
	 * @param the_previous_value the value to be set
	 */
	public void setPreviousValue(final double the_previous_value) {
		super.setPreviousValue(the_previous_value);
	}


	/**
	 * Get current value.
	 * 
	 * @return current value.
	 */
	public double getCurrentValue() {
		return super.getCurrentValue();
	}

	/**
	 * Get previous value.
	 * 
	 * @return previous value.
	 */
	public double getPreviousValue() {
		return super.getPreviousValue();
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
