package lesson49;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerUsername(String username);

    List<Task> findByOwnerUsernameAndDoneTrue(String username);

    List<Task> findByOwnerUsernameAndDoneFalse(String username);

    Optional<Task> findByIdAndOwnerUsername(Long id, String username);
}
