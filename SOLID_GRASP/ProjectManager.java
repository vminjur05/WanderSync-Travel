import java.util.logging.Logger;

private static final Logger LOGGER = Logger.getLogger(ProjectManager.class.getName());

public class ProjectManager extends TeamMember {
    public ProjectManager(String name, String email) {
        super(name, email);
    }

    public void overseeProject(Project project) {
        LOGGER.info(getName() + " is overseeing the project: " + project.getName());
    }
}
