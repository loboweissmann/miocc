package itexto.miocc.entities;

public abstract class Attribute {

	/** the name of the property */
	protected String name;
	/** The value of the property */
	protected String value;
	/** The referenced bean */
	protected String referencedBean;
	/** the type of the property (optional) */
	protected String type;
	/* The owner of the attribute */
	protected Bean owner;
	
	public Bean getOwner() {return this.owner;}
	public void setOwner(Bean bean) {this.owner = bean;}

	public abstract String getDescription();
	
	public Attribute() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setReferencedBean(String referencedBean) {
		this.referencedBean = referencedBean;
	}

	public String getReferencedBean() {
		return referencedBean;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	

}