package co.edu.uptc.model;

public class Notification {
  private String message;
  private String date;
  private String idEmployeeFrom;

  public Notification(String message, String date, String idEmployeeFrom) {
    this.message = message;
    this.date = date;
    this.idEmployeeFrom = idEmployeeFrom;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getIdEmployeeFrom() {
    return idEmployeeFrom;
  }

  public void setIdEmployeeFrom(String idEmployeeFrom) {
    this.idEmployeeFrom = idEmployeeFrom;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

}
