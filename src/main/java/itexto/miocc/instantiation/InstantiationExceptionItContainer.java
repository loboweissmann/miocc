package itexto.miocc.instantiation;

import itexto.miocc.ContainerException;

public class InstantiationExceptionItContainer extends ContainerException {

	public InstantiationExceptionItContainer() {
		// TODO Auto-generated constructor stub
	}

	public InstantiationExceptionItContainer(String message) {
		super(message);
		
	}

	public InstantiationExceptionItContainer(String message, Exception ex) {
		super(message, ex);
		
	}

	public InstantiationExceptionItContainer(Exception ex) {
		super(ex);
		
	}

}
