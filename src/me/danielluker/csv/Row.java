package me.danielluker.csv;

import java.util.HashMap;
import java.util.Map;

/***
 * Class used to append and remove from a {@link Table} object
 * @author danielluker
 *
 */
public class Row {

	private Map<String, Object> values;
	private String[] columnIndices;

	Row(String[] columns, Object[] vals) {
		this.values = new HashMap<>();
		for (int i = 0; i < columns.length; i++) {
			this.values.put(columns[i], vals[i]);
		}
	}

	Row(Map<String, Object> vals, String[] columnIndices) {
		this.values = vals;
		this.columnIndices = columnIndices;
	}
	
	public Map<String, Object> getValues() {
		return this.values;
	}
	
	public String toCsvRow() {
		StringBuilder sb = new StringBuilder();
		for(Object o : this.values.values()) 
			sb.append(o).append(',');
		return sb.substring(0, sb.length() - 1);
	}
	
	public Object getCell(String columnLabel) {
		return this.values.get(columnLabel);
	}
	
	public Object getCell(int columnIndex) {
		return this.values.get(this.columnIndices[columnIndex]);
	}
	
	@Override
	public String toString() {
		return this.values.toString();
	}
	
	public String toStringWithColumnOrdering(String delimiter) {
		StringBuilder sb = new StringBuilder();
		for(String s : this.columnIndices) 
			sb.append(this.values.get(s) + delimiter + " ");
		return sb.substring(0, sb.length()-delimiter.length()-1);
	}

}
