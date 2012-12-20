package itexto.miocc.parser;

import itexto.miocc.ContainerException;
import itexto.miocc.entities.*;

import java.util.HashMap;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.IOException;
import java.io.InputStream;


/**
 * Classe responsável por parsear um arquivo XML 
 * @author Henrique
 */
public class Parser {
	
	private Document parseXmlFile(InputStream input) throws ParserException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		try {
			return factory.newDocumentBuilder().parse(input);
		} catch (SAXException e) {
			throw new ParserException("Error while building the document", e);
		} catch (IOException e) {
			throw new ParserException("Error while reading the input stream", e);
		} catch (ParserConfigurationException e) {
			throw new ParserException("Parser configuration error", e);
		}
		
	}
	
	/**
	 * Return the given attribute
	 * @param node
	 * @return
	 */
	private Attribute parseAttribute(Node node) throws ParserException
	{
		Attribute attribute = null;
		if (node.getNodeName().equals("property"))
			attribute = new Property();
		else if (node.getNodeName().equals("constructor-arg"))
			attribute = new ConstructorArg();
		else
			throw new ParserException("Unknown element: " + node.getNodeName());
		
		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++)
		{
			Node currentNode = attributes.item(i);
			if (currentNode.getNodeName().equals("name"))
				attribute.setName(currentNode.getNodeValue());
			else if (currentNode.getNodeName().equals("value"))
				attribute.setValue(currentNode.getNodeValue());
			else if (currentNode.getNodeName().equals("ref"))
				attribute.setReferencedBean(currentNode.getNodeValue());
			else if (currentNode.getNodeName().equals("type"))
				attribute.setType(currentNode.getNodeValue());
			else if (currentNode.getNodeName().equals("order"))
				((ConstructorArg) attribute).setOrder(Integer.parseInt(currentNode.getNodeValue()));
		}
		
		return attribute;
	}
	
	/** Return a bean definition */
	private Bean getBeanDefinition(Node node) throws ParserException, ContainerException
	{
		
		Bean bean = new Bean();
		
		/*
		 * Getting the bean attributes
		 */
		NamedNodeMap attributes = node.getAttributes();
		try
		{
			bean.setName(attributes.getNamedItem("name").getNodeValue());
		}
		catch (NullPointerException ex)
		{
			throw new ParserException("Name unknown for this bean");
		}
		
		try
		{
			bean.setId(attributes.getNamedItem("id").getNodeValue());
		}
		catch (NullPointerException ex)
		{
			throw new ParserException("Id unknown for this bean");
		}
		
		Node scopeNode = attributes.getNamedItem("scope");
		if (scopeNode == null)
			bean.setScope(Scope.Instance);
		else
		{
			bean.setScope(Scope.getScope(scopeNode.getNodeValue()));
			if (bean.getScope() == null)
				bean.setScope(Scope.Instance);
		}
		
		
		/*
		 * Getting all the constructor arguments and properties for the bean
		 */
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			if (children.item(i).getNodeName().equals("property"))
			{
				bean.addProperty((Property) parseAttribute(children.item(i)));
			} 
			else if (children.item(i).getNodeName().equals("constructor-arg"))
			{
				bean.addConstructorArg((ConstructorArg) parseAttribute(children.item(i)));
			}			
		}
		return bean;
	}
	
	/** Class responsible for parsing the root node 
	 * @throws ContainerException 
	 * @throws ParserException */
	private HashMap<String, Bean> parseRoot(Node node) throws ParserException, ContainerException
	{
		HashMap<String, Bean> list = new java.util.HashMap<String, Bean>();
		
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeName().equals("bean"))
			{
				Bean parsedBean = getBeanDefinition(nodes.item(i));
				if (parsedBean != null)
				{
					list.put(parsedBean.getId(), parsedBean);
				}
			}
		}
		
		return list;
	}
	
	public HashMap<String, Bean> getBeans(InputStream config) throws ContainerException
	{
		if (config == null)
			throw new ParserException("Null config file");
		Document doc = parseXmlFile(config);
		
		NodeList nodeList = doc.getChildNodes();
		Node rootNode = null;
		int counter = 0;
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			if (nodeList.item(i).getNodeName().equals("beans"))
			{
				counter++;
				rootNode = nodeList.item(i);
			}
		}
		
		if (rootNode == null || counter != 1)
			throw new ParserException("Root node not found or more than one root node found");
		
		HashMap<String, Bean> result = parseRoot(rootNode);
		new DependencyBuilder().createDependencies(result);
		return result;
	}

}
