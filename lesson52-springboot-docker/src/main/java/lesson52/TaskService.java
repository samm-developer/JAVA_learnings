package lesson52;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Business logic lives here (not in the controller).
 * Controller → Service → Repository
 */
@Service
public class TaskService {

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
        return repo.save(new Task(title, done, currentUser()));
    }

    public Task update(Long id, String title, boolean done) {
        Task existing = getOwned(id);
        existing.setTitle(title);
        existing.setDone(done);
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.delete(getOwned(id));
    }
}
