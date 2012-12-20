package itexto.miocc.entities;

/** A property related to a given bean */
public class Property extends Attribute {

	/** Default constructor */
	public Property() {}
	
	public String getDescription()
	{
		return "Property";
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Property)
		{
			Property prop = (Property) obj;
			if (prop != null && prop.getName().equals(getName()))
			{
				if (prop.getType() != null && getType() != null)
					return prop.getType().equals(getType());
			}
		}
		return false;
	}
}
