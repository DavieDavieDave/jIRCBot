package jIRCBot;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class BadWords {

	/* 
	 * Searches for bad words from the sting provided and returns a boolean
	 */
	public boolean badWords(String line) {

		try {

			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);

			properties.reload();

			String[] badWords = properties.getString("badWords").toLowerCase().split("\\|");
			String[] wordList = line.toLowerCase().split("\\s+");
			
			for (String word : wordList) {

				return Arrays.asList(badWords).contains(word);

			}
			
		} catch (ConfigurationException e) {
			
			System.out.print(e.getMessage());
			
		}
		
		return false;
		
	}

}
