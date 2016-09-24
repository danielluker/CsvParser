package me.danielluker.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * Class which represents the data contained in the table.
 * 
 * @author danielluker
 *
 */
public class Table implements iTable {

	public static Table createFromFile(String filename) throws FileNotFoundException, CSVMalformedException {
		File f = new File(filename);
		Table newInstance = null;
		try(Scanner sc = new Scanner(f)) {
			newInstance = new Table(sc);
		} catch(IOException e) {
			System.err.println("ERROR: Unable to instantiate scanner. Please check input file");
			System.err.println("WARN: Unable to create Table instance; returning null");
		}
		return newInstance;
	}

	private Map<String, List<Object>> columns;
	private String[] columnIndices;
	private int numRows, numColumns;
	private boolean debugMode = false;
	
	public static void main(String args[]) {
		String test = "\"\"\"\",\",\",,";
		System.out.println(splitCsvString(test, ',').toString());
	}

	private static List<String> splitCsvString(String input, char delimiter) {
		StringBuilder s = new StringBuilder(input);
		List<String> csvElements = new ArrayList<>();
		int start = 0;
		for(int i = 0; i < s.length(); i++) {
			
			if(s.charAt(i) == '"') { // found a "
				StringBuilder sb = new StringBuilder();
				for(int j = i + 1; j < s.length(); j++) {
					if(s.charAt(j) == '"') {
						if(s.charAt(j+1) == '"') {
							sb.append('"');
							j++;
						} else {
							start = j+1;
							break;
						}
					} else {
						sb.append(s.charAt(j));
					}
				}
				csvElements.add(sb.toString());
				i = start;
				continue;
			}
			
			else if(s.charAt(i) == delimiter) {
				csvElements.add(s.substring(start, i));
				start = i+1;
			}
		}
		// To add the very last string
		csvElements.add(s.substring(start, s.length()));
		
		return csvElements;
	}
	
	private Table(Scanner sc) throws CSVMalformedException {
		this.columns = new HashMap<>();
		if (!sc.hasNextLine())
			throw new CSVMalformedException("No lines found in file");
		columnIndices = splitCsvString(sc.nextLine(), ',').toArray(new String[0]);
		for (String cols : columnIndices) {
			List<Object> tList = new ArrayList<>();
			this.columns.put(cols, tList);
		}
		this.numColumns = columnIndices.length;
		int iterator = 0;
		while (sc.hasNextLine()) {
			List<String> vals = splitCsvString(sc.nextLine(), ',');
			if (vals.size() != this.numColumns) {
				throw new CSVMalformedException(iterator, CSVMalformedException.ERR_COLUMN_COUNT);
			}
			for (int i = 0; i < this.numColumns; i++) {
				this.columns.get(columnIndices[i]).add(vals.get(i));
			}
			iterator++;
		}
		;
		this.numRows = iterator;
	}

	public boolean setDebug(boolean debug) {
		boolean oldVal = this.debugMode;
		this.debugMode = debug;
		return oldVal;
	}

	@Override
	public List<Object> getColumn(String columnLabel) {
		return this.columns.get(columnLabel);
	}

	@Override
	public Row getRow(int rowNumber) {
		if (rowNumber >= this.numRows) {
			System.err.println("ERR:\tTrying to access a row larger than the size of the table");
			return null;
		}
		Map<String, Object> rData = new HashMap<>();
		for (String s : this.columns.keySet()) {
			Object retVal = this.columns.get(s).get(rowNumber);
			rData.put(s, retVal);
		}
		return new Row(rData, columnIndices);
	}

	@Override
	public Row getRowByColumnValue(String columnLabel, Object value) {
		if (this.columns.get(columnLabel) == null) {
			System.err.println("ERR:\tspecified column [" + columnLabel + "] not found!");
			return null;
		}
		List<?> column = this.columns.get(columnLabel);
		for (int i = 0; i < column.size(); i++) {
			if (value.equals(column.get(i))) {
				return getRow(i);
			}
		}
		System.err.println("WARN:\tSpecified row not found!");
		return null;
	}

	@Override
	public String[] getHeaders() {
		return this.columns.keySet().toArray(new String[this.numColumns]);
	}

	@Override
	public Iterator<Row> getAllRows() {
		LinkedList<Row> list = new LinkedList<Row>();
		// using linkedlist to guarantee constant time for all add operations
		for (int i = 0; i < this.numRows; i++) {
			list.add(getRow(i));
		}
		return list.iterator();
	}

	@Override
	public int appendRow(Row newRow) {
		return insertRow(this.numRows, newRow);
	}

	@Override
	public Row removeLastRow() {
		return deleteRow(this.numColumns);
	}

	@Override
	public int insertRow(int atIndex, Row newRow) {
		Map<String, Object> vals = newRow.getValues();
		if (vals.size() != this.numColumns) {
			System.err.println("ERR:\tNew row column count does not match table!");
			if (debugMode) {
				// Finding out which columns do not match
				for (String s : vals.keySet()) {
					if (!this.columns.containsKey(s)) {
						String template = "DEBUG:\t%s is not present in table columns";
						System.err.println(String.format(template, s));
					}
				}
			}
			return -1;
		}
		for (Entry<String, Object> entry : vals.entrySet()) {
			String col = entry.getKey();
			Object obj = entry.getValue();
			if (!this.columns.containsKey(col)) {
				System.err.println("ERR:\t" + entry.getKey() + " is not a valid column");
				return -1;
			}
			this.columns.get(col).add(atIndex, obj);
		}
		return ++this.numRows;
	}

	@Override
	public Row deleteRow(int atIndex) {
		if (atIndex < 0 || atIndex >= this.numRows) {
			System.err.println("Attempting to delete a row at an invalid index: " + atIndex);
			return null;
		}
		Map<String, Object> m = new HashMap<>();
		for (String s : this.columns.keySet()) {
			List<Object> l = this.columns.get(s);
			m.put(s, l.remove(atIndex));
		}
		this.numRows--;
		return new Row(m, this.columnIndices);
	}

	@Override
	public Row updateRow(int atIndex, Row updatedRow) {
		Row oldRow = deleteRow(atIndex);
		insertRow(atIndex, updatedRow);
		return oldRow;
	}

	@Override
	public List<Object> dropColumn(String columnLabel) {
		List<Object> ret = this.columns.remove(columnLabel);
		if (ret == null)
			System.err.println("WARN:\tColumn [" + columnLabel + "] not found in table");
		else
			this.numColumns--;
		return ret;
	}

	@Override
	public int addColumn(String columnLabel) {
		if (this.columns.containsKey(columnLabel)) {
			System.err.println("WARN:\tColumn label to insert already exists! Not inserting");
			return -1;
		}
		List<Object> newList = new ArrayList<>(this.numRows);
		for (int i = 0; i < this.numRows; i++)
			newList.add(null);
		return ++this.numColumns;
	}

	@Override
	public int addColumnFromList(String columnLabel, List<Object> columnData) {
		if (this.columns.containsKey(columnLabel)) {
			System.err.println("WARN:\tColumn label to insert already exists! Not inserting");
			return -1;
		}
		if (columnData.size() != this.numRows) {
			System.err.println("ERR:\tNumber of entries in new column does not match table! Not inserting");
			return -1;
		}
		this.columns.put(columnLabel, columnData);
		return ++this.numColumns;
	}

	@Override
	public Object updateSingleCell(String columnLabel, int atIndex, Object newValue) {
		if (!this.columns.containsKey(columnLabel)) {
			System.err.println("ERR:\tColumn [" + columnLabel + "] not found!");
			return null;
		}
		if (this.numRows <= atIndex || atIndex < 0) {
			System.err.println("ERR:\tRow index out out bounds! [" + atIndex + "]");
			return null;
		}
		Object oldValue = this.columns.get(columnLabel).remove(atIndex);
		this.columns.get(columnLabel).add(atIndex, newValue);
		return oldValue;
	}

	@Override
	public void alterColumn(String columnLabel, Function<Object, Object> alterator) {
		if (!this.columns.containsKey(columnLabel)) {
			System.err.println("ERR:\tColumn [" + columnLabel + "] not found!");
			return;
		}
		List<Object> newList = this.columns.get(columnLabel).stream().map(alterator).collect(Collectors.toList());
		this.columns.put(columnLabel, newList);
	}

	@Override
	public File writeToFile(String filename) {
		File file = new File(filename);
		try (PrintWriter pr = new PrintWriter(file)) {
			writeHeader(pr);
			writeRows(pr);
		} catch (FileNotFoundException e) {
			System.err.println("Unable to create requested file: [" + filename + "]");
			return null;
		}
		return file;
	}

	private void writeHeader(PrintWriter pr) {
		StringBuilder header = new StringBuilder();
		for (String s : getHeaders())
			header.append(s).append(',');
		String head = header.substring(0, header.length() - 1);
		pr.println(head);
	}

	private void writeRows(PrintWriter pr) {
		getAllRows().forEachRemaining(row -> {
			pr.println(row.toCsvRow());
		});
	}

	@Override
	public OutputStream writeToStream(OutputStream outStream) {
		PrintWriter pr = new PrintWriter(outStream);
		writeHeader(pr);
		writeRows(pr);
		return outStream;
	}

}