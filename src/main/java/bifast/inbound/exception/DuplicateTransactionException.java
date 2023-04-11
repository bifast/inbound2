package bifast.inbound.exception;

public class DuplicateTransactionException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateTransactionException(String errorMessage) {  
		
    	super(errorMessage);  
    }  

}
