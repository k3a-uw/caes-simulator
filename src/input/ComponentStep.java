package input;

public class ComponentStep {
	
	private static final int TYPE_VALUESET = 0;
	private static final int TYPE_VALUEADD = 1;
	private static final int TYPE_VALUESCALE = 2;
	
	private String id;
	private String name; 
	private int    value_set_type;
	private double component_value;
	
	public ComponentStep(final String the_id, final String the_name, final int the_type, final double the_value)
	{
		id               =  the_id;
		name             =  the_name;
		value_set_type   =  the_type;
		component_value  =  the_value;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String value) {
		id = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		name = value;
	}
	
	public int getType() {
		return value_set_type;
	}
	
	public void setType(int value) {
		value_set_type = value;
	}
	
	public double getValue() {
		return component_value;
	}
	
	public void setValue(double value) {
		component_value = value;
	}
	
	public double calcNewDouble(double value)
	{
		switch (value_set_type)
		{
		case TYPE_VALUESET:
			return component_value;
		case TYPE_VALUEADD:
			return component_value + value;
		case TYPE_VALUESCALE:
			return component_value * value;
		default:
			return component_value;
		}
	}
	
	
	public static int convertName(String the_name)
	{
		
		if (the_name.toUpperCase().equals("VALUESET"))
		{
			return TYPE_VALUESET;
		} else if (the_name.toUpperCase().equals("VALUEADD"))
		{
			return TYPE_VALUEADD;
		} else if (the_name.toUpperCase().equals("VALUESCALE"))
		{
			return TYPE_VALUESCALE;
		} else
		{
			throw new IllegalArgumentException(the_name + " is not a valid ComponentStep Type");
		}
	
		
		
	}	
}
