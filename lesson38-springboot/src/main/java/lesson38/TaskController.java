package lesson38;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public TaskController() {
        // starter data
        tasks.add(new Task(nextId.getAndIncrement(), "Learn Spring Boot", true));  // done
        tasks.add(new Task(nextId.getAndIncrement(), "Build a REST API", false));
    }

    // GET http://localhost:8080/api/tasks
    @GetMapping
    public List<Task> getAll() {
        return tasks;
    }

    // Fixed path BEFORE /{id} (best practice)
    // GET http://localhost:8080/api/tasks/done
    @GetMapping("/done")
    public List<Task> getDone() {
        return tasks.stream()
                .filter(Task::isDone)
                .toList();
    }

    // GET http://localhost:8080/api/tasks/1
    @GetMapping("/{id}")
    public Task getOne(@PathVariable int id) {
        return find(id);
    }

    // POST http://localhost:8080/api/tasks
    // Body: { "title": "New task", "done": false }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task incoming) {
        Task created = new Task(nextId.getAndIncrement(), incoming.getTitle(), incoming.isDone());
        tasks.add(created);
        return created;
    }

    // PUT http://localhost:8080/api/tasks/1
    @PutMapping("/{id}")
    public Task update(@PathVariable int id, @RequestBody Task incoming) {
        Task existing = find(id);
        existing.setTitle(incoming.getTitle());
        existing.setDone(incoming.isDone());
        return existing;
    }

    // DELETE http://localhost:8080/api/tasks/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        Task existing = find(id);
        tasks.remove(existing);
    }

    private Task find(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
