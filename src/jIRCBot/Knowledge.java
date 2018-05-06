package jIRCBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Knowledge {
	
	public static String learn(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)\\s(.*)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "OK, I now know about " + m.group(2);
			return answer;
		}
		return "Sorry, I don't understand!";
	}
	
	public static String forget(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "OK, I forgot about " + m.group(2);
			return answer;
		} 
		return "Sorry, I don't understand!";
	}
	
	public static String query(String message) {
		Pattern pattern = Pattern.compile("\\?(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "Here's what I know about " + m.group(1);
			return answer;
		}
		return "Sorry, but I don't know about" + m.group(1);
	}
	
	public static String tell(String message, String sender, String receiver) {
		// TODO allow the bot to 'tell' somebody about a topic
		return null;
	}

}
