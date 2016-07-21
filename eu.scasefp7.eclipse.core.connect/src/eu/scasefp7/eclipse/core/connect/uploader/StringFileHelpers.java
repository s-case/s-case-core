package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import eu.scasefp7.eclipse.core.connect.Activator;

/**
 * Class that contains helper functions for reading files and string manipulation.
 * 
 * @author themis
 */
public class StringFileHelpers {

	/**
	 * Reads the contents of a file to string.
	 * 
	 * @param file the file of which the contents are read.
	 * @return the contents of the file.
	 */
	public static String readFileToString(File file) {
		String text = "";
		try {
			Scanner scanner = new Scanner(file);
			text = scanner.useDelimiter("\\A").next();
			scanner.close();
		} catch (FileNotFoundException e) {
			Activator.log("Error reading file " + file.getName(), e);
		}
		return text;
	}

	/**
	 * Reads the contents of an Eclipse file to string.
	 * 
	 * @param file the Eclipse file of which the contents are read.
	 * @return the contents of the Eclipse file.
	 */
	public static String readFileToString(IFile file) {
		String text = "";
		try {
			Scanner scanner = new Scanner(file.getContents());
			text = scanner.useDelimiter("\\A").next();
			scanner.close();
		} catch (CoreException e) {
			Activator.log("Error reading file " + file.getName(), e);
		}
		return text;
	}

	/**
	 * Converts a string to a sequence of bytes.
	 * 
	 * @param text the string to be converted to bytes.
	 * @return a byte array as a string of bytes delimited with commas.
	 */
	public static String convertStringToBytes(String text) {
		StringBuilder byteArrayBuilder = new StringBuilder();
		byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
		String delim = "";
		for (byte b : bytes) {
			byteArrayBuilder.append(delim);
			delim = ",";
			byteArrayBuilder.append(b);
		}
		return byteArrayBuilder.toString();
	}

}
