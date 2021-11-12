package one.pmsoft.dayplanner.controller;

import one.pmsoft.dayplanner.model.Task;
import one.pmsoft.dayplanner.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;
    @Test
    void httpGet_returnsAllTasks(){
        //given
        int initial = repo.findAll().size();
        repo.save(new Task("test", LocalDateTime.now()));
        repo.save(new Task("test2",LocalDateTime.now()));
        //when
        Task[] result = restTemplate.getForObject("http://localhost:"+port+"/tasks", Task[].class);
        //then
        assertThat(result).hasSize(initial + 2);
    }
}