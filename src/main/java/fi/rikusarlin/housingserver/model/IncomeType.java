package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;
public enum IncomeType {
    SALARY, PENSION, SOCIAL_BENEFIT, STUDY_GRANT, DIVIDEND, INTEREST, OTHER
}
