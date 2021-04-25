package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;
public enum ExpenseType {
    RENT, ELECTRICITY, WATER, INTEREST, MAINTENANCE_CHARGE, OTHER
}
