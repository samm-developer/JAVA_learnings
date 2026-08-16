package lesson44;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(TaskRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Task("Learn roles", false));
                repo.save(new Task("Delete needs ADMIN", true));
            }
        };
    }
}
