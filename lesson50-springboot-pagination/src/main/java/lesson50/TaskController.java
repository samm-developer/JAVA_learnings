package lesson50;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;
    private final UserRepository userRepository;

    public TaskController(TaskRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return auth.getName();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private AppUser currentUser() {
        return userRepository.findByUsername(currentUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Task getOwned(Long id) {
        if (isAdmin()) {
            return repo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        }
        return repo.findByIdAndOwnerUsername(id, currentUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    /**
     * Paginated list.
     * Query params (Spring binds them into Pageable automatically):
     *   ?page=0&size=5&sort=title,asc
     */
    @GetMapping
    public Page<Task> getAll(Pageable pageable) {
        if (isAdmin()) {
            return repo.findAll(pageable);
        }
        return repo.findByOwnerUsername(currentUsername(), pageable);
    }

    @GetMapping("/done")
    public List<Task> getDone() {
        if (isAdmin()) {
            return repo.findAll().stream().filter(Task::isDone).toList();
        }
        return repo.findByOwnerUsernameAndDoneTrue(currentUsername());
    }

    @GetMapping("/todo")
    public List<Task> getTodo() {
        if (isAdmin()) {
            return repo.findAll().stream().filter(t -> !t.isDone()).toList();
        }
        return repo.findByOwnerUsernameAndDoneFalse(currentUsername());
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) {
        return getOwned(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest request) {
        boolean done = Boolean.TRUE.equals(request.getDone());
        AppUser owner = currentUser();
        return repo.save(new Task(request.getTitle(), done, owner));
    }

    @PutMapping("/{id}")
    public Task update(
            @PathVariable Long id,
            @Validated({Default.class, OnUpdate.class}) @RequestBody TaskRequest request) {
        Task existing = getOwned(id);
        existing.setTitle(request.getTitle());
        existing.setDone(request.getDone());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Task existing = getOwned(id);
        repo.delete(existing);
    }
}
