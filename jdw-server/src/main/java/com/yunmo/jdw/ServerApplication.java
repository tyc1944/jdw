package com.yunmo.jdw;

import com.yunmo.boot.oauth2.resource.ResourceServerAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.zalando.problem.spring.web.autoconfigure.security.ProblemSecurityAutoConfiguration;

@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SpringBootApplication(exclude = ProblemSecurityAutoConfiguration.class)
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServerApplication.class);
        app.run(args);
    }

}
