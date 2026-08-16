package lesson43;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(TaskRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Task("Learn security", false));
                repo.save(new Task("Call API with password", true));
            }
        };
    }
}
