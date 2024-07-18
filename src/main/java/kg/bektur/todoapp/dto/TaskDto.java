package kg.bektur.todoapp.dto;

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
