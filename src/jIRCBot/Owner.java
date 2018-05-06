package jIRCBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Owner {

	public static String getOwner() {
		
    	// Read in config.properties	
    	Properties prop = new Properties();
    	InputStream input = null;
    		
    	try {
    		input = new FileInputStream("config.properties");
    		prop.load(input);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	String botOwner	= prop.getProperty("botOwner");
		
		return botOwner;
	}

}
