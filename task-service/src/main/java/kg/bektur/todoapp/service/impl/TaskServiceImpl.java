package kg.bektur.todoapp.service.impl;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.entity.Task;
import kg.bektur.todoapp.enumuration.TaskStatus;
import kg.bektur.todoapp.exception.TaskNotFoundException;
import kg.bektur.todoapp.mapper.TaskMapper;
import kg.bektur.todoapp.repository.TaskRepository;
import kg.bektur.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Override
    public List<TaskDto> findAll(Authentication authentication) {
        return taskRepository.findAllByUserId(UUID.fromString(authentication.getName())).stream()
                .map(taskMapper::taskToTaskDto)
                .toList();
    }

    @Override
    public TaskDto find(Long id, Authentication authentication) {
        return taskRepository.findByIdAndUserId(id, UUID.fromString(authentication.getName()))
                .map(taskMapper::taskToTaskDto)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public TaskDto create(TaskCreateDto toDoCreateDto, Authentication authentication) {
        Task task = taskMapper.taskCreateDtoToTask(toDoCreateDto);
        task.setStatus(TaskStatus.NOT_DONE);
        task.setUserId(UUID.fromString(authentication.getName()));
        Task save = taskRepository.save(task);
        return taskMapper.taskToTaskDto(save);
    }

    @Override
    public TaskDto update(TaskCreateDto taskDto, Long id, Authentication authentication) {
        Task task = taskRepository.findByIdAndUserId(id, UUID.fromString(authentication.getName())).orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        return taskMapper.taskToTaskDto(taskRepository.save(task));
    }

    @Override
    public void deleteById(Long id, Authentication authentication) {
        taskRepository.softDelete(id, UUID.fromString(authentication.getName()));
    }

    @Override
    public TaskDto doneTask(Long id, Authentication authentication) {
        Task task = taskRepository.findByIdAndUserId(id, UUID.fromString(authentication.getName())).orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(TaskStatus.DONE);
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }
}
