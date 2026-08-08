// This class belongs to the controller package
package com.example.ebps_service.controller;

// Import UploadResponse DTO (used to send response back to client)
import com.example.ebps_service.dto.response.UploadResponse;

// Import UploadService (contains the business logic)
import com.example.ebps_service.service.UploadService;

// Lombok annotation to automatically generate constructor
import lombok.RequiredArgsConstructor;

// Import Slf4j for logging
import lombok.extern.slf4j.Slf4j;

// Used to return HTTP status codes like 200 OK
import org.springframework.http.HttpStatus;

// Used to return complete HTTP Response (Status + Body)
import org.springframework.http.ResponseEntity;

// Imports Spring annotations like @RestController, @RequestMapping,
// @PostMapping, @RequestParam, etc.
import org.springframework.web.bind.annotation.*;

// Represents an uploaded file (Excel, PDF, Image, etc.)
import org.springframework.web.multipart.MultipartFile;

// IOException may occur while reading the uploaded file
import java.io.IOException;

// Marks this class as a REST API Controller
@RestController

// Base URL for all APIs in this controller
// Final endpoint becomes:
// http://localhost:8080/api/upload
@RequestMapping("/api/upload")

// Lombok automatically creates a constructor for final fields
@RequiredArgsConstructor

// @Slf4j annotation
// Lombok generates a logger field: private static final Logger log = LoggerFactory.getLogger(UploadController.class);
@Slf4j
public class UploadController {

    // Spring automatically injects the UploadService object
    // Controller uses this service to process uploaded files
    private final UploadService uploadService;

    // Handles HTTP POST requests
    // Used when the client uploads a file
    @PostMapping
    public ResponseEntity<UploadResponse> uploadFile(

            // Reads the uploaded file from the request
            // The request must contain a form-data field named "file"
            //
            // Example in Postman:
            // Key   : file
            // Type  : File
            // Value : transactions.xlsx
            @RequestParam("file") MultipartFile file) {

        try {
            log.info("Received upload request for file: {}", file.getOriginalFilename());

            // Validate that file is not empty
            if (file.isEmpty()) {
                log.warn("Uploaded file is empty");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(UploadResponse.builder()
                                .message("Uploaded file is empty. Please upload a valid Excel file.")
                                .totalRecords(0)
                                .successRecords(0)
                                .failedRecords(0)
                                .build());
            }

            // Call the service layer
            // Pass the uploaded file to UploadService
            // Service will:
            // 1. Read the Excel file
            // 2. Validate the data
            // 3. Save data into the database
            // 4. Return UploadResponse
            UploadResponse response = uploadService.uploadTransactions(file);

            log.info("Upload completed successfully for file: {}", file.getOriginalFilename());

            // Return HTTP Status 200 (OK)
            // Response body contains UploadResponse
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (IOException e) {
            log.error("IOException during file upload: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(UploadResponse.builder()
                            .message("Error reading the uploaded file. Please ensure it is a valid Excel file: " + e.getMessage())
                            .totalRecords(0)
                            .successRecords(0)
                            .failedRecords(0)
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error during file upload: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UploadResponse.builder()
                            .message("An unexpected error occurred during file upload. Please try again later.")
                            .totalRecords(0)
                            .successRecords(0)
                            .failedRecords(0)
                            .build());
        }
    }
}