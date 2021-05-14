package com.company.controller;

import com.company.model.Task;
import com.company.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskRepository repo;

    @Test
    void http_returnsGivenTask() throws Exception {

        // when
        when(repo.findById(anyInt()))
                .thenReturn(java.util.Optional.of(new Task("foo", LocalDateTime.now())));

        // when + then
        mockMvc.perform(get("/tasks/" + 123))
                .andDo(print())
                .andExpect(content().string(containsString("\"description\":\"foo\"")));
    }


}
