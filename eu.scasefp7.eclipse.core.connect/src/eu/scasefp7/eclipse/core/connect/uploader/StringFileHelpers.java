package eu.scasefp7.eclipse.core.connect.uploader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import eu.scasefp7.eclipse.core.connect.Activator;

public class StringFileHelpers {

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
