package one.pmsoft.dayplanner;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
  //Podejscie uproszczone, bez sensownej hierarchi
  //  private boolean allowMultipleTasksFromTemplate;
  //
  //  public boolean isAllowMultipleTasksFromTemplate() {
  //      return allowMultipleTasksFromTemplate;
  //  }
  //
  //  public void setAllowMultipleTasksFromTemplate(boolean allowMultipleTasksFromTemplate) {
  //      this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
  //  }

    //Podejscie z zastosowaniem sensownej hierarchii
    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public static class Template{
       private boolean allowMultipleTasks;

        public boolean isAllowMultipleTasks() {
            return allowMultipleTasks;
        }

        public void setAllowMultipleTasks(final boolean allowMultipleTasks) {
            this.allowMultipleTasks = allowMultipleTasks;
        }
    }
}
