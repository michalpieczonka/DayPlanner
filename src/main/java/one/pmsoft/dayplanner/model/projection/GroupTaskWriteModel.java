package one.pmsoft.dayplanner.model.projection;

import one.pmsoft.dayplanner.model.Task;

import java.time.LocalDateTime;

//Tworzenie Taska jak powinno wygladac/co potrzebne
public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task toTask(){
       return new Task(description,deadline);
    }
}
