package itexto.miocc.parser;

import itexto.miocc.ContainerException;
import itexto.miocc.annotations.*;
import itexto.miocc.entities.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Alimenta o container através das anotações @Named (nas classes) e @Inject 
 * (nos setters e construtores) 
 * @author Éderson Cássio
 */
public class AnnotationParser implements Parser {

   private HashMap<String, Class> classes = new HashMap<>();
   
   /**
    * O construtor achará todas as classes anotadas com @Component 
    * @param pacotes Nomes de pacotes a pesquisar (subpacotes inclusos automaticamente)
    */
   public AnnotationParser(String ... pacotes) {
      for (String p: pacotes) {
         List<Class> classesPacote = ClassPathScanner.getClassesArquivo(p);
         try {
            classesPacote.addAll( ClassPathScanner.getClassesJar(p) );
         }
         catch (Exception ex) {}
         
         for (Class classe: classesPacote) {
            if ( classe.isAnnotationPresent(Named.class) ) {
               Named anotacao = (Named) classe.getAnnotation(Named.class);
               String valor = anotacao.value();
               
               // Se o componente não tiver nome (value da anotação), 
               // criamos um nome camelCase para ele
               if (valor.equals("")) {
                  String nome = classe.getSimpleName();
                  valor = Character.toLowerCase( nome.charAt(0) ) + nome.substring(1);
               }
               
               classes.put(valor, classe);
            }
         }
      }
   }
   
   /**
    * Obtenção dos Beans a partir da anotação das classes
    */
   @Override
   public HashMap<String, Bean> getBeans() throws ContainerException {
      HashMap<String, Bean> result = new HashMap<>();
      
      for (String id: classes.keySet()) {
         Bean bean = criarBean(id);
         result.put(id, bean);
      }
      
      new DependencyBuilder().createDependencies(result);
      return result;
   }
   
   /**
    * Cria um Bean a partir de uma entrada no HashMap de classes anotadas
    */
   private Bean criarBean(String id) throws ContainerException {
      Bean bean    = new Bean();
      Class classe = classes.get(id);
      bean.setName( classe.getName() );
      bean.setId(id);
      
      if ( classe.isAnnotationPresent(Singleton.class) )
         bean.setScope(Scope.Singleton);
      else
         bean.setScope(Scope.Instance);
      
      processarInjects(bean, classe);
      
      return bean;
   }
   
   /**
    * Processar os itens marcados com @Inject na classe do Bean
    */
   private void processarInjects(Bean bean, Class classe) throws ContainerException {
      List<Property> metodos = processarMetodos(classe);
      List<ConstructorArg> construtores = processarConstrutor(classe);
      
      for (Property prop: metodos)
         bean.addProperty(prop);
      
      for (ConstructorArg constr: construtores)
         bean.addConstructorArg(constr);
   }
   
   /**
    * Processar métodos com @Inject
    * @return Os Property's populados com os dados do método
    */
   public List<Property> processarMetodos(Class classe) throws ContainerException {
      List<Property> propriedades = new ArrayList<>();
      Method[]       setters      = classe.getMethods();
      
      for (Method set: setters) {
         if ( set.isAnnotationPresent(Inject.class) ) {
            if (set.getParameterTypes().length != 1) {
               String qual = classe.getSimpleName() + " -- " + set.getName();
               throw new ContainerException("Um setter deve receber somente um parâmetro!\n" +
                     qual);
            }
            
            Property prop = new Property();
            String   nome = set.getName();
            
            // Se o nome começa com "set", tira e transforma em camelCase
            if ( nome.startsWith("set") ) {
               nome = nome.substring(3);
               nome = Character.toLowerCase( nome.charAt(0) ) + nome.substring(1);
            }
            
            prop.setName(nome);
            prop.setReferencedBean(nome);
            propriedades.add(prop);
         }
      }
      
      return propriedades;
   }
   
   /**
    * Processar o <b>primeiro</b> construtor encontrado com @Inject.
    * Aqui a convenção é: 
    * Dado um construtor <b>@Inject public MinhaClasse(Outra x, MaisOutra y) { ... }</b>
    * Iremos injetar nele os beans "outra" e "maisOutra" (nomes em camelCase)
    * 
    * @return Os ConstructorArg's populados com os dados do construtor
    */
   private List<ConstructorArg> processarConstrutor(Class classe) {
      List<ConstructorArg> constructorArgs = new ArrayList<>();
      Constructor[]        construtores    = classe.getConstructors();     
      
      for (Constructor constr: construtores) {
         if ( constr.isAnnotationPresent(Inject.class) ) {
            Class[] argumentos = constr.getParameterTypes();
            int ordem  = 0;
            
            for (Class tipoArg: argumentos) {
               // Nome do bean em camelCase
               String referencedBean = tipoArg.getSimpleName();
               referencedBean = Character.toLowerCase( referencedBean.charAt(0) ) + 
                                referencedBean.substring(1);
               
               ConstructorArg constrArg = new ConstructorArg();
               constrArg.setName( tipoArg.getName() );
               constrArg.setOrder(ordem);
               constrArg.setReferencedBean(referencedBean);
               constrArg.setType( tipoArg.getName() );
               constructorArgs.add(constrArg);
               ordem++;
            }
            
            // Vamos por enquanto permitir só um construtor
            break;
         }
      }
      
      return constructorArgs;
   }
   
}