package co.edu.uptc.presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import co.edu.uptc.model.Employee;
import co.edu.uptc.model.Enterprise;
import co.edu.uptc.model.Notification;
import co.edu.uptc.model.Request;
import co.edu.uptc.net.Connection;
import co.edu.uptc.persistence.Persistence;

public class ClientThread extends Thread {

  private Connection connection;
  private boolean isAdmin;
  private boolean isEmployee;
  private String validateEmployeeLoginId;
  private Persistence persistence;
  private Enterprise enterprise;

  public ClientThread(Socket socket, Enterprise enterprise, Persistence persistence) {
    this.connection = new Connection(socket);
    this.persistence = persistence;
    this.enterprise = enterprise;
  }

  @Override
  public void run() {
    try {
      startMenu();
    } catch (Exception e) {
      System.err.println("Error en el hilo del cliente");
    }
  }

  public void startMenu() {
    try {
      connection.connect();
      String message = "";
      while (!message.equals("Exit")) {
        message = connection.receive();
        String[] parts = message.split("-");
        switch (parts[0]) {

          case "ValidateData":
            validateLogin(parts);
            break;

          case "NextNotificationEmployee":
            NextNotificationEmployee(parts[1]);
            break;

          case "NextNotificationAdmin":
            NextNotificationAdmin(parts[1]);
            break;

          case "EmployeeLogin":
            employeeLogin(parts[1]);
            break;

          case "ShowMyRequests":
            ShowMyRequests(parts[1]);
            break;

          case "SendRequest":
            sendMyRequest(parts);
            break;

          case "ShowMyBonifications":
            showMyBonifications(parts[1]);
            break;

          case "ShowMyNotifications":
            showMyNotifcations(parts[1]);
            break;

          case "EditMyProfile":
            editMyProfile(parts[1]);
            break;

          case "SaveEditMyProfile":
            saveEditMyProfile(parts);
            break;

          case "ShowMyProfile":
            showMyProfile(parts[1]);
            break;
          case "AdminLogin":
            adminLogin(parts[1]);
            break;

          case "SendShowEmployee":
            sendShowEmployee(parts[1]);
            break;

          case "SearchShowEmployee":
            searchShowEmployee(parts[1]);
            break;

          case "SendEditEmployee":
            sendEditEmployee(parts[1]);
            break;

          case "SearchEditEmployee":
            searchEditEmployee(parts[1]);
            break;

          case "SaveEditEmployee":
            saveEditEmployee(parts);
            break;

          case "SendDeleteEmployee":
            sendDeleteEmployee(parts[1]);
            break;

          case "SearchDeleteEmployee":
            searchDeleteEmployee(parts[1]);
            break;

          case "SaveAddEmployee":
            saveAddEmployee(parts);
            break;

          case "SendShowTeam":
            sendShowTeam(parts[1]);
            break;

          case "SearchShowTeam":
            searchShowTeam(parts[1]);
            break;

          case "SendEditTeam":
            sendEditTeam(parts[1]);
            break;

          case "SaveEditTeam":
            saveEditTeam(parts);
            break;

          case "SearchEditTeam":
            searchEditTeam(parts[1]);
            break;

          case "SaveAddTeam":
            saveAddTeam(parts);
            break;

          case "SearchDeleteTeam":
            searchDeleteTeam(parts[1]);
            break;

          case "SendDeleteTeam":
            sendDeleteTeam(parts[1]);
            break;

          case "SearchBonificationEmployee":
            searchBonificationEmployee(parts[1]);
            break;

          case "SendBonificationEmployee":
            sendBonificationEmployee(parts[1]);
            break;

          case "loadEmployeePayroll":
            loadEmployeePayroll(parts);
            break;

          case "ViewRequestsEmployee":
            ShowMyRequestsAdmin(parts[1]);
            break;

          case "ApproveRequest":
            setStatusRequestAdmin(parts);
            break;

          case "DenyRequest":
            setStatusRequestAdmin(parts);
            break;

          case "ViewNotificationsEmployee":
            showMyNotifcations(parts[1]);
            break;

          case "MoveEmployeeToOtherGroup":
            MoveToOtheGroup(parts);
            break;

          default:
            break;
        }
      }
    } catch (Exception e) {
      System.err.println("Se ha cerrado la conexión");
    } finally {
      connection.close();
    }
  }

  private void MoveToOtheGroup(String[] parts) {

    String message = enterprise.MoveToOtheGroup(parts[1], parts[2]);
    connection.send(message);
  }

  private void setStatusRequestAdmin(String[] parts) {
    String message = enterprise.setStatusRequestAdmin(parts);
    boolean isLeader = enterprise.isLeader(Long.parseLong(parts[2]));
    if (!message.split("-")[0].equals("false")) {
      if (parts[0].equals("ApproveRequest")) {
        generateNotification("Su solicitud de: " + parts[4] + " ha sido aprobada. Tenga en cuenta que:" + parts[6],
            generateDate(), validateEmployeeLoginId, isLeader, parts[2]);
        persistence.deleteRequest(validateEmployeeLoginId);
      } else {

        generateNotification("Su solicitud de: " + parts[4] + " ha sido rechazada." + " Motivo: " + parts[6],
            generateDate(), validateEmployeeLoginId, isLeader, parts[2]);
        persistence.deleteRequest(validateEmployeeLoginId);
      }
    }
    connection.send(message);
  }

  private void ShowMyRequestsAdmin(String idAdmin) {
    String data = "";
    if (idAdmin == null || idAdmin.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getAdminRequest(idAdmin));
    }
    connection.send(data);
  }

  private void sendMyRequest(String[] parts) {
    String[] data = transformationData(parts);

    String message = enterprise.addMyRequest(data);

    String[] messageIntern = message.split("-");
    if (messageIntern[0].equals("true")) {
      Request request = new Request(data[4], data[5], data[3], "Pendiente", "", data[0]);
      persistence.addRequest(request);
      generateNotification("Usted ha recibido una solicitud de tipo: " + data[4], data[3], data[0], true, null);
    }

    connection.send(message);
  }

  private void generateNotification(String message, String date, String idEmployeeFrom, boolean isLeader,
      String idEmployeeTo) {

    Notification notification = new Notification(message, date, idEmployeeFrom);
    if (isLeader) {
      boolean successLeader = enterprise.addNotificationToLeader(notification);
      if (successLeader) {
        persistence.addNotificationToLeader(notification);
      }
    } else {
      boolean successEmployee = enterprise.addNotificationToEmployee(notification, idEmployeeTo);
      if (successEmployee) {
        persistence.addNotificationToEmployee(notification, idEmployeeTo);
      }
    }
  }

  private void NextNotificationAdmin(String idAdmin) {
    String data = enterprise.getNextNotificationAdmin(idAdmin);
    deleteNotification();
    connection.send(data);
  }

  private void NextNotificationEmployee(String idEmployee) {
    String data = enterprise.getNextNotificationEmployee(idEmployee);
    deleteNotification();
    connection.send(data);
  }

  private void loadEmployeePayroll(String[] data) {
    String[] updatePayroll = transformationData(data);
    ArrayList<String> dataEmployee = transformationDataToArrayList(updatePayroll);
    String message = enterprise.updateEmployeePayroll(dataEmployee);

    if (message.split("-")[0].equals("true")) {
      Employee updatedEmployee = enterprise.getEmployeeById(dataEmployee.get(2));
      if (enterprise.isLeader(updatedEmployee.getId())) {
        persistence.updateTeamLeaderPayroll(updatedEmployee);
      } else {
        persistence.updateEmployeePayroll(updatedEmployee);

      }
      generateNotification("Su nómina ha sido actualizada", generateDate(), validateEmployeeLoginId, false,
          dataEmployee.get(2));

    }

    connection.send(message);

  }

  private void ShowMyRequests(String employeeId) {
    String data = "";
    if (employeeId == null || employeeId.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeeRequests(employeeId));
    }
    connection.send(data);

  }

  public boolean convertStatusToBoolean(String status) {
    return status.equals("true");
  }

  public void employeeLogin(String status) {
    if (convertStatusToBoolean(status)) {
      this.isEmployee = true;
      this.isAdmin = false;
    } else {
      this.isEmployee = false;
      this.isAdmin = false;
    }
  }

  public void adminLogin(String status) {
    if (convertStatusToBoolean(status)) {
      this.isAdmin = true;
      this.isEmployee = false;
    } else {
      this.isAdmin = false;
      this.isEmployee = false;
    }
  }

  public String[] transformationData(String[] data) {
    String[] dataTranform = new String[data.length - 1];
    for (int i = 1; i < data.length; i++) {
      dataTranform[i - 1] = data[i];
    }
    return dataTranform;
  }

  public void validateLogin(String[] data) {
    String response = "";
    String[] dataTranform = transformationData(data);
    String id = dataTranform[0];
    String yearBirth = dataTranform[1];
    if (id.isEmpty() || yearBirth.isEmpty() || id.contains(" ") || yearBirth.contains(" ") || !id.matches("[0-9]+")
        || !yearBirth.matches("[0-9]+")) {
      response = "false" + "-" + "El id y el año de nacimiento no pueden estar vacios ni tener espacios ni letras";
    } else {

      response = validateLogin(id, yearBirth);
    }
    connection.send(response);
  }

  public String validateLogin(String id, String yearBirthInput) {
    if (!isAdmin && !isEmployee) {
      return "false" + "-" + "Seleccione un rol en la parte superior";
    }
    this.validateEmployeeLoginId = id;
    String yearBirth = yearBirthInput;

    if (validateEmployeeLoginId.isEmpty() || yearBirth.isEmpty()) {
      return "false" + "-" + "Ingrese todos los datos";
    }
    if (isAdmin) {
      if (enterprise.validateAdminLogin(validateEmployeeLoginId, yearBirth)) {
        String[] admin = enterprise.getEmployeeAsArray(validateEmployeeLoginId);
        return "true" + "-" + String.join("-", admin);
      } else {
        return "false" + "-" + "Datos incorrectos para administrador";
      }
    } else if (isEmployee) {
      if (enterprise.validateEmployeeLogin(validateEmployeeLoginId, yearBirth)) {
        String[] employee = enterprise.getEmployeeAsArray(validateEmployeeLoginId);
        return "true" + "-" + String.join("-", employee);
      } else {
        return "false" + "-" + "Datos incorrectos para empleado";
      }
    }
    return "false" + "-" + "Datos incorrectos";
  }

  public void showMyProfile(String idEmployee) {
    String[] dataEmployee = sendDataEmployee(idEmployee);
    connection.send(String.join("-", dataEmployee));
  }

  public String[] sendDataEmployee(String idEmployee) {
    String[] employee = enterprise.getEmployeeAsArray(idEmployee);
    return employee;
  }

  public void editMyProfile(String idEmployee) {
    String[] dataEdit = sendDataEmployee(idEmployee);
    connection.send(String.join("-", dataEdit));
  }

  public void saveEditMyProfile(String[] dataEdit) {
    String[] dataEditMyProfile = transformationData(dataEdit);
    String messageUpdate = updateMyProfile(dataEditMyProfile);
    connection.send(messageUpdate);
  }

  public String updateMyProfile(String[] dataEdit) {
    ArrayList<String> data = transformationDataToArrayList(dataEdit);
    for (String string : data) {
      if (string.isEmpty()) {
        return "false-Ingrese todos los datos";
      }
    }
    String id = data.get(3);
    boolean isLeader = data.get(12).equals("true");

    String message = enterprise.updateEmployee(data, id, true);
    String[] messageIntern = message.split("-");
    if (messageIntern[0].equals("true")) {
      if (isLeader) {
        Employee updatedLeader = enterprise.getEmployeeById(id);
        persistence.updateLeaderInJson(updatedLeader);
      } else {
        Employee updatedEmployee = enterprise.getEmployeeById(id);
        persistence.updateEmployeeInJson(updatedEmployee);
      }
      return message;
    } else {
      return message;
    }

  }

  public void sendShowEmployee(String IdEmployeeSearch) {
    String data = "";
    if (IdEmployeeSearch == null || IdEmployeeSearch.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeeAsArray(IdEmployeeSearch));

    }
    connection.send(data);
  }

  public void searchShowEmployee(String IdEmployeeSearch) {
    String employeeFind = findEmployee(IdEmployeeSearch);
    connection.send(employeeFind);
  }

  public String findEmployee(String id) {
    String data = "";

    if (id.isEmpty() || id.contains(" ") || !id.matches("[0-9]+")) {
      return "false" + "-" + "Ingrese una identificación válida";
    }
    String[] employee = enterprise.getEmployeeAsArray(id);
    if (employee.length == 0) {
      data = "false" + "-" + "Empleado no encontrado";
    } else {
      data = "true" + "-" + employee[0] + "-" + employee[1] + "-" + employee[11];
    }
    return data;
  }

  public void sendEditEmployee(String idEmployeeEditSend) {
    String data = "";
    if (idEmployeeEditSend == null || idEmployeeEditSend.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeeAsArray(idEmployeeEditSend));
    }
    connection.send(data);
  }

  public void searchEditEmployee(String idEmployeeEditSearch) {
    String findEmployeeSearchEdit = findEmployee(idEmployeeEditSearch);
    connection.send(findEmployeeSearchEdit);
  }

  public void saveEditEmployee(String[] dataEditEmployee) {
    String[] dataEditEmployeeTransform = transformationData(dataEditEmployee);
    String updateEmp = updateEmployee(dataEditEmployeeTransform);
    connection.send(updateEmp);
  }

  public String updateEmployee(String[] dataEditEmployee) {
    ArrayList<String> data = transformationDataToArrayList(dataEditEmployee);
    for (String string : data) {
      if (string.isEmpty()) {
        return "false-Ingrese todos los datos";
      }
    }
    String id = data.get(3);
    boolean isLeader = data.get(12).equals("true");

    String message = enterprise.updateEmployee(data, id, true);
    String[] messageIntern = message.split("-");
    if (messageIntern[1].equals("Actualizado exitosamente.")) {
      if (isLeader) {
        Employee updatedLeader = enterprise.getEmployeeById(id);
        persistence.updateLeaderInJson(updatedLeader);
      } else {
        Employee updatedEmployee = enterprise.getEmployeeById(id);
        persistence.updateEmployeeInJson(updatedEmployee);
      }
      generateNotification("Su perfil ha sido actualizado", generateDate(), validateEmployeeLoginId, false, id);
      return message;
    } else {
      return message;
    }

  }

  public String generateDate() {
    return enterprise.generateDate();
  }

  public ArrayList<String> transformationDataToArrayList(String[] data) {
    ArrayList<String> dataTranform = new ArrayList<>();
    for (String string : data) {
      dataTranform.add(string);
    }
    return dataTranform;
  }

  public void sendDeleteEmployee(String idEmployeeDelete) {
    String data = "";
    if (idEmployeeDelete == null || idEmployeeDelete.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      try {
        String[] deleteEmployee = deleteEmployee(idEmployeeDelete).split("-");
        if (deleteEmployee[0].equals("true")) {
          data = "true" + "-" + "Empleado eliminado exitosamente.";
        } else {
          data = "false" + "-" + "Empleado no encontrado";
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    connection.send(data);
  }

  public String deleteEmployee(String idEmp) {
    long id = Long.parseLong(idEmp);
    String[] response = enterprise.deleteEmployee(id).split("-");
    if (response[0].equals("true")) {
      persistence.deleteEmployee(id);
      return "true" + "-" + "Empleado eliminado exitosamente.";
    } else {
      return "false" + "-" + "Empleado no encontrado";
    }
  }

  public void searchDeleteEmployee(String IdEmployeeSearchDelete) {
    String findEmployeeSearchDelete = findEmployee(IdEmployeeSearchDelete);
    connection.send(findEmployeeSearchDelete);
  }

  public void saveAddEmployee(String[] dataAddEmployee) {
    String[] dataAddEmployeeTransform = transformationData(dataAddEmployee);
    String addEmp = addEmployee(dataAddEmployeeTransform);
    connection.send(addEmp);
  }

  public String addEmployee(String[] dataAddEmployee) {
    ArrayList<String> data = transformationDataToArrayList(dataAddEmployee);

    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).contains("*") && i != 14 && i != 15) {
        return "false-Ingrese todos los datos";
      }
    }

    String idTeam = data.get(16).isEmpty() ? null : data.get(16);
    boolean isLeader = data.get(12).isEmpty() ? false : Boolean.parseBoolean(data.get(12));
    if (isLeader) {
      String message = enterprise.addLeader(data, idTeam, false);
      if (message.split("-")[0].equals("true")) {
        Employee newLeader = enterprise.getEmployeeById(data.get(3));
        persistence.addLeaderToTeam(newLeader, idTeam);
        return message;
      } else {
        return message;
      }
    } else {
      String message = enterprise.addEmployee(data, idTeam, false);
      if (message.split("-")[0].equals("true")) {
        Employee newEmployee = enterprise.getEmployeeById(data.get(3));
        persistence.addEmployee(newEmployee, idTeam);
        return message;
      } else {
        return message;
      }
    }
  }

  public void sendShowTeam(String idTeamSearch) {
    String data = "";
    if (idTeamSearch == null || idTeamSearch.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getTeamAsArray(idTeamSearch));
    }
    connection.send(data);
  }

  public void searchShowTeam(String idTeamSearch) {
    String teamFind = findTeam(idTeamSearch);
    connection.send(teamFind);
  }

  public String findTeam(String idTeam) {
    if (idTeam.isEmpty() || idTeam.contains(" ") || !idTeam.matches("[0-9]+")) {
      return "false" + "-" + "Ingrese una identificación válida";
    }
    String[] team = enterprise.getTeamAsArray(idTeam);
    if (team[0].equals("false") && team[1].equals("Equipo no encontrado")) {
      return "false" + "-" + "Equipo no encontrado";
    }
    String name = team[0] + "-" + team[1];
    return name;
  }

  public void sendEditTeam(String idTeamSearchEdit) {
    String data = "";
    if (idTeamSearchEdit == null || idTeamSearchEdit.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getTeamAsArray(idTeamSearchEdit));
    }
    connection.send(data);
  }

  public void saveEditTeam(String[] fieldsEditTeam) {
    String[] fieldsEditTeamTransform = transformationData(fieldsEditTeam);
    String editTeam = editTeam(fieldsEditTeamTransform);
    connection.send(editTeam);
  }

  public String editTeam(String[] fields) {
    String IdTeamSearch = fields[2];
    ArrayList<String> data = transformationDataToArrayList(fields);

    try {
      String message = enterprise.updateTeam(data, IdTeamSearch);
      if (message.split("-")[0].equals("true")) {
        persistence.updateTeam(data, IdTeamSearch);
        return message;
      } else {
        return message;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "false";
  }

  public void searchEditTeam(String IdTeamSearchEdit) {
    String findTeamSearchEdit = findTeam(IdTeamSearchEdit);
    connection.send(findTeamSearchEdit);
  }

  public void saveAddTeam(String[] dataAddTeam) {
    if (dataAddTeam.length < 2) {
      connection.send("Ingrese todos los datos");
    }

    String[] dataAddTeamTransform = transformationData(dataAddTeam);
    String addTeam = addTeam(dataAddTeamTransform);
    connection.send(addTeam);
  }

  public String addTeam(String[] dataAddTeam) {
    ArrayList<String> data = transformationDataToArrayList(dataAddTeam);
    String message = enterprise.addTeam(data);
    if (message.split("-")[0].equals("true")) {
      persistence.addTeam(data);
      return message;
    } else {
      return message;
    }
  }

  public void searchDeleteTeam(String IdTeamSearchDelete) {
    String findTeamSearchDelete = findTeam(IdTeamSearchDelete);
    connection.send(findTeamSearchDelete);
  }

  public void sendDeleteTeam(String IdTeamSearchDelete) {
    String data = "";
    if (IdTeamSearchDelete == null || IdTeamSearchDelete.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      try {
        String deleteTeam = deleteTeam(IdTeamSearchDelete);
        if (deleteTeam.split("-")[0].equals("true")) {
          data = "true" + "-" + "Equipo eliminado exitosamente.";
        } else {
          data = "false" + "-" + "Equipo no encontrado";
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    connection.send(data);
  }

  public String deleteTeam(String idTeam) throws Exception {
    String id = idTeam;
    enterprise.deleteTeam(id);
    persistence.deleteTeam(id);
    return "true" + "-" + "Equipo eliminado exitosamente.";
  }

  public void searchBonificationEmployee(String idEmployee) {
    String data = "";
    if (idEmployee.isEmpty() || idEmployee.contains(" ") || !idEmployee.matches("[0-9]+")) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = "true-" + String.join("-", enterprise.getEmployeeAsArray(idEmployee));
    }
    connection.send(data);
  }

  public void sendBonificationEmployee(String idEmployee) {
    String data = "";
    if (idEmployee == null || idEmployee.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeePayrollAsArray(idEmployee));
    }
    connection.send(data);
  }

  public void showMyNotifcations(String idEmployee) {

    String data = "";
    if (idEmployee == null || idEmployee.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeeNotifications(idEmployee));
      deleteNotification();
    }

    connection.send(data);
  }

  public void showMyBonifications(String idEmployee) {
    String data = "";
    if (idEmployee == null || idEmployee.isEmpty()) {
      data = "false" + "-" + "Ingrese una identificación";
    } else {
      data = String.join("-", enterprise.getEmployeePayrollAsArray(idEmployee));
    }
    connection.send(data);
  }

  public boolean deleteNotification() {
    return persistence.deleteNotification(validateEmployeeLoginId);

  }
}
