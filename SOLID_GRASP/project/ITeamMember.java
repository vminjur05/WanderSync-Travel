package project;

public interface ITeamMember {
    String getName();
    String getEmail();
    void joinProject(Project project);
    void leaveProject(Project project);
}
