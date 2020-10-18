package fi.rikusarlin.housingserver.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ExpenseType
 */
public enum ExpenseType {
  
  RENT("RENT"),
  
  ELECTRICITY("ELECTRICITY"),
  
  WATER("WATER"),
  
  INTEREST("INTEREST"),
  
  MAINTENANCE_CHARGE("MAINTENANCE_CHARGE"),
  
  OTHER("OTHER");

  private String value;

  ExpenseType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ExpenseType fromValue(String text) {
    for (ExpenseType b : ExpenseType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + text + "'");
  }
}

