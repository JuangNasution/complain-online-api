package id.co.bni.ets.complain_online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

// this is to disable default login page from spring security
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
public class RefundOnline extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RefundOnline.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RefundOnline.class);
    }
}
