package lesson40;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;

    // Spring injects TaskRepository automatically (no "new TaskRepository()")
    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getAll() {
        return repo.findAll();
    }

    @GetMapping("/done")
    public List<Task> getDone() {
        return repo.findDone();
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task incoming) {
        return repo.insert(incoming.getTitle(), incoming.isDone());
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable int id, @RequestBody Task incoming) {
        boolean ok = repo.update(id, incoming.getTitle(), incoming.isDone());
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return getOne(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        boolean ok = repo.delete(id);
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }
}
