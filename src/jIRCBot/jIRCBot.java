package jIRCBot;

import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class jIRCBot extends ListenerAdapter {
        @Override
        public void onGenericMessage(GenericMessageEvent event) {
     	
        	// Ask the 8-ball
            if (event.getMessage().startsWith("!8ball")) {
                event.respondWith(EightBall.askTheBall());
            // Ask the BOFH
            } else if (event.getMessage().startsWith("!bofh")) {
            	event.respondWith(BOFH.askTheBOFH());
            // Learn a topic
            } else if (event.getMessage().startsWith("!learn")) {
            	String message = event.getMessage().toString();
            	String user = event.getUser().getNick().toString();
            	event.respondWith(Knowledge.learn(message, user));
            // Forget a topic
            } else if (event.getMessage().startsWith("!forget")) {
            	String message = event.getMessage().toString();
            	String user = event.getUser().getNick().toString();
            	event.respondWith(Knowledge.forget(message, user));
            // Query a topic
            } else if (event.getMessage().startsWith("?")) {
            	String message = event.getMessage().toString();
            	event.respondWith(Knowledge.query(message));
            // Return the title of a URL
            } else if (event.getMessage().contains("http")) {
            	String message = event.getMessage().toString();
            	String urlTitle = URLToolbox.getURLTitle(message);
            	if (urlTitle != null) {
            		event.respondWith("^ " + urlTitle);
            	}
            // Gracefully quit if requested by bot owner
            } else if (event.getMessage().startsWith("!quit")) {
            	String user = event.getUser().getNick().toString();
            	String owner = Owner.getOwner().toString();
            	if (Objects.equals(user, owner)) {
            		event.getBot().stopBotReconnect();
            		event.getBot().sendIRC().quitServer();
            	}
            }
        }
        
        /*
         * Main void
         */
        public static void main(String[] args) throws Exception {
        	
        	// Prepare the database
        	Knowledge kb = new Knowledge();
        	kb.createKnowledgeDB();
        	kb.createKnowledgeTable();

        	// Read in configuration and start the bot
        	try {
        		PropertiesConfiguration properties = new PropertiesConfiguration("config.properties");
        		String ircName			= properties.getString("ircName");
        		String ircLogin			= properties.getString("ircLogin");
        		String ircRealName		= properties.getString("ircRealName");
        		String ircServer		= properties.getString("ircServer");
        		String ircSASLPassword	= properties.getString("ircSASLPassword");
        		int ircPort				= properties.getInt("ircPort");
        		
        		String[] ircChannels = properties.getString("ircChannels").split("\\|");
        		
        		Iterable<String> channelList = Arrays.asList(ircChannels);
        		
	            // Configure what we want our bot to do
	            Configuration configuration = new Configuration.Builder()
	                            .setName(ircName)
	                            .setLogin(ircLogin)
	                            .setRealName(ircRealName)
	                            .addServer(ircServer, ircPort)
	                            .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
	                            .setSocketFactory(new UtilSSLSocketFactory().disableDiffieHellman())
	                            .addCapHandler(new SASLCapHandler(ircName, ircSASLPassword))
	                            .addAutoJoinChannels(channelList)
	                            .addListener(new jIRCBot())
	                            .buildConfiguration();

	            // Create our bot with the configuration
	            PircBotX bot = new PircBotX(configuration);
	            
	            // Connect to the server
	            bot.startBot();
	            
        	} catch (ConfigurationException e) {
        		System.out.print(e.getMessage());
        	}
        }
}