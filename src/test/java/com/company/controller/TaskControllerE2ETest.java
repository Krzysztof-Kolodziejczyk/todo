package com.company.controller;

import com.company.model.Task;
import com.company.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    TaskRepository repo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void http_returnsAllTasks(){
        // given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // then

        assertThat(result).hasSize(initial + 2);
    }

    @Test
    void http_returnTaskById_notExist(){
        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/-1", Task.class);

        // then
        assertNull(result);
    }

    @Test
    void http_returnTaskById_ifPresent(){

        // given
        var task = repo.save(new Task("bar", LocalDateTime.now()));

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + task.getId() , Task.class);

        assertEquals(result.getId(), task.getId());
    }

}