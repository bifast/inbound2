package bifast.inbound.exception;

public class NonSettleCreditTransferException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonSettleCreditTransferException(String errorMessage) {  
		
    	super(errorMessage);  
    }  

}
