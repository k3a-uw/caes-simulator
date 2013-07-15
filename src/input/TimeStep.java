package input;

import java.util.ArrayList;


public class TimeStep {
	
	private int stepValue;
	private ArrayList<ComponentStep> components;
	
	public TimeStep()
	{
		stepValue = -1;
		components = new ArrayList<ComponentStep>();
	}
	
	
	public void setStepValue(final int value)
	{
		stepValue = value;
	}

	public void addComponent(ComponentStep value)
	{
		components.add(value);
	}
	
	public int getStepValue()
	{
		return stepValue;
	}
	
	public ComponentStep[] getComponentSteps()
	{
		ComponentStep[] toReturn = new ComponentStep[components.size()];
		for (int i=0; i < components.size(); i++)
			toReturn[i] = components.get(i);
		return toReturn;
	}
	
	@Override
	public String toString()
	{
		return "StepValue="+stepValue+"\tComponent Count: " + (components == null ? "null" : components.size());
	}
 
}
