package fi.rikusarlin.housingserver.exception;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Info about error(s) in backend processing")
public class ApiErrorMessage   {
  private OffsetDateTime timestamp;

  private Integer status;

  private String error;

  private String path;

  private List<ErrorMessageMessage> message = null;

  public ApiErrorMessage timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  */
  @ApiModelProperty(value = "")

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ApiErrorMessage status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * HTTP status code
   * @return status
  */
  @ApiModelProperty(value = "HTTP status code")


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ApiErrorMessage error(String error) {
    this.error = error;
    return this;
  }

  /**
   * description of error
   * @return error
  */
  @ApiModelProperty(value = "description of error")


  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public ApiErrorMessage path(String path) {
    this.path = path;
    return this;
  }

  /**
   * URL path of error
   * @return path
  */
  @ApiModelProperty(value = "URL path of error")


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ApiErrorMessage message(List<ErrorMessageMessage> message) {
    this.message = message;
    return this;
  }

  public ApiErrorMessage addMessageItem(ErrorMessageMessage messageItem) {
    if (this.message == null) {
      this.message = new ArrayList<>();
    }
    this.message.add(messageItem);
    return this;
  }

  /**
   * Get message
   * @return message
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<ErrorMessageMessage> getMessage() {
    return message;
  }

  public void setMessage(List<ErrorMessageMessage> message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiErrorMessage errorMessage = (ApiErrorMessage) o;
    return Objects.equals(this.timestamp, errorMessage.timestamp) &&
        Objects.equals(this.status, errorMessage.status) &&
        Objects.equals(this.error, errorMessage.error) &&
        Objects.equals(this.path, errorMessage.path) &&
        Objects.equals(this.message, errorMessage.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, error, path, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorMessage {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

