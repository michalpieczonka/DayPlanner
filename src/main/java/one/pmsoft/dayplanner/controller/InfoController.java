package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSource,final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/info/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
