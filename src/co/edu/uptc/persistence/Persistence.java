package co.edu.uptc.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import co.edu.uptc.model.Employee;
import co.edu.uptc.model.Notification;
import co.edu.uptc.model.Request;
import co.edu.uptc.model.Team;
import co.edu.uptc.structures.MyQueue;
import co.edu.uptc.structures.MyStack;

public class Persistence {
    private List<Team> teams;
    private String filePath;

    public Persistence(String file) {
        this.filePath = file;
        this.teams = loadTeams();
    }

    public List<Team> loadTeams() {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(MyStack.class, new StackDeserializer())
                        .registerTypeAdapter(MyQueue.class, new QueueDeserializer())
                        .create();
                List<Team> teams = new ArrayList<>();
                for (JsonElement teamElement : teamsArray) {
                    Team team = gson.fromJson(teamElement, Team.class);
                    teams.add(team);
                }
                return teams;
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void updateLeaderInJson(Employee updateLeader) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");

                    long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();

                    if (leaderId == updateLeader.getId()) {
                        leaderObject.addProperty("name", updateLeader.getName());
                        leaderObject.addProperty("lastName", updateLeader.getLastName());
                        leaderObject.addProperty("gender", updateLeader.getGender());
                        leaderObject.addProperty("id", updateLeader.getId());
                        leaderObject.addProperty("email", updateLeader.getEmail());
                        leaderObject.addProperty("phone", updateLeader.getPhone());
                        leaderObject.addProperty("city", updateLeader.getCity());
                        leaderObject.addProperty("yearBirth", updateLeader.getYearBirth());
                        leaderObject.addProperty("monthBirth", updateLeader.getMonthBirth());
                        leaderObject.addProperty("dayBirth", updateLeader.getDayBirth());

                        JsonObject jobObject = leaderObject.getAsJsonObject("jobEmployee");
                        jobObject.addProperty("area", updateLeader.getJobEmployee().getArea());
                        jobObject.addProperty("position", updateLeader.getJobEmployee().getPosition());
                        jobObject.addProperty("isAdmin", updateLeader.getJobEmployee().getIsAdmin());
                        jobObject.addProperty("isLeader", updateLeader.getJobEmployee().getIsLeader());
                        JsonObject salaryObject = leaderObject.getAsJsonObject("salaryEmployee");
                        salaryObject.addProperty("salaryGross",
                                updateLeader.getSalaryEmployee().getSalaryGross());
                        salaryObject.addProperty("salaryNet",
                                updateLeader.getSalaryEmployee().getSalaryNet());
                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }
                        break;
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeInJson(Employee updatedEmployee) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                    for (JsonElement memberElement : membersArray) {
                        JsonObject memberObject = memberElement.getAsJsonObject();
                        long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                        if (memberId == updatedEmployee.getId()) {
                            memberObject.addProperty("name", updatedEmployee.getName());
                            memberObject.addProperty("lastName", updatedEmployee.getLastName());
                            memberObject.addProperty("gender", updatedEmployee.getGender());
                            memberObject.addProperty("id", updatedEmployee.getId());
                            memberObject.addProperty("email", updatedEmployee.getEmail());
                            memberObject.addProperty("phone", updatedEmployee.getPhone());
                            memberObject.addProperty("city", updatedEmployee.getCity());
                            memberObject.addProperty("yearBirth", updatedEmployee.getYearBirth());
                            memberObject.addProperty("monthBirth", updatedEmployee.getMonthBirth());
                            memberObject.addProperty("dayBirth", updatedEmployee.getDayBirth());

                            JsonObject jobObject = memberObject.getAsJsonObject("jobEmployee");
                            jobObject.addProperty("area", updatedEmployee.getJobEmployee().getArea());
                            jobObject.addProperty("position", updatedEmployee.getJobEmployee().getPosition());
                            jobObject.addProperty("isAdmin", updatedEmployee.getJobEmployee().getIsAdmin());
                            jobObject.addProperty("isLeader", updatedEmployee.getJobEmployee().getIsLeader());

                            JsonObject salaryObject = memberObject.getAsJsonObject("salaryEmployee");
                            salaryObject.addProperty("salaryGross",
                                    updatedEmployee.getSalaryEmployee().getSalaryGross());
                            salaryObject.addProperty("salaryNet",
                                    updatedEmployee.getSalaryEmployee().getSalaryNet());

                            try (FileWriter writer = new FileWriter(filePath)) {
                                writer.write(new Gson().toJson(jsonObject));
                            }
                            break;
                        }
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeePayroll(Employee updatedEmployee) {
        if (updatedEmployee == null) {
            System.err.println("El empleado actualizado es nulo");
            return;
        }

        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                if (teamsArray != null) {
                    for (JsonElement teamElement : teamsArray) {
                        JsonObject teamObject = teamElement.getAsJsonObject();
                        JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");

                        if (membersArray != null) {
                            for (JsonElement memberElement : membersArray) {
                                JsonObject memberObject = memberElement.getAsJsonObject();
                                long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();

                                if (memberId == updatedEmployee.getId()) {
                                    JsonObject salaryObject = memberObject.getAsJsonObject("salaryEmployee");
                                    if (salaryObject != null) {
                                        salaryObject.addProperty("salaryGross",
                                                updatedEmployee.getSalaryEmployee().getSalaryGross());
                                        salaryObject.addProperty("auxTransport",
                                                updatedEmployee.getSalaryEmployee().getAuxTransport());
                                        salaryObject.addProperty("salaryNet",
                                                updatedEmployee.getSalaryEmployee().getSalaryNet());
                                        salaryObject.addProperty("productivtyBonus",
                                                updatedEmployee.getSalaryEmployee().getProductivityBonus());
                                        salaryObject.addProperty("learningBonus",
                                                updatedEmployee.getSalaryEmployee().getLearningBonus());
                                    }

                                    JsonObject vacationsObject = memberObject.getAsJsonObject("vacationsEmployee");
                                    if (vacationsObject != null) {
                                        vacationsObject.addProperty("vacationsDays",
                                                updatedEmployee.getVacationsEmployee().getVacationsDays());
                                        vacationsObject.addProperty("vacationsTaken",
                                                updatedEmployee.getVacationsEmployee().getVacationsTaken());
                                        vacationsObject.addProperty("totalVacations",
                                                updatedEmployee.getVacationsEmployee().getTotalVacations());
                                    }

                                    try (FileWriter writer = new FileWriter(filePath)) {
                                        writer.write(new Gson().toJson(jsonObject));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTeamLeaderPayroll(Employee updatedEmployee) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                    long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                    if (leaderId == updatedEmployee.getId()) {
                        JsonObject salaryObject = leaderObject.getAsJsonObject("salaryEmployee");
                        salaryObject.addProperty("salaryGross",
                                updatedEmployee.getSalaryEmployee().getSalaryGross());
                        salaryObject.addProperty("auxTransport",
                                updatedEmployee.getSalaryEmployee().getAuxTransport());
                        salaryObject.addProperty("salaryNet",
                                updatedEmployee.getSalaryEmployee().getSalaryNet());
                        salaryObject.addProperty("productivtyBonus",
                                updatedEmployee.getSalaryEmployee().getProductivityBonus());
                        salaryObject.addProperty("learningBonus",
                                updatedEmployee.getSalaryEmployee().getLearningBonus());

                        JsonObject vacationsObject = leaderObject.getAsJsonObject("vacationsEmployee");
                        vacationsObject.addProperty("vacationsDays",
                                updatedEmployee.getVacationsEmployee().getVacationsDays());
                        vacationsObject.addProperty("vacationsTaken",
                                updatedEmployee.getVacationsEmployee().getVacationsTaken());
                        vacationsObject.addProperty("totalVacations",
                                updatedEmployee.getVacationsEmployee().getTotalVacations());
                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }
                        break;
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTeam(ArrayList<String> data, String idTeam) throws FileNotFoundException, IOException {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    String teamId = teamObject.getAsJsonPrimitive("idTeam").getAsString();

                    if (teamId.equals(idTeam)) {
                        teamObject.addProperty("nameTeam", data.get(0));
                        teamObject.addProperty("areaTeam", data.get(1));
                        teamObject.addProperty("idTeam", data.get(2));
                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }
                        break;
                    }
                }
            }
        }
    }

    public void deleteEmployee(long employeeId) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();

                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        if (leaderObject != null && leaderObject.has("id")) {
                            long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                            if (leaderId == employeeId) {
                                teamObject.remove("leaderTeam");
                                break;
                            }
                        }
                    }
                    if (teamObject.has("membersTeam")) {
                        JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                        for (int i = 0; i < membersArray.size(); i++) {
                            JsonObject memberObject = membersArray.get(i).getAsJsonObject();
                            long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                            if (memberId == employeeId) {
                                membersArray.remove(i);

                                break;
                            }
                        }
                    }
                }
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(new Gson().toJson(jsonObject));
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee newEmployee, String idTeam) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    String teamId = teamObject.getAsJsonPrimitive("idTeam").getAsString();
                    if (teamId.equals(idTeam)) {
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(MyStack.class, new StackSerializer())
                                .registerTypeAdapter(MyQueue.class, new QueueSerializer())
                                .create();

                        JsonObject newEmployeeObject = gson.toJsonTree(newEmployee).getAsJsonObject();
                        if (!teamObject.has("membersTeam")) {
                            teamObject.add("membersTeam", new JsonArray());
                        }
                        JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                        membersArray.add(newEmployeeObject);
                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }
                        return;

                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeam(String id) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (int i = 0; i < teamsArray.size(); i++) {
                    JsonObject teamObject = teamsArray.get(i).getAsJsonObject();
                    String teamId = teamObject.getAsJsonPrimitive("idTeam").getAsString();

                    if (teamId.equals(id)) {
                        JsonObject destinationTeam = null;
                        for (int j = 0; j < teamsArray.size(); j++) {
                            JsonObject teamObject2 = teamsArray.get(j).getAsJsonObject();
                            String teamId2 = teamObject2.getAsJsonPrimitive("idTeam").getAsString();

                            if (teamId2.equals("0")) {
                                destinationTeam = teamObject2;
                                break;
                            }
                        }
                        if (destinationTeam == null) {
                            destinationTeam = new JsonObject();
                            destinationTeam.addProperty("nameTeam", "Sin grupo");
                            destinationTeam.addProperty("areaTeam", "Sin area");
                            destinationTeam.addProperty("idTeam", "0");

                            JsonObject leaderObject = new JsonObject();
                            leaderObject.addProperty("name", "Sin grupo");
                            leaderObject.addProperty("lastName", "Sin grupo");
                            leaderObject.addProperty("id", 0);

                            destinationTeam.add("leaderTeam", leaderObject);
                            destinationTeam.add("membersTeam", new JsonArray());

                            teamsArray.add(destinationTeam);
                        }
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");

                        destinationTeam.getAsJsonArray("membersTeam").add(leaderObject);
                        leaderObject.getAsJsonObject("jobEmployee").addProperty("isLeader", false);
                        if (membersArray != null) {

                            destinationTeam.getAsJsonArray("membersTeam").addAll(membersArray);
                        }
                        teamsArray.remove(i);
                        jsonObject.add("Teams", teamsArray);
                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }

                        return;
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLeaderToTeam(Employee newLeader, String idTeam) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    String teamId = teamObject.getAsJsonPrimitive("idTeam").getAsString();

                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(MyStack.class, new StackSerializer())
                            .registerTypeAdapter(MyQueue.class, new QueueSerializer())
                            .create();

                    if (teamId.equals(idTeam)) {
                        if (teamObject.has("leaderTeam")) {
                            JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                            if (leaderObject.has("id")
                                    && leaderObject.getAsJsonPrimitive("id").getAsLong() == newLeader.getId()) {
                                return;
                            }
                        }
                        JsonObject newLeaderObject = gson.toJsonTree(newLeader).getAsJsonObject();
                        teamObject.add("leaderTeam", newLeaderObject);

                        try (FileWriter writer = new FileWriter(filePath)) {
                            writer.write(new Gson().toJson(jsonObject));
                        }
                        return;
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployeeById(long employeeId) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == employeeId) {
                return leader;
            }
            for (Employee member : team.getMembersTeam()) {
                if (member.getId() == employeeId) {
                    return member;
                }
            }
        }
        return null;
    }

    public void addTeam(ArrayList<String> teamData) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            String teamName = teamData.get(0);
            String teamArea = teamData.get(1);
            String teamId = teamData.get(2);
            String leaderId = teamData.get(4);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                if (teamsArray == null) {
                    teamsArray = new JsonArray();
                    jsonObject.add("Teams", teamsArray);
                }
                JsonObject newTeam = new JsonObject();
                newTeam.addProperty("nameTeam", teamName);
                newTeam.addProperty("areaTeam", teamArea);
                newTeam.addProperty("idTeam", teamId);

                addLeaderToTeam(jsonObject, newTeam, leaderId);

                teamsArray.add(newTeam);

                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(new Gson().toJson(jsonObject));
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLeaderToTeam(JsonObject jsonObject, JsonObject newTeam, String leaderId) throws IOException {
        if (leaderId != null && !leaderId.isEmpty()) {
            Employee leader = findEmployeeById(Long.parseLong(leaderId));
            if (leader != null) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(MyStack.class, new StackSerializer())
                        .registerTypeAdapter(MyQueue.class, new QueueSerializer())
                        .create();

                JsonObject leaderObject = gson.toJsonTree(leader).getAsJsonObject();
                newTeam.add("leaderTeam", leaderObject);
                leaderObject.getAsJsonObject("jobEmployee").addProperty("isLeader", true);

                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");
                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                    if (membersArray != null) {
                        List<JsonElement> updatedMembers = new ArrayList<>();
                        for (JsonElement memberElement : membersArray) {
                            JsonObject memberObject = memberElement.getAsJsonObject();
                            JsonPrimitive memberIdPrimitive = memberObject.getAsJsonPrimitive("id");
                            if (memberIdPrimitive != null && memberIdPrimitive.isNumber()) {
                                long memberId = memberIdPrimitive.getAsLong();
                                if (memberId != leader.getId()) {
                                    updatedMembers.add(memberObject);
                                }
                            }
                        }
                        teamObject.add("membersTeam", gson.toJsonTree(updatedMembers).getAsJsonArray());
                    }
                }
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(gson.toJson(jsonObject));
                }
            } else {
                System.err.println("Leader not found: " + leaderId);
            }
        }
    }

    public boolean isLeaderInAnyTeam(String leaderId) {
        for (Team team : teams) {
            Employee leader = team.getLeaderTeam();
            if (leader != null && leader.getId() == Long.parseLong(leaderId)) {
                return true;
            }
        }
        return false;
    }

    public void addRequest(Request request) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                        if (leaderId == Long.parseLong(request.getIdEmployeeFrom())) {
                            if (!leaderObject.has("requestsEmployee")) {
                                leaderObject.add("requestsEmployee", new JsonArray());
                            }
                            JsonArray requestsArray = leaderObject.getAsJsonArray("requestsEmployee");
                            requestsArray.add(new Gson().toJsonTree(request));
                            try (FileWriter writer = new FileWriter(filePath)) {
                                writer.write(new Gson().toJson(jsonObject));
                            }
                            return;
                        }
                    }
                    if (teamObject.has("membersTeam")) {
                        JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                        for (JsonElement memberElement : membersArray) {
                            JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                            JsonObject memberObject = memberElement.getAsJsonObject();
                            long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                            if (memberId == Long.parseLong(request.getIdEmployeeFrom())) {
                                if (!leaderObject.has("requestsEmployee")) {
                                    leaderObject.add("requestsEmployee", new JsonArray());
                                }
                                JsonArray requestsArray = leaderObject.getAsJsonArray("requestsEmployee");
                                requestsArray.add(new Gson().toJsonTree(request));
                                try (FileWriter writer = new FileWriter(filePath)) {
                                    writer.write(new Gson().toJson(jsonObject));
                                }
                                return;
                            }
                        }
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNotificationToLeader(Notification notification) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                        if (leaderId == Long.parseLong(notification.getIdEmployeeFrom())) {
                            if (!leaderObject.has("notificationsEmployee")) {
                                leaderObject.add("notificationsEmployee", new JsonArray());
                            }
                            JsonArray notificationsArray = leaderObject.getAsJsonArray("notificationsEmployee");
                            notificationsArray.add(new Gson().toJsonTree(notification));
                            try (FileWriter writer = new FileWriter(filePath)) {
                                writer.write(new Gson().toJson(jsonObject));
                            }
                            return;
                        } else {
                            if (teamObject.has("membersTeam")) {
                                JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                                for (JsonElement memberElement : membersArray) {
                                    JsonObject memberObject = memberElement.getAsJsonObject();
                                    long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                                    if (memberId == Long.parseLong(notification.getIdEmployeeFrom())) {
                                        if (!leaderObject.has("notificationsEmployee")) {
                                            leaderObject.add("notificationsEmployee", new JsonArray());
                                        }
                                        JsonArray notificationsArray = leaderObject
                                                .getAsJsonArray("notificationsEmployee");
                                        notificationsArray.add(new Gson().toJsonTree(notification));
                                        try (FileWriter writer = new FileWriter(filePath)) {
                                            writer.write(new Gson().toJson(jsonObject));
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                throw new JsonSyntaxException("Expected JSON object");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void addNotificationToEmployee(Notification notification, String idEmployeeTo) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                        if (leaderId == Long.parseLong(idEmployeeTo)) {
                            if (!leaderObject.has("notificationsEmployee")) {
                                leaderObject.add("notificationsEmployee", new JsonArray());
                            }
                            JsonArray notificationsArray = leaderObject.getAsJsonArray("notificationsEmployee");
                            notificationsArray.add(new Gson().toJsonTree(notification));
                            try (FileWriter writer = new FileWriter(filePath)) {
                                writer.write(new Gson().toJson(jsonObject));
                            }
                            return;
                        } else {
                            if (teamObject.has("membersTeam")) {
                                JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                                for (JsonElement memberElement : membersArray) {
                                    JsonObject memberObject = memberElement.getAsJsonObject();
                                    long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                                    if (memberId == Long.parseLong(idEmployeeTo)) {
                                        if (!memberObject.has("notificationsEmployee")) {
                                            memberObject.add("notificationsEmployee", new JsonArray());
                                        }
                                        JsonArray notificationsArray = memberObject
                                                .getAsJsonArray("notificationsEmployee");
                                        notificationsArray.add(new Gson().toJsonTree(notification));
                                        try (FileWriter writer = new FileWriter(filePath)) {
                                            writer.write(new Gson().toJson(jsonObject));
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                throw new JsonSyntaxException("Expected JSON object");

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public boolean deleteNotification(String idEmployee) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                        if (leaderId == Long.parseLong(idEmployee)) {
                            if (leaderObject.has("notificationsEmployee")) {
                                JsonArray notificationsArray = leaderObject.getAsJsonArray("notificationsEmployee");
                                if (notificationsArray.size() > 0) {
                                    notificationsArray.remove(notificationsArray.size() - 1);
                                }
                                try (FileWriter writer = new FileWriter(filePath)) {
                                    writer.write(new Gson().toJson(jsonObject));
                                }
                                return true;
                            }
                        } else {
                            if (teamObject.has("membersTeam")) {
                                JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                                for (JsonElement memberElement : membersArray) {
                                    JsonObject memberObject = memberElement.getAsJsonObject();
                                    long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                                    if (memberId == Long.parseLong(idEmployee)) {
                                        if (memberObject.has("notificationsEmployee")) {
                                            JsonArray notificationsArray = memberObject
                                                    .getAsJsonArray("notificationsEmployee");
                                            if (notificationsArray.size() > 0) {
                                                notificationsArray.remove(notificationsArray.size() - 1);
                                            }
                                            try (FileWriter writer = new FileWriter(filePath)) {
                                                writer.write(new Gson().toJson(jsonObject));
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                throw new JsonSyntaxException("Expected JSON object");

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    public void deleteRequest(String employeeId) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray teamsArray = jsonObject.getAsJsonArray("Teams");

                for (JsonElement teamElement : teamsArray) {
                    JsonObject teamObject = teamElement.getAsJsonObject();
                    if (teamObject.has("leaderTeam")) {
                        JsonObject leaderObject = teamObject.getAsJsonObject("leaderTeam");
                        long leaderId = leaderObject.getAsJsonPrimitive("id").getAsLong();
                        if (leaderId == Long.parseLong(employeeId)) {
                            if (leaderObject.has("requestsEmployee")) {
                                JsonArray requestsArray = leaderObject.getAsJsonArray("requestsEmployee");
                                if (requestsArray.size() > 0) {
                                    requestsArray.remove(0);
                                }
                                try (FileWriter writer = new FileWriter(filePath)) {
                                    writer.write(new Gson().toJson(jsonObject));
                                }
                                return;
                            }
                        } else {
                            if (teamObject.has("membersTeam")) {
                                JsonArray membersArray = teamObject.getAsJsonArray("membersTeam");
                                for (JsonElement memberElement : membersArray) {
                                    JsonObject memberObject = memberElement.getAsJsonObject();
                                    long memberId = memberObject.getAsJsonPrimitive("id").getAsLong();
                                    if (memberId == Long.parseLong(employeeId)) {
                                        if (memberObject.has("requestsEmployee")) {
                                            JsonArray requestsArray = memberObject.getAsJsonArray("requestsEmployee");
                                            if (requestsArray.size() > 0) {
                                                requestsArray.remove(0);
                                            }
                                            try (FileWriter writer = new FileWriter(filePath)) {
                                                writer.write(new Gson().toJson(jsonObject));
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                throw new JsonSyntaxException("Expected JSON object");

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
