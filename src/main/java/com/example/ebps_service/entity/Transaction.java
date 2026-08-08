// This class belongs to the entity package
package com.example.ebps_service.entity;

// Import JPA annotations for database mapping
import jakarta.persistence.*;

// Import Lombok annotations for auto-generating code
import lombok.*;

// Import BigDecimal for storing currency values
// More precise than double or float
import java.math.BigDecimal;

// Import LocalDate for storing only dates (without time)
import java.time.LocalDate;

// @Entity annotation
// Marks this class as a JPA entity
// This class will be mapped to a database table named "transactions"
@Entity

// @Table annotation with indexes
// Specifies the table name and database indexes
// Indexes speed up queries on accountNumber, transactionDate, etc.
@Table(
	// Table name in the database
	name = "transactions",
	
	// Create indexes for faster searching
	indexes = {
		// Index 1: Search by account number
		// Example: Find all transactions for account "ACC123"
		@Index(name = "idx_account_number", columnList = "accountNumber"),
		
		// Index 2: Search by transaction date
		// Example: Find all transactions on a specific date
		@Index(name = "idx_transaction_date", columnList = "transactionDate"),
		
		// Index 3: Search by both account and date
		// Example: Find all transactions for "ACC123" between two dates
		@Index(name = "idx_account_transaction_date",
				columnList = "accountNumber,transactionDate")
	}
)

// @Getter annotation
// Lombok generates getter methods for all fields
// Example: getId(), getTransactionId(), getAccountNumber(), etc.
@Getter

// @Setter annotation
// Lombok generates setter methods for all fields
// Example: setId(), setTransactionId(), setAccountNumber(), etc.
@Setter

// @NoArgsConstructor annotation
// Lombok generates an empty constructor
// Example: new Transaction();
@NoArgsConstructor

// @AllArgsConstructor annotation
// Lombok generates a constructor with all fields
// Example: new Transaction(1L, "TXN123", "ACC123", date1, date2, ...);
@AllArgsConstructor

// @Builder annotation
// Lombok generates a Builder for creating objects
// Example:
// Transaction.builder()
//      .transactionId("TXN123")
//      .accountNumber("ACC123")
//      .build();
@Builder

// Transaction entity class
// Represents a financial transaction record
// Each transaction stores details like account, amount, dates, etc.
public class Transaction {

	// @Id annotation
	// Marks this as the PRIMARY KEY of the transactions table
	@Id
	
	// @GeneratedValue annotation
	// Auto-increment strategy (database generates the next ID)
	// Example: First record ID=1, second ID=2, etc.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	// Primary key field
	// Unique identifier for each transaction record
	// Example: 1, 2, 3, etc.
	private Long id;

	// @Column annotation
	// nullable = false: This field is REQUIRED (not NULL allowed)
	// unique = true: No two transactions can have the same transactionId
	@Column(nullable = false, unique = true)
	
	// Unique transaction identifier
	// Example:
	// "TXN20250115001"
	// "TXN20250115002"
	private String transactionId;

	// @Column annotation
	// nullable = false: Account number is REQUIRED
	@Column(nullable = false)
	
	// Customer's account number
	// Used to identify whose account this transaction belongs to
	// Example:
	// "ACC123456789"
	// "SAVINGS001"
	private String accountNumber;

	// @Column annotation
	// nullable = false: Transaction date is REQUIRED
	@Column(nullable = false)
	
	// Date when the transaction occurred
	// Uses LocalDate (only date, no time)
	// Example:
	// 2025-01-15 (January 15, 2025)
	private LocalDate transactionDate;

	// @Column annotation
	// nullable = false: Settlement date is REQUIRED
	@Column(nullable = false)
	
	// Date when the transaction was settled
	// Settled date may be different from transaction date
	// (takes 1-2 business days for settlement)
	// Example:
	// Transaction Date: 2025-01-15
	// Settle Date: 2025-01-17
	private LocalDate settleDate;

	// @Column annotation
	// nullable = false: Amount is REQUIRED
	@Column(nullable = false)
	
	// Transaction amount (money value)
	// Uses BigDecimal for accurate financial calculations
	// (avoids floating-point precision issues)
	// Example:
	// 15000.50 (15 thousand rupees and 50 paise)
	// 1000.00
	private BigDecimal amount;

	// @Column annotation
	// nullable = false: Quantity is REQUIRED
	@Column(nullable = false)
	
	// Number of items/units involved in the transaction
	// Example:
	// If buying 100 shares: quantity = 100
	// If buying 5 items: quantity = 5
	private Integer quantity;

	// @Column annotation
	// nullable = false: Country is REQUIRED
	@Column(nullable = false)
	
	// Country where the transaction occurred
	// Example:
	// "India"
	// "USA"
	// "UK"
	private String country;

	// @Column annotation
	// nullable = false: Currency is REQUIRED
	@Column(nullable = false)
	
	// Currency used in the transaction
	// Example:
	// "INR" (Indian Rupee)
	// "USD" (US Dollar)
	// "EUR" (Euro)
	private String currency;

	// @Column annotation
	// nullable = false: Account type is REQUIRED
	@Column(nullable = false)
	
	// Type of account used for this transaction
	// Example:
	// "Savings" - Personal savings account
	// "Current" - Business current account
	// "Demat" - Dematerialized account for stocks
	private String accountType;
}