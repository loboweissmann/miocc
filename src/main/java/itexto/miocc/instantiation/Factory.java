package itexto.miocc.instantiation;

import itexto.miocc.Context;
import itexto.miocc.entities.*;

import java.beans.BeanDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
/**
 * Class responsible for returning a new instance of the given bean
 * @author Henrique
 */
public class Factory {
	
	private Map<String, Object> getDependencies(Bean bean) throws InstantiationExceptionItContainer
	{
		Map<String, Object> dependencies = new HashMap<String, Object>();
		
		if (bean != null && ! bean.getDependencies().isEmpty())
		{
			for (Bean dependency : bean.getDependencies())
			{
				dependencies.put(dependency.getId(), newInstance(dependency));
			}
		}
		
		return dependencies;
	}
	
	/**
	 * Creates a new instance of the given bean. Just a simple bean, with no dependencies
	 * @param bean
	 * @return
	 * @throws itexto.miocc.instantiation.InstantiationExceptionItContainer
	 */
	private Object instantiate(Bean bean) throws itexto.miocc.instantiation.InstantiationExceptionItContainer
	{
		if (bean == null)
			return null; //No bean, no shell
		if (bean.getName() == null)
			throw new InstantiationExceptionItContainer("Class name undefined for the bean " + bean.getName());
		
		
		Class claz;
		
		try {
			claz = Class.forName(bean.getName());
		} catch (ClassNotFoundException ex) {
			throw new InstantiationExceptionItContainer("Class " + bean.getName() + " could not be instantiated", ex);
		}
		
		if (bean.getConstructorArgs().isEmpty())
		{
			// Instantiation by default constructor. Piece of cake.
			try {
				return claz.newInstance();
			} catch (InstantiationException e) {
				throw new InstantiationExceptionItContainer("Instantiation exception for " + bean.getId(), e);
			} catch (IllegalAccessException e) {
				throw new InstantiationExceptionItContainer("Illegal access. Private constructor?", e);
			}			
		}
		else
		{
			// Instatiation by constructor
			Constructor constructor = findConstructor(bean, claz);
			if (constructor == null)
				throw new InstantiationExceptionItContainer("Could not find a constructor for bean " + bean.getName());
			else
			{
				// Now, let´s fill the constructor
				Object[] constructorValues = fillConstructor(bean, constructor);
				try {
					// The creature is alive!
					return constructor.newInstance(constructorValues);
				} catch (IllegalArgumentException e) {
					throw new InstantiationExceptionItContainer("Illegal argument exception: " + bean.getId(), e);
				} catch (InstantiationException e) {
					throw new InstantiationExceptionItContainer("Instantiation exception " + bean.getId(), e);
				} catch (IllegalAccessException e) {
					throw new InstantiationExceptionItContainer("Illegal Access exception " + bean.getId(), e);
				} catch (InvocationTargetException e) {
					throw new InstantiationExceptionItContainer("Invocation target exception " + bean.getId(), e);
				}
			}
			
		}
		
	}
	
	/**
	 * Return the arguments of the given constructor
	 * @param bean
	 * @param constructor
	 * @return
	 * @throws InstantiationExceptionItContainer
	 */
	private Object[] fillConstructor(Bean bean, Constructor constructor) throws InstantiationExceptionItContainer
	{
		if (bean == null || constructor == null)
			throw new InstantiationExceptionItContainer("Bean or constructor undefined");
		
		
		Object[] argumentValues = new Object[constructor.getParameterTypes().length];
		
		for (ConstructorArg argument : bean.getConstructorArgs())
		{
			if (argument.getReferencedBean() != null)
				argumentValues[argument.getOrder()] = getContext().getBean(argument.getReferencedBean());
			else
			{
				Class type = constructor.getParameterTypes()[argument.getOrder()];
				if (type.isPrimitive())
				{
					argumentValues[argument.getOrder()] = getPrimitiveValue(type, argument.getValue());					
				}
				else
				{
					if (type.getName().equals("java.lang.String")) // Piece of ccake
						argumentValues[argument.getOrder()] = argument.getValue() == null ? null : argument.getValue();
					
				}
			}
		}
		
		return argumentValues;
	}
	
	/**
	 * Encapsulate the primitive type into its respective wrapper
	 * @param claz the primitive type to be processed
	 * @param value the value to be converted to the given wraper
	 * @return the given wrapper.
	 * @throws InstantiationExceptionItContainer
	 */
	private Object getPrimitiveValue(Class claz, String value) throws InstantiationExceptionItContainer
	{
		if (claz != null && claz.isPrimitive() && value != null)
		{
			if (claz.getName().equals("int"))
				return new Integer(value).intValue();
			else if (claz.getName().equals("double"))
				return new Double(value).doubleValue();
			else if (claz.getName().equals("char"))
			{
				if (value.trim().length() != 1)
					throw new InstantiationExceptionItContainer("Char primitive too long " + value);
				else
					return new java.lang.Character(value.charAt(0)).charValue();
			} else if (claz.getName().equals("boolean"))
				return new Boolean(value.trim().equals("true"));
			else if (claz.getName().equals("short"))
				return new Short(value).shortValue();
		}
		else
		{
			if (claz.getName().equals("java.lang.String"))
				return value;
		}
		return null;
	}
	
	/**
	 * Find the corresponding constructor for the given bean
	 * @param bean
	 * @return
	 */
	private Constructor findConstructor(Bean bean, Class claz) throws InstantiationExceptionItContainer
	{
		// No bean and no Clazz? No constructor!
		if (bean == null || claz == null)
			return null;
		// Class != bean class? No donut for you!
		if (! bean.getName().equals(claz.getName()))
			throw new InstantiationExceptionItContainer("Class != " + bean.getName());
		
		Constructor constructors[] = claz.getConstructors();
		if (constructors != null && constructors.length > 0)
		{
			List<Constructor> candidates = new Vector<Constructor>();
			for (Constructor constructor : constructors)
			{
				if (constructor.getParameterTypes().length == bean.getConstructorArgs().size())
					candidates.add(constructor);
			}
			
			if (candidates.size() == 1)
			{
				// Probably found the constructor
				return candidates.get(0);
			}
			else if (candidates.size() > 1)
			{
				/*
				 * there´s more than one constructor.
				 * In this case, the user MUST define all the types of the
				 * constructor arguments.
				 * Below is the test wich verifies if all the constructor arguments
				 * have their types defined
				 */
				for (ConstructorArg argument : bean.getConstructorArgs())
				{
					if (argument.getType() == null)
						throw new InstantiationExceptionItContainer("There´s more than one constructor in this bean that could be used. Please, specify the type of each constructor parameter for bean " + bean.getName());
				}
				
				List<Constructor> finalists = new Vector<Constructor>();
				/*
				 * 
				 * If we get in here, now we must find THE constructor of
				 * this bean
				 * 
				 */			
				for (Constructor finalist : candidates)
				{
					int foundArguments = 0;
					for (ConstructorArg arg : bean.getConstructorArgs())
					{
						if (finalist.getParameterTypes()[arg.getOrder()].getName().equals(arg.getName()))
							foundArguments++;
					}
					
					if (foundArguments == finalist.getParameterTypes().length)
						return finalist;  // here he is!
				}
			}
			
		}
		
		return null;
	}
	
	/**
	 * Return a new instance of the given bean
	 * @param bean The definition of the bean to be instantiated
	 * @return	the new instance of the bean
	 * @throws InstantiationExceptionItContainer if something goes wrong :)
	 */
	public Object newInstance(Bean bean) throws InstantiationExceptionItContainer
	{
		if (bean != null)
		{
			Object instance = this.instantiate(bean);
			fillBean(bean, instance);
			return instance;		
		}
		return null;
	}
	
	/**
	 * Fill all the bean properties
	 * @param bean
	 * @param instance
	 * @throws InstantiationExceptionItContainer 
	 */
	private void fillBean(Bean bean, Object instance) throws InstantiationExceptionItContainer
	{
		if (bean != null && instance != null)
		{
			Class claz = instance.getClass();
			//BeanDescriptor descriptor = new BeanDescriptor(claz);
			
			for (Property prop : bean.getProperties())
			{
				Method method = getSetMethod(prop, claz);
				if (prop.getReferencedBean() != null)
				{
					try {
						method.invoke(instance, getContext().getBean(prop.getReferencedBean()));
					} catch (IllegalArgumentException e) {
						throw new InstantiationExceptionItContainer("Illegal argument ", e);
					} catch (IllegalAccessException e) {
						throw new InstantiationExceptionItContainer("Illegal access ", e);
					} catch (InvocationTargetException e) {
						throw new InstantiationExceptionItContainer("Invocation target error", e);
					}
				}
				else
				{
					try {
						method.invoke(instance, getPrimitiveValue(method.getParameterTypes()[0], prop.getValue()));
					} catch (IllegalArgumentException e) {
						throw new InstantiationExceptionItContainer("Illegal argument ", e);
					} catch (IllegalAccessException e) {
						throw new InstantiationExceptionItContainer("Illegal access ", e);
					} catch (InvocationTargetException e) {
						throw new InstantiationExceptionItContainer("Invocation target error", e);
					}
				}
			}
		}
	}
	
	/**
	 * Return the given set method of a given propertie
	 * @param name
	 * @param claz
	 * @return
	 * @throws InstantiationExceptionItContainer 
	 */
	private Method getSetMethod(Property property, Class claz) throws InstantiationExceptionItContainer
	{
		if (property == null || claz == null)
			return null;
		
		List<Method> candidates = new Vector<Method>();
		String methodName = "set" + 
			property.getName().substring(0, 1).toUpperCase() + 
			property.getName().substring(1);
		
		for (Method method : claz.getMethods())
		{
			if (method.getName().equals(methodName) &&
				method.getParameterTypes().length == 1)
			{
				candidates.add(method);
			}
		}
		
		if (candidates.isEmpty())
			return null;
		else
		{
			if (candidates.size() == 1)
				return candidates.get(0);
			else if (candidates.size() > 1)
			{
				/*
				 * In this case, the type of the method must be specified by
				 * the definition of the property
				 */
				if (property.getType() == null)
					throw new InstantiationExceptionItContainer("Please, specify the type of the method " + methodName + " of the bean " + property.getOwner().getId());
				for (Method method : candidates)
				{
					if (method.getParameterTypes()[0].getName().equals(property.getType()))
						return method;
				}
			}
		}
		
		return null;
	}
	
	private Map<String, Bean> definitions;
	private Map<String, Bean> getDefinitions()
	{
		return this.definitions;
	}
	
	private Context context;
	private Context getContext() {return this.context;}
	
	/** Default constructor */
	public Factory(Context context) 
	{
		this.definitions = context.getDefinitions();
		this.context = context;
	}

}
