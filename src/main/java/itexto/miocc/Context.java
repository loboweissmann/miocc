package itexto.miocc;
import java.io.InputStream;
import java.util.*;

import itexto.miocc.entities.*;
import itexto.miocc.instantiation.Factory;
import itexto.miocc.instantiation.InstantiationExceptionItContainer;
import itexto.miocc.parser.*;
/**
 * Main class of the itexto Container. Responsible for instantiating the 
 * declared beans
 * beans carregados pelo container 
 * @author Henrique
 */
public class Context {
	
	/** The singletons of the context */
	private HashMap<String, Object> singletons;
	
	/** Return all the singletons of the given HashMap */
	private HashMap<String, Object> getSingletons()
	{
		if (singletons == null)
			singletons = new HashMap<String, Object>();
		return singletons;
	}
	
	
	
	/**
	 * The bean definitions
	 */
	private HashMap<String, Bean> beans;
	
	/**
	 * Returns a read only list of all the 
	 */
	public Map<String, Bean> getDefinitions()
	{
		return Collections.unmodifiableMap(getBeans());
	}
	
	/**
	 * Return the bean definitions
	 * @return
	 */
	private HashMap<String, Bean> getBeans()
	{
		return this.beans;
	}
	
	/**
	 * Retorna a instância do bean em questão
	 * @param beanName
	 * @return
	 */
	public Object getBean(String id) throws InstantiationExceptionItContainer
	{
		Bean definition = 
			getBeans() == null ? null : getBeans().get(id.trim());
		if (definition != null)
		{
			Factory factory = new Factory(this);
			switch (definition.getScope())
			{
				case Instance:
					return factory.newInstance(definition);
				case Singleton:
					Object singleton = getSingletons().get(definition.getId());
					if (singleton != null)
						return singleton;
					else
					{
						Object newSingleton = factory.newInstance(definition);
						getSingletons().put(definition.getId(), newSingleton);
						return newSingleton;
					}
					
			}
		}
		return null;
	}
	
	/** Construtor que busca todos os dados a partir do parser */
	public Context(Parser parser) throws ContainerException
	{
		this.beans = parser.getBeans();
	}
	
	/** Cosntrutor padrão */
	public Context() {}

}
