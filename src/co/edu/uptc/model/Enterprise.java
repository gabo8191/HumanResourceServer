package co.edu.uptc.model;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.edu.uptc.structures.MyStack;

public class Enterprise {
    private List<Team> teams;

    public Enterprise(List<Team> teams) {
        this.teams = teams;
    }

    public Employee getEmployeeById(String id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)) {
                return leader;
            }

            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(id)) {
                    return employee;
                }
            }
        }

        return null;
    }

    public Team getTeamOfEmployee(String id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)) {
                return team;
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(id)) {
                    return team;
                }
            }
        }
        return null;
    }

    public String[] getPaymentDataAsArray(String id) {
        String[] employee = getEmployeeAsArray(id);
        long grossSalary = Long.parseLong(employee[17]);
        long auxTransport = Long.parseLong(employee[18]);
        long netSalary = Long.parseLong(employee[19]);
        long productivityBonus = Long.parseLong(employee[20]);
        long learningBonus = Long.parseLong(employee[21]);

        String[] paymentData = new String[5];
        paymentData[0] = grossSalary + "";
        paymentData[1] = auxTransport + "";
        paymentData[2] = netSalary + "";
        paymentData[3] = productivityBonus + "";
        paymentData[4] = learningBonus + "";

        return paymentData;

    }

    public String validationDataPayroll(ArrayList<String> data) {
        for (String string : data) {
            if (string.contains("-")) {
                return "No puede contener guión medio en ninguno de los campos";
            }
        }

        if (data.get(3).matches(".*[a-zA-Z]+.*")) {
            return "El salario bruto no puede contener letras";
        }
        if (data.get(4).matches(".*[a-zA-Z]+.*")) {
            return "El auxilio de transporte no puede contener letras";
        }
        if (data.get(5).matches(".*[a-zA-Z]+.*")) {
            return "El salario neto no puede contener letras";
        }
        if (data.get(6).matches(".*[a-zA-Z]+.*")) {
            return "El bonus de productividad no puede contener letras";
        }
        if (data.get(7).matches(".*[a-zA-Z]+.*")) {
            return "El bonus de aprendizaje no puede contener letras";
        }

        if (Integer.parseInt(data.get(9)) > Integer.parseInt(data.get(10))) {
            return "Las vacaciones tomadas no pueden ser mayores que las restantes";
        }

        if (data.get(8).matches(".*[a-zA-Z]+.*")) {
            return "Los días de vacaciones no pueden contener letras";
        }
        if (data.get(9).matches(".*[a-zA-Z]+.*")) {
            return "Las vacaciones tomadas no pueden contener letras";
        }
        if (data.get(10).matches(".*[a-zA-Z]+.*")) {
            return "El restante de vacaciones no puede contener letras";
        }

        for (String string : data) {
            if (string == null || string.equals("")) {
                return "No puede haber campos vacíos";
            }
        }
        return "";
    }

    public boolean validateAdminLogin(String id, String yearBirth) {

        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)
                    && leader.getYearBirth() == Integer.parseInt(yearBirth)
                    && leader.getJobEmployee().getIsAdmin()) {
                return true;
            }
        }
        return false;
    }

    public String[] getEmployeeAsArray(String id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)) {
                return getEmployeeArray(leader);
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(id)) {
                    return getEmployeeArray(employee);
                }
            }
        }
        return new String[0];
    }

    public String[] getEmployeePayrollAsArray(String id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)) {
                return getEmployeePaymentArray(leader);
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(id)) {
                    return getEmployeePaymentArray(employee);
                }
            }
        }
        return new String[0];
    }

    public String[] getEmployeePaymentArray(Employee employee) {
        String[] employeeArray = new String[11];
        employeeArray[0] = employee.getName();
        employeeArray[1] = employee.getLastName();
        employeeArray[2] = employee.getId() + "";

        employeeArray[3] = employee.getSalaryEmployee().getSalaryGross() + "";
        employeeArray[4] = employee.getSalaryEmployee().getAuxTransport() + "";
        employeeArray[5] = employee.getSalaryEmployee().getSalaryGross()
                + employee.getSalaryEmployee().getAuxTransport()
                + employee.getSalaryEmployee().getProductivityBonus() + employee.getSalaryEmployee().getLearningBonus()
                + "";
        employeeArray[6] = employee.getSalaryEmployee().getProductivityBonus() + "";
        employeeArray[7] = employee.getSalaryEmployee().getLearningBonus() + "";

        employeeArray[8] = employee.getVacationsEmployee().getVacationsDays() + "";
        employeeArray[9] = employee.getVacationsEmployee().getVacationsTaken() + "";
        employeeArray[10] = employee.getVacationsEmployee().getTotalVacations() + "";
        return employeeArray;
    }

    public String[] getTeamAsArray(String id) {
        for (Team team : teams) {
            if (team.getIdTeam().equals(id)) {
                return getTeamArray(team);
            }
        }
        return new String[] { "false", "Equipo no encontrado." };
    }

    private String[] getTeamArray(Team team) {
        String[] teamArray = new String[5];
        teamArray[0] = team.getNameTeam();
        teamArray[1] = team.getAreaTeam();
        teamArray[2] = team.getIdTeam();
        teamArray[3] = team.getLeaderTeam().getName();
        teamArray[4] = String.valueOf(team.getLeaderTeam().getId());
        return teamArray;
    }

    public String updateTeam(ArrayList<String> team, String id) {

        if (!validateInformationTeams(team, true).equals("")) {
            return "false-" + validateInformationTeams(team, true);
        }
        Employee employee = getEmployeeById(team.get(4));

        if (employee != null) {
            employee.getJobEmployee().setIsLeader(true);
            LeaderTeam teamLeader = convertEmployeeToTeamLeader(employee);
            for (Team teamInList : teams) {
                if (teamInList.getIdTeam().equals(id)) {
                    teamInList.setNameTeam(team.get(0));
                    teamInList.setAreaTeam(team.get(1));
                    Employee leader = convertTeamLeaderToEmployee(teamInList.getLeaderTeam());
                    teamInList.getMembersTeam().add(leader);
                    teamInList.setLeaderTeam(teamLeader);
                    return "true-Actualizado exitosamente.";
                }
            }
        }
        return "false-Algo salió mal.";
    }

    public String addTeam(ArrayList<String> team) {
        if (!validateInformationTeams(team, false).equals("")) {
            return "false-" + validateInformationTeams(team, false);
        }

        Team newTeam = new Team();
        newTeam.setNameTeam(team.get(0));
        newTeam.setAreaTeam(team.get(1));
        newTeam.setIdTeam(team.get(2));

        String idLeader = team.get(4);
        Employee leader = getEmployeeById(idLeader);
        if (leader != null) {
            leader.getJobEmployee().setIsLeader(true);
            LeaderTeam teamLeader = convertEmployeeToTeamLeader(leader);
            teamLeader.getJobEmployee().setIsLeader(true);
            newTeam.setLeaderTeam(teamLeader);
            teams.add(newTeam);
            deleteEmployee(Long.parseLong(idLeader));
        }
        return "true-Creado exitosamente.";
    }

    public String validateInformationTeams(ArrayList<String> team, boolean isEdit) {

        for (String string : team) {
            if (string.contains("-")) {
                return "No puede contener guión medio en ninguno de los campos";
            }
        }

        if (!isEdit) {
            for (int i = 0; i < team.size(); i++) {
                if (i != 3 && (team.get(i).equals("*") || team.get(i).equals(""))) {
                    return "No puede haber campos vacíos";
                }
            }
        }

        if (team.get(0).matches(".*\\d.*")) {
            return "El nombre no debe contener números";
        }
        if (team.get(1).matches(".*\\d.*")) {
            return "El área no debe contener números";
        }
        if (!isEdit) {
            for (Team teamInList : teams) {
                if (teamInList.getIdTeam().equals(team.get(2))) {
                    return "El id ya existe";
                }
            }
            for (Team teamInList : teams) {
                Employee leader = teamInList.getLeaderTeam();
                if (leader != null && leader.getId() == Long.parseLong(team.get(4))) {
                    return "El empleado ya es líder";
                }
            }
        }
        for (int i = 0; i < team.size(); i++) {
            if (i != 3 && (team.get(i) == null || team.get(i).equals(""))) {
                return "No puede haber campos vacíos";
            }
        }
        boolean employeeExists = false;
        for (Team teamInList : teams) {
            Employee leader = teamInList.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(team.get(4))) {
                employeeExists = true;
            }
            for (Employee employee : teamInList.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(team.get(4))) {
                    employeeExists = true;
                }
            }
        }
        if (!employeeExists) {
            return "El empleado no existe";
        }
        return "";
    }

    public void deleteTeam(String id) throws Exception {
        Team currentTeam = getTeamById(id);

        if (currentTeam != null) {
            LeaderTeam leader = currentTeam.getLeaderTeam();
            if (leader != null) {
                Employee employeeLeader = convertTeamLeaderToEmployee(leader);
                employeeLeader.getJobEmployee().setIsLeader(false);
                List<Employee> members = currentTeam.getMembersTeam();
                if (members != null) {
                    Team newTeam = getTeamById("0");
                    if (newTeam != null) {
                        members.add(employeeLeader);
                        for (Employee employee : members) {
                            newTeam.addMemberTeam(employee);
                        }
                        teams.add(newTeam);
                        teams.removeIf(team -> team.getIdTeam().equals(id));
                    } else {
                        throw new Exception("Error: Team with ID 0 not found.");
                    }
                } else {
                    throw new Exception("Error: Members list is null.");
                }
            } else {
                throw new Exception("Error: Team leader is null.");
            }
        } else {
            throw new Exception("Error: Team with ID " + id + " not found.");
        }
    }

    public Team getTeamById(String id) {
        for (Team team : teams) {
            if (team.getIdTeam().equals(id)) {
                return team;
            }
        }
        return null;
    }

    private String[] getEmployeeArray(Employee employee) {
        String[] employeeArray = new String[23];
        employeeArray[0] = employee.getName();
        employeeArray[1] = employee.getLastName();
        employeeArray[2] = employee.getGender();
        employeeArray[3] = employee.getId() + "";
        employeeArray[4] = employee.getEmail();
        employeeArray[5] = employee.getPhone() + "";
        employeeArray[6] = employee.getCity();
        employeeArray[7] = employee.getYearBirth() + "";
        employeeArray[8] = employee.getMonthBirth() + "";
        employeeArray[9] = employee.getDayBirth() + "";

        employeeArray[10] = employee.getJobEmployee().getArea();
        employeeArray[11] = employee.getJobEmployee().getPosition();
        employeeArray[12] = employee.getJobEmployee().getIsLeader() + "";
        employeeArray[13] = employee.getJobEmployee().getIsAdmin() + "";

        employeeArray[14] = employee.getVacationsEmployee().getVacationsDays() + "";
        employeeArray[15] = employee.getVacationsEmployee().getVacationsTaken() + "";
        employeeArray[16] = employee.getVacationsEmployee().getTotalVacations() + "";

        employeeArray[17] = employee.getSalaryEmployee().getSalaryGross() + "";
        employeeArray[18] = employee.getSalaryEmployee().getAuxTransport() + "";
        employeeArray[19] = employee.getSalaryEmployee().getSalaryNet() + "";
        employeeArray[20] = employee.getSalaryEmployee().getProductivityBonus() + "";
        employeeArray[21] = employee.getSalaryEmployee().getLearningBonus() + "";
        employeeArray[22] = getTeamOfEmployee(employee.getId() + "").getIdTeam();
        return employeeArray;
    }

    public boolean validateEmployeeLogin(String id, String yearBirth) {
        long idLong = Long.parseLong(id);
        int yearBirthInt = Integer.parseInt(yearBirth);
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == idLong && leader.getYearBirth() == yearBirthInt) {
                return true;
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == idLong && employee.getYearBirth() == yearBirthInt) {
                    return true;
                }
            }
        }
        return false;
    }

    public String addLeader(ArrayList<String> employee, String teamId, boolean isEdit) {
        if (!validateInformation(employee, isEdit).equals("")) {
            return "false-" + validateInformation(employee, isEdit);
        }
        for (Team team : teams) {
            if (team.getIdTeam().equals(teamId)) {
                Employee newLeader = new Employee();
                updateEmployeeData(employee, newLeader);
                team.setLeaderTeam(convertEmployeeToTeamLeader(newLeader));
                return "true-Actualizado exitosamente.";
            }
        }
        return "false-Algo salió mal.";
    }

    public String addEmployee(ArrayList<String> employee, String teamId, boolean isEdit) {
        if (!validateInformation(employee, isEdit).equals("")) {
            return "false-" + validateInformation(employee, isEdit);
        }
        for (Team team : teams) {
            if (team.getIdTeam().equals(teamId)) {
                if (team.getMembersTeam() == null) {
                    team.setMembersTeam(new ArrayList<>());
                }
                Employee newEmployee = new Employee();
                updateEmployeeData(employee, newEmployee);
                team.addMemberTeam(newEmployee);
                return "true-Actualizado exitosamente.";
            }
        }
        return "false-Algo salió mal.";
    }

    public String updateEmployee(ArrayList<String> employee, String id, boolean isEdit) {
        if (!validateInformation(employee, isEdit).equals("")) {
            return "false" + "-" + validateInformation(employee, isEdit);
        }
        boolean isPossibleLeader = Boolean.parseBoolean(employee.get(12));
        if (isPossibleLeader) {
            for (Team team : teams) {
                if (team.getIdTeam().equals(employee.get(16))) {
                    if (team.getLeaderTeam() == null) {
                        Employee newLeader = new Employee();
                        updateEmployeeData(employee, newLeader);
                        team.setLeaderTeam(convertEmployeeToTeamLeader(newLeader));
                        return "true-Actualizado exitosamente.";
                    } else {
                        Employee leader = team.getLeaderTeam();
                        if (leader.getId() != Long.parseLong(id)) {
                            return "false-Ya existe un líder en el equipo";
                        }
                    }
                }

            }
        }

        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(id)) {
                updateEmployeeData(employee, leader);
                return "true-Actualizado exitosamente.";
            }
            for (Employee employeeInTeam : team.getMembersTeam()) {
                if (employeeInTeam.getId() == Long.parseLong(id)) {
                    updateEmployeeData(employee, employeeInTeam);
                    return "true-Actualizado exitosamente.";
                }
            }
        }

        return "false - Algo salió mal.";
    }

    public String updateEmployeePayroll(ArrayList<String> dataEmployee) {
        if (!validationDataPayroll(dataEmployee).equals("")) {
            return "false" + "-" + validationDataPayroll(dataEmployee) + "-" + "0" + "-" + "0";
        }

        for (Team team : teams) {
            Employee leader = convertTeamLeaderToEmployee(team.getLeaderTeam());

            if (leader != null && leader.getId() == Long.parseLong(dataEmployee.get(2))) {
                updateEmployeeDataPayroll(dataEmployee, leader);

                return "true" + "-" + "Actualizado exitosamente." + "-" + leader.getSalaryEmployee().getSalaryNet()
                        + "-" + leader.getVacationsEmployee().getTotalVacations();
            }
            for (Employee employeeInTeam : team.getMembersTeam()) {
                if (employeeInTeam.getId() == Long.parseLong(dataEmployee.get(2))) {
                    updateEmployeeDataPayroll(dataEmployee, employeeInTeam);
                    return "true" + "-" + "Actualizado exitosamente." + "-"
                            + employeeInTeam.getSalaryEmployee().getSalaryNet() + "-"
                            + employeeInTeam.getVacationsEmployee().getTotalVacations();
                }
            }
        }
        return "false" + "-" + "Algo salió mal." + "-" + "0" + "-" + "0";
    }

    public void updateEmployeeDataPayroll(ArrayList<String> dataEmployee, Employee employee) {
        employee.setName(dataEmployee.get(0));
        employee.setLastName(dataEmployee.get(1));
        employee.setId(Long.parseLong(dataEmployee.get(2)));

        employee.getSalaryEmployee().setSalaryGross(Long.parseLong(dataEmployee.get(3)));
        employee.getSalaryEmployee().setAuxTransport(Long.parseLong(dataEmployee.get(4)));
        employee.getSalaryEmployee().setSalaryNet(Long.parseLong(dataEmployee.get(5)));
        employee.getSalaryEmployee().setProductivityBonus(Long.parseLong(dataEmployee.get(6)));
        employee.getSalaryEmployee().setLearningBonus(Long.parseLong(dataEmployee.get(7)));

        employee.getVacationsEmployee().setVacationsDays(Integer.parseInt(dataEmployee.get(8)));
        employee.getVacationsEmployee().setVacationsTaken(Integer.parseInt(dataEmployee.get(9)));
        employee.getVacationsEmployee().setTotalVacations(Integer.parseInt(dataEmployee.get(10)));
    }

    public String validateInformation(ArrayList<String> employee, boolean isEdit) {
        for (String string : employee) {
            if (string.contains("-")) {
                return "No puede contener guión medio en ninguno de los campos";
            }
        }
        if (employee.get(0).matches(".*\\d.*")) {
            return "El nombre no debe contener números";
        }
        if (employee.get(1).matches(".*\\d.*")) {
            return "El apellido no debe contener números";
        }
        if (!employee.get(2).equals("Masculino") && !employee.get(2).equals("Femenino")) {
            return "El género debe ser Masculino o Femenino";
        }

        if (employee.get(3).matches(".*[a-zA-Z]+.*")) {
            return "El id no puede contener letras";
        }
        if (!employee.get(4).contains("@")) {
            return "El email debe contener @";
        }
        if (employee.get(5).matches(".*[a-zA-Z]+.*")) {
            return "El teléfono no puede contener letras";
        }

        if (employee.get(6).matches(".*\\d.*")) {
            return "La ciudad no puede contener números";
        }
        if (employee.get(7).matches(".*[a-zA-Z]+.*")) {
            return "El año de nacimiento no puede contener letras";
        }
        if (employee.get(8).matches(".*[a-zA-Z]+.*")) {
            return "El mes de nacimiento no puede contener letras";
        }
        if (employee.get(9).matches(".*[a-zA-Z]+.*")) {
            return "El día de nacimiento no puede contener letras";
        }
        if (employee.get(10).matches(".*\\d.*")) {
            return "El área no puede contener números";
        }
        if (employee.get(11).matches(".*\\d.*")) {
            return "El cargo no puede contener números";
        }
        if (!employee.get(12).equals("true") && !employee.get(12).equals("false")) {
            return "El lider debe ser true o false";
        }
        if (!employee.get(13).equals("true") && !employee.get(13).equals("false")) {
            return "El admin debe ser true o false";
        }
        for (String string : employee) {
            if (string == null || string.equals("")) {
                return "No puede haber campos vacíos";
            }
        }
        boolean teamExists = false;
        for (Team team : teams) {
            if (team.getIdTeam().equals(employee.get(16))) {
                teamExists = true;
            }
        }
        if (!teamExists) {
            return "El equipo no existe";
        }
        if (!isEdit) {

            for (Team team : teams) {
                Employee leader = team.getLeaderTeam();
                if (leader != null && leader.getId() == Long.parseLong(employee.get(3))) {
                    return "El id ya existe";
                }
                for (Employee employeeInTeam : team.getMembersTeam()) {
                    if (employeeInTeam.getId() == Long.parseLong(employee.get(3))) {
                        return "El id ya existe";
                    }
                }
            }
            boolean isPossibleLeader = Boolean.parseBoolean(employee.get(12));

            if (isPossibleLeader) {
                for (Team team : teams) {
                    if (team.getIdTeam().equals(employee.get(16))) {
                        Employee existingLeader = team.getLeaderTeam();
                        if (existingLeader != null && !isEmptyEmployee(existingLeader)) {
                            return "Ya existe un líder en el equipo";
                        }
                    }
                }
            }
        }
        return "";

    }

    public boolean isLeader(long id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyEmployee(Employee employee) {
        return employee.getName() == null && employee.getLastName() == null
                && employee.getGender() == null && employee.getId() == 0 && employee.getEmail() == null
                && employee.getPhone() == 0 && employee.getCity() == null
                && employee.getYearBirth() == 0 && employee.getMonthBirth() == 0
                && employee.getDayBirth() == 0 && employee.getJobEmployee().getArea() == null
                && employee.getJobEmployee().getPosition() == null && employee.getJobEmployee().getIsLeader() == false
                && employee.getVacationsEmployee().getVacationsDays() == 0
                && employee.getVacationsEmployee().getVacationsTaken() == 0
                && employee.getSalaryEmployee().getSalaryGross() == 0
                && employee.getSalaryEmployee().getAuxTransport() == 0
                && employee.getSalaryEmployee().getSalaryNet() == 0 &&
                employee.getSalaryEmployee().getProductivityBonus() == 0
                && employee.getSalaryEmployee().getLearningBonus() == 0;
    }

    public String deleteEmployee(long id) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == id) {
                team.setLeaderTeam(null);
                return "true-Empleado eliminado exitosamente.";
            }
            Iterator<Employee> iterator = team.getMembersTeam().iterator();
            while (iterator.hasNext()) {
                Employee employeeInTeam = iterator.next();
                if (employeeInTeam.getId() == id) {
                    iterator.remove();
                    return "true-Empleado eliminado exitosamente.";
                }
            }
        }
        return "false-Empleado no encontrado.";
    }

    private void updateEmployeeData(ArrayList<String> employee, Employee employeeToUpdate) {
        if (employee.get(14).equals("*")) {
            employee.set(14, "0");
        }
        if (employee.get(15).equals("*")) {
            employee.set(15, "0");
        }
        employeeToUpdate.setName(employee.get(0));
        employeeToUpdate.setLastName(employee.get(1));
        employeeToUpdate.setGender(employee.get(2));
        employeeToUpdate.setId(Long.parseLong(employee.get(3)));
        employeeToUpdate.setEmail(employee.get(4));
        employeeToUpdate.setPhone(Long.parseLong(employee.get(5)));
        employeeToUpdate.setCity(employee.get(6));
        employeeToUpdate.setYearBirth(Integer.parseInt(employee.get(7)));
        employeeToUpdate.setMonthBirth(Integer.parseInt(employee.get(8)));
        employeeToUpdate.setDayBirth(Integer.parseInt(employee.get(9)));
        employeeToUpdate.getJobEmployee().setArea(employee.get(10));
        employeeToUpdate.getJobEmployee().setPosition(employee.get(11));
        employeeToUpdate.getJobEmployee().setIsLeader(Boolean.parseBoolean(employee.get(12)));
        employeeToUpdate.getJobEmployee().setIsAdmin(Boolean.parseBoolean(employee.get(13)));
        employeeToUpdate.getSalaryEmployee()
                .setSalaryGross(Long.parseLong(employee.get(14)));
        employeeToUpdate.getSalaryEmployee()
                .setSalaryNet(Long.parseLong(employee.get(15)));
    }

    public LeaderTeam convertEmployeeToTeamLeader(Employee employee) {
        LeaderTeam teamLeader = new LeaderTeam();
        teamLeader.setName(employee.getName());
        teamLeader.setLastName(employee.getLastName());
        teamLeader.setGender(employee.getGender());
        teamLeader.setId(employee.getId());
        teamLeader.setEmail(employee.getEmail());
        teamLeader.setPhone(employee.getPhone());
        teamLeader.setCity(employee.getCity());
        teamLeader.setYearBirth(employee.getYearBirth());
        teamLeader.setMonthBirth(employee.getMonthBirth());
        teamLeader.setDayBirth(employee.getDayBirth());
        teamLeader.setJobEmployee(employee.getJobEmployee());
        teamLeader.setVacationsEmployee(employee.getVacationsEmployee());
        teamLeader.setSalaryEmployee(employee.getSalaryEmployee());

        return teamLeader;
    }

    public Employee convertTeamLeaderToEmployee(LeaderTeam leader) {
        Employee employee = new Employee();
        employee.setName(leader.getName());
        employee.setLastName(leader.getLastName());
        employee.setGender(leader.getGender());
        employee.setId(leader.getId());
        employee.setEmail(leader.getEmail());
        employee.setPhone(leader.getPhone());
        employee.setCity(leader.getCity());
        employee.setYearBirth(leader.getYearBirth());
        employee.setMonthBirth(leader.getMonthBirth());
        employee.setDayBirth(leader.getDayBirth());
        employee.setJobEmployee(leader.getJobEmployee());
        employee.setVacationsEmployee(leader.getVacationsEmployee());
        employee.setSalaryEmployee(leader.getSalaryEmployee());
        employee.setNotificationsEmployee(new MyStack<Notification>());
        return employee;
    }

    public String[] getEmployeeRequests(String idEmployee) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idEmployee)) {
                return getRequestsEmployeeArray(leader);
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(idEmployee)) {
                    return getRequestsEmployeeArray(employee);
                }
            }
        }
        return new String[0];
    }

    private String[] getRequestsEmployeeArray(Employee employee) {
        Date date = new Date(System.currentTimeMillis());
        String[] requestsArray = new String[3];
        requestsArray[0] = employee.getName() + " " + employee.getLastName();
        requestsArray[1] = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        requestsArray[2] = employee.getId() + "";
        return requestsArray;
    }

    public String getNextNotificationAdmin(String idAdmin) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idAdmin) && leader.getJobEmployee().getIsAdmin()) {
                return getNextNotification(leader);
            }
        }
        return "No hay notificaciones" + "-" + "/" + "-" + "/";
    }

    public String getNextNotificationEmployee(String idEmployee) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idEmployee)) {
                return getNextNotification(leader);
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(idEmployee)) {
                    return getNextNotification(employee);
                }
            }
        }
        return "No hay notificaciones" + "-" + "/" + "-" + "/";
    }

    private String getNextNotification(Employee employee) {
        if (employee.getNotificationsEmployee().isEmpty()) {
            return "No hay notificaciones" + "-" + "/" + "-" + "/";
        }

        Notification notification = employee.getNotificationsEmployee().pop();

        if (notification != null) {
            Employee employeeFrom = getEmployeeById(notification.getIdEmployeeFrom());
            return employeeFrom.getName() + " " + employeeFrom.getLastName() + "-" + notification.getDate() + "-"
                    + notification.getMessage();
        }

        return "No hay notificaciones" + "-" + "/" + "-" + "/";
    }

    public String[] getEmployeeNotifications(String idEmployee) {

        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idEmployee)) {
                return getNotificationsArray(leader);
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(idEmployee)) {
                    return getNotificationsArray(employee);
                }
            }
        }
        return new String[] { "false", "No hay notificaciones", "false" };
    }

    private String[] getNotificationsArray(Employee employee) {

        if (employee.getNotificationsEmployee().isEmpty()) {
            return new String[] { "false", "No hay notificaciones", "false" };
        }

        Notification notification = employee.getNotificationsEmployee().pop();
        if (notification != null) {
            Employee employeeFrom = getEmployeeById(notification.getIdEmployeeFrom());
            String[] notificationsArray = new String[3];
            notificationsArray[0] = employeeFrom.getName() + " " + employeeFrom.getLastName();
            notificationsArray[1] = notification.getDate();
            notificationsArray[2] = notification.getMessage();
            return notificationsArray;
        }
        return new String[] { "false", "No hay notificaciones", "false" };
    }

    public String addMyRequest(String[] data) {
        String validation = validationInformationRequest(data, false);

        if (!validation.equals("")) {
            return "false" + "-" + validation;
        }

        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(data[0])) {
                leader.addRequest(new Request(data[3], data[4], data[2], "Pendiente", "/", data[0]));
                return "true" + "-" + "Su solicitud ha sido enviada al lider: " + leader.getName() + " "
                        + leader.getLastName() + "-" + leader.getId();
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(data[0])) {
                    leader.addRequest(new Request(data[3], data[4], data[2], "Pendiente", data[5], data[0]));
                    return "true" + "-" + "Su solicitud ha sido enviada al lider: " + leader.getName() + " "
                            + leader.getLastName() + "-" + leader.getId();
                }
            }
        }
        return "false" + "-" + "Algo salió mal";

    }

    private String validationInformationRequest(String[] data, boolean isAdmin) {
        String message = "";
        for (String string : data) {
            if (string.contains("-")) {
                message = "No puede contener guión medio en ninguno de los campos";
            }
        }

        if (data[1].matches(".*\\d.*")) {
            message = "El nombre no debe contener números";
        }

        if (data[2].matches(".*[a-zA-Z]+.*")) {
            message = "El id no puede contener letras";
        }

        if (data[3].matches(".*[a-zA-Z]+.*")) {
            message = "La fecha no puede contener letras";
        }

        if (!data[4].equals("Permiso") && !data[4].equals("Vacaciones") && !data[4].equals("Incapacidad")
                && !data[4].equals("Otro")) {
            message = "El tipo debe ser Permiso, Vacaciones, Incapacidad u Otro";
        }

        if (data[5].equals("*")) {
            data[5] = "";
        }

        if (data[5].contains("-")) {
            message = "La descripción no puede contener guión medio";
        }

        if (data[6].equals("*")) {
            data[6] = "";
        }

        if (data[5].contains("-")) {
            message = "Los comentarios no pueden contener guión medio";
        }

        if (!isAdmin && data[5].equals("")) {
            message = "La descripción es obligatoria";
        }
        if (isAdmin && data[6].equals("")) {
            message = "Los comentarios son obligatorios";
        }

        return message;

    }

    public String getAdminRequest(String idAdmin) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idAdmin)) {
                return getAdminRequest(leader);
            }
        }

        return "false" + "-" + "Algo salió mal";
    }

    private String getAdminRequest(Employee leader) {
        if (leader.getRequestsEmployee().isEmpty()) {
            return "false" + "-" + "No hay solicitudes";
        }

        Request request = leader.getRequestsEmployee().peek();
        if (request != null) {
            Employee employeeFrom = getEmployeeById(request.getIdEmployeeFrom());
            return employeeFrom.getName() + " " + employeeFrom.getLastName() + "-" + request.getIdEmployeeFrom() + "-" +
                    request.getDate() + "-" + request.getType() + "-" + request.getDescription() + "-"
                    + request.getComments();
        }
        return "false" + "-" + "No hay solicitudes";
    }

    public String setStatusRequestAdmin(String[] data) {
        if (data[0].equals("ApproveRequest")) {
            return approveRequest(data);
        } else if (data[0].equals("DenyRequest")) {
            return rejectRequest(data);
        }
        return "false" + "-" + "Algo salió mal";

    }

    private String processRequest(String[] data, boolean isApproved) {
        Employee employee = getEmployeeById(data[2]);

        if (data[6].contains("*")) {
            return "false" + "-" + "Los comentarios son obligatorios";
        }

        if (employee != null) {
            String action = isApproved ? "aprobada" : "rechazada";
            String message = "Su solicitud de: " + data[4] + " ha sido " + action;
            Date date = new Date(System.currentTimeMillis());
            String dateRequest = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
            String idEmployeeFrom = getTeamOfEmployee(data[2]).getLeaderTeam().getId() + "";
            employee.addNotification(new Notification(message, dateRequest, idEmployeeFrom));
            return getNextRequest(getTeamOfEmployee(data[2]).getLeaderTeam());
        }
        return "false" + "-" + "Algo salió mal";
    }

    private String getNextRequest(LeaderTeam leaderTeam) {
        if (leaderTeam.getRequestsEmployee().isEmpty()) {
            return "false" + "-" + "No hay solicitudes";
        }

        Request request = leaderTeam.getRequestsEmployee().poll();
        if (request != null) {
            Employee employeeFrom = getEmployeeById(request.getIdEmployeeFrom());
            return "true-" + employeeFrom.getName() + " " + employeeFrom.getLastName() + "-"
                    + request.getIdEmployeeFrom() + "-" +
                    request.getDate() + "-" + request.getType() + "-" + request.getDescription() + "-"
                    + request.getComments();
        }
        return "false" + "-" + "No hay solicitudes";
    }

    private String rejectRequest(String[] data) {
        return processRequest(data, false);
    }

    private String approveRequest(String[] data) {
        return processRequest(data, true);
    }

    public boolean addNotificationToLeader(Notification notification) {

        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(notification.getIdEmployeeFrom())) {
                leader.addNotification(notification);
                return true;
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(notification.getIdEmployeeFrom())) {
                    leader.addNotification(notification);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addNotificationToEmployee(Notification notification, String idEmployeeTo) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(idEmployeeTo)) {
                leader.addNotification(notification);
                return true;
            }
            for (Employee employee : team.getMembersTeam()) {
                if (employee.getId() == Long.parseLong(idEmployeeTo)) {
                    employee.addNotification(notification);
                    return true;
                }
            }
        }
        return false;
    }

    public String generateDate() {
        Date date = new Date(System.currentTimeMillis());
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public String MoveToOtheGroup(String idTeam1, String idTeam2) {
        Team team1 = getTeamById(idTeam1);
        Team team2 = getTeamById(idTeam2);

        if (team1 != null && team2 != null) {
            for (Employee employee : team1.getMembersTeam()) {
                team2.addMemberTeam(employee);
            }
            team1.getMembersTeam().clear();
            return "true" + "-" + "Empleados movidos exitosamente.";
        }
        return "false" + "-" + "Algo salió mal.";

    }

}
