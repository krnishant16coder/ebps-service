// This interface belongs to the service package
package com.example.ebps_service.service;

// Import UploadResponse DTO
import com.example.ebps_service.dto.response.UploadResponse;

// Import MultipartFile (uploaded file from HTTP request)
import org.springframework.web.multipart.MultipartFile;

// Import IOException
import java.io.IOException;

// UploadService interface
// Defines the contract for upload-related business operations
// Any class implementing this interface MUST implement the uploadTransactions method
// Interfaces are used to define a blueprint that multiple classes can follow
public interface UploadService {

	// uploadTransactions method
	// Processes the uploaded Excel file and saves transactions to the database
	//
	// Parameter:
	// - file (MultipartFile): The Excel file uploaded by the user
	//
	// Returns:
	// - UploadResponse: Contains upload status and statistics
	//   Example: {
	//     "message": "File uploaded successfully",
	//     "totalRecords": 100,
	//     "successRecords": 98,
	//     "failedRecords": 2
	//   }
	//
	// Throws:
	// - IOException: If file reading fails
	UploadResponse uploadTransactions(MultipartFile file) throws IOException;

}