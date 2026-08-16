package lesson48;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(
            TaskRepository taskRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.count() == 0) {
                userRepo.save(new AppUser("learner", passwordEncoder.encode("secret"), "USER"));
                userRepo.save(new AppUser("admin", passwordEncoder.encode("admin123"), "ADMIN"));
            }

            if (taskRepo.count() == 0) {
                taskRepo.save(new Task("Learn MockMvc tests", false));
                taskRepo.save(new Task("Run mvn test", true));
            }
        };
    }
}
