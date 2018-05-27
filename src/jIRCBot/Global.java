package jIRCBot;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

class Global {

	private static Global instance = null;
	
	// Configuration file
	public String config;
	
	// Version
	public String version;
	public String source;
	
	// Files
	public String knowledgeDB;
	public String urlDB;
	
	// Properties
	public String ircName;
	public String ircLogin;
	public String ircRealName;
	public String ircServer;
	public String ircSASLPassword;
	public String[] ircChannels;
	public int ircPort;
	public Boolean ircJoinOnKick;
	
	private Global() throws ConfigurationException {

		// Configuration file
		config = "config.properties";
		
		PropertiesConfiguration properties = new PropertiesConfiguration(config);
		properties.setListDelimiter('\u002C');
		properties.reload();
		
		// Version
		version			= "0.1";
		source			= "github.com/DavieDavieDave/jIRCBot";
		
		// Files
		knowledgeDB		= properties.getString("dbKnowledge");
		urlDB			= properties.getString("dbUrl");
		
		// Properties
		ircName			= properties.getString("ircName");
		ircLogin		= properties.getString("ircLogin");
		ircRealName		= properties.getString("ircRealName");
		ircServer		= properties.getString("ircServer");
		ircPort			= properties.getInt("ircPort");
		ircSASLPassword	= properties.getString("ircSASLPassword");
		ircChannels		= properties.getStringArray("ircChannels");
		ircJoinOnKick	= Boolean.parseBoolean(properties.getString("ircJoinOnKick"));
		
	}
	
	public static Global getInstance() throws ConfigurationException {
		
		if (instance == null)
			instance = new Global();
		
		return instance;
		
	}
	
}
