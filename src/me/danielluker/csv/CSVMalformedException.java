package me.danielluker.csv;

public class CSVMalformedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CSVMalformedException(String message) {
		super(message);
	}
	
	
	public CSVMalformedException(int lineNumber, String errorCode) {
		super(String.format(errorCode, lineNumber));
	}
	
	public static final String ERR_COLUMN_COUNT = "Wrong number of columns at line %d";
	
}
