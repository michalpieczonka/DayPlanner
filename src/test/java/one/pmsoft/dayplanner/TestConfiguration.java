package one.pmsoft.dayplanner;

import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Configuration
public class TestConfiguration {
    @Bean
    TaskRepository testRepo(){
        return new TaskRepository(){

            private Map<Integer, Task> tasks = new HashMap<>();


            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(Task entity) {
                return tasks.put(tasks.size()+1, entity);
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public boolean existsById(Integer id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return false;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public List<Task> findAllByGroup_Id(Integer groupID) {
                return List.of();
            }
        };
    }
}
