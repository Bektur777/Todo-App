package kg.bektur.todoapp.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with this id = " + id);
    }
}
