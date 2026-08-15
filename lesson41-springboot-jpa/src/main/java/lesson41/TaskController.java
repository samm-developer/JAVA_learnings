package lesson41;

import org.springframework.http.HttpStatus;
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
        return repo.findByDoneTrue(); // method name → Spring writes the query
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) { 
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task incoming) {
        incoming.setId(null); // let DB generate id
        return repo.save(incoming);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task incoming) {
        Task existing = getOne(id);
        existing.setTitle(incoming.getTitle());
        existing.setDone(incoming.isDone());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repo.deleteById(id);
    }
}
