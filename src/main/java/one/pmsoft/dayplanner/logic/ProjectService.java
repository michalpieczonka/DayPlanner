package one.pmsoft.dayplanner.logic;

import one.pmsoft.dayplanner.TaskConfigurationProperties;
import one.pmsoft.dayplanner.model.*;
import one.pmsoft.dayplanner.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project save(final Project toSave){
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId){
        if(!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("Only one undone group from project is allowed !");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                        project.getSteps().stream()
                                .map(projectStep ->  new Task(
                                        projectStep.getDescription(),
                                        deadline.plusDays(projectStep.getDaysToDeadline()))
                                ).collect(Collectors.toSet())
                       );
                    targetGroup.setProject(project);
                return taskGroupRepository.save(targetGroup);
                }).orElseThrow(()-> new IllegalArgumentException("Project with given ID not found !"));

    return new GroupReadModel(result);
    }


}
