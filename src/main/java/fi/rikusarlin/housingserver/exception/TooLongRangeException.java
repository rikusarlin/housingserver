package fi.rikusarlin.housingserver.exception;

import java.time.LocalDate;

public class TooLongRangeException extends RuntimeException {

	private static final long serialVersionUID = 3268613956410383434L;

	public TooLongRangeException(LocalDate start, LocalDate end) {
        super("Date range between "+start+" and " +end+ " is too long, max 1 year");
    }

}
