package com.invaliddomain.jircbot.tools;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;

import com.invaliddomain.jircbot.Global;

public class User {

	/*
	 * Add a user
	 */
	public static Boolean addUser(String user) {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			String[] botUsers = properties.getStringArray("botUsers");
			
			if (!Arrays.asList(botUsers).contains(user)) {

				String[] tempArray = new String[ botUsers.length + 1 ];
				for (int i=0; i<botUsers.length; i++)
				{
				    tempArray[i] = botUsers[i];
				}
				tempArray[botUsers.length] = user;
				botUsers = tempArray;   
	
				String newBotUsers = String.join(",", botUsers);
				
				properties.setProperty("botUsers", newBotUsers);
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
	 * Delete a user
	 */
	public static Boolean delUser(String user) {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			String[] botUsers = properties.getStringArray("botUsers");
			
			if (Arrays.asList(botUsers).contains(user)) {
			
				botUsers = (String[]) ArrayUtils.removeElement(botUsers, user);
				
				String newBotUsers = String.join(",", botUsers);
				
				properties.setProperty("botUsers", newBotUsers);
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
	 * List users
	 */
	public static String listUsers() throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.setListDelimiter('\u002C');
		properties.reload();
		
		return String.join(", ", properties.getStringArray("botUsers"));

	}
	
	
	/*
	 * Add a bad user
	 */
	public static Boolean addBadUser(String user) {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			String[] badUsers = properties.getStringArray("badUsers");
			
			if (!Arrays.asList(badUsers).contains(user)) {

				String[] tempArray = new String[ badUsers.length + 1 ];
				for (int i=0; i<badUsers.length; i++)
				{
				    tempArray[i] = badUsers[i];
				}
				tempArray[badUsers.length] = user;
				badUsers = tempArray;   
	
				String newBadUsers = String.join(",", badUsers);
				
				properties.setProperty("badUsers", newBadUsers);
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
	 * Delete a bad user
	 */
	public static Boolean delBadUser(String user) {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			String[] badUsers = properties.getStringArray("badUsers");
			
			if (Arrays.asList(badUsers).contains(user)) {
			
				badUsers = (String[]) ArrayUtils.removeElement(badUsers, user);
				
				String newBadUsers = String.join(",", badUsers);
				
				properties.setProperty("badUsers", newBadUsers);
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
	 * List bad users
	 */
	public static String listBadUsers() throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.setListDelimiter('\u002C');
		properties.reload();
		
		return String.join(", ", properties.getStringArray("badUsers"));

	}
	
	/*
	 * Compares the user to a know list of bad users and returns a boolean
	 */
	public static boolean isBadUser(String user) {

		try {

			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();

			if (Arrays.asList(properties.getStringArray("badUsers")).contains(user)) {
				return true;
			} else {
				return false;
			}

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}

		return false;

	}
	
}
