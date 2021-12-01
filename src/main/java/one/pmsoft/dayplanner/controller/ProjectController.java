package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.logic.ProjectService;
import one.pmsoft.dayplanner.model.Project;
import one.pmsoft.dayplanner.model.ProjectStep;
import one.pmsoft.dayplanner.model.projection.ProjectWriteModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService pService;

    public ProjectController(ProjectService pService) {
        this.pService = pService;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project",new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model){
        if(bindingResult.hasErrors()){
            return"projects";
        }
        pService.save(current);
        model.addAttribute("project",new ProjectWriteModel());
        model.addAttribute("projects",getProjects());
        model.addAttribute("message", "Nowy projekt zostal dodany !");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline){
        try{
            pService.createGroup(deadline,id);
            model.addAttribute("message","Dodano grupę zadań !");
        } catch(IllegalStateException | IllegalArgumentException e){
            model.addAttribute("message","Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return pService.readAll();
    }
}
