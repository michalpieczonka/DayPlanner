package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.logic.ProjectService;
import one.pmsoft.dayplanner.model.Project;
import one.pmsoft.dayplanner.model.ProjectStep;
import one.pmsoft.dayplanner.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model){
        pService.save(current);
        model.addAttribute("project",new ProjectWriteModel());
        model.addAttribute("message", "Nowy projekt zostal dodany !");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return pService.readAll();
    }
}
