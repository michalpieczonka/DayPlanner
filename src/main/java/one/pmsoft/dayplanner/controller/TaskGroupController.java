package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.logic.TaskGroupService;
import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskGroup;
import one.pmsoft.dayplanner.model.TaskGroupRepository;
import one.pmsoft.dayplanner.model.TaskRepository;
import one.pmsoft.dayplanner.model.projection.GroupReadModel;
import one.pmsoft.dayplanner.model.projection.GroupWriteModel;
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

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository repository;
    private final TaskGroupService service;

    public TaskGroupController(TaskRepository repository, TaskGroupService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel newTaskGroup){
        logger.info("New task group has been received");
        GroupReadModel result = service.createGroup(newTaskGroup);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/id{}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
