// This class belongs to the com.example.ebps_service package
package com.example.ebps_service;

// Import SpringApplication
// This is used to start the Spring Boot application
import org.springframework.boot.SpringApplication;

// Import SpringBootApplication annotation
// This annotation enables auto-configuration and component scanning
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication annotation
// This is a combination of 3 annotations:
// 1. @Configuration - allows Java-based configuration
// 2. @EnableAutoConfiguration - tells Spring Boot to guess configurations
// 3. @ComponentScan - scans for @Component, @Service, @Repository classes
@SpringBootApplication

// Main class of the Spring Boot Application
// Execution starts from the main() method
public class EbpsServiceApplication {

	// main() is the entry point of any Java application
	// This method is called when you run: java -jar app.jar
	// String[] args - stores command-line arguments passed to the application
	public static void main(String[] args) {
		
		// SpringApplication.run() starts the Spring Boot application
		// EbpsServiceApplication.class - tells Spring this is the main class
		// args - passes any command-line arguments to the application
		// Example usage:
		// java -jar app.jar --server.port=9090
		// The port 9090 will be passed as args
		SpringApplication.run(EbpsServiceApplication.class, args);
	}

}
