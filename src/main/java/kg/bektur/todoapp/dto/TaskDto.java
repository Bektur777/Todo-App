package kg.bektur.todoapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kg.bektur.todoapp.enumuration.TaskStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;
}
