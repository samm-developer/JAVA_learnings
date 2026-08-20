package lesson53;

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
            AppUser learner;
            AppUser admin;

            if (userRepo.count() == 0) {
                learner = userRepo.save(new AppUser("learner", passwordEncoder.encode("secret"), "USER"));
                admin = userRepo.save(new AppUser("admin", passwordEncoder.encode("admin123"), "ADMIN"));
            } else {
                learner = userRepo.findByUsername("learner").orElseThrow();
                admin = userRepo.findByUsername("admin").orElseThrow();
            }

            if (taskRepo.count() == 0) {
                // Many tasks so pagination is visible
                for (int i = 1; i <= 12; i++) {
                    boolean done = i % 3 == 0;
                    taskRepo.save(new Task("Actuator task " + i, done, learner));
                }
                taskRepo.save(new Task("Admin-only task", false, admin));
            }
        };
    }
}
