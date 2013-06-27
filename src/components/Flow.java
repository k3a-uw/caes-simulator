package components;

import error.ValueOverflowException;

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
			final String the_control) throws ValueOverflowException
	{

		super(the_name, the_id, Component.TYPE_FLOW);
		my_src_id = the_src_id;
		my_sink_id = the_sink_id;
		my_max_capacity = the_max_capacity;
		my_control_name = the_control;
		setCurrentValue(the_cur_level);
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
	 * Set the ID of the source.
	 * 
	 * @param the_src_id
	 *            the source ID
	 */
	public void setMy_src_id(final String the_src_id)
	{
		my_src_id = the_src_id;
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
	 * Return the maximum capacity of the flow.
	 * 
	 * @return flow max capacity
	 */
	public double getMy_max_capacity()
	{
		return my_max_capacity;
	}

	/**
	 * Set the value of the maximum capacity for the flow.
	 * 
	 * @param the_max_capacity
	 *            the maximum capacity of the flow
	 */
	public void setMy_max_capacity(final double the_max_capacity)
	{
		my_max_capacity = the_max_capacity;
	}

	/**
	 * Return the current capacity for the flow.
	 * 
	 * @return current capacity
	 */
	public double getMy_cur_level()
	{
		return super.getCurrentValue();
	}

	/**
	 * Set the current capacity for the flow.
	 * 
	 * @param the_cur_level
	 *            current value of the flow
	 * @throws ValueOverflowException
	 */
	public void setMy_cur_level(final double the_cur_level)
			throws ValueOverflowException
	{
		setCurrentValue(the_cur_level);
	}

	public void setSource(Component c)
	{
		source = c;
	}

	public void setSink(Component c)
	{
		sink = c;
	}

	public void setControl(Control c)
	{
		control = c;
	}

	public String getControlName()
	{
		return my_control_name;
	}

	public void calcNewValue()
	{
		Double val = control.getPreviousValue();
		try
		{
			source.setCurrentValue(source.getPreviousValue() - val);
		} catch (ValueOverflowException e)
		{
			System.out.println("An error occurred in " + getName() + " when setting source: " + source.getName() + 
				 " to " + (source.getPreviousValue() - val));
		}
		
		try
		{
			sink.setCurrentValue(sink.getPreviousValue() + val);
		} catch (ValueOverflowException e)
		{
			System.out.println("An error occurred in " + getName() + " when setting sink: " + sink.getName() + 
					 " to " + (sink.getPreviousValue() - val));
		}

	}

}
