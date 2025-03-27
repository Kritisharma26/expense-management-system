package com.example.expnecemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.expnecemgmt"}) // ✅ Ensures Spring scans all sub-packages
public class EnterpriseExpenseManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseExpenseManagementSystemApplication.class, args);
    }

    // ✅ Log all registered Spring Beans (Including Controllers)
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("✅ Listing All Registered Beans in Spring Context:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}


