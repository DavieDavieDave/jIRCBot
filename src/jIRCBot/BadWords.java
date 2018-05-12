package jIRCBot;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;

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

	/*
	 * Add word
	 */
	public static Boolean addWord(String word) {
		
		try {
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();
			
			String[] badWords = properties.getString("badWords").split("\\|");

			if (!Arrays.asList(badWords).contains(word)) {

				String[] tempArray = new String[ badWords.length + 1 ];
				for (int i=0; i<badWords.length; i++)
				{
				    tempArray[i] = badWords[i];
				}
				tempArray[badWords.length] = word;
				badWords = tempArray;   
	
				String newBadWords = String.join("|", badWords);
				
				properties.setProperty("badWords", newBadWords);
				properties.save();
				
				return true;
			
			} else {
				
				return false;
				
			}

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}
		
		return false;

	}

	/*
	 * Delete word
	 */
	public static Boolean delWord(String word) {

		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();

			String[] badWords = properties.getString("badWords").split("\\|");

			if (Arrays.asList(badWords).contains(word)) {

				badWords = (String[]) ArrayUtils.removeElement(badWords, word);
				
				String newBadWords = String.join("|", badWords);
				
				properties.setProperty("badWords", newBadWords);
				properties.save();
				
				return true;
				
			} else {

				return false;

			}

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}

		return false;

	}

	/*
	 * List words
	 */
	public static String listWords() {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();

			return properties.getString("badWords").replaceAll("\\|", ", ");

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}

		return null;
	}

}
