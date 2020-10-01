package fi.rikusarlin.housingserver;

public class NotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6561617885497885691L;

	public NotFoundException(String description, int id) {
        super(description+" id not found : " + id);
    }

}
