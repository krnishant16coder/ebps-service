// This interface belongs to the repository package
package com.example.ebps_service.repository;

// Import Transaction entity
import com.example.ebps_service.entity.Transaction;

// Import JpaRepository from Spring Data
// JpaRepository provides built-in CRUD methods
import org.springframework.data.jpa.repository.JpaRepository;

// Import LocalDate for date parameters
import java.time.LocalDate;

// Import List collection
import java.util.List;

// TransactionRepository interface
// Extends JpaRepository to inherit CRUD operations
// Also defines custom query methods for searching transactions
//
// Generic parameters:
// - Transaction: The entity class this repository manages
// - Long: The type of the primary key (id field)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	// Custom query method 1: Find transactions by account number
	// Spring Data JPA automatically generates the SQL query:
	// SELECT * FROM transactions WHERE accountNumber = ?
	//
	// Parameter:
	// - accountNumber (String): The account to search for
	//
	// Returns:
	// - List<Transaction>: All transactions for that account
	//
	// Example usage:
	// List<Transaction> accountTxns = transactionRepository.findByAccountNumber("ACC123");
	// This retrieves all transactions belonging to account "ACC123"
	List<Transaction> findByAccountNumber(String accountNumber);

	// Custom query method 2: Find transactions within a date range
	// Spring Data JPA automatically generates the SQL query:
	// SELECT * FROM transactions
	// WHERE transactionDate >= ? AND transactionDate <= ?
	//
	// Parameters:
	// - fromDate (LocalDate): Start date of the range
	// - toDate (LocalDate): End date of the range
	//
	// Returns:
	// - List<Transaction>: All transactions between the two dates
	//
	// Example usage:
	// LocalDate start = LocalDate.of(2025, 1, 1);
	// LocalDate end = LocalDate.of(2025, 1, 31);
	// List<Transaction> janTxns = transactionRepository.findByTransactionDateBetween(start, end);
	// This retrieves all transactions in January 2025
	List<Transaction> findByTransactionDateBetween(
			LocalDate fromDate,
			LocalDate toDate
	);

	// Custom query method 3: Find transactions by account AND date range (combined filter)
	// Spring Data JPA automatically generates the SQL query:
	// SELECT * FROM transactions
	// WHERE accountNumber = ?
	//   AND transactionDate >= ?
	//   AND transactionDate <= ?
	//
	// Parameters:
	// - accountNumber (String): The account to search for
	// - fromDate (LocalDate): Start date of the range
	// - toDate (LocalDate): End date of the range
	//
	// Returns:
	// - List<Transaction>: Transactions for that account within the date range
	//
	// Example usage:
	// LocalDate start = LocalDate.of(2025, 1, 1);
	// LocalDate end = LocalDate.of(2025, 1, 31);
	// List<Transaction> results = transactionRepository
	//     .findByAccountNumberAndTransactionDateBetween("ACC123", start, end);
	// This retrieves all transactions for account "ACC123" in January 2025
	List<Transaction> findByAccountNumberAndTransactionDateBetween(
			String accountNumber,
			LocalDate fromDate,
			LocalDate toDate
	);
}