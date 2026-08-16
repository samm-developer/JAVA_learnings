package lesson50;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByOwnerUsername(String username, Pageable pageable);

    List<Task> findByOwnerUsernameAndDoneTrue(String username);

    List<Task> findByOwnerUsernameAndDoneFalse(String username);

    Optional<Task> findByIdAndOwnerUsername(Long id, String username);
}
