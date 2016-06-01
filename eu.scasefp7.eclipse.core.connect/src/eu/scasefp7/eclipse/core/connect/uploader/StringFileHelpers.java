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
		String byteArray = "";
		byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
		for (byte b : bytes) {
			byteArray += b;
			byteArray += ",";
		}
		byteArray = byteArray.length() > 0 ? byteArray.substring(0, byteArray.length() - 1) : "";
		return byteArray;
	}

}
