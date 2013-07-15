package input;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

public class Sandbox {


	public static void main(String[] args) throws XMLStreamException, FileNotFoundException
	{
		InputStreamer is;
		is = new InputStreamer("trunk/src/resources/input.xml");
		
		while (is.hasNext())
		{
			TimeStep current = is.getNextStep();
			System.out.println(current);
		}
	}
		

}
