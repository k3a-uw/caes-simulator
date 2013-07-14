package datamodel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import components.Component;

/**
 * @author Kevin E. Anderson
 * @modifier Shane Kwon
 * @version 0
 */
public class SimulationDataStructure extends Observable
{
	private Map<String, Component> my_components = new HashMap<String, Component>();

	public SimulationDataStructure()
	{

	}

	public void addComponent(final Component the_component)
	{
		if (!my_components.containsKey(the_component.getName()))
		{
			my_components.put(the_component.getName(), the_component);
		}
	}
	
	public Map<String, Component> getCompleteMap()
	{
		return my_components;
	}
	
	public Collection<Component> getAllComponents() {
		return my_components.values();
	}
	
	@Override
	public String toString()
	{
		
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><TABLE><TR><TD>Type</TD><TD>Name</TD><TD>PreviousValue</TD><TD>CurrentValue</TD></TR>");
		for (Entry<String, Component> comp : my_components.entrySet())
		{
			sb.append("<TR><TD>");
			switch (comp.getValue().getType())
			{
			case 0:
				sb.append("FLOW");
				break;
			case 1:
				sb.append("STOCK");
				break;
			case 2:
				sb.append("SUBSYSTEM");
				break;
			case 3:
				sb.append("CONTROL");
				break;
			case 4:
				sb.append("SENSOR");
				break;
			case 5:
				sb.append("CLOUD");
				break;
			}
			sb.append("</TD><TD>");
			sb.append(comp.getKey());
			sb.append("</TD><TD align=\"right\">");
			if (Double.isInfinite(comp.getValue().getPreviousValue()))
			{
				sb.append("infinity");
			} else {
				sb.append(String.format("%.5f",comp.getValue().getPreviousValue()));
			}
			
			sb.append("</TD><TD align=\"right\">");
			if (Double.isInfinite(comp.getValue().getCurrentValue()))
			{
				sb.append("infinity");
			} else {
				sb.append(String.format("%.5f",comp.getValue().getCurrentValue()));
			}
			sb.append("</TD></TR>");
	
		}
		sb.append("</TABLE>");
		return sb.toString();
	}
	
	/**
	 * Creating a public interface to let the data structure know that it has indeed changed.
	 * It is a little ignorant to the data inside it self.  Should probably make it observe
	 * The objects inside of it. (that would be better design).
	 */
	public void setChanged(boolean value)
	{
		if (value)
		{ 
			setChanged();
		}
		else
		{
			clearChanged();
		}
	}
}
