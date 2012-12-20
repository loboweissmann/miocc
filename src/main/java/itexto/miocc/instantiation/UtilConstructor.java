package itexto.miocc.instantiation;


import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Vector;

import itexto.miocc.entities.*;

/**
 * Utility class with methods for dealing with constructors
 * @author Henrique
 *
 */
public class UtilConstructor {
	
	/**
	 * Return the constructor with minimum parameters
	 */
	public Constructor getConstructor(Bean bean, Class claz) throws InstantiationExceptionItContainer
	{
		int numParameters = bean.getBiggerConstructorArgOrder() + 1;
		Constructor constructors[] = claz.getConstructors();
		Constructor result = null;
		List<Constructor> foundConstructors = new Vector<Constructor>();
		
		for (Constructor constructor : constructors)
		{
			if (constructor.getParameterTypes().length == numParameters)
			{
				foundConstructors.add(constructor);
				result = constructor;
			}
		}
		
		/**
		 * There are more than one type of constructors with the same number
		 * of parameters.
		 * 
		 * Now comes the hard part. We must discover wich one to use
		 */
		if (foundConstructors.size() > 1)
		{
			//TODO: improve the constructor selection algorithm
			throw new InstantiationExceptionItContainer("It was not possible to find a unique constructor for your class");
			
		}
		
		return result;
	}
	
	/**
	 * Create a new instance of the given bean using it´s defined constructor parameters
	 * @param bean The bean definition to be instantiated
	 * @return
	 * @throws itexto.miocc.instantiation.InstantiationExceptionItContainer
	 */
	public Object newInstance(Bean bean, Class claz) throws itexto.miocc.instantiation.InstantiationExceptionItContainer
	{
		Constructor constructor = getConstructor(bean, claz);
		if (constructor != null)
		{
			//return constructor.newInstance(arg0)
		}
		return null;
	}
	
	/** Default constructor */
	public UtilConstructor()
	{
		
	}

}
