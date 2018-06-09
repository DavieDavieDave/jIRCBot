package com.invaliddomain.jircbot.tools;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;

import com.invaliddomain.jircbot.Global;

public class BadWords {

	/* 
	 * Searches for bad words from the sting provided and returns a boolean
	 */
	public boolean badWords(String line) {

		try {

			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();

			String[] badWords = properties.getStringArray("badWords");
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
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			String[] badWords = properties.getStringArray("badWords");

			if (!Arrays.asList(badWords).contains(word.toLowerCase())) {

				String[] tempArray = new String[ badWords.length + 1 ];
				for (int i=0; i<badWords.length; i++)
				{
				    tempArray[i] = badWords[i];
				}
				tempArray[badWords.length] = word.toLowerCase();
				badWords = tempArray;   
	
				String newBadWords = String.join(",", badWords);
				
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
			properties.setListDelimiter('\u002C');
			properties.reload();

			String[] badWords = properties.getStringArray("badWords");

			if (Arrays.asList(badWords).contains(word.toLowerCase())) {

				badWords = (String[]) ArrayUtils.removeElement(badWords, word.toLowerCase());
				
				String newBadWords = String.join(",", badWords);
				
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
			properties.setListDelimiter('\u002C');
			properties.reload();

			String[] badWords = properties.getStringArray("badWords");
			
			return String.join(", ", badWords);

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}

		return null;
	}

}
