package fi.rikusarlin.housingserver.exception;

public class NotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6561617885497885691L;

	public NotFoundException(String description, int id) {
        super(description+" not found : " + id);
    }

}
