package kg.bektur.todoapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCreateDto {

    @Size(min = 5, max = 50, message = "Title length should be between 5 and 50")
    private String title;

    @NotNull(message = "Description should not be empty")
    private String description;
}
