package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class); //Tworzenie logow z klasy TaskController
    private final TaskRepository repository;

    TaskController(final TaskRepository repository){
        this.repository = repository;
    }

    //Dla post zwracamy zwykle 201
    @PostMapping(path = "/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task newTask){
        logger.info("New task has been received");
        Task result = repository.save(newTask);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }


    //W ponizszy kontroler wchodzimy tylko wtedy, gdy w zadaniu nie ma ponizszych parametrow
    @GetMapping(value = "/tasks", params = {"!sort","!page","!size"})
    //Ponizej zapis rownowazny wynikajacy z hierarchii
    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    //Reprezentuje odpowiedz
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable.");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping(value = "/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        //Zapis rownowazny
      // if(!repository.existsById(id)){
      //     return ResponseEntity.notFound().build();
      // }
      // return ResponseEntity.ok(repository.findById(id).get());
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    //Dla put zwracamy zwykle 204
    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate){
        logger.info("Task to update has been received.");
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }



}
