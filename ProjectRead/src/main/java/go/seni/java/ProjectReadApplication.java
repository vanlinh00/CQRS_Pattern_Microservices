package go.seni.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProjectReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectReadApplication.class, args);
    }
}
