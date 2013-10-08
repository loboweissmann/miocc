package itexto.miocc.parser;

import itexto.miocc.ContainerException;
import itexto.miocc.entities.Bean;
import java.util.HashMap;

public interface Parser {
   
   public HashMap<String, Bean> getBeans() throws ContainerException;
   
}