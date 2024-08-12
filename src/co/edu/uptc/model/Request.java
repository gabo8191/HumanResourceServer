package co.edu.uptc.model;

public class Request {
  private String type;
  private String description;
  private String date;
  private String status;
  private String comments;
  private String idEmployeeFrom;

  public Request(String type, String description, String date, String status, String comments, String idEmployeeFrom) {
    this.type = type;
    this.description = description;
    this.date = date;
    this.status = status;
    this.comments = comments;
    this.idEmployeeFrom = idEmployeeFrom;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public String getDate() {
    return date;
  }

  public String getStatus() {
    return status;
  }

  public String getComments() {
    return comments;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getIdEmployeeFrom() {
    return idEmployeeFrom;
  }

  public void setIdEmployeeFrom(String idEmployeeFrom) {
    this.idEmployeeFrom = idEmployeeFrom;
  }

}
