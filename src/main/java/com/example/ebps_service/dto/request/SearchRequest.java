// This class belongs to the dto.request package
package com.example.ebps_service.dto.request;

// Lombok annotation
// Automatically generates Getters, Setters,
// toString(), equals(), and hashCode() methods
import lombok.Data;

// Lombok annotation
// Provides Builder Pattern for creating objects
import lombok.Builder;

// Lombok annotation
// Automatically creates a No-Argument Constructor
// Example:
// SearchRequest request = new SearchRequest();
import lombok.NoArgsConstructor;

// Lombok annotation
// Automatically creates an All-Argument Constructor
// Example:
// new SearchRequest("12345", fromDate, toDate);
import lombok.AllArgsConstructor;

// Java class used for storing only Date (yyyy-MM-dd)
import java.time.LocalDate;

// @Data is a shortcut annotation
// It automatically creates:
// ✔ Getters
// ✔ Setters
// ✔ toString()
// ✔ equals()
// ✔ hashCode()
@Data

// Allows object creation using Builder Pattern
// Example:
// SearchRequest.builder()
//      .accountNumber("12345")
//      .fromDate(LocalDate.now())
//      .toDate(LocalDate.now())
//      .build();
@Builder

// Creates an empty constructor
// Example:
// SearchRequest request = new SearchRequest();
@NoArgsConstructor

// Creates a constructor with all fields
// Example:
// new SearchRequest("12345", fromDate, toDate);
@AllArgsConstructor

// DTO Class
// Used to transfer search request data
// between Controller and Service
public class SearchRequest {

    // Stores Account Number entered by the user
    // Example:
    // accountNumber = "123456789"
    private String accountNumber;

    // Stores the starting date of the search
    // Example:
    // fromDate = 2025-01-01
    private LocalDate fromDate;

    // Stores the ending date of the search
    // Example:
    // toDate = 2025-01-31
    private LocalDate toDate;

}