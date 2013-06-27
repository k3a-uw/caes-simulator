package components;

/**
 * Cloud class
 * 
 * @author Fuat
 * 
 */

public class Cloud extends Stock
{
	/**
	 * the id C0.1.1, C0.1.2
	 */
	private String id;
	/**
	 * name of the PotantialPeople
	 */
	private String name;
	/**
	 * units are people
	 */
	private String units;
	/**
	 * previous max value
	 */
	private double cur_level;

	/**
	 * @param the_name
	 * @param the_id
	 * @param the_max_value
	 * @param the_unit
	 * @param id
	 */
	public Cloud(String the_name, String the_id, double the_cur_value,
			String the_unit)
	{
		super(the_name, the_id, the_cur_value, the_unit);

		id = the_id;

		name = the_name;

		units = the_unit;

		cur_level = the_cur_value;

	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the name of the components
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the units
	 */
	public String getUnits()
	{
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(String units)
	{
		this.units = units;
	}

	/**
	 * @return the cur_level
	 */
	public double getCur_level()
	{
		return cur_level;
	}

	/**
	 * @param cur_level
	 *            the cur_level to set
	 */
	public void setCur_level(double cur_level)
	{
		this.cur_level = cur_level;
	}

}
