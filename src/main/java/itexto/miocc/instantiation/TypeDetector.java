package itexto.miocc.instantiation;

import itexto.miocc.entities.*;

import java.lang.reflect.*;

/**
 * Class responsible for the discovery of the type of a given
 * property or constructor argument
 * @author Henrique
 */
public class TypeDetector {
	
	/**
	 * Return the type of the constructor argument
	 * @param argument The argument to be analized
	 * @param claz The claz to be analized
	 * @return The Class representing the argument type
	 * @throws InstantiationExceptionItContainer if no constructor is found for this Class
	 */
	public Class getConstructorArgumentType(ConstructorArg argument, Class claz) throws InstantiationExceptionItContainer
	{
		if (argument != null && argument.getOwner() != null && claz != null)
		{
			Constructor construct = new UtilConstructor().getConstructor(argument.getOwner(), claz);
			Class parameterTypes[] = construct.getParameterTypes();
			return parameterTypes[argument.getOrder()];
		}
		return null;
	}
	
	/**
	 * Method wich guesses the type of a given string based on the following 
	 * alghorithm:
	 * <hr/>
	 * If only numbers are found on the string, this is a Integer type<br/>
	 * If only numbers and the '.' or ',' characters are found, we have a float
	 * If the value of the string is (in lower case) 'true' or 'false', we have a boolean
	 * Else, we have a string
	 * @param value
	 * @return
	 */
	public Class guessType(String value)
	{
		if (value != null)
		{
			
		}
		return String.class;
	}
	
	/**
	 * Default constructor
	 */
	public TypeDetector() {}

}
