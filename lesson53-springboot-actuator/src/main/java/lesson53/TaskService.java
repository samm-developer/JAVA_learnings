package lesson53;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Business logic + proper logging (never System.out in real apps).
 * <p>
 * SLF4J is the API; Logback is the implementation Spring Boot uses by default.
 * Use placeholders {} — strings are built only if that level is enabled.
 */
@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repo;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repo, UserRepository userRepository) {
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

    public Page<Task> list(Pageable pageable) {
        log.debug("Listing tasks for user={} admin={} page={}", currentUsername(), isAdmin(), pageable.getPageNumber());
        if (isAdmin()) {
            return repo.findAll(pageable);
        }
        return repo.findByOwnerUsername(currentUsername(), pageable);
    }

    public List<Task> listDone() {
        if (isAdmin()) {
            return repo.findAll().stream().filter(Task::isDone).toList();
        }
        return repo.findByOwnerUsernameAndDoneTrue(currentUsername());
    }

    public List<Task> listTodo() {
        if (isAdmin()) {
            return repo.findAll().stream().filter(t -> !t.isDone()).toList();
        }
        return repo.findByOwnerUsernameAndDoneFalse(currentUsername());
    }

    public Task getById(Long id) {
        return getOwned(id);
    }

    public Task create(String title, boolean done) {
        Task saved = repo.save(new Task(title, done, currentUser()));
        log.info("Created task id={} title='{}' by={}", saved.getId(), saved.getTitle(), currentUsername());
        return saved;
    }

    public Task update(Long id, String title, boolean done) {
        Task existing = getOwned(id);
        existing.setTitle(title);
        existing.setDone(done);
        Task saved = repo.save(existing);
        log.info("Updated task id={} by={}", saved.getId(), currentUsername());
        return saved;
    }

    public void delete(Long id) {
        Task existing = getOwned(id);
        repo.delete(existing);
        log.warn("Deleted task id={} by={}", id, currentUsername());
    }
}
