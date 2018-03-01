# CsvParser
A simple and lightweight Java CSV parser library

## Code Examples

### Reading from a table

```java
import me.danielluker.csv.Table;

	/***
	 * Example method which will use a table saved in a file.
	 *
	 * @param filename
	 *            - Relative or absolute path to csv file
	 */
	public void useTable(String filename) {

		// Instantiating the table object --- SEE DIFFERENT createFromFile
		// method overloadings to configure how the table is parsed and can be
		// used
		Table t = Table.createFromFile(filename);

		// Retrieving the value (as a string) of the jth column in the ith row
		int i = 5, j = 2; // for example
		String val = t.getCell(i, j);
		System.out.println(val);

		// Retrieving an entire column to use its values...
		/*
		 * Suppose we know the third column contains all numbers, and we want to
		 * add these values to get a total
		 */
		int columnIndex = 2; // 0-based index
		int sum = 0;
		for(Object s : t.getColumn(columnIndex))
			sum += Integer.parseInt((String) s);
		System.out.println(sum);
		
		// Printing out the values of a specific row... for example, the 6th row
		int rowIndex = 5;
		System.out.println(t.getRow(rowIndex).toString());
		// If we want to print the values in the order they are in the file, we do:
		System.out.println(t.getRow(rowIndex).toStringWithColumnOrdering(",")); // or we can provide any delimiter
		
	}
```
	
### Writing to a table
	
*This is still in progress*. If you want to help, please do a pull request... have implemented a basic "addColumn" method, but need to make sure it works. Adding a row is done.
I will also implement creating tables from scratch!
	
### Writing a table to file

```java
// followed from the variables above
String newFile = "/example/file/path/example.csv";
File f = t.writeToFile(newFile);
```

So, you can load a csv table (or create one from scratch), read the values, add your own data, and save it to a file!. Simple, with intuitive and natural API calls.

## More complex functionality

There are other cool things you can do. For instance, you can cast entire columns to certain values.
So, if you know, let's say, the second column contains all decimal values, you could do the following:

```java
int columnIndex = 1;
String label = table.getHeaders()[columnIndex];
// cast the values from strings into double
table.alterColumn(label, s -> Double.parseDouble((String) s);
// now, let's say we want to replace these values with a very simple normalized value: the difference from the mean
// First, we need to find the mean (using the sum calculated above, in the Reading section)
double mean = (double) sum / (double) table.getNumRows();
// Now we apply the normalization function to all the column
table.alterColumn(label, val -> (double) val - mean);
```
Now, that column will be replaced with the new normalized value. Lambda's are very powerful, and in this scenario allow the flexibility to do anything with your data



___
Daniel F. Luker 
	
