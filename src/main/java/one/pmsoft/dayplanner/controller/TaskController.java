package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RepositoryRestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class); //Tworzenie logow z klasy TaskController
    private final TaskRepository repository;

    TaskController(final TaskRepository repository){
        this.repository = repository;
    }

    //W ponizszy kontroler wchodzimy tylko wtedy, gdy w zadaniu nie ma ponizszych parametrow
    @GetMapping(value = "/tasks", params = {"!sort","!page","!size"})
    //Ponizej zapis rownowazny wynikajacy z hierarchii
    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    //Reprezentuje odpowiedz
    ResponseEntity<?> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }
}
