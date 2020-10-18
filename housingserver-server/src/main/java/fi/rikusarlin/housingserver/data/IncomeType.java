package fi.rikusarlin.housingserver.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets IncomeType
 */
public enum IncomeType {
  
  SALARY("SALARY"),
  
  PENSION("PENSION"),
  
  SOCIAL_BENEFIT("SOCIAL_BENEFIT"),
  
  STUDY_GRANT("STUDY_GRANT"),
  
  DIVIDEND("DIVIDEND"),
  
  INTEREST("INTEREST"),
  
  OTHER("OTHER");

  private String value;

  IncomeType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static IncomeType fromValue(String text) {
    for (IncomeType b : IncomeType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + text + "'");
  }
}

