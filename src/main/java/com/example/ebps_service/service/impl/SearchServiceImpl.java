// This class belongs to the service.impl package
package com.example.ebps_service.service.impl;

// Import TransactionResponse DTO
import com.example.ebps_service.dto.response.TransactionResponse;
import com.example.ebps_service.dto.response.AccountTransactionsResponse;

// Import Transaction entity
import com.example.ebps_service.entity.Transaction;

// Import TransactionRepository
import com.example.ebps_service.repository.TransactionRepository;

// Import SearchService interface
import com.example.ebps_service.service.SearchService;

// Import RequiredArgsConstructor (Lombok)
import lombok.RequiredArgsConstructor;

// Import @Service annotation
import org.springframework.stereotype.Service;

// Import LocalDate
import java.time.LocalDate;

// Import List collection
import java.util.List;

// @Service annotation
// Marks this class as a service layer component
// Spring automatically manages this class
@Service

// @RequiredArgsConstructor annotation
// Lombok creates a constructor that injects the transactionRepository
// Example:
// public SearchServiceImpl(TransactionRepository transactionRepository) {
//     this.transactionRepository = transactionRepository;
// }
@RequiredArgsConstructor

// SearchServiceImpl class
// Implementation of SearchService interface
// Contains business logic for searching transactions
public class SearchServiceImpl implements SearchService {

	// Final field: TransactionRepository
	// Used to perform database queries on transactions table
	// @RequiredArgsConstructor injects this field from Spring
	private final TransactionRepository transactionRepository;

	// @Override annotation
	// Indicates this method overrides the method from SearchService interface
	@Override
	
	// searchAccountTransactions method
	// Searches for transactions based on account number and date range
	// Returns an account-level response (accountNumber, totalTransactions and transactions)
	public AccountTransactionsResponse searchAccountTransactions(
			String accountNumber,
			LocalDate fromDate,
			LocalDate toDate) {

		// Step 1: Query the database to find matching transactions
		List<Transaction> transactions =
				transactionRepository.findByAccountNumberAndTransactionDateBetween(
					accountNumber,
					fromDate,
					toDate
				);

		// Step 2: Convert Transaction entities to TransactionResponse DTOs
		List<TransactionResponse> txResponses = transactions.stream()
				.map(this::mapToResponse)
				.toList();

		// Build account-level response
		String acct = null;
		if (!txResponses.isEmpty()) {
			acct = txResponses.get(0).getAccountNumber();
		}

		return AccountTransactionsResponse.builder()
				.accountNumber(acct)
				.totalTransactions(txResponses.size())
				.transactions(txResponses)
				.build();
	}

	// mapToResponse helper method
	// Converts a Transaction entity to a TransactionResponse DTO
	// DTOs (Data Transfer Objects) are used to send data to the client
	// Only includes fields that the client needs to see
	//
	// Parameter:
	// - transaction (Transaction): The transaction entity from database
	//
	// Returns:
	// - TransactionResponse: DTO with transaction details
	private TransactionResponse mapToResponse(Transaction transaction) {

		// TransactionResponse.builder() starts creating a new TransactionResponse object
		return TransactionResponse.builder()
		
				// Set transactionId from the Transaction entity
				// transaction.getTransactionId() retrieves the transactionId field
				.transactionId(transaction.getTransactionId())
				
				// Set accountNumber from the Transaction entity
				.accountNumber(transaction.getAccountNumber())
				
				// Set transactionDate from the Transaction entity
				.transactionDate(transaction.getTransactionDate())
				
				// Set settleDate from the Transaction entity
				.settleDate(transaction.getSettleDate())
				
				// Set amount from the Transaction entity
				// Uses BigDecimal (already a proper type)
				.amount(transaction.getAmount())
				
				// Set quantity from the Transaction entity
				.quantity(transaction.getQuantity())
				
				// Set country from the Transaction entity
				.country(transaction.getCountry())
				
				// Set currency from the Transaction entity
				.currency(transaction.getCurrency())
				
				// Set accountType from the Transaction entity
				.accountType(transaction.getAccountType())
				
					.build();
	}

	// Backward-compatible list-returning method
	@Override
	public List<TransactionResponse> searchTransactions(String accountNumber, LocalDate fromDate, LocalDate toDate) {
		AccountTransactionsResponse acctResp = searchAccountTransactions(accountNumber, fromDate, toDate);
		if (acctResp == null || acctResp.getTransactions() == null) {
				return java.util.List.of();
		}
		return acctResp.getTransactions();
	}
}
