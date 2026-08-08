// This class belongs to the grpc package
package com.example.ebps_service.grpc;

// Import TransactionResponse DTO
import com.example.ebps_service.dto.response.TransactionResponse;

// Import SearchService for business logic
import com.example.ebps_service.service.SearchService;

// Import gRPC StreamObserver for streaming responses
// gRPC is a high-performance RPC framework
// StreamObserver sends data back to the client
import io.grpc.stub.StreamObserver;

// Import RequiredArgsConstructor (Lombok)
import lombok.RequiredArgsConstructor;

// Import @GrpcService annotation
// Marks this class as a gRPC service
import net.devh.boot.grpc.server.service.GrpcService;

// Import LocalDate for date conversion
import java.time.LocalDate;

// Import List collection
import java.util.List;

// @GrpcService annotation
// Marks this class as a gRPC service endpoint
// gRPC is an alternative to REST APIs for faster communication
@GrpcService

// @RequiredArgsConstructor annotation
// Lombok creates a constructor that injects searchService
@RequiredArgsConstructor

// AccountGrpcService class
// Implements gRPC service methods for transaction operations
// extends AccountServiceGrpc.AccountServiceImplBase extends a gRPC base class
// This provides the framework for handling gRPC requests
public class AccountGrpcService extends AccountServiceGrpc.AccountServiceImplBase {

	// Final field: SearchService
	// Used to perform business logic for searching transactions
	// @RequiredArgsConstructor injects this field from Spring
	private final SearchService searchService;

	// @Override annotation
	// This method overrides the gRPC service method
	@Override
	
	// getTransactions method
	// gRPC method that retrieves transactions for a customer
	// This is called when a gRPC client sends a TransactionRequest
	//
	// Parameters:
	// - request (TransactionRequest): Contains accountNumber, fromDate, toDate
	// - responseObserver (StreamObserver): Used to send response back to client
	public void getTransactions(TransactionRequest request,
							   StreamObserver<TransactionResponseList> responseObserver) {

		// Step 1: Extract parameters from gRPC request
		// request.getAccountNumber() gets the account number from the request
		// LocalDate.parse(request.getFromDate()) converts string date to LocalDate
		// LocalDate.parse(request.getToDate()) converts string date to LocalDate
		//
		// Then call the SearchService to search the database
		// Example:
		// Request: {accountNumber: "ACC123", fromDate: "2025-01-01", toDate: "2025-01-31"}
		// This searches for all transactions for ACC123 in January 2025
		List<TransactionResponse> transactions =
				searchService.searchTransactions(
						request.getAccountNumber(),
						LocalDate.parse(request.getFromDate()),
						LocalDate.parse(request.getToDate())
				);

		// Step 2: Create a builder for the gRPC response list
		// TransactionResponseList.newBuilder() creates a builder object
		// Builder is used to construct the gRPC response message
		// gRPC uses Protocol Buffers (binary format) for faster serialization
		TransactionResponseList.Builder responseList =
				TransactionResponseList.newBuilder();

		// Step 3: Loop through each transaction and add it to the response
		// for (TransactionResponse transaction : transactions)
		// Iterates through each transaction returned from the search
		for (TransactionResponse transaction : transactions) {

			// Step 3a: Create a gRPC TransactionResponse
			// com.example.ebps_service.grpc.TransactionResponse is the gRPC version
			// (Different from the DTO TransactionResponse in dto.response package)
			// newBuilder() starts creating the gRPC message
			com.example.ebps_service.grpc.TransactionResponse grpcResponse =
					com.example.ebps_service.grpc.TransactionResponse.newBuilder()
					
					// Set transactionId in the gRPC response
					.setTransactionId(transaction.getTransactionId())
					
					// Set accountNumber in the gRPC response
					.setAccountNumber(transaction.getAccountNumber())
					
					// Set transactionDate as string (gRPC requires string format)
					// .toString() converts LocalDate to "yyyy-MM-dd" format
					// Example: 2025-01-15
					.setTransactionDate(transaction.getTransactionDate().toString())
					
					// Set settleDate as string
					// .toString() converts LocalDate to "yyyy-MM-dd" format
					.setSettleDate(transaction.getSettleDate().toString())
					
					// Set amount as double (gRPC uses double for decimals)
					// .doubleValue() converts BigDecimal to double
					.setAmount(transaction.getAmount().doubleValue())
					
					// Set quantity in the gRPC response
					.setQuantity(transaction.getQuantity())
					
					// Set country in the gRPC response
					.setCountry(transaction.getCountry())
					
					// Set currency in the gRPC response
					.setCurrency(transaction.getCurrency())
					
					// Set accountType in the gRPC response
					.setAccountType(transaction.getAccountType())
					
					// .build() creates the final gRPC TransactionResponse message
					.build();

			// Step 3b: Add this transaction to the response list
			// responseList.addTransactions(grpcResponse) adds the transaction to the list
			// Multiple transactions accumulate in responseList
			responseList.addTransactions(grpcResponse);
		}

		// Step 4: Send the complete response to the client
		// responseObserver.onNext(responseList.build()) sends the response
		// responseList.build() creates the final TransactionResponseList message
		// onNext() indicates that data is being sent to the client
		responseObserver.onNext(responseList.build());

		// Step 5: Signal completion to the client
		// responseObserver.onCompleted() tells the client the response is complete
		// No more data will be sent
		// This signals the end of the gRPC stream
		responseObserver.onCompleted();
	}
}