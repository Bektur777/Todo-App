package kg.bektur.todoapp.service;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.entity.Task;
import kg.bektur.todoapp.enumuration.TaskStatus;
import kg.bektur.todoapp.exception.TaskNotFoundException;
import kg.bektur.todoapp.mapper.TaskMapper;
import kg.bektur.todoapp.repository.TaskRepository;
import kg.bektur.todoapp.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @InjectMocks
    TaskServiceImpl taskService;

    Task task;

    TaskDto taskDto;

    @BeforeEach
    void initData() {
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

    @Test
    void findTasks_CheckIfTasksExists_ReturnsListOfTaskDto() {
        List<Task> tasks = Collections.singletonList(task);
        List<TaskDto> taskDtos = Collections.singletonList(taskDto);

        // when
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.findAll();

        // then
        assertThat(result).isEqualTo(taskDtos);
    }

    @Test
    void findTask_CheckIfTaskExists_ReturnsTaskDto() {
        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.find(1L);

        // then
        assertThat(taskDto).isEqualTo(result);
    }

    @Test
    void findTask_CheckIfTaskDoesntExist_Returns_TaskNotFoundException() {
        // given
        Long id = 1L;

        // when
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.find(id));
    }

    @Test
    void makeTaskDone_CheckIfTaskIsDone_ReturnsTaskDto() {
        // given
        Long id = 1L;

        taskDto.setStatus(TaskStatus.DONE);

        // when
        when(taskRepository.findById(id)).thenReturn(Optional.ofNullable(task));
        assert task != null;
        task.setStatus(TaskStatus.DONE);
        when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.doneTask(id);

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

        TaskDto result = taskService.create(taskCreateDto);

        // then
        assertThat(result).isEqualTo(taskDto);
    }

    @Test
    void deleteTaskById_ShouldCallSoftDeleteInRepository() {
        // given
        Long taskId = 1L;

        // when
        taskService.deleteById(taskId);

        // then
        verify(taskRepository).softDelete(taskId);
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
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        assert task != null;
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        when(taskMapper.taskToTaskDto(Mockito.any())).thenReturn(this.taskDto);

        TaskDto result = taskService.update(taskDto, taskId);

        // then
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        assertThat(result).isEqualTo(this.taskDto);
    }
}
