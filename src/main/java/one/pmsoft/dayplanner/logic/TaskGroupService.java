package one.pmsoft.dayplanner.logic;

import one.pmsoft.dayplanner.TaskConfigurationProperties;
import one.pmsoft.dayplanner.model.TaskGroup;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import one.pmsoft.dayplanner.model.TaskRepository;
import one.pmsoft.dayplanner.model.projection.GroupReadModel;
import one.pmsoft.dayplanner.model.projection.GroupWriteModel;
import org.hibernate.id.Configurable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {

    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository,  final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    //Tworzenie grupy z WriteModelu
    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    //Czytanie grupy
    public List<GroupReadModel> readAll(){
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    //Zakanczanie grupy (Nie mozna zakonczyc grupy, jesli w grupie znajduej sie jakikolwiek task z done=false)
    public void toggleGroup(int groupId){
       if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
           throw new IllegalStateException("Group has undone tasks. Do all the tasks first !");
       }
       TaskGroup result = repository.findById(groupId).orElseThrow(
               ()-> new IllegalArgumentException("TaskGroup with given ID not found"));
       result.setDone(!result.isDone());
       repository.save(result);
    }
}
