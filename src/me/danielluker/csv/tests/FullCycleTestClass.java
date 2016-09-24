package me.danielluker.csv.tests;

import java.util.Iterator;

import me.danielluker.csv.Row;
import me.danielluker.csv.Table;

public class FullCycleTestClass {
	
	
	public static void printTable(Table t) {
		Iterator<Row> rows = t.getAllRows();
		for(String s : t.getHeaders()) {
			System.out.printf("%s\t", s);
		}
		System.out.println();
		while(rows.hasNext()) {
			Row row = rows.next();
			System.out.println(row.toStringWithColumnOrdering("\t"));
		};
	}
	
	
	public static final String INITIAL_FILE_LOCATION = "test.csv";


	public static void main(String[]args) throws Exception { 
		Table t = Table.createFromFile(INITIAL_FILE_LOCATION);
		printTable(t);
	}
	
	
}
