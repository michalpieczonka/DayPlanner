package one.pmsoft.dayplanner.logic;

import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TempService {
    @Autowired
    List<String> temp(TaskGroupRepository repository){
        //FIXME: N+1
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream()
                .map(Task::getDescription)
        ).collect(Collectors.toList());
    }
}
