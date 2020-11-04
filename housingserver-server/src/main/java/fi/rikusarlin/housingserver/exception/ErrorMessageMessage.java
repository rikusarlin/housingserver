package fi.rikusarlin.housingserver.exception;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class ErrorMessageMessage   {
  private String fieldName;

  private String errorMessage;

  private String errorCode;

  public ErrorMessageMessage fieldName(String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  /**
   * name of field, if applicable, where error occured
   * @return fieldName
  */
  @ApiModelProperty(value = "name of field, if applicable, where error occured")


  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public ErrorMessageMessage errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  /**
   * a more detailed description of the error
   * @return errorMessage
  */
  @ApiModelProperty(value = "a more detailed description of the error")


  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ErrorMessageMessage errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  /**
   * error code
   * @return errorCode
  */
  @ApiModelProperty(value = "error code")


  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorMessageMessage errorMessageMessage = (ErrorMessageMessage) o;
    return Objects.equals(this.fieldName, errorMessageMessage.fieldName) &&
        Objects.equals(this.errorMessage, errorMessageMessage.errorMessage) &&
        Objects.equals(this.errorCode, errorMessageMessage.errorCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldName, errorMessage, errorCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorMessageMessage {\n");
    
    sb.append("    fieldName: ").append(toIndentedString(fieldName)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


