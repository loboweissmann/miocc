package itexto.miocc.entities;

/**
 * One argument of the constructor for a given bean 
 * @author Henrique
 */
public class ConstructorArg extends Attribute {
	/** The order of the attribute */
	private int order;

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}
	
	public String getDescription()
	{
		return "Constructor argument";
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ConstructorArg)
		{
			return ((ConstructorArg) obj).getOrder() == getOrder();
		}
		return false;
	}

}
