package me.danielluker.csv;

import java.util.Iterator;
import java.util.List;

public interface iReadOnlyTable {

	public List<Object> getColumn(String columnLabel);

	public List<Object> getColumn(int columnIndex);
	
	public Row getRow(int rowNumber);

	public Row getRowByColumnValue(String columnLabel, Object value);

	public String[] getHeaders();

	public Iterator<Row> getAllRows();
	
	public String getCell(int rowIndex, int colIndex);
	
}
