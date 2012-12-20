package itexto.miocc.parser;

import itexto.miocc.entities.*;

import java.util.*;

/**
 * Class responsible for checking all the dependencies in the given beans
 * @author Henrique
 *
 */
public class DependencyBuilder {
	
	private void processAttribute(Bean bean, Attribute attribute, Map<String, Bean> beans) throws ParserException
	{
		if (bean != null && beans != null && ! beans.isEmpty())
		{
			if (attribute.getReferencedBean() != null &&
				attribute.getReferencedBean().trim().equals("") == false)
			{
				if (attribute.getValue() != null && ! attribute.getValue().trim().equals(""))
					throw new ParserException("The " + attribute.getDescription() + " already have referenced a external bean: " + attribute.getReferencedBean());
				Bean referencedBean = beans.get(attribute.getReferencedBean().trim());
				if (referencedBean == null)
					throw new ParserException("Could not find the bean " + attribute.getReferencedBean());
				bean.addDependencie(referencedBean);
			}
		}
	}
	
	/**
	 * Create all the dependencies for the beans in the map
	 * @param beans
	 * @throws ParserException
	 */
	public void createDependencies(Map<String, Bean> beans) throws ParserException
	{
		if (beans != null && ! beans.isEmpty())
		{
			Iterator<Bean> iterator = beans.values().iterator();
			while (iterator.hasNext())
			{
				Bean current = iterator.next();
				// Iterando pelas propriedades
				for (Property property : current.getProperties())
				{
					processAttribute(current, property, beans);
				}
				// Iterando pelos construtores
				for (ConstructorArg constructor : current.getConstructorArgs())
				{
					processAttribute(current, constructor, beans);
				}
			}
		}
	}

}
