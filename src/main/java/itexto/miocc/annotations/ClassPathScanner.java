package itexto.miocc.annotations;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassPathScanner {
   
   /**
    * Scans all classes accessible from the context class loader which belong to
    * the given package and subpackages.
    * 
    * @param packageName
    *           The base package
    * @return The classes
    * @throws ClassNotFoundException
    * @throws IOException
    */
   public static List<Class> getClassesArquivo(String packageName) {
      try {
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         assert classLoader != null;
         String path = packageName.replace('.', '/');
         Enumeration<URL> resources = classLoader.getResources(path);
         List<File> dirs = new ArrayList<>();
         
         while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
         }
         
         List<Class> classes = new ArrayList<>();
         
         for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
         }
         
         return classes;
      }
      catch (Exception ex) {
         ex.printStackTrace();  // Inesperado! Afinal só vai carregar classes que achar ;)
         return null;
      }
   }

   /**
    * Recursive method used to find all classes in a given directory and
    * subdirs.
    * 
    * @param directory
    *           The base directory
    * @param packageName
    *           The package name for classes found inside the base directory
    * @return The classes
    * @throws ClassNotFoundException
    */
   private static List<Class> findClasses(File directory, String packageName)
         throws ClassNotFoundException {
      
      List<Class> classes = new ArrayList<>();
      
      if (!directory.exists()) {
         return classes;
      }
      
      File[] files = directory.listFiles();
      
      for (File file : files) {
         if (file.isDirectory()) {
            assert !file.getName().contains(".");
            classes
                  .addAll(findClasses(file, packageName + "." + file.getName()));
         } else if (file.getName().endsWith(".class")) {
            classes.add(Class.forName(packageName + '.'
                  + file.getName().substring(0, file.getName().length() - 6)));
         }
      }
      
      return classes;
   }
   
   public static List<Class> getClassesJar(String packageName) {
      try {
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         URL packageURL;
         List<Class> names = new ArrayList<>();
   
         String jarPackageName = packageName.replace(".", "/");
         packageURL = classLoader.getResource(jarPackageName);
   
         if ( packageURL != null && packageURL.getProtocol().equals("jar") ) {
            String jarFileName;
            JarFile jf;
            Enumeration<JarEntry> jarEntries;
            String entryName;
   
            // build jar file name, then loop through zipped entries
            jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
            
            while (jarEntries.hasMoreElements()) {
               entryName = jarEntries.nextElement().getName();
               
               if ( entryName.startsWith(jarPackageName) && entryName.endsWith(".class") ) {
                  String className = entryName.replace(".class", "").replace('/', '.');
                  Class clazz = Class.forName(className);
                  names.add(clazz);
               }
            }
            
            jf.close();
         }
   
         return names;
      }
      catch (Exception ex) {
         ex.printStackTrace();  // Inesperado! Afinal só vai carregar classes que achar ;)
         return null;
      }
   }
   
}