package fi.rikusarlin.housingserver.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CaseState
 */
public enum CaseState {
  
  NEW("NEW"),
  
  ACTIVE("ACTIVE"),
  
  WAITING("WAITING"),
  
  READY("READY"),
  
  CANCELED("CANCELED");

  private String value;

  CaseState(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CaseState fromValue(String text) {
    for (CaseState b : CaseState.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + text + "'");
  }
}

