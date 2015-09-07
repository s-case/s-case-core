package eu.scasefp7.eclipse.core.ontologytoyamltools;

import java.util.Arrays;
import java.util.HashSet;

/**
 * A stemmer for english language words.
 * 
 * @author themis
 */
public class Stemmer {

	/**
	 * Stems a word given as input and returns the stemmed term.
	 * 
	 * @param word the word to be stemmed.
	 * @return the stemmed term.
	 */
	public static String stem(String word) {
		word = word.toLowerCase();
		word = word.split("_")[0];
		int wordLength = word.length();

		// Add exceptions here
		HashSet<String> exceptions = new HashSet<String>(Arrays.asList("user", "developer"));

		// Returned the stemmed word
		if (exceptions.contains(word))
			return word;
		else if (word.endsWith("iness"))
			return word.substring(0, wordLength - 3) + "y";
		else if (word.endsWith("tion") || word.endsWith("sion") || word.endsWith("full") || word.endsWith("ical"))
			return word.substring(0, wordLength - 4);
		else if (word.endsWith("ness"))
			return word.substring(0, wordLength - 3);
		else if (word.endsWith("sses"))
			return word.substring(0, wordLength - 2);
		else if (word.endsWith("ist") || word.endsWith("ize") || word.endsWith("ful") || word.endsWith("ing"))
			return word.substring(0, wordLength - 3);
		else if (word.endsWith("ies"))
			return word.substring(0, wordLength - 2);
		else if (word.endsWith("ily"))
			return word.substring(0, wordLength - 3) + "y";
		else if (word.endsWith("eed"))
			return word.substring(0, wordLength - 3) + "ed";
		else if (word.endsWith("er") || word.endsWith("or") || word.endsWith("en") || word.endsWith("ic")
				|| word.endsWith("ed") || word.endsWith("ly") || word.endsWith("es"))
			return word.substring(0, wordLength - 2);
		else if (word.endsWith("s"))
			return word.substring(0, wordLength - 1);
		else
			return word;
	}
}
