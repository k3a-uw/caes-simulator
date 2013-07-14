package components;

import java.util.ArrayList;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import error.InvalidXMLException;

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
	public static final String RECURSIVE   = "recursive";
	public static final String CONSTANT    = "constant";

	/**
	 * The type of function related to this control.  Should
	 * equal one of the public static finals.
	 */
	private String my_function_type;

	/**
	 * A boolean to know if the control is attempting
	 * to be initialized.
	 */
	private boolean isInitializing;
	
	/**
	 * A boolean to identify if a control has been initialized.
	 */
	private boolean isInitialized;

	/**
	 * The function for this control.
	 */
	private String my_function;

	/**
	 * The intitial value.Set to object to check for null for lack of initialization.
	 */
	private Double my_initial_value;

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
			final double the_initial_value) 
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

	
	public void setInitialized(boolean value)
	{
		isInitialized = value;
	}
	/**
	 * @return <code>true</code> if the control has been initialized, false otherwise.
	 */
	public boolean isInitialized()
	{
		return isInitialized;
	}
	
	public void initValue() throws InvalidXMLException
	{
		if (isInitializing)
			throw new InvalidXMLException("The is an infinite loop of initialization.  Please check your initial values");

		isInitializing = true;
		if (!isInitialized())
		{
			for(Component c : myParameters)
			{
				if (c.getType() == Component.TYPE_CONTROL)
				{
					Control cc = (Control) c;
					
					if (!cc.isInitialized())
						cc.initValue();
				}
			}
			
			my_initial_value = evaluateFunction();
			setCurrentValue(my_initial_value);
			setPreviousValue(my_initial_value);
		}
		setInitialized(true);
		isInitializing = false;
	}
	/**
	 * {@inheritDoc}
	 */
	public void calcNewValue() {
		setCurrentValue(evaluateFunction());
	}
	
	private double evaluateFunction()
	{
		Evaluator evaluator = new Evaluator();
		String function = "";

		switch (my_function_type) {
		case CONDITIONAL:
			String flag = "";
			function = 
					String.format("%.20f", myParameters.get(0).getPreviousValue()) + 
					myOperators.get(1) + 
					String.format("%.20f", myParameters.get(1).getPreviousValue());

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
					return Double.parseDouble(myOperators.get(2));
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
			} else if (flag.equals("0.0")) { // false
				try
				{
					return Double.parseDouble(myOperators.get(3));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case FUNCTION:
		case RECURSIVE:
			function = my_function;

			for (int i = 0; i < myParameters.size(); i++) {
				function = function.replace("{" + myParameters.get(i).getName() + "}",
						"" + String.format("%.20f", myParameters.get(i).getPreviousValue()));
			}

			try
			{
				return Double.parseDouble(evaluator.evaluate(function));
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			catch (EvaluationException e)
			{
				System.err.println(e.getMessage() + ": " + function);
			}
			break;
		case CONSTANT:
			return my_initial_value;
		}
	
		return 0.0;
	}
}