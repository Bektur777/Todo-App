package kg.bektur.todoapp.mapper;

import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    TaskDto taskToTaskDto(Task task);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    Task taskCreateDtoToTask(TaskCreateDto taskCreateDto);
}
