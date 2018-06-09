package com.invaliddomain.jircbot.tools;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mindrot.jbcrypt.*;

import com.invaliddomain.jircbot.Global;

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
	
}
