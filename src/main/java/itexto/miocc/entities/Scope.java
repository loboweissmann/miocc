package itexto.miocc.entities;

/**
 * The kinds of scope for the itexto Container
 * @author Henrique
 */
public enum Scope {
	
	Instance, Singleton;
	
	
	public static Scope getScope(String name)
	{
		String value = name == null ? "" : name.trim().toLowerCase();
		if (value.equals("instance"))
			return Instance;
		else if (value.equals("singleton"))
			return Singleton;
		else
			return null;
	}
	
}
