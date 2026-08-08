// This class belongs to the entity package
package com.example.ebps_service.entity;

// Import @Entity annotation
// This marks the class as a JPA entity (database table)
import jakarta.persistence.*;

// Import Lombok annotations
// These automatically generate getters, setters, and constructors
import lombok.*;

// Import LocalDateTime for storing date and time
import java.time.LocalDateTime;

// @Entity annotation
// Marks this class as a JPA entity
// This class will be mapped to a database table
@Entity

// @Table annotation
// Specifies the name of the database table
// Table name = "upload_history"
@Table(name = "upload_history")

// @Getter annotation
// Lombok automatically generates getter methods for all fields
// Example: getId(), getFileName(), getTotalRecords(), etc.
@Getter

// @Setter annotation
// Lombok automatically generates setter methods for all fields
// Example: setId(), setFileName(), setTotalRecords(), etc.
@Setter

// @NoArgsConstructor annotation
// Lombok automatically generates an empty constructor
// Example: new UploadHistory();
@NoArgsConstructor

// @AllArgsConstructor annotation
// Lombok automatically generates a constructor with all fields
// Example: new UploadHistory(1L, "file.xlsx", 100, 98, 2, now);
@AllArgsConstructor

// @Builder annotation
// Lombok generates a Builder for creating objects easily
// Example:
// UploadHistory.builder()
//      .fileName("file.xlsx")
//      .totalRecords(100)
//      .build();
@Builder

// UploadHistory entity class
// Represents a record in the upload_history table
// Stores information about file uploads
public class UploadHistory {

	// @Id annotation
	// Marks this field as the PRIMARY KEY of the table
	@Id
	
	// @GeneratedValue annotation
	// Tells database to auto-increment the ID
	// strategy = GenerationType.IDENTITY means:
	// Database will automatically assign the next ID
	// Example: First record gets ID=1, second gets ID=2, etc.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	// Primary key field
	// Stores the unique ID of each upload record
	// Example: 1, 2, 3, etc.
	private Long id;

	// @Column annotation
	// Specifies column details
	// nullable = false means this field is REQUIRED
	// NULL values are NOT allowed
	@Column(nullable = false)
	
	// Stores the name of the uploaded Excel file
	// Example:
	// "transactions.xlsx"
	// "accounts_2025_01.xlsx"
	private String fileName;

	// @Column annotation
	// nullable = false means total record count MUST be provided
	@Column(nullable = false)
	
	// Stores the total number of records found in the uploaded file
	// Example:
	// If Excel file has 100 rows, totalRecords = 100
	private Integer totalRecords;

	// @Column annotation
	// nullable = false means success record count MUST be provided
	@Column(nullable = false)
	
	// Stores the number of records successfully saved to the database
	// Example:
	// If 98 out of 100 records were valid and saved
	// successRecords = 98
	private Integer successRecords;

	// @Column annotation
	// nullable = false means failed record count MUST be provided
	@Column(nullable = false)
	
	// Stores the number of records that failed during upload
	// Example:
	// If 2 records had invalid data
	// failedRecords = 2
	private Integer failedRecords;

	// @Column annotation
	// nullable = false means upload timestamp MUST be provided
	@Column(nullable = false)
	
	// Stores the date and time when the file was uploaded
	// LocalDateTime includes both date and time
	// Example:
	// 2025-01-15T10:30:45 (January 15, 2025 at 10:30:45 AM)
	private LocalDateTime uploadedAt;
}