package org.springframework.social.salesforce.api;

import java.util.ArrayList;
import java.util.List;

public class BulkApiException extends Exception {
  
  private static final long serialVersionUID = -8075835777791735976L;

  private List<String> errors = new ArrayList<String>();

  public BulkApiException() {
    super();
  }

  public BulkApiException(String message) {
    super(message);
  }

  public BulkApiException(String message, List<String> errors) {
    super(message);
    this.errors = errors;
  }

  public BulkApiException(Throwable cause) {
    super(cause);
  }

  public BulkApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public BulkApiException(String message, Throwable cause, ArrayList<String> errors) {
    super(message, cause);
    this.errors = errors;
  }

  public BulkApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public List<String> getErrors() {
    return this.errors;
  }
}
