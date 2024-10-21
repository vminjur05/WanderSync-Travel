import java.util.logging.Logger;

public class ProjectManager extends TeamMember {
    public ProjectManager(String name, String email) {
        super(name, email);
    }

    public void overseeProject(Project project) {
        LOGGER.info(getName() + " is overseeing the project: " + project.getName());
    }
}
