public class ProjectManager extends TeamMember {
    public ProjectManager(String name, String email) {
        super(name, email);
    }

    public void overseeProject(Project project) {
        System.out.println(getName() + " is overseeing the project: " + project.getName());
    }
}
