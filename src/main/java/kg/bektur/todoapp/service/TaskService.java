package kg.bektur.todoapp.service;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;

public interface TaskService extends DefaultService<TaskDto, TaskCreateDto> {

    TaskDto doneTask(Long id);
}
