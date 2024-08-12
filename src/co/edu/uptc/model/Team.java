package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String nameTeam;
    private String areaTeam;
    private String idTeam;
    private LeaderTeam leaderTeam;
    private List<Employee> membersTeam;

    public Team() {
        this.membersTeam = new ArrayList<>();
        this.leaderTeam = new LeaderTeam();
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public LeaderTeam getLeaderTeam() {
        return leaderTeam;
    }

    public void setLeaderTeam(LeaderTeam leaderTeam) {
        this.leaderTeam = leaderTeam;
    }

    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getAreaTeam() {
        return areaTeam;
    }

    public void setAreaTeam(String areaTeam) {
        this.areaTeam = areaTeam;
    }

    public List<Employee> getMembersTeam() {
        return membersTeam;
    }

    public void setMembersTeam(List<Employee> membersTeam) {
        this.membersTeam = membersTeam;
    }

    public void addMemberTeam(Employee employee) {
        membersTeam.add(employee);
    }

    public void removeMemberTeam(Employee employee) {
        membersTeam.remove(employee);
    }

    public boolean searchMemberTeam(Employee employee) {
        return membersTeam.contains(employee);
    }

}
