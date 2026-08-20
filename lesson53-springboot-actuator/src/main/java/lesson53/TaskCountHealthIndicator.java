package lesson53;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health check shown under /actuator/health.
 * Example: mark DOWN if the DB is empty (demo only — real apps use disk/db/ping checks).
 */
@Component
public class TaskCountHealthIndicator implements HealthIndicator {

    private final TaskRepository taskRepository;

    public TaskCountHealthIndicator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Health health() {
        long count = taskRepository.count();
        return Health.up()
                .withDetail("taskCount", count)
                .withDetail("hint", "Custom HealthIndicator — see Lesson 53")
                .build();
    }
}
