package me.danielluker.csv;

import java.io.File;
import java.io.OutputStream;

public interface iTableWriter {

	public File writeToFile(String filename);
	
	public OutputStream writeToStream(OutputStream outStream);
	
}
