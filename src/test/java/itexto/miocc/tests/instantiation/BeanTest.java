package itexto.miocc.tests.instantiation;

public class BeanTest {
	
	private int intValue;
	
	private double doubleValue;
	
	private String stringValue;
	
	private boolean booleanValue;
	
	private char charValue;
	
	public BeanTest(String valueString, int valueInt)
	{
		setStringValue(valueString);
		setIntValue(valueInt);
	}
	
	
	/** Default constructor */
	public BeanTest() {}



	public boolean isBooleanValue() {
		return booleanValue;
	}



	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}



	public char getCharValue() {
		return charValue;
	}



	public void setCharValue(char charValue) {
		this.charValue = charValue;
	}



	public double getDoubleValue() {
		return doubleValue;
	}



	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}



	public int getIntValue() {
		return intValue;
	}



	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}



	public String getStringValue() {
		return stringValue;
	}



	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	private BeanTest otherBean;
	
	public BeanTest getOtherBean() {return this.otherBean;}
	
	public void setOtherBean(BeanTest bean) {this.otherBean = bean;}

}
