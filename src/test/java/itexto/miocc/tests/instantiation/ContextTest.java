package itexto.miocc.tests.instantiation;

import itexto.miocc.*;
import junit.framework.*;

public class ContextTest extends TestCase {
	
	private static Context context;
	
	public void setUp() throws Exception
	{
		try
		{
		if (context == null)
			context = new Context(getClass().getClassLoader().getResourceAsStream("itexto/miocc/tests/instantiation/beans.xml"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public void testString() throws Exception
	{
		BeanTest beanTest = (BeanTest) context.getBean("beanTest");
		assertNotNull(beanTest);
		assertNotNull(beanTest.getStringValue());
		assertEquals(beanTest.getStringValue(), "Just a string");
	}
	
	public void testInt() throws Exception
	{
		BeanTest beanTest = (BeanTest) context.getBean("beanTest");
		assertNotNull(beanTest);
		assertEquals(beanTest.getIntValue(), 1979);
	}
	
	public void testDouble() throws Exception
	{
		BeanTest beanTest = (BeanTest) context.getBean("beanTest");
		assertNotNull(beanTest);
		assertFalse(beanTest.getDoubleValue() == 0);
		assertEquals(beanTest.getDoubleValue(), 24.0);
	}
	
	public void testChar() throws Exception
	{
		BeanTest beanTest = (BeanTest) context.getBean("beanTest");
		assertNotNull(beanTest);
		assertEquals(beanTest.getCharValue(), 'k');
	}
	
	public void testBoolean() throws Exception
	{
		BeanTest beanTest = (BeanTest) context.getBean("beanTest");
		assertNotNull(beanTest);
		assertTrue(beanTest.isBooleanValue());
	}
	
	public void testDependencie() throws Exception
	{
		BeanTest dependent = (BeanTest) context.getBean("other");
		assertNotNull(dependent);
		assertNotNull(dependent.getOtherBean());
		assertNotNull(dependent.getStringValue());
		assertEquals(dependent.getStringValue(), "other");
		assertEquals(dependent.getOtherBean().getStringValue(), "Just a string");
		assertEquals(dependent.getOtherBean().getCharValue(), 'k');
	}
	
	public void testConstructor() throws Exception
	{
		BeanTest bean = (BeanTest) context.getBean("constructor");
		assertNotNull(bean);
		assertNotNull(bean.getStringValue());
		assertEquals(bean.getStringValue(), "Just a string");
	}
	
	/** Default constructor */
	public ContextTest() {}

}
