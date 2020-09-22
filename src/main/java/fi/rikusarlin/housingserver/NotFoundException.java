package fi.rikusarlin.housingserver;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String description, int id) {
        super(description+" id not found : " + id);
    }

}
