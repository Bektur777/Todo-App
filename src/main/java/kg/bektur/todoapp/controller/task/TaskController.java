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
import kg.bektur.todoapp.exception.TaskNotFoundException;
import kg.bektur.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{taskId}")
@Tag(name = "Task Management", description = "Task API to work with task")
public class TaskController {

    private final TaskService taskService;

    @ModelAttribute("task")
    public TaskDto getTask(@PathVariable("taskId") Long taskId, Authentication authentication) {
        return taskService.find(taskId, authentication);
    }

    @GetMapping
    @Operation(summary = "Get task by ID", description = "Find a task by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public TaskDto findTask(@PathVariable("taskId") Long taskId, Authentication authentication) {
        return taskService.find(taskId, authentication);
    }

    @SneakyThrows
    @PatchMapping
    @Operation(summary = "Update task", description = "Updates the details of an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<?> updateTask(@Valid @RequestBody TaskCreateDto taskDto,
                                        @PathVariable("taskId") Long taskId,
                                        BindingResult bindingResult,
                                        Authentication authentication) {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception)
                throw exception;
            else
                throw new BindException(bindingResult);
        }

        taskService.update(taskDto, taskId, authentication);

        return ResponseEntity.ok("Task updated successfully");
    }

    @PatchMapping("done")
    @Operation(summary = "Mark task as done", description = "Marks the task as completed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task marked as done",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TaskDto> doneTask(@PathVariable("taskId") Long taskId, Authentication authentication) {
        TaskDto taskDto = taskService.doneTask(taskId, authentication);

        return ResponseEntity
                .ok()
                .body(taskDto);
    }

    @DeleteMapping
    @Operation(summary = "Delete task by id", description = "Deletes an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId, Authentication authentication) {
        taskService.deleteById(taskId, authentication);

        return ResponseEntity.ok("Task deleted successfully");
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND, e.getMessage())
                );
    }
}