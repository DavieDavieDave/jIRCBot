package jIRCBot;

import java.util.Arrays;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.mindrot.jbcrypt.*;

public class Owner {
		
	/*
	 * Authenticate the owner
	 */
	public static Boolean authenticateOwner(String user, String password, String mask) throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();
		
		if (BCrypt.checkpw(password, properties.getString("botOwnerPassword"))) {
			properties.setProperty("botOwner", user);
			properties.setProperty("botOwnerMask", mask);
			properties.save();
			return true;
		}

		return false;
		
	}
	
	/*
	 * Verify owner
	 */
	public static Boolean isOwner(String user, String mask) throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();
		
		if (user.equals(properties.getString("botOwner")) && mask.equals(properties.getString("botOwnerMask")))
			return true;

		//throw new Exception("Authentication Error");
		return false;
		
	}
	
	/*
	 * Set owner password
	 */
	public static Boolean setPassword(String user, String password, String mask) {
		
		try {
			
			Global global = Global.getInstance();
			
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();
			
			if (user.equals(properties.getString("botOwner")) && mask.equals(properties.getString("botOwnerMask"))) {
				String bcryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				properties.setProperty("botOwnerPassword", bcryptPassword);
				properties.save();
				return true;
			} 
						
		} catch (ConfigurationException e) {
			System.out.print(e.getMessage());
		}
		
		return false;
		
	}


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
	
}
