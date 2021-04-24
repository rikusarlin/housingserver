package fi.rikusarlin.housingserver.exception;

public class DuplicateNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = -4607606142856879448L;

	public DuplicateNotAllowedException(String description) {
        super(description);
    }

}
