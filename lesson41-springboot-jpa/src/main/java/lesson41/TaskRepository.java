package lesson41;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring implements this interface for you — no SQL needed for basic CRUD
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDoneTrue();
}
