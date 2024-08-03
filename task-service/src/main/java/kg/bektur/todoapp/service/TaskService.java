package kg.bektur.todoapp.service;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import org.springframework.security.core.Authentication;

public interface TaskService extends DefaultService<TaskDto, TaskCreateDto> {

    TaskDto doneTask(Long id, Authentication authentication);
}
