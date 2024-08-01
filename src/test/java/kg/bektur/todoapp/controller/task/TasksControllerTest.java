package kg.bektur.todoapp.controller.task;

import com.google.gson.Gson;
import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.enumuration.TaskStatus;
import kg.bektur.todoapp.service.TaskService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TasksControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @Autowired
    Gson gson;

    @SneakyThrows
    @Test
    void createTask_IfRequestIsValid_ReturnSuccessMessage() {
        // given
        TaskCreateDto createDto = TaskCreateDto.builder()
                .title("New title")
                .description("New description")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(createDto));

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().string("Successfully created task")
                );
    }

    @SneakyThrows
    @Test
    void findAllTasks_ReturnsListOfTasks() {
        // given
        List<TaskDto> tasks = List.of(
                TaskDto.builder()
                        .id(1L)
                        .title("New title1")
                        .description("New description1")
                        .status(TaskStatus.NOT_DONE)
                        .build(),
                TaskDto.builder()
                        .id(2L)
                        .title("New title2")
                        .description("New description2")
                        .status(TaskStatus.NOT_DONE)
                        .build()
        );
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/tasks");

        // when
        when(taskService.findAll(Mockito.any())).thenReturn(tasks);
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                  {"id": 1, "title": "New title1", "description": "New description1", "status": "NOT_DONE"},
                                  {"id": 2, "title": "New title2", "description": "New description2", "status": "NOT_DONE"}
                                ]
                                """
                        )
                );
    }
}
