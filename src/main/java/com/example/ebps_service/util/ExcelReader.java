// This class belongs to the util package
package com.example.ebps_service.util;

// Import Transaction entity
import com.example.ebps_service.entity.Transaction;

// Import DateTimeFormatter for parsing dates from strings
import java.time.format.DateTimeFormatter;

// Import Apache POI library for reading Excel files
// POI = Poor Obfuscation Implementation
// It's a library that allows Java to read/write Excel files
import org.apache.poi.ss.usermodel.*;

// Import @Component annotation
// Marks this class as a Spring component for auto-wiring
import org.springframework.stereotype.Component;

// Import MultipartFile
// Represents an uploaded file from the HTTP request
import org.springframework.web.multipart.MultipartFile;

// Import IOException
// May be thrown when reading file fails
import java.io.IOException;

// Import BigDecimal for storing currency values
import java.math.BigDecimal;

// Import LocalDate for storing dates
import java.time.LocalDate;

// Import ZoneId for timezone conversion
import java.time.ZoneId;

// Import ArrayList for storing list of transactions
import java.util.ArrayList;

// Import List interface
import java.util.List;

// Import Slf4j for logging
import lombok.extern.slf4j.Slf4j;

// Import DateTimeParseException to catch date parsing errors
import java.time.format.DateTimeParseException;

// @Component annotation
// Tells Spring this is a component that should be managed by Spring
// Spring will automatically create an instance of this class
@Component

// @Slf4j annotation
// Lombok generates a logger field: private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);
@Slf4j

// ExcelReader class
// Reads transaction data from uploaded Excel files
public class ExcelReader {

	// readExcel method
	// Reads an Excel file and converts it into a list of Transaction objects
	//
	// Parameter:
	// - file (MultipartFile): The uploaded Excel file
	//
	// Returns:
	// - List<Transaction>: A list of transaction objects extracted from the Excel
	//
	// Throws:
	// - IOException: If file reading fails
	public List<Transaction> readExcel(MultipartFile file) throws IOException {

		// Create an empty ArrayList to store transactions
		// ArrayList can dynamically grow as we add transactions
		List<Transaction> transactions = new ArrayList<>();

		log.info("Starting to process Excel file: {}", file.getOriginalFilename());

		// WorkbookFactory.create() opens the Excel file
		// file.getInputStream() gets the file's content as a stream of bytes
		// The stream is passed to WorkbookFactory to create a Workbook object
		// Workbook is the root object representing the entire Excel file
		Workbook workbook = WorkbookFactory.create(file.getInputStream());

		try {
			// workbook.getSheetAt(0) gets the first sheet (index 0) from the workbook
			// Most Excel files have data on the first sheet
			// Sheet represents a tab/worksheet in the Excel file
			Sheet sheet = workbook.getSheetAt(0);

			// Verify and log headers
			Row headerRow = sheet.getRow(0);
			DataFormatter formatter = new DataFormatter();
			String[] expectedHeaders = {"Transaction ID","Account Number","Transaction Date","Settle Date","Amount","Quantity","Country","Currency","Account Type"};
			if (headerRow != null) {
				StringBuilder headersLog = new StringBuilder();
				for (int c = 0; c < expectedHeaders.length; c++) {
					Cell hc = headerRow.getCell(c);
					String hv = formatter.formatCellValue(hc).trim();
					headersLog.append(String.format("col%d='%s' ", c, hv));
					if (!hv.equalsIgnoreCase(expectedHeaders[c])) {
						log.warn("Header mismatch at col {}: expected='{}' actual='{}'", c, expectedHeaders[c], hv);
					}
				}
				log.info("Excel headers: {}", headersLog.toString().trim());
			} else {
				log.warn("No header row found in Excel sheet");
			}

			// for loop to iterate through all rows in the sheet
			// i = 1: Start from row 1 (skipping row 0 which contains headers)
			// i <= sheet.getLastRowNum(): Continue until the last row
			// getLastRowNum() returns the index of the last row with data
			// Example: If Excel has 100 rows of data, getLastRowNum() returns 99
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				// sheet.getRow(i) retrieves the row at index i
				// Returns the Row object if data exists, or null if the row is empty
				Row row = sheet.getRow(i);

				// if (row == null): Check if this row is empty/null
				// If empty, skip this row using continue
				// This prevents NullPointerException
				if (row == null) {
					log.debug("Skipping blank row {}", i);
					continue;
				}

				try {
					log.info("Processing row {}", i);

					// Extract cell values
					String transactionId = getString(row.getCell(0));
					String accountNumber = getString(row.getCell(1));
					LocalDate transactionDate = getDate(row.getCell(2));
					LocalDate settleDate = getDate(row.getCell(3));
					double amountValue = getNumeric(row.getCell(4));
					int quantity = (int) getNumeric(row.getCell(5));
					String country = getString(row.getCell(6));
					String currency = getString(row.getCell(7));
					String accountType = getString(row.getCell(8));

					// Log parsed values for debugging
					log.info("Row {} parsed -> transactionId='{}', accountNumber='{}', transactionDate='{}', settleDate='{}', amount={}, quantity={}, country='{}', currency='{}', accountType='{}'",
							i,
							transactionId,
							accountNumber,
							transactionDate == null ? "null" : transactionDate.toString(),
							settleDate == null ? "null" : settleDate.toString(),
							amountValue,
							quantity,
							country,
							currency,
							accountType);

					// Validate required fields before creating transaction
					if (transactionId.isEmpty()) {
						String raw = formatter.formatCellValue(row.getCell(0));
						log.warn("Row {}: Transaction ID is missing (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (accountNumber.isEmpty()) {
						String raw = formatter.formatCellValue(row.getCell(1));
						log.warn("Row {}: Account Number is missing (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (transactionDate == null) {
						String raw = formatter.formatCellValue(row.getCell(2));
						log.warn("Row {}: Transaction Date is missing or invalid (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (settleDate == null) {
						String raw = formatter.formatCellValue(row.getCell(3));
						log.warn("Row {}: Settle Date is missing or invalid (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (amountValue <= 0) {
						String raw = formatter.formatCellValue(row.getCell(4));
						log.warn("Row {}: Amount is invalid or zero (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (quantity <= 0) {
						String raw = formatter.formatCellValue(row.getCell(5));
						log.warn("Row {}: Quantity is invalid or zero (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (country.isEmpty()) {
						String raw = formatter.formatCellValue(row.getCell(6));
						log.warn("Row {}: Country is missing (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (currency.isEmpty()) {
						String raw = formatter.formatCellValue(row.getCell(7));
						log.warn("Row {}: Currency is missing (raw='{}'), skipping row", i, raw);
						continue;
					}
					if (accountType.isEmpty()) {
						String raw = formatter.formatCellValue(row.getCell(8));
						log.warn("Row {}: Account Type is missing (raw='{}'), skipping row", i, raw);
						continue;
					}

					// Transaction.builder() starts creating a new Transaction object
					// Builder pattern allows readable step-by-step object construction
					Transaction transaction = Transaction.builder()
				
							// Set transactionId from cell 0 (first column)
							// getString() extracts text from the cell
							// Example: Cell A (column 0) contains "TXN001"
							.transactionId(transactionId)
						
							// Set accountNumber from cell 1 (second column)
							// Example: Cell B (column 1) contains "ACC123456789"
							.accountNumber(accountNumber)
						
							// Set transactionDate from cell 2 (third column)
							// getDate() extracts date from the cell
							// Example: Cell C (column 2) contains "15/01/2025"
							.transactionDate(transactionDate)
						
							// Set settleDate from cell 3 (fourth column)
							// Example: Cell D (column 3) contains "17/01/2025"
							.settleDate(settleDate)
						
							// Set amount from cell 4 (fifth column)
							// getNumeric() extracts numeric value as double
							// BigDecimal.valueOf() converts double to BigDecimal
							// Example: Cell E (column 4) contains "15000.50"
							.amount(BigDecimal.valueOf(amountValue))
						
							// Set quantity from cell 5 (sixth column)
							// (int) casts the double value to integer
							// Example: Cell F (column 5) contains "100"
							.quantity(quantity)
						
							// Set country from cell 6 (seventh column)
							// Example: Cell G (column 6) contains "India"
							.country(country)
						
							// Set currency from cell 7 (eighth column)
							// Example: Cell H (column 7) contains "INR"
							.currency(currency)
						
							// Set accountType from cell 8 (ninth column)
							// Example: Cell I (column 8) contains "Savings"
							.accountType(accountType)
						
							// .build() creates the final Transaction object
							// Returns a new Transaction with all the set fields
							.build();

					// Add the newly created transaction to the list
					// transactions.add() appends it to the end of the list
					transactions.add(transaction);
					log.debug("Successfully parsed row {}: {}", i, transactionId);

				} catch (DateTimeParseException e) {
					log.error("Row {}: Invalid date format - {}", i, e.getMessage());
					continue;
				} catch (NumberFormatException e) {
					log.error("Row {}: Invalid numeric format - {}", i, e.getMessage());
					continue;
				} catch (Exception e) {
					log.error("Row {}: Error parsing row - {}", i, e.getMessage(), e);
					continue;
				}
			}

			log.info("Finished processing Excel file. Total rows read: {}", transactions.size());

		} finally {
			// workbook.close() closes the Excel file
			// Frees up memory and system resources
			// IMPORTANT: Always close files to prevent memory leaks
			workbook.close();
		}

		// Return the list of all transactions extracted from the Excel file
		return transactions;
	}

	// getString() helper method
	// Extracts text from an Excel cell
	// Handles null cells gracefully
	// Uses DataFormatter to handle both String and Numeric cells
	//
	// Parameter:
	// - cell (Cell): The Excel cell to read from
	//
	// Returns:
	// - String: The text value from the cell (or empty string if null)
	private String getString(Cell cell) {

		// if (cell == null): Check if the cell is empty/null
		// If null, return an empty string instead of throwing an error
		if (cell == null) {
			return "";
		}

		// Use DataFormatter to handle both String and Numeric cells
		// DataFormatter.formatCellValue() converts any cell type to String
		// This prevents exceptions when a numeric cell is read as string
		// Example:
		// Cell with numeric value 123 → "123" (not an exception)
		// Cell with text "India" → "India"
		// Cell with date 15/01/2025 → "15/01/2025"
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell).trim();
	}

	// getNumeric() helper method
	// Extracts numeric values from an Excel cell
	// Handles null cells gracefully
	// Handles both numeric cells and text cells containing numbers
	//
	// Parameter:
	// - cell (Cell): The Excel cell to read from
	//
	// Returns:
	// - double: The numeric value from the cell (or 0 if null or invalid)
	private double getNumeric(Cell cell) {

		// if (cell == null): Check if the cell is empty/null
		// If null, return 0 as a default value
		if (cell == null) {
			return 0;
		}

		// Check if cell is numeric type
		if (cell.getCellType() == CellType.NUMERIC) {
			// cell.getNumericCellValue() extracts the number from the cell
			// Returns a double value
			// Example:
			// Cell value: 15000.50
			// Result: 15000.50 (as double)
			return cell.getNumericCellValue();
		}

		// If cell is text, try to parse it as a number
		if (cell.getCellType() == CellType.STRING) {
			String value = cell.getStringCellValue().trim();
			if (!value.isEmpty()) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					log.debug("Cannot parse cell value as numeric: {}", value);
					return 0;
				}
			}
		}

		// For all other cases (BLANK, BOOLEAN, ERROR, FORMULA), return 0
		return 0;
	}

	// getDate() helper method
	// Extracts dates from an Excel cell
	// Handles different date formats (numeric and text)
	//
	// Parameter:
	// - cell (Cell): The Excel cell to read from
	//
	// Returns:
	// - LocalDate: The date value (or null if cell is empty)
	private LocalDate getDate(Cell cell) {

		if (cell == null) {
			return null;
		}

		DataFormatter formatter = new DataFormatter();

		// If numeric and formatted as date, use getDateCellValue
		if (cell.getCellType() == CellType.NUMERIC) {
			try {
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue()
							.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();
				} else {
					// Try to interpret numeric as Excel serial date
					try {
						java.util.Date d = DateUtil.getJavaDate(cell.getNumericCellValue());
						return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					} catch (Exception e) {
						log.debug("Numeric cell not a date: {}", e.getMessage());
					}
				}
			} catch (Exception e) {
				log.debug("Error parsing numeric date cell: {}", e.getMessage());
				// fallthrough to try parsing as string
			}
		}

		// Attempt to parse string representation using formatter
		String text = formatter.formatCellValue(cell).trim();
		if (text.isEmpty()) {
			return null;
		}

		// Try multiple date formats
		String[] patterns = {"dd/MM/yyyy","yyyy-MM-dd","MM/dd/yyyy","d/M/yyyy"};
		for (String p : patterns) {
			try {
				return LocalDate.parse(text, DateTimeFormatter.ofPattern(p));
			} catch (DateTimeParseException ignored) {
			}
		}

		// Try ISO_LOCAL_DATE
		try {
			return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (DateTimeParseException e) {
			log.debug("Cannot parse date text '{}' with known patterns: {}", text, e.getMessage());
			return null;
		}
	}

}