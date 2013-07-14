package components;


/**
 * Flows class. Edited by Sean Chung.
 * 
 * @author Shane Kwon
 * @version Spring 13
 * 
 */
public class Flow extends Component
{
	/**
	 * ID of the source to the flow.
	 */
	private String my_src_id;

	private Component source;
	private Component sink;
	private Control control;

	/**
	 * ID of the sink from the flow.
	 */
	private String my_sink_id;

	/**
	 * The maximum capacity of the flow.
	 */
	private double my_max_capacity;

	/**
	 * The control used to get the value to subtract from source and add to
	 * sink.
	 */
	private String my_control_name;

	/**
	 * Constructor for FLows.
	 * 
	 * @param the_name
	 *            the name of the Flow
	 * @param the_id
	 *            the id of the flow
	 * @param the_src_id
	 *            the id of the source
	 * @param the_sink_id
	 *            the id of the sink
	 * @param the_max_capacity
	 *            maximum capacity of the flow
	 * @throws ValueOverflowException
	 */
	public Flow(final String the_name, final String the_id,
			final String the_src_id, final String the_sink_id,
			final double the_max_capacity, final double the_cur_level,
			final String the_control) 
	{

		super(the_name, the_id, Component.TYPE_FLOW);
		my_src_id = the_src_id;
		my_sink_id = the_sink_id;
		my_max_capacity = the_max_capacity;
		my_control_name = the_control;
		setCurrentValue(the_cur_level);
	}

	/**
	 * Set the current value of this stock up to its maximum value.
	 * 
	 * @param the_current_value the value to be set.
	 */
	@Override
	public void setCurrentValue(final double the_current_value) {
		if (Double.isInfinite(my_max_capacity)) {
			current_value = the_current_value;
		} else {
			current_value = Math.min(the_current_value, my_max_capacity);
		}
	}

	public void calcNewValue()
	{
		double val = control.getPreviousValue();
	
		setCurrentValue(val);

		//SUBTRACT FROM SOURCE
		source.subtract(current_value);
		
		//ADD TO SINK
		sink.add(current_value);
	
	}

	public String getControlName()
	{
		return my_control_name;
	}

	/**
	 * Return the ID of the sink.
	 * 
	 * @return sink ID
	 */
	public String getMy_sink_id()
	{
		return my_sink_id;
	}

	/**
	 * Return the ID of the source.
	 * 
	 * @return source ID
	 */
	public String getMy_src_id()
	{
		return my_src_id;
	}

	/**
	 * Set the ID of the sink.
	 * 
	 * @param the_id
	 *            the sink ID
	 */
	public void setMy_sink_id(final String the_id)
	{
		my_sink_id = the_id;
	}

	/**
	 * Set the ID of the source.
	 * 
	 * @param the_src_id
	 *            the source ID
	 */
	public void setMy_src_id(final String the_src_id)
	{
		my_src_id = the_src_id;
	}

	public void setSink(Component c)
	{
		sink = c;
	}

	public void setSource(Component c)
	{
		source = c;
	}

	public void setControl(Control c)
	{
		control = c;
	}

}
