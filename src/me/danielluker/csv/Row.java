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

	Row(String[] columns, Object[] vals) {
		this.values = new HashMap<>();
		for (int i = 0; i < columns.length; i++) {
			this.values.put(columns[i], vals[i]);
		}
	}

	Row(Map<String, Object> vals) {
		this.values = vals;
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
	
	@Override
	public String toString() {
		return this.values.toString();
	}

}
