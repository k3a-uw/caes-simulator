package datamodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import components.Component;
/**
 * A class to loop through the configuration file and instantiate the objects
 * laid out in the config.xml file.  Uses config.xsd for the schema.
 * @author Shane Kwon
 * @modifier Kevin E. Andersonx
 */
public class MainControl
{
	private static final int STOPPED = 1;
	private static final int RUNNING = 2;
	private static final int PAUSED  = 3;
	private static final int COMPLETED = 4;
	
	private int my_status;
	private int current_step = 0;

	private SimulationDataStructure my_data_structure;
	private int my_steps;
	private File my_file;
	private PrintWriter my_output_writer;
	
	public MainControl(SimulationDataStructure the_data_structure, final int the_steps) {
		my_data_structure = the_data_structure;
		my_steps = the_steps;
		my_file = new File("output.csv");
		try
		{
			my_output_writer = new PrintWriter(my_file);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// write the columns of the csv output
		my_output_writer.print("time step,");
		for (Component c : my_data_structure.getAllComponents()) {
			my_output_writer.print(c.getName() + ",");
		}
		my_output_writer.println();
	}
	
	public void run() {
		synchronized (this)
		{
			my_status = RUNNING;
		}
				
		while (my_status == RUNNING) {
			next();
			
			if (current_step >= my_steps)
			{
				synchronized (this)
				{
					my_status = COMPLETED;
				}
			}
		}
		my_output_writer.close();
	}
	
	public void pause()
	{
		synchronized (this)
		{
			my_status = PAUSED;
		}
	}
	
	public void step()
	{
		next();
	}
	
	
	private void next() {
		
		my_output_writer.print(current_step + ",");

		for (Component c : my_data_structure.getAllComponents()) {
			my_output_writer.print(c.getCurrentValue() + ",");
			c.backup();
		}
		for (Component c : my_data_structure.getAllComponents()) {
			c.calcNewValue();
		}
		
		my_output_writer.println();
		current_step++;

		my_data_structure.setChanged(true); //TODO FIX THIS HACK!
		my_data_structure.notifyObservers();
	}
	
	public int getCurrentStep()
	{
		return current_step;
	}
	
	public File getFile() {
		return my_file;
	}
}
