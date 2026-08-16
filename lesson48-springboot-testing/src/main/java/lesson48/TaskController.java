package lesson48;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getAll() {
        return repo.findAll();
    }

    @GetMapping("/done")
    public List<Task> getDone() {
        return repo.findByDoneTrue();
    }

    @GetMapping("/todo")
    public List<Task> getTodo() {
        return repo.findByDoneFalse();
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest request) {
        boolean done = Boolean.TRUE.equals(request.getDone());
        return repo.save(new Task(request.getTitle(), done));
    }

    @PutMapping("/{id}")
    public Task update(
            @PathVariable Long id,
            @Validated({Default.class, OnUpdate.class}) @RequestBody TaskRequest request) {
        Task existing = getOne(id);
        existing.setTitle(request.getTitle());
        existing.setDone(request.getDone());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repo.deleteById(id);
    }
}
