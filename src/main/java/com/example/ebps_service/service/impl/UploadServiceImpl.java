// This class belongs to the service.impl package
package com.example.ebps_service.service.impl;

// Import UploadResponse DTO
import com.example.ebps_service.dto.response.UploadResponse;

// Import Transaction entity
import com.example.ebps_service.entity.Transaction;

// Import UploadHistory entity
import com.example.ebps_service.entity.UploadHistory;

// Import TransactionRepository for database operations
import com.example.ebps_service.repository.TransactionRepository;

// Import UploadHistoryRepository for database operations
import com.example.ebps_service.repository.UploadHistoryRepository;

// Import UploadService interface
import com.example.ebps_service.service.UploadService;

// Import ExcelReader utility
import com.example.ebps_service.util.ExcelReader;

// Import RequiredArgsConstructor (Lombok annotation)
import lombok.RequiredArgsConstructor;

// Import Slf4j for logging
import lombok.extern.slf4j.Slf4j;

// Import @Service annotation
import org.springframework.stereotype.Service;

// Import MultipartFile (uploaded file)
import org.springframework.web.multipart.MultipartFile;

// Import IOException
import java.io.IOException;

// Import LocalDateTime for getting current timestamp
import java.time.LocalDateTime;

// Import List collection
import java.util.List;

// @Service annotation
// Marks this class as a service layer component in Spring
// Spring automatically manages the lifecycle of this class
@Service

// @RequiredArgsConstructor annotation
// Lombok automatically creates a constructor with all final fields
// This allows Spring to inject dependencies
// Example:
// public UploadServiceImpl(TransactionRepository transactionRepository,
//                        UploadHistoryRepository uploadHistoryRepository,
//                        ExcelReader excelReader) { ... }
@RequiredArgsConstructor

// @Slf4j annotation
// Lombok generates a logger field: private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
@Slf4j

// UploadServiceImpl class
// Implementation of UploadService interface
// Contains the actual business logic for file upload
public class UploadServiceImpl implements UploadService {

	// Final field: TransactionRepository
	// Used to perform database operations on transactions table
	// @RequiredArgsConstructor injects this field from Spring
	private final TransactionRepository transactionRepository;
	
	// Final field: UploadHistoryRepository
	// Used to perform database operations on upload_history table
	// @RequiredArgsConstructor injects this field from Spring
	private final UploadHistoryRepository uploadHistoryRepository;
	
	// Final field: ExcelReader
	// Used to read and parse Excel files
	// @RequiredArgsConstructor injects this field from Spring
	private final ExcelReader excelReader;

	// @Override annotation
	// Indicates that this method overrides a method from the interface
	// Ensures the method signature matches the interface exactly
	@Override
	
	// uploadTransactions method
	// Implements the method defined in UploadService interface
	// This is the main method that handles file uploads
	//
	// Parameter:
	// - file (MultipartFile): The uploaded Excel file
	//
	// Returns:
	// - UploadResponse: Upload result and statistics
	//
	// Throws:
	// - IOException: If file reading fails
	public UploadResponse uploadTransactions(MultipartFile file) throws IOException {

		try {
			log.info("Starting upload process for file: {}", file.getOriginalFilename());

			// Step 1: Read the Excel file using ExcelReader
			// excelReader.readExcel(file) returns a List of Transaction objects
			// Each Transaction object represents one row from the Excel file
			// Example:
			// The Excel file contains 100 rows of transaction data
			// This line creates 100 Transaction objects in a list
			List<Transaction> transactions = excelReader.readExcel(file);

			log.info("Excel parsing completed. Total transactions parsed: {}", transactions.size());

			if (transactions.isEmpty()) {
				log.warn("No valid transactions found in the Excel file");
				return UploadResponse.builder()
						.message("No valid transactions found in the Excel file.")
						.totalRecords(0)
						.successRecords(0)
						.failedRecords(0)
						.build();
			}

			try {
				// Step 2: Save all transactions to the database
				// transactionRepository.saveAll(transactions) stores all transactions
				// transactionRepository is a Spring Data JPA repository
				// It automatically generates SQL INSERT statements for each transaction
				// Example:
				// Before: transactions list is in memory
				// After: All transactions are stored in the database
				log.info("Saving {} transactions to database", transactions.size());
				transactionRepository.saveAll(transactions);
				log.info("Successfully saved all transactions to database");

			} catch (Exception e) {
				log.error("Error saving transactions to database: {}", e.getMessage(), e);
				return UploadResponse.builder()
						.message("Error saving transactions to database: " + e.getMessage())
						.totalRecords(transactions.size())
						.successRecords(0)
						.failedRecords(transactions.size())
						.build();
			}

			// Step 3: Create an UploadHistory record
			// This record tracks metadata about the upload (file name, timestamps, etc.)
			// UploadHistory.builder() starts creating a new UploadHistory object
			UploadHistory history = UploadHistory.builder()
			
					// Set fileName to the original name of the uploaded file
					// file.getOriginalFilename() returns the name user gave to the file
					// Example: "transactions_2025_01.xlsx"
					.fileName(file.getOriginalFilename())
					
					// Set totalRecords to the number of rows processed
					// transactions.size() returns the count of transactions
					// Example: If Excel has 100 rows, totalRecords = 100
					.totalRecords(transactions.size())
					
					// Set successRecords to the number of successfully saved records
					// In this simple implementation, we assume all records succeeded
					// transactions.size() = all records saved successfully
					// Example: 100 records saved = successRecords = 100
					.successRecords(transactions.size())
					
					// Set failedRecords to the number of failed records
					// In this simple implementation, no records failed
					// So failedRecords = 0
					// (In a real app, you'd validate each record and count failures)
					.failedRecords(0)
					
					// Set uploadedAt to the current date and time
					// LocalDateTime.now() returns the current instant
					// Example: 2025-01-15T10:30:45.123
					.uploadedAt(LocalDateTime.now())
					
					// .build() creates the final UploadHistory object
					.build();

			// Step 4: Save the upload history record to the database
			// uploadHistoryRepository.save(history) stores the UploadHistory object
			// This creates a new row in the upload_history table
			uploadHistoryRepository.save(history);

			log.info("Upload history saved for file: {}", file.getOriginalFilename());

			// Step 5: Create and return the response
			// UploadResponse.builder() starts creating the response object
			return UploadResponse.builder()
			
					// Set message to provide user feedback
					// This message tells the user that upload was successful
					.message("File uploaded successfully.")
					
					// Set totalRecords to the number of processed records
					.totalRecords(transactions.size())
					
					// Set successRecords to the number of successfully saved records
					.successRecords(transactions.size())
					
					// Set failedRecords to the number of failed records
					.failedRecords(0)
					
					// .build() creates the final UploadResponse object
					// This object is sent back to the client/controller
					.build();

		} catch (IOException e) {
			log.error("IOException while reading Excel file: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error during file upload: {}", e.getMessage(), e);
			throw new IOException("Unexpected error during file upload: " + e.getMessage(), e);
		}
	}
}