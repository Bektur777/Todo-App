package kg.bektur.todoapp.service;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.entity.Task;
import kg.bektur.todoapp.enumuration.TaskStatus;
import kg.bektur.todoapp.exception.TaskNotFoundException;
import kg.bektur.todoapp.mapper.TaskMapper;
import kg.bektur.todoapp.repository.TaskRepository;
import kg.bektur.todoapp.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapper taskMapper;

    @Mock
    Authentication mockAuthentication;

    @Mock
    UUID mockUuid;

    @InjectMocks
    TaskServiceImpl taskService;

    static Task task;

    static TaskDto taskDto;

    @BeforeAll
    static void initData() {
        // given
        task = Task.builder()
                .title("New title")
                .description("New description")
                .build();

        taskDto = TaskDto.builder()
                .title("New title")
                .description("New description")
                .status(TaskStatus.NOT_DONE)
                .build();
    }

    @BeforeEach
    void initUser() {
        mockUuid = UUID.randomUUID();
        when(mockAuthentication.getName()).thenReturn(String.valueOf(mockUuid));
    }

    @Test
    void findTasks_CheckIfTasksExists_ReturnsListOfTaskDto() {
        List<Task> tasks = Collections.singletonList(task);
        List<TaskDto> taskDtos = Collections.singletonList(taskDto);

        // when
        when(taskRepository.findAllByUserId(mockUuid)).thenReturn(tasks);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.findAll(mockAuthentication);

        // then
        assertThat(result).isEqualTo(taskDtos);
    }

    @Test
    void findTask_CheckIfTaskExists_ReturnsTaskDto() {
        // when
        when(taskRepository.findByIdAndUserId(1L, mockUuid)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.find(1L, mockAuthentication);

        // then
        assertThat(taskDto).isEqualTo(result);
    }

    @Test
    void findTask_CheckIfTaskDoesntExist_Returns_TaskNotFoundException() {
        // given
        Long id = 1L;

        // when
        when(taskRepository.findByIdAndUserId(id, mockUuid)).thenReturn(Optional.empty());

        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.find(id, mockAuthentication));
    }

    @Test
    void makeTaskDone_CheckIfTaskIsDone_ReturnsTaskDto() {
        // given
        Long id = 1L;

        taskDto.setStatus(TaskStatus.DONE);

        // when
        when(taskRepository.findByIdAndUserId(id, mockUuid)).thenReturn(Optional.ofNullable(task));
        assert task != null;
        task.setStatus(TaskStatus.DONE);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.doneTask(id, mockAuthentication);

        // then
        assertThat(result).isEqualTo(taskDto);
    }

    @Test
    void createTask_CheckIfTaskIsCreated_ReturnsTaskDto() {
        // given
        TaskCreateDto taskCreateDto = TaskCreateDto.builder()
                .title("New title")
                .description("New description")
                .build();

        // when
        when(taskMapper.taskCreateDtoToTask(taskCreateDto)).thenReturn(task);
        task.setStatus(TaskStatus.NOT_DONE);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.create(taskCreateDto, mockAuthentication);

        // then
        assertThat(result).isEqualTo(taskDto);
    }

    @Test
    void deleteTaskById_ShouldCallSoftDeleteInRepository() {
        // given
        Long taskId = 1L;

        // when
        taskService.deleteById(taskId, mockAuthentication);

        // then
        verify(taskRepository).softDelete(taskId, mockUuid);
    }

    @Test
    void updateTask_ShouldUpdateTask_And_ReturnUpdatedTaskDto() {
        // given
        long taskId = 1L;
        TaskCreateDto taskDto = TaskCreateDto.builder()
                .title("New title")
                .description("New description")
                .build();

        // when
        when(taskRepository.findByIdAndUserId(taskId, mockUuid)).thenReturn(Optional.ofNullable(task));
        assert task != null;
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        when(taskMapper.taskToTaskDto(Mockito.any())).thenReturn(this.taskDto);

        TaskDto result = taskService.update(taskDto, taskId, mockAuthentication);

        // then
        verify(taskRepository).findByIdAndUserId(taskId, mockUuid);
        verify(taskRepository).save(task);
        assertThat(result).isEqualTo(this.taskDto);
    }
}
