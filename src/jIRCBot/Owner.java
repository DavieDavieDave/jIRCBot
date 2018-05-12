package jIRCBot;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.mindrot.jbcrypt.*;

public class Owner {
	
	/*
	 * Get the bot owner
	 */
	public static String getOwner() throws ConfigurationException {
		
		try {
			
			Global global = Global.getInstance();
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			String botOwner = properties.getString("botOwner");
			return botOwner;
			
		} catch (ConfigurationException e) {
			
			System.out.print(e.getMessage());
			
		}
		
		return null;
		
	}

	/*
	 * Set the bot owner
	 */
	public static Boolean setPassword(String user, String password) {
		
		try {
			
			Global global = Global.getInstance();
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			
			String bcryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());

			String botOwner = properties.getString("botOwner");

			if (user.equals(botOwner)) {
			
				properties.setProperty("botOwnerPassword", bcryptPassword);
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
	 * Authenticate the bot owner
	 */
	public static Boolean authenticateOwner(String user, String password) {

		try {
		
			Global global = Global.getInstance();
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			
			String botOwner = properties.getString("botOwner");
			String botOwnerPassword = properties.getString("botOwnerPassword");
			
			if (user.equals(botOwner) && BCrypt.checkpw(password, botOwnerPassword)) {
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
	 * Add a user
	 */
	public static Boolean addUser(String user) {
		
		try {
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();
			
			String[] botUsers = properties.getString("botUsers").split("\\|");

			if (!Arrays.asList(botUsers).contains(user)) {

				String[] tempArray = new String[ botUsers.length + 1 ];
				for (int i=0; i<botUsers.length; i++)
				{
				    tempArray[i] = botUsers[i];
				}
				tempArray[botUsers.length] = user;
				botUsers = tempArray;   
	
				String newBotUsers = String.join("|", botUsers);
				
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
			properties.reload();
			
			String[] botUsers = properties.getString("botUsers").split("\\|");
			
			if (Arrays.asList(botUsers).contains(user)) {
			
				botUsers = (String[]) ArrayUtils.removeElement(botUsers, user);
				
				String newBotUsers = String.join("|", botUsers);
				
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
	public static String listUsers() {
		
		try {
			
			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.reload();
				
			return properties.getString("botUsers").replaceAll("\\|", ", ");
			
		} catch (ConfigurationException e) {
			
			System.out.print(e.getMessage());
			
		}
		
		return null;
	}
	
}
