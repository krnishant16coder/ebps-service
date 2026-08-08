// This interface belongs to the repository package
package com.example.ebps_service.repository;

// Import UploadHistory entity
import com.example.ebps_service.entity.UploadHistory;

// Import JpaRepository from Spring Data
// JpaRepository provides built-in methods for database operations
import org.springframework.data.jpa.repository.JpaRepository;

// UploadHistoryRepository interface
// Extends JpaRepository to inherit CRUD operations
// CRUD = Create, Read, Update, Delete
//
// Generic parameters:
// - UploadHistory: The entity class this repository manages
// - Long: The type of the primary key (id field)
public interface UploadHistoryRepository
		extends JpaRepository<UploadHistory, Long> {
	
	// JpaRepository provides these methods automatically:
	// - save(UploadHistory): Saves a single record to database
	// - saveAll(List): Saves multiple records to database
	// - findById(Long): Retrieves a record by ID
	// - findAll(): Retrieves all records
	// - delete(UploadHistory): Deletes a record
	// - deleteAll(): Deletes all records
	// - count(): Counts total records
	// - exists(Long): Checks if record with ID exists
	//
	// Example usage in service:
	// uploadHistoryRepository.save(uploadHistory);
	// List<UploadHistory> all = uploadHistoryRepository.findAll();
	// UploadHistory record = uploadHistoryRepository.findById(1L).get();
}