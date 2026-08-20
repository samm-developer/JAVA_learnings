package lesson53;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Lesson 53 — Spring Boot Actuator + Logging
 * <p>
 * Run:  cd lesson53-springboot-actuator && mvn spring-boot:run
 * <p>
 * Try:
 *   curl http://localhost:8098/actuator/health
 *   curl http://localhost:8098/actuator/info
 *   # metrics need ADMIN JWT (seed user: admin / admin123 from DataLoader)
 *   curl -H "Authorization: Bearer YOUR_JWT" http://localhost:8098/actuator/metrics
 */
@SpringBootApplication
public class TaskActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskActuatorApplication.class, args);
    }
}
