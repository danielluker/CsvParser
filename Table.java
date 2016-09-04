package me.danielluker.csvParser;


import java.io.*;

public class Table implements iTable {
	
	public static Table createTableFromFile(String filename) throws FileNotFoundException {
		File f = new File(filename);
		Table newInstance = null;
		try(Scanner sc = new Scanner(f)) {
			newInstance = new Table(sc);
			newInstance.sourceFile = f;
		} catch(IOException e) {
			System.err.println("ERROR: Unable to instantiate scanner. Please check input file");
			System.err.println("WARN: Unable to create Table instance; returning null");
		}
		return newInstance;
	}

	private int numColumns;
	private int numRows;
	private Map<String, Class<?>> headerTypes;
	private Map<String, List<?>> columns;


	private transient File sourceFile;


	private Table(Scanner sc) throws CSVMalformedException {
		this.headerTypes = new HashMap<>();
		this.columns = new HashMap<>();
		readAndParse(sc);
	}

	private void readAndParse(Scanner sc) throws CSVMalformedException {
		String[] head = sc.nextLine().split(",");
		for(String s : headers) {
			this.headerTypes.put(s, null);
			this.columns.put(s, new ArrayList<>());
		}
		this.numColumns = head.length;
		int linenumber;
		for(String temp = sc.nextLine, linenumber = 0; !sc.hasNext(); temp = sc.nextLine(), linenumber++) {
			String[] row = temp.split(",");
			if(row.length != this.numColumns)
				throw new CSVMalformedException(parentFile, linenumber);
			for(int i = 0; i < this.numColumns; i++) {
				List<?> col = this.columns.get(head[i]);
				col.add(row[i]);
			}
		}
		this.numRows = linenumber;
	}

	public void setColumnDataType(String columnLabel, Class<?> dataType){
		List<?> col = this.columns.get(columnLabel);

		// Do a fancy stream map collect here to parse all elements in the column from their string representations.
		// set the new column list data type, and update in the headerTypes map accordingly. 
		//col.stream.forEach()
	}

}