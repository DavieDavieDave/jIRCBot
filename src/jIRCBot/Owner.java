package jIRCBot;

import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.mindrot.jbcrypt.*;

public class Owner {
	
	private static Owner instance = null;
	
	public String botOwner;
	public String botOwnerMask;
	public String botOwnerPassword;
	public String[] botUsers;
	
	private Owner() throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		try {

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();
			
			botOwner			= properties.getString("botOwner");
			botOwnerMask		= properties.getString("botOwnerMask");
			botOwnerPassword	= properties.getString("botOwnerPassword");
			botUsers			= properties.getStringArray("botUsers");
			
		} catch (ConfigurationException e) {
			System.out.print(e.getMessage());
		}
		
	}
	
	public static Owner getInstance() throws ConfigurationException {
		
		if (instance == null)
			instance = new Owner();
		
		return instance;
		
	}
	
	/*
	 * Authenticate the owner
	 */
	public Boolean authenticateOwner(String user, String password, String mask) throws ConfigurationException {
		
		Global global = Global.getInstance();
		Owner owner = Owner.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		
		if (user.equals(owner.botOwner) && BCrypt.checkpw(password, owner.botOwnerPassword)) {
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
	public Boolean isOwner(String user, String mask) throws ConfigurationException {
		
		Owner owner = Owner.getInstance();
		if (user.equals(owner.botOwner) && mask.equals(owner.botOwnerMask))
			return true;
		
		return false;
		
	}
	
	/*
	 * Set owner password
	 */
	public Boolean setPassword(String user, String password, String mask) {
		
		try {
			
			Global global = Global.getInstance();
			Owner owner = Owner.getInstance();
			
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			
			if (user.equals(owner.botOwner) && mask.equals(owner.botOwnerMask)) {
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
	public Boolean addUser(String user) {
		
		try {
			
			Global global = Global.getInstance();
			Owner owner = Owner.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			
			if (!Arrays.asList(owner.botUsers).contains(user)) {

				String[] tempArray = new String[ owner.botUsers.length + 1 ];
				for (int i=0; i<owner.botUsers.length; i++)
				{
				    tempArray[i] = owner.botUsers[i];
				}
				tempArray[owner.botUsers.length] = user;
				owner.botUsers = tempArray;   
	
				String newBotUsers = String.join(",", owner.botUsers);
				
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
	public Boolean delUser(String user) {
		
		try {
			
			Global global = Global.getInstance();
			Owner owner = Owner.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			
			if (Arrays.asList(owner.botUsers).contains(user)) {
			
				owner.botUsers = (String[]) ArrayUtils.removeElement(owner.botUsers, user);
				
				String newBotUsers = String.join(",", owner.botUsers);
				
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
	public String listUsers() throws ConfigurationException {
		
		Owner owner = Owner.getInstance();
		return String.join(", ", owner.botUsers);

	}
	
}
