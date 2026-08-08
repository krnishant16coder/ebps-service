// This class belongs to the dto.response package
package com.example.ebps_service.dto.response;

// Lombok annotation
// Automatically generates:
// ✔ Getters
// ✔ Setters
// ✔ toString()
// ✔ equals()
// ✔ hashCode()
import lombok.Data;

// Lombok annotation
// Allows object creation using Builder Pattern
import lombok.Builder;

// Lombok annotation
// Creates an empty constructor
// Example:
// UploadResponse response = new UploadResponse();
import lombok.NoArgsConstructor;

// Lombok annotation
// Creates a constructor with all fields
// Example:
// new UploadResponse("Success",100,98,2);
import lombok.AllArgsConstructor;

// Generates:
// Getter
// Setter
// toString()
// equals()
// hashCode()
@Data

// Enables Builder Pattern
// Example:
// UploadResponse.builder()
//      .message("Success")
//      .totalRecords(100)
//      .build();
@Builder

// Generates an empty constructor
@NoArgsConstructor

// Generates a constructor with all fields
@AllArgsConstructor

// DTO (Data Transfer Object)
// Used to send upload result from Service
// back to the Controller and then to the Client
public class UploadResponse {

    // Stores the upload status message
    // Example:
    // "File uploaded successfully"
    // "Upload failed"
    private String message;

    // Total number of records found in the uploaded Excel file
    // Example:
    // Excel contains 100 rows
    private int totalRecords;

    // Number of records successfully saved into the database
    // Example:
    // 98 records inserted successfully
    private int successRecords;

    // Number of records that failed during upload
    // Example:
    // 2 records failed because of invalid data
    private int failedRecords;

}