package jIRCBot;

class Global {
	
	/* these are bad! -- use singletons!
	public static String config			= "config.properties";
	public static String knowledgeDB	= "knowledge.db";
	public static String urlDB			= "url.db";
	*/
	
	private static Global instance = null;
	
	public String config;
	public String knowledgeDB;
	public String urlDB;
	
	private Global() {
		config		= "config.properties";
		knowledgeDB	= "knowledge.db";
		urlDB		= "url.db";
	}
	
	public static Global getInstance() {
		if (instance == null)
			instance = new Global();
		return instance;
	}
	
}
