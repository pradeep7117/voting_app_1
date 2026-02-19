package com.example.VotingApplication;

import java.awt.Desktop;
import java.net.URI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VotingApplication1Application {

    public static void main(String[] args) {
        SpringApplication.run(VotingApplication1Application.class, args);
        
        // Check if the desktop environment is supported
        if(Desktop.isDesktopSupported()) {
            try {
                // CHANGED: 8888 -> 8080 to match your server port
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            } catch (Exception e) {
                System.err.println("Failed to open browser: " + e.getMessage());
            }
        } else {
            System.out.println("Desktop not supported. Please open http://localhost:8080 manually.");
        }
    }
}