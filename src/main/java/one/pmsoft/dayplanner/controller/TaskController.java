package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.logic.TaskService;
import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class); //Tworzenie logow z klasy TaskController
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(@Qualifier("sqlTaskRepository") final TaskRepository repository, TaskService service){
        this.repository = repository;
        this.service = service;
    }

    //Dla post zwracamy zwykle 201
    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task newTask){
        logger.info("New task has been received");
        Task result = repository.save(newTask);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }


    //W ponizszy kontroler wchodzimy tylko wtedy, gdy w zadaniu nie ma ponizszych parametrow
    @GetMapping(params = {"!sort","!page","!size"})
    //Ponizej zapis rownowazny wynikajacy z hierarchii
    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    //Reprezentuje odpowiedz
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable.");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping(value = "/{id}")
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

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }


    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate){
        logger.info("Task to update has been received.");
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task->{
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        logger.info("Task to update has been received.");
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task->task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }



}
