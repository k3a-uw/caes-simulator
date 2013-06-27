package datamodel;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import components.Cloud;
import components.Component;
import components.Control;
import components.Flow;
import components.Stock;

import error.InvalidXMLException;
import error.ValueOverflowException;

/**
 * A class to loop through the configuration file and instantiate the objects
 * laid out in the config.xml file.  Uses config.xsd for the schema.
 * @author Kevin E. Anderson
 * @modifier Kenny Kong
 * @version 0
 */
public class Loader {

	/**
	 * A parsed config_file used to build the {@link Loader#data_structure}
	 */
	private Document config_file;

	/**
	 * A data structure containing all of the instantiated objects defined by the
	 * {@link Loader#config_file}
	 */
	private SimulationDataStructure data_structure;


	/**
	 * Store the timeSteps at load time.  No need to keep looking it up.
	 */
	private int timeSteps = -1;
	
	/**
	 * Generally called by {@link datamodel.MainControl}, returns the number of timeSteps
	 * requested by the system.  
	 */
	public int getTimeSteps()
	{
		return timeSteps;		
	}
	
	
	/**
	 * loadFile simply builds a parsing factory and then parses the configuration file
	 * into a {@link org.w3c.dom.Document} for easy node navigation.
	 * @param file_to_parse The file captured by the UI and provided to the loader.
	 * @throws InvalidXMLException if the file is not a valid XML file.
	 * @throws ParserConfigurationException if a builder cannot be configured.
	 */
	public void loadFile(File file_to_parse) throws InvalidXMLException, ParserConfigurationException
	{

		DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
		domFact.setNamespaceAware(false);   // ALWAYS PAY ATTENTION TO NAMESPACE

		DocumentBuilder build;
		build = domFact.newDocumentBuilder();  //throws ParserConfigException


		// PARSE THE FILE
		try {
			config_file = build.parse(file_to_parse);
		} catch (Exception e) {
			throw new InvalidXMLException("The file is bad!");
		}
	}

	/**
	 * Reads through the parsed {@link Loader#config_file} and controls the
	 * population of objects into the {@link SimlationDataStructure}.
	 * @return A fully built SimluationDataStructure.
	 * @throws InvalidXMLException if the XML is bad.
	 */
	public SimulationDataStructure createDataStructure() throws InvalidXMLException
	{
		/* Performance Note (k3a):
		 * ORDER SHOULDN'T MATTER IF WE ARE ONLY LINKING OBJECTS BY ID RATHER
		 * THAN MEMORY LOCATION. IF WE I.D. THEM BY MEMORY LOCATION HOWEVER, 
		 * WE WOULD LIKELY SEE GREATER PERFORMANCE WHEN DEALING WITH LARGE
		 * SYSTEMS.  IF THAT IS THE CASE ORDER WILL MATTER AND SOME OBJECTS
		 * (WITH SOURCE AND SINK IDs) WILL NEED TO BE CHANGED
		 */	

		data_structure = new SimulationDataStructure();

		try {
			buildSystem();  //get time steps
			buildStocks();  
			buildClouds();
			buildControls(); 
			buildFlows();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidXMLException("Cannot Parse all Components!");
		}

		linkDataStructure();
		
		return data_structure;
	}
	
	
	/**
	 * Links all objects together, called after data
	 */
	private void linkDataStructure()
	{
		Map<String, Component> map = data_structure.getCompleteMap();
		
		for (Entry<String, Component> e : map.entrySet())
		{
			Component current = (Component) e.getValue();
			switch (current.getType())  //ONLY DEAL WITH FLOWS AND CONTROLS.
			{
			case Component.TYPE_FLOW:
				//LOOKUP THE SOURCE, SINK AND CONTROL AND PASS THEM TO FLOW
				Flow fc = (Flow) current;
				fc.setSource(map.get(fc.getMy_src_id()));
				fc.setSink(map.get(fc.getMy_sink_id()));
				Control control = (Control) map.get(fc.getControlName());
				fc.setControl(control);
				break;
			// CONTROL NEEDS TO BE GIVEN ALL OF THE PARAMETER OBJECTS
			case Component.TYPE_CONTROL:
				Control cc = (Control) current;
				//Component[] params = extractComponents(cc.getfunction(), cc.getFunctionType());
				//cc.setParameters(params);
				extractComponents(cc);
				break;
			}
		}
	}
	
	/**
	 * Function to Component Parsing.
	 */
	private void extractComponents(Control the_control)
	{
		//	TODO THIS IS _NOT_ THE MOST EFFICIENT WAY TO MANAGE THIS PARSING
		//  BUT IT IS GOOD FOR NOW.  WILL RECODE LATER.
		Map<String,Component> map = data_structure.getCompleteMap();
		ArrayList<Component> params = new ArrayList<Component>();
		ArrayList<String> operators = new ArrayList<String>();
		
		if (data_structure != null)
		{
			String[] token = the_control.getfunction().split("[,\\s{}]+");
			for (int i = 0; i < token.length; i++)
			{
				if (map.containsKey(token[i]))
				{
					params.add(map.get(token[i]));
				}
				else
				{
					operators.add(token[i]);
				}
			}
			
			the_control.setParameters(params);
			the_control.setOperators(operators);
		}
	}
	
	/**
	 * Helper method to create {@link Cloud}s and populate them into
	 * {@link Loader#data_structure}.
	 * @throws ValueOverflowException 
	 */
	private void buildClouds() throws ValueOverflowException
	{
		String cloudQuery = "//system/cloud";
		NodeList cloudNodes = null;

		try{
			cloudNodes = getNode(cloudQuery);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (cloudNodes.getLength() > 0) {

			// LOOP THROUGH NODES
			for (int i = 0; i < cloudNodes.getLength(); i++)
			{
				NamedNodeMap cloudInfo = cloudNodes.item(i).getAttributes();
				String cloudID    = cloudInfo.getNamedItem("id").getNodeValue();
				String cloudName  = cloudInfo.getNamedItem("name").getNodeValue();
				String cloudUnits = cloudInfo.getNamedItem("units").getNodeValue();
				double cloudLevel  = Double.parseDouble(cloudInfo.getNamedItem("cur_level").getNodeValue());
				data_structure.addComponent(new Cloud(cloudName, cloudID, cloudLevel, cloudUnits));
			}

		} else {
			System.err.println("No Stocks to Add!");
		}	


	}
	/**
	 * Helper method to grab system properties and populate them into needed fields.
	 * @throws InvalidXMLException If the system does not follow the proper schema.
	 */
	private void buildSystem() throws InvalidXMLException
	{
		String systemQuery = "//system[1]";
		NodeList sysNodes = null;

		try{
			sysNodes = getNode(systemQuery);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (sysNodes.getLength() > 0) {
			NamedNodeMap sysInfo = sysNodes.item(0).getAttributes();
			try {
				timeSteps = Integer.parseInt(sysInfo.getNamedItem("timeSteps").getNodeValue());
			} catch (Exception e){
				throw new InvalidXMLException("TimeSteps are incorrect!!!");
			}
	
		} else {
			System.err.println("No Systems!!!!!");
		}
	}



	/**
	 * Helper method to create {@link Stock}s and populate them into
	 * {@link Loader#data_structure}.
	 * @throws ValueOverflowException 
	 */
	private void buildStocks() throws ValueOverflowException
	{
		String stockQuery = "//system/stock";
		NodeList stockNodes = null;

		try{
			stockNodes = getNode(stockQuery);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (stockNodes.getLength() > 0) {
			Stock[] stocks = new Stock[stockNodes.getLength()];

			// LOOP THROUGH NODES
			for (int i = 0; i < stockNodes.getLength(); i++)
			{
				NamedNodeMap stockInfo = stockNodes.item(i).getAttributes();
				String stockID    = stockInfo.getNamedItem("id").getNodeValue();
				String stockName  = stockInfo.getNamedItem("name").getNodeValue();
				String stockUnits = stockInfo.getNamedItem("units").getNodeValue();
				int max_level  = Integer.parseInt(stockInfo.getNamedItem("max_level").getNodeValue());
				int cur_level  = Integer.parseInt(stockInfo.getNamedItem("cur_level").getNodeValue());
				if (max_level == 0) { max_level = Integer.MAX_VALUE; }
				stocks[i] = new Stock(stockName, stockID, max_level, stockUnits);
				stocks[i].setCurrentValue(cur_level);
				stocks[i].setPreviousValue(cur_level);
				data_structure.addComponent(stocks[i]);
			}

		} else {
			System.err.println("No Stocks to Add!");
		}	


	}

	/**
	 * Helper method to create {@link Control} and populate them into
	 * {@link Loader#data_structure}.
	 * @throws ValueOverflowException 
	 */
	private void buildControls() throws ValueOverflowException
	{
		String query = "//system/control";
		NodeList listOfNodes = null;

		try{
			listOfNodes = getNode(query);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (listOfNodes.getLength() > 0) {

			// LOOP THROUGH NODES AND ADD THEM TO THE STRUCTURE.
			for (int i = 0; i < listOfNodes.getLength(); i++)
			{
				NamedNodeMap nodeMap = listOfNodes.item(i).getAttributes();
				String id    = nodeMap.getNamedItem("id").getNodeValue();
				String name  = nodeMap.getNamedItem("name").getNodeValue();
				String funct = nodeMap.getNamedItem("function").getNodeValue();
				String type  = nodeMap.getNamedItem("type").getNodeValue();
				Double value = Double.parseDouble(nodeMap.getNamedItem("initialvalue").getNodeValue()); 

				data_structure.addComponent(new Control(name, id, funct, type, value));
			}


		} else {
			System.err.println("No Stocks to Add!");
		}	

	}

	/**
	 * Helper method to create {@link Flow} and populate them into
	 * {@link Loader#data_structure}.
	 * @throws ValueOverflowException 
	 */
	private void buildFlows() throws ValueOverflowException
	{
		String query = "//system/flow";
		NodeList listOfNodes = null;

		try{
			listOfNodes = getNode(query);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (listOfNodes.getLength() > 0) {
			Flow[] flows = new Flow[listOfNodes.getLength()];

			// LOOP THROUGH NODES
			for (int i = 0; i < listOfNodes.getLength(); i++)
			{
				NamedNodeMap nodeMap = listOfNodes.item(i).getAttributes();
				String id    = nodeMap.getNamedItem("id").getNodeValue();
				String name  = nodeMap.getNamedItem("name").getNodeValue();
				Double max_capacity = Double.parseDouble(nodeMap.getNamedItem("max_capacity").getNodeValue());
				Double cur_level    = Double.parseDouble(nodeMap.getNamedItem("cur_level").getNodeValue());
				String src_id = nodeMap.getNamedItem("src_id").getNodeValue();
				String sink_id = nodeMap.getNamedItem("sink_id").getNodeValue();
				String control_name = nodeMap.getNamedItem("control_name").getNodeValue();
				flows[i] = new Flow(name, id, src_id,sink_id,max_capacity, cur_level, control_name);
				data_structure.addComponent(flows[i]);

			}


		} else {
			System.err.println("No Stocks to Add!");
		}	

	}	



	/**
	 * Returns a List of Nodes as a result of the provided query.
	 * @param query The query string that one would like to compile.
	 * @return A compiled version of the query string.
	 * @throws XPathExpressionException If the query cannot be compiled, this method will throw an exception.
	 */
	private NodeList getNode(String query) throws XPathExpressionException
	{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(query);

		Object result = null;
		try {
			result = expr.evaluate(config_file, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		NodeList nodes = (NodeList) result;

		return nodes;
	}
	
}
