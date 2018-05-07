package jIRCBot;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Owner {

	static String botConfig = "config.properties";
	
	/*
	 * Get the bot owner
	 */
	public static String getOwner() {
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration(botConfig);
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
			PropertiesConfiguration properties = new PropertiesConfiguration(botConfig);
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
