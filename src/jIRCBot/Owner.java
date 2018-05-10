package jIRCBot;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Owner {
	
	/*
	 * Get the bot owner
	 */
	public static String getOwner() {
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
	public static void setOwner(String user, String password) {
		try {
			Global global = Global.getInstance();
			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setProperty("botOwner", user);
			properties.setProperty("botOwnerPassword", password);
			properties.save();
		} catch (ConfigurationException e) {
			System.out.print(e.getMessage());
		}
	}
	
	/*
	 * Authenticate the bot owner
	 */
	public static void passwordAuth(String user, String password) {
		// TODO Simple password authentication
	}
	
}
