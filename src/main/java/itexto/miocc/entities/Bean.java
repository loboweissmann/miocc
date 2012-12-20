package itexto.miocc.entities;

import itexto.miocc.ContainerException;

import java.util.List;
import java.util.Vector;

public class Bean {
	
	/** The id of the bean */
	private String id;
	/** The name of the class wich will be instantiated */
	private String name;
	/** The type of the scope */
	private Scope scope;
	
	/** The properties of the given bean */
	protected List<Property> properties;
	
	/**
	 * Return all the properties of the given bean
	 * @return
	 */
	public List<Property> getProperties()
	{
		if (properties == null)
			properties = new Vector<Property>();
		return this.properties;
	}
	
	/** The list of the constructor arguments */
	protected List<ConstructorArg> constructorArgs;
	/**
	 * Return all the constructor arguments for the given bean
	 */
	public List<ConstructorArg> getConstructorArgs()
	{
		if (constructorArgs == null)
			constructorArgs = new Vector<ConstructorArg>();
		return constructorArgs;
	}
	
	/** The default constructor */
	public Bean() {}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}
	
	/**
	 * Return the instance of the given property
	 * @param property
	 * @return
	 */
	protected Property getProperty(Property property)
	{
		for (Property p : getProperties())
		{
			if (p.equals(property))
				return p;
		}
		return null;
	}
	
	/**
	 * This bean external dependencies
	 */
	protected List<Bean> dependencies;
	
	/**
	 * Return the list of external dependencies for this bean
	 * @return
	 */
	public List<Bean> getDependencies()
	{
		if (dependencies == null)
			dependencies = new Vector<Bean>();
		return dependencies;
	}
	
	/**
	 * Add a new dependencie to the bean
	 * @param bean
	 */
	public void addDependencie(Bean bean)
	{
		if (bean != null && bean.getId().equals(getId()) == false && ! getDependencies().contains(bean))
			getDependencies().add(bean);
	}
	
	/**
	 * Add a new property in the definition of the bean
	 * @param property
	 * @throws ContainerException if the property is already defined for this bean
	 */
	public void addProperty(Property property) throws ContainerException
	{
		if (property != null)
		{
			Property present = getProperty(property);
			if (present != null)
				throw new ContainerException("Property already defined for this bean");
			property.setOwner(this);
			getProperties().add(property);
		}
	}
	
	protected ConstructorArg getConstructorArg(ConstructorArg constructor)
	{
		for (ConstructorArg ca : getConstructorArgs())
		{
			if (ca.equals(constructor))
				return ca;
		}
		return null;
	}
	
	public void addConstructorArg(ConstructorArg constructor) throws ContainerException
	{
		if (constructor != null)
		{
			ConstructorArg present = getConstructorArg(constructor);
			if (present != null)
				throw new ContainerException("Constructor argument already defined for this bean");
			constructor.setOwner(this);
			getConstructorArgs().add(constructor);
		}
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Bean)
		{
			Bean test = (Bean) obj;
			return test.getId() != null && test.getId().equals(getId());
		}
		return false;
	}
	
	/**
	 * Return the constructor argument with the bigger order
	 * @return
	 */
	public int getBiggerConstructorArgOrder()
	{
		int result = -1;
		if (! getConstructorArgs().isEmpty())
		{
			for (ConstructorArg arg : getConstructorArgs())
			{
				if (arg.getOrder() > result)
					result = arg.getOrder();
			}
		}
		return result;
	}
	
	public Class[] getConstructorParameters()
	{
		// O primeiro passo consiste em descobrir o número total de parâmetros
		if (! getConstructorArgs().isEmpty())
		{
			int bigger = this.getBiggerConstructorArgOrder();
			
		}
		return null;
	}
	
}
