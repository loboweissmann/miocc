package itexto.miocc.tests.entities;

import itexto.miocc.entities.Bean;
import junit.framework.TestCase;

public class BeanTest extends TestCase {

	public void testGetProperties() {
		Bean bean = new Bean();
		assertNotNull(bean.getProperties());
		assertTrue(bean.getProperties().size() == 0);
	}

	public void testGetConstructorArgs() {
		Bean bean = new Bean();
		assertNotNull(bean.getConstructorArgs());
		assertTrue(bean.getConstructorArgs().size() == 0);
	}

}
