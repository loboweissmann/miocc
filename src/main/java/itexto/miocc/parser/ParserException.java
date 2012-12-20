package itexto.miocc.parser;

import itexto.miocc.ContainerException;

/**
 * Exception wich is throwed by the Container parser
 * @author Henrique
 *
 */
public class ParserException extends ContainerException {
	
	public ParserException(String message, Exception ex)
	{
		super(message, ex);
	}
	
	public ParserException(String message)
	{
		super(message);
	}
	
	public ParserException(Exception ex)
	{
		super(ex);
	}

}
