package eu.scasefp7.eclipse.core.ontologytoyamltools;

/**
 * A class that holds string manipulation functions.
 * 
 * @author themis
 */
public class StringHelpers {

	/**
	 * Receives a word construct split in underscores and converts it to camelCase.
	 * Example: virtual_machine --> virtualMachine
	 * 
	 * @param wordConstruct the word construct to change its split.
	 * @return the camelCased word construct
	 */
	public static String underscoreToCamelCase(String wordConstruct) {
		String[] words = wordConstruct.split("_");
		String camelCaseWordConstruct = "";
		for (String word : words) {
			if (!camelCaseWordConstruct.equals(""))
				camelCaseWordConstruct += word.substring(0, 1).toUpperCase() + word.substring(1);
			else
				camelCaseWordConstruct += word;
		}
		return camelCaseWordConstruct;
	}
}
