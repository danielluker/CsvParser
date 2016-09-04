package me.danielluker.csv;

import java.util.List;
import java.util.function.Function;

public interface iTable extends iReadOnlyTable, iTableWriter {
	
	public int appendRow(Row newRow);

	public Row removeLastRow();

	public int insertRow(int atIndex, Row newRow);

	public Row deleteRow(int atIndex);

	public Row updateRow(int atIndex, Row updatedRow);

	public List<Object> dropColumn(String columnLabel);

	public int addColumn(String columnLabel);

	public int addColumnFromList(String columnLabel, List<Object> columnData);

	public Object updateSingleCell(String columnLabel, int atIndex, Object newValue);

//	public <T> void setColumnDataType(String columnLabel, Class<T> dataType);

	public void alterColumn(String columnLabel, Function<Object, Object> alterator);

}