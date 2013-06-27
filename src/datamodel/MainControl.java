package datamodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import components.Component;

public class MainControl
{
	private SimulationDataStructure my_data_structure;
	private int my_steps;
	private File my_file;
	private PrintWriter my_output_writer;
	
	public MainControl(final SimulationDataStructure the_data_structure, final int the_steps) {
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
	
	public void start() {
		int current_step = 0;
		while (current_step < my_steps) {
			my_output_writer.print(current_step + ",");
			next();
			my_output_writer.println();
			current_step++;
		}
		my_output_writer.close();
	}
	
	private void next() {
		for (Component c : my_data_structure.getAllComponents()) {
			my_output_writer.print(c.getCurrentValue() + ",");
			c.backup();
		}
		for (Component c : my_data_structure.getAllComponents()) {
			c.calcNewValue();
		}
	}
	
	public File getFile() {
		return my_file;
	}
}
