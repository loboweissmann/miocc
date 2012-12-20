package itexto.miocc;

/**
 * Exception throwed, captured by the itexto Container
 * @author Henrique
 *
 */
public class ContainerException extends Exception {
	
	/** The message */
	protected String message;
	
	/** The encapsulated exception */
	protected Exception exception;
	
	public Exception getException() {return this.exception;}
	
	protected void setException(Exception ex)
	{
		this.exception = ex;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	protected void setMessage(String value)
	{
		this.message = value;
	}
	
	/** Default constructor */
	public ContainerException() {}
	
	public ContainerException(String message)
	{
		setMessage(message);
	}
	
	public ContainerException(String message, Exception ex)
	{
		setMessage(message);
		setException(ex);
	}
	
	public ContainerException(Exception ex)
	{
		setException(ex);
	}

}
