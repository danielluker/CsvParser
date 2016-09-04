package me.danielluker.csv;


public interface iReadOnlyTable {

	public Object[] getColumn(String columnLabel);

	public Map<String, ?> getRow(int rowNumber);

	public Map<String, ?> getRowByColumnValue(String columnLabel, Object value);

	public List<String> getHeaders();

	public Iterator<Map<String, ?>> getAllRows();

}