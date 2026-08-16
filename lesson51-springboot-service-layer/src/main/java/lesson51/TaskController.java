package lesson51;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Thin controller: HTTP only. Business rules are in TaskService.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Page<Task> getAll(Pageable pageable) {
        return taskService.list(pageable);
    }

    @GetMapping("/done")
    public java.util.List<Task> getDone() {
        return taskService.listDone();
    }

    @GetMapping("/todo")
    public java.util.List<Task> getTodo() {
        return taskService.listTodo();
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest request) {
        boolean done = Boolean.TRUE.equals(request.getDone());
        return taskService.create(request.getTitle(), done);
    }

    @PutMapping("/{id}")
    public Task update(
            @PathVariable Long id,
            @Validated({Default.class, OnUpdate.class}) @RequestBody TaskRequest request) {
        return taskService.update(id, request.getTitle(), request.getDone());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
