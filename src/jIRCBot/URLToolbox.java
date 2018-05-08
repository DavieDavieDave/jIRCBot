package jIRCBot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

public class URLToolbox {
	
	/*
	 * Return the <TITLE> of the URL provided
	 */
	public static String getURLTitle(String url) {

		String rxUrl = ".*((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
		Pattern pattern = Pattern.compile(rxUrl);
		Matcher m = pattern.matcher(url);
		
		if (m.matches()) {
		
			String parsedURL = m.group(1);
			InputStream response = null;
		    try {
		    
		    	response = new URL(parsedURL).openStream();
		        Scanner scanner = new Scanner(response);
		        String responseBody = scanner.useDelimiter("\\A").next();
		        String urlTitle = (responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>")));
		        scanner.close();
		        
		        String title = StringEscapeUtils.unescapeHtml(urlTitle.trim());
		        
		        BadWords bw = new BadWords();
		        
		        if (!bw.badWords(title)) {
		        	return title;
		        } else {
		        	return null;
		        }
		        
		    } catch (IOException ex) {
		        
		    	ex.printStackTrace();
		    	
		    } finally {
		        
		    	try {
		            response.close();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    	
		    }
		    
		}
	    
		return null;
	}
	
}