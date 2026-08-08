// This interface belongs to the service package
package com.example.ebps_service.service;

// Import TransactionResponse DTO
import com.example.ebps_service.dto.response.TransactionResponse;
import com.example.ebps_service.dto.response.AccountTransactionsResponse;

// Import LocalDate for date parameters
import java.time.LocalDate;

// Import List collection
import java.util.List;

// SearchService interface
// Defines the contract for search-related business operations
// Any class implementing this interface MUST implement the searchTransactions method
// This interface specifies what search functionalities should be available
public interface SearchService {

	// Original searchTransactions method retained for backward compatibility
	// Searches for transactions and returns a list of TransactionResponse DTOs
	List<TransactionResponse> searchTransactions(
			String accountNumber,
			LocalDate fromDate,
			LocalDate toDate
	);

	// New method to return account-level response (accountNumber + total + transactions)
	AccountTransactionsResponse searchAccountTransactions(
			String accountNumber,
			LocalDate fromDate,
			LocalDate toDate
	);

}