package kg.bektur.todoapp.controller.task;

import com.google.gson.Gson;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.enumuration.TaskStatus;
import kg.bektur.todoapp.service.TaskService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WithMockUser(username = "username", authorities = {"SCOPE_task_edit"})
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @Autowired
    Gson gson;

    TaskDto taskDto;

    @BeforeEach
    void initData() {
        taskDto = TaskDto.builder()
                .id(1L)
                .title("New title")
                .description("New description")
                .status(TaskStatus.NOT_DONE)
                .build();
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "username", authorities = {"SCOPE_task_view"})
    void findTaskById_IfTaskExist_ReturnTaskDto() {
        // given
        long taskId = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/tasks/{taskId}", taskId);

        // when
        when(taskService.find(eq(taskId), Mockito.any())).thenReturn(taskDto);
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().string(gson.toJson(taskDto))
                );
    }

    @SneakyThrows
    @Test
    void doneTaskById_IfTaskExist_UpdateToDone_And_ReturnTaskDto() {
        // given
        long taskId = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/tasks/{taskId}/done", taskId);

        // when
        when(taskService.doneTask(eq(taskId), Mockito.any())).thenReturn(taskDto);
        taskDto.setStatus(TaskStatus.DONE);
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().string(gson.toJson(taskDto))
                );
    }

    @SneakyThrows
    @Test
    void deleteTaskById_IfTaskToDeleteExist_ReturnSuccessMessage() {
        // given
        Long taskId = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/tasks/{taskId}", taskId);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("Task deleted successfully")
                );
    }

    @SneakyThrows
    @Test
    void updateTaskById_IfTaskIsValid_ReturnSuccessMessage() {
        // given
        long taskId = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(taskDto));

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("Task updated successfully")
                );
    }

    @SneakyThrows
    @Test
    void updateTaskById_IfTaskDoesNotValid_ReturnInvalidMessage() {
        // given
        long taskId = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {"id": 1, "title": " ", "description": " ", "status":  "NOT_DONE"}
                        """);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "errors": [
                                        "Title length should be between 5 and 50"
                                    ]
                                }
                        """)
                );
    }
}
