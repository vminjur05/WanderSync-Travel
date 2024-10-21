import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<ITask> tasks;
    private List<ITeamMember> teamMembers;

    public Project(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public void addTask(ITask task) {
        tasks.add(task);
    }

    public void removeTask(ITask task) {
        tasks.remove(task);
    }

    public void addTeamMember(ITeamMember member) {
        teamMembers.add(member);
    }

    public void removeTeamMember(ITeamMember member) {
        teamMembers.remove(member);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public List<ITask> getTasks() {
        return tasks;
    }

    public List<ITeamMember> getTeamMembers() {
        return teamMembers;
    }
}
