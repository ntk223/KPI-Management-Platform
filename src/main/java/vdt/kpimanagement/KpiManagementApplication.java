package vdt.kpimanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KpiManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(KpiManagementApplication.class, args);
    }

}
