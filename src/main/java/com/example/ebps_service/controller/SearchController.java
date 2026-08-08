// This class belongs to the controller package
package com.example.ebps_service.controller;

// Import DTO class that will be returned to the client
import com.example.ebps_service.dto.response.TransactionResponse;
import com.example.ebps_service.dto.response.AccountTransactionsResponse;

// Import Service class to perform business logic
import com.example.ebps_service.service.SearchService;

// Lombok annotation that automatically creates a constructor
import lombok.RequiredArgsConstructor;

// Used to convert request date (String) into LocalDate
import org.springframework.format.annotation.DateTimeFormat;

// Used to return HTTP responses (Status + Data)
import org.springframework.http.ResponseEntity;

// Imports Spring annotations like @RestController, @GetMapping, @RequestParam, etc.
import org.springframework.web.bind.annotation.*;

// Java class for working with dates
import java.time.LocalDate;

// Java collection that stores multiple objects
import java.util.List;

// Marks this class as a REST API Controller
@RestController

// Base URL for all APIs in this controller
// Final endpoint becomes: http://localhost:8080/api/search
@RequestMapping("/api/search")

// Lombok automatically generates a constructor for final fields
@RequiredArgsConstructor
public class SearchController {

    // Spring injects the SearchService object here
    // Controller will use this object to call service methods
    private final SearchService searchService;

    // Handles HTTP GET requests
    // Example:
    // GET /api/search?accountNumber=123&fromDate=2025-01-01&toDate=2025-01-31
    @GetMapping
    public ResponseEntity<AccountTransactionsResponse> searchTransactions(

            // Reads accountNumber from the URL
            // Example: accountNumber=12345
            @RequestParam String accountNumber,

            // Reads fromDate from the URL
            // Converts "2025-01-01" into LocalDate automatically
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            // Reads toDate from the URL
            // Converts "2025-01-31" into LocalDate automatically
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        // Call the service layer
        // Pass account number, start date and end date
        // Service searches the database and returns matching transactions
        AccountTransactionsResponse response =
                searchService.searchAccountTransactions(
                        accountNumber,
                        fromDate,
                        toDate
                );

        // If no transactions found, return empty response as required
        if (response == null || response.getTransactions() == null || response.getTransactions().isEmpty()) {
            return ResponseEntity.ok(AccountTransactionsResponse.builder()
                    .accountNumber(null)
                    .totalTransactions(0)
                    .transactions(java.util.List.of())
                    .build());
        }

        // Return HTTP Status 200 (OK)
        // Response body contains the account-level response
        return ResponseEntity.ok(response);
    }
}