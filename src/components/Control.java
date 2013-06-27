package components;

import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import error.ValueOverflowException;

/**
 * Controls class. Adjusts functions as needed. Extends function class.
 * 
 * @author Sean Chung
 * @modifier Kenny Kong
 * @version Spring 13
 */
public class Control extends Component {
	
	/**
	 * Values relating to the type of function to parse.  Strings
	 * provided by the user in the XML
	 */
	public static final String CONDITIONAL = "conditional";
	public static final String FUNCTION    = "function";
	public static final String CONSTANT    = "constant";

	/**
	 * The type of function related to this control.  Should
	 * equal one of the public static finals.
	 */
	private String my_function_type;
	
	/**
	 * The function for this control.
	 */
	private String my_function;
	
	/**
	 * The intitial value.
	 */
	private double my_initial_value;

	/**
	* The set of parameters to use in string replace
	* when passing the function to the parser.
	*/
	private ArrayList<Component> myParameters;
	
	/**
	 * The set of operators to use in the function
	 */
	private ArrayList<String> myOperators;
	
	/**
	 * Constructor.
	 * 
	 * @param the_name name of the Control
	 * @param the_id specific identifier of the controls
	 * @param the_adjust_value value to adjust the function by
	 * @param the_comp_id the id of the function this is a control for
	 * @throws ValueOverflowException 
	 */
	public Control(final String the_name,  final String the_id,
			final String the_function, final String the_function_type,
			final double the_initial_value) throws ValueOverflowException
	{
		super(the_name, the_id, Component.TYPE_CONTROL);

		my_function = the_function;
		my_function_type = the_function_type;
		my_initial_value = the_initial_value;
		setCurrentValue(the_initial_value);
		setPreviousValue(the_initial_value);
		
	}
	
	/**
	 * Accepts references to the parameters that exist in the function.
	 * @param the_params Can be empty if no parameters exist for the control.  This
	 * happens when the type is constant (not conditional or function).
	 */
	public void setParameters(ArrayList<Component> the_params)
	{
		myParameters = the_params;
	}
	
	public void setOperators(ArrayList<String> the_operators)
	{
		myOperators = the_operators;
	}
	

	public String getFunctionType()
	{
		return my_function_type;
	}
	
	/**
	 * Set the function this will be a control for.
	 * 
	 * @param the_function the function to be controlled
	 */
	public void setfunction(final String the_function) {
		my_function = the_function;
	}
	
	/**
	 * Get the function that this control is for.
	 * 
	 * @return the function 
	 */
	public String getfunction() {
		return my_function;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void calcNewValue() {
		Evaluator evaluator = new Evaluator();
		String function = "";
		
		switch (my_function_type) {
		case CONDITIONAL:
			String flag = "";
			//System.out.println(myParameters.size());
			//System.out.println(myOperators.toString());
			function = myParameters.get(0).getPreviousValue() + 
					myOperators.get(1) + myParameters.get(1).getPreviousValue();
			//System.out.println(function);
			try
			{
				flag = evaluator.evaluate(function);
			} catch (EvaluationException e)
			{
				System.err.println(e.getMessage() + ": " + function);
			}

			if (flag.equals("1.0")) { // true
				try
				{
					this.setCurrentValue(Double.parseDouble(myOperators.get(2)));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValueOverflowException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (flag.equals("0.0")) { // false
				try
				{
					this.setCurrentValue(Double.parseDouble(myOperators.get(3)));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValueOverflowException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			break;
		case FUNCTION:
			function = my_function;
			//System.out.println(my_function);
			for (int i = 0; i < myParameters.size(); i++) {
				function = function.replace("{" + myParameters.get(i).getName() + "}",
						"" + myParameters.get(i).getPreviousValue());
			}
			//System.out.println(function);
			try
			{
				//System.out.println(Double.parseDouble(evaluator.evaluate(function)));
				try
				{
					this.setCurrentValue(Double.parseDouble(evaluator.evaluate(function)));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValueOverflowException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (EvaluationException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println(e.getMessage() + ": " + function);
			}
			break;
		case CONSTANT:
			try
			{
				this.setCurrentValue(my_initial_value);
			} catch (ValueOverflowException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}