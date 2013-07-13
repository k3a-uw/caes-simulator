package xmlobjects;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Sandbox {

	
	 public static void testParseEntireDocument() throws JAXBException {
         JAXBContext ctx = JAXBContext.newInstance(new Class[] {InputSet.class});
         Unmarshaller um = ctx.createUnmarshaller();
         InputSet steps = (InputSet) um.unmarshal(new File("trunk/src/resources/input.xml"));
         
         for (xmlobjects.Step step : steps.timestep)
         {
        	 System.out.println(step.stepValue);
        	 for(Object o : step.getStockOrFlowOrControl())
        	 {
        		 System.out.println("Hello?" + o);
        	 }
         }
         
    }
	 
	   public static void main(String[] args)
	{
		System.out.println("I'm running!");
		try {
			testParseEntireDocument();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
