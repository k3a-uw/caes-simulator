package input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Iterator;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import components.Component;

import error.InvalidXMLException;

/**
 * A streamer to generate TimeStep objects from the provided XML input file.
 * @author Kevin E. Anderson (k3a@uw.edu)
 */
public class InputStreamer {

	private final int CACHE_SIZE = 10;
	private File inputFile;
	private XMLEventReader stream;
	private ArrayDeque<TimeStep> step_cache;

	public InputStreamer(String filename) throws FileNotFoundException, XMLStreamException
	{
		inputFile = new File(filename);
		if(!inputFile.exists())
			throw new FileNotFoundException();

		createStream();
		step_cache = new ArrayDeque<TimeStep>();
	}

	private void createStream() throws FileNotFoundException, XMLStreamException
	{
		FileReader fr = new FileReader(inputFile);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		stream = factory.createFilteredReader(
				factory.createXMLEventReader(fr), new StartFilter());
	}

	/**
	 * Returns and consumes the next timestep in cache.
	 * If one does not exist getNextStep() will try and fill 
	 * the cache before returning the timestep.
	 * @return The {@link TimeStep} at the front of the cache.  If there are 
	 * no steps remaining the method will return <code>null</code>.
	 */
	public TimeStep getNextStep()
	{
		checkCache();
		return step_cache.poll();
	}

	/**
	 * Returns but does not consume the next timestep in cache.
	 * If one does not exist getNextStep() will try and fill 
	 * the cache before returning the timestep.
	 * @return The {@link TimeStep} at the front of the cache.  If there are 
	 * no steps remaining the method will return <code>null</code>.
	 */
	public TimeStep peek()
	{
		checkCache();
		return step_cache.peek();
	}

	private void checkCache()
	{
		if (step_cache.isEmpty() && stream.hasNext())
			try {
				fillCache();
			} catch (XMLStreamException | InvalidXMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void fillCache() throws XMLStreamException, InvalidXMLException
	{

		while (stream.hasNext() && step_cache.size() < CACHE_SIZE)
		{
			StartElement e = (StartElement) stream.nextEvent();

			//START READING TIMESTEP
			if (e.getName().getLocalPart().equals("timestep"))
			{
				TimeStep newStep = new TimeStep();
				Iterator stepIterator = e.getAttributes();
				while (stepIterator.hasNext())
				{
					Attribute attribute = (Attribute) stepIterator.next();
					if(attribute.getName().toString().equals("stepValue"))
					{
						newStep.setStepValue(Integer.parseInt(attribute.getValue()));
					}
				}

				//CONTINUE TO STREAM UNTI YOU FIND THE NEXT TIMESTEP.  CREATE COMPONENTS ALONG THEW AY
				while(stream.hasNext() && !stream.peek().asStartElement().getName().getLocalPart().equals("timestep"))
				{
					StartElement nextElement = stream.nextEvent().asStartElement();
					Iterator componentIterator = nextElement.getAttributes();

					String id   = "";
					String name = "";
					int type    = -1;
					double value = 0;

					//EXTRACT THE ATTRIBUTES
					while(componentIterator.hasNext())
					{
						Attribute attr = (Attribute) componentIterator.next();
						String attrName = attr.getName().getLocalPart().toString();
						if(attrName.equals("id"))
						{
							id = attr.getValue();
						} else if (attrName.equals("name"))
						{
							name = attr.getValue();
						} else if (attrName.equals("type"))
						{
							type = ComponentStep.convertName(attr.getValue());
						} else if (attrName.equals("value"))
						{
							value = Double.parseDouble(attr.getValue());
						} // IGNORE INVALID ATTRIBUTES.
					}

					if (id.isEmpty() || name.isEmpty() || type == -1)
						throw new InvalidXMLException("A Component for TimeStep ID: " + newStep.getStepValue() + " is Invalid!");

					newStep.addComponent(new ComponentStep(id, name, type, value));					
				}

				//NOW THAT I HAVE A BUILD TIMESTEP I CAN ADD IT TO THE CACHE
				step_cache.add(newStep);
			} //END READING TIMESTEP
		}
	}

	public boolean hasNext()
	{
		return stream.hasNext() || !step_cache.isEmpty();
	}

	class StartFilter implements EventFilter
	{
		@Override
		public boolean accept(XMLEvent event) {
			return event.isStartElement();
		}

	}
}
