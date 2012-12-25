package itexto.miocc.tests.parser;

import itexto.miocc.entities.Bean;
import itexto.miocc.entities.Scope;
import itexto.miocc.parser.Parser;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;
public class ParserTest extends TestCase {

	
	public void testGetBeans() throws Exception {
		Parser parser = new Parser();
		InputStream input = getClass().getClassLoader().getResourceAsStream("itexto/miocc/tests/instantiation/NewFile.xml");
		assertNotNull(input);
		Map<String, Bean> beans = parser.getBeans(input);
		
		assertNotNull("Sem beans", beans);
		assertTrue(beans.size() > 0);
		assertTrue(beans.containsKey("beanTest"));
		Bean parsed = beans.get("beanTest");
		assertNotNull(parsed);
		assertTrue(parsed.getProperties().size() > 0);
		assertEquals(parsed.getScope(), Scope.Singleton);
	}
	
	
	public void testGetDependencies() throws Exception {
		Parser parser = new Parser();
		InputStream input = getClass().getClassLoader().getResourceAsStream("itexto/miocc/tests/instantiation/NewFile.xml");
		assertNotNull(input);
		Map<String, Bean> beans = parser.getBeans(input);
		Bean withDependencies = beans.get("beanTest");
		assertNotNull(withDependencies);
		Bean dependencie = beans.get("referencedBean");
		assertNotNull(dependencie);
		assertTrue(withDependencies.getDependencies().contains(dependencie));
	}

}
