package kg.bektur.todoapp.controller.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.bektur.todoapp.dto.TaskCreateDto;
import kg.bektur.todoapp.dto.TaskDto;
import kg.bektur.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task API to get all tasks and create task")
public class TasksController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TaskDto.class))
            )
    })
    public List<TaskDto> findTasks(Authentication currentUser) {
        return taskService.findAll(currentUser);
    }

    @SneakyThrows
    @PostMapping
    @Operation(summary = "Create new task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created task",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreateDto taskCreateDto, Authentication currentUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }

        taskService.create(taskCreateDto, currentUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Successfully created task");
    }
}



