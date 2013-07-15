package ui;

import input.InputStreamer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.xml.stream.XMLStreamException;

import datamodel.Loader;
import datamodel.MainControl;
import datamodel.SimulationDataStructure;

/**
 * Simulator.java is the class that handles the UserInterface fo the simulator.
 * 
 * @author Kenny Kong
 * @modifier Kevin E. Anderson
 *
 */
public class Simulator implements ActionListener, Observer
{
	private JLabel step_label = new JLabel();
	private JLabel input_label = new JLabel();
	private JFrame frame;
	private JLabel output_label;
	private JTextField config_file_path;
	private JTextField input_file_path;
	private JButton config_file_button;
	private JButton input_file_button;
	private JButton load_config_button;
	private JButton load_input_button;
	private JButton start_button;
	private JButton pause_button;
	private JButton step_button;
	
	private SimulationDataStructure my_data;
	
	private JFileChooser config_file_chooser;
	private JFileChooser input_file_chooser;
	
	private MainControl my_maincontrol;	
	
	public Simulator()
	{
		setup();
	}
	
	private void setup()
	{
		frame = new JFrame("Simulator");
		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		config_file_chooser = new JFileChooser();
		input_file_chooser = new JFileChooser();
		
		output_label = new JLabel();
		JScrollPane scroll_pane = new JScrollPane(output_label,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scroll_pane, BorderLayout.CENTER);
		
		JPanel north_panel = new JPanel();
		north_panel.setLayout(new GridLayout(1,2));
		
		step_label.setText("Current Step: 0");		
		input_label.setText("Input Not Loaded");
		north_panel.add(input_label);
		north_panel.add(step_label);
		
		
		
		frame.add(step_label, BorderLayout.NORTH);
		frame.add(input_label, BorderLayout.NORTH);
		JPanel file_panel = new JPanel();
		file_panel.setLayout(new GridLayout(3, 3));
		
		config_file_path = new JTextField();
		file_panel.add(config_file_path);
		config_file_button = new JButton("Choose Config File...");
		config_file_button.addActionListener(this);
		file_panel.add(config_file_button);
		
		load_config_button = new JButton("Load Config");
		file_panel.add(load_config_button);
		load_config_button.addActionListener(this);
		load_config_button.setEnabled(false);
		
		input_file_path = new JTextField();
		file_panel.add(input_file_path);
		input_file_button = new JButton("Choose Input File...");
		input_file_button.addActionListener(this);
		file_panel.add(input_file_button);
		
		load_input_button = new JButton("Load Input");
		file_panel.add(load_input_button);
		load_input_button.addActionListener(this);
		load_input_button.setEnabled(false);
				
		start_button = new JButton("Start");
		start_button.addActionListener(this);
		start_button.setEnabled(false);
		file_panel.add(start_button);
		
		pause_button = new JButton("Pause");
		pause_button.addActionListener(this);
		pause_button.setEnabled(false);
		file_panel.add(pause_button);
		
		step_button = new JButton("Step");
		step_button.addActionListener(this);
		step_button.setEnabled(false);
		file_panel.add(step_button);
				
		frame.add(file_panel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent the_event)
	{
		//System.out.println(the_event);
		if (the_event.getSource() == config_file_button)
		{
			int result = config_file_chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				config_file_path.setText(config_file_chooser.getSelectedFile().getPath());
				load_config_button.setEnabled(true);
			}
		}
		else if (the_event.getSource() == input_file_button)
		{
			int result = input_file_chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				input_file_path.setText(input_file_chooser.getSelectedFile().getPath());
				load_input_button.setEnabled(true);
			}
		}
		else if (the_event.getSource() == load_config_button)
		{
			//TODO USE THE FILE TO LOAD THE DATA INTO SIMULATION DATA STRUCTURE.
			Loader loader = new Loader();
			try
			{
				System.out.println("Loaded!");
				loader.loadFile(new File(config_file_path.getText()));
				my_data = loader.createDataStructure();
				my_data.addObserver(this);
				output_label.setText(my_data.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			my_maincontrol = new MainControl(my_data, loader.getTimeSteps());
			start_button.setEnabled(true);
			pause_button.setEnabled(true);
			step_button.setEnabled(true);
		}
		else if (the_event.getSource() == start_button) {
		// RUN THE ENGINE IN A NEW THREAD
			SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					my_maincontrol.run();
					return null;

				}
			};
			worker.execute();			
		}
		else if(the_event.getSource() == pause_button) {
			my_maincontrol.pause();
		}
		else if(the_event.getSource() == step_button)
		{
			my_maincontrol.step();
		}
		else if(the_event.getSource() == load_input_button)
		{
			try {
				my_maincontrol.setInputStreamer(new InputStreamer(input_file_path.getText()));
				input_label.setText("Input Loaded");
			} catch (FileNotFoundException | XMLStreamException e) {
				input_label.setText("Input Failed");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(final String... the_args)
	{
		new Simulator();
	}

	@Override
	public void update(Observable o, Object arg) {
		output_label.setText(my_data.toString());
		step_label.setText("Current Step: " + my_maincontrol.getCurrentStep());
	}
}
