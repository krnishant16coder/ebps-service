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
// Creates an empty (No-Argument) constructor
// Example:
// TransactionResponse response = new TransactionResponse();
import lombok.NoArgsConstructor;

// Lombok annotation
// Creates a constructor with all fields
// Example:
// new TransactionResponse(...all fields...);
import lombok.AllArgsConstructor;

// Used for storing decimal values like money
// More accurate than double or float
import java.math.BigDecimal;

// Used to store only the date (yyyy-MM-dd)
import java.time.LocalDate;

// Generates Getters, Setters, toString(), equals(), hashCode()
@Data

// Enables Builder Pattern
// Example:
// TransactionResponse.builder()
//      .accountNumber("12345")
//      .amount(new BigDecimal("1000"))
//      .build();
@Builder

// Generates an empty constructor
@NoArgsConstructor

// Generates a constructor with all fields
@AllArgsConstructor

// DTO (Data Transfer Object)
// Used to send transaction data from the Service
// back to the Controller and finally to the Client
public class TransactionResponse {

    // Unique ID of the transaction
    // Example:
    // TXN100001
    private String transactionId;

    // Customer's account number
    // Example:
    // 123456789
    private String accountNumber;

    // Date on which the transaction occurred
    // Example:
    // 2025-01-10
    private LocalDate transactionDate;

    // Date on which the transaction was settled
    // Example:
    // 2025-01-12
    private LocalDate settleDate;

    // Transaction amount
    // BigDecimal is used because it provides
    // accurate calculations for money values
    // Example:
    // 15000.75
    private BigDecimal amount;

    // Quantity involved in the transaction
    // Example:
    // 100
    private Integer quantity;

    // Country where the transaction occurred
    // Example:
    // India
    private String country;

    // Currency used in the transaction
    // Example:
    // INR, USD, EUR
    private String currency;

    // Type of account
    // Example:
    // Savings
    // Current
    // Demat
    private String accountType;

}