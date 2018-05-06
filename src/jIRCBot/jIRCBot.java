package jIRCBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class jIRCBot extends ListenerAdapter {
        @Override
        public void onGenericMessage(GenericMessageEvent event) {
     	
        	// Ask the 8ball
            if (event.getMessage().startsWith("!8ball")) {
                event.respondWith(EightBall.askTheBall());
            }
            // BOFH
            if (event.getMessage().startsWith("!bofh")) {
            	event.respondWith(BOFH.askTheBOFH());
            }
            // Learn a topic
            if (event.getMessage().startsWith("!learn")) {
            	String message = event.getMessage().toString();
            	String user = event.getUser().getNick().toString();
            	event.respond(Knowledge.learn(message, user));
            }
            // Forget a topic
            if (event.getMessage().startsWith("!forget")) {
            	String message = event.getMessage().toString();
            	String user = event.getUser().getNick().toString();
            	event.respond(Knowledge.forget(message, user));
            }
            // Query a topic
            if (event.getMessage().startsWith("?")) {
            	String message = event.getMessage().toString();
            	event.respondWith(Knowledge.query(message));
            }
            // Quit gracefully
            if (event.getMessage().startsWith("!quit")) {
            	event.getBot().stopBotReconnect();
            	event.getBot().sendIRC().quitServer("Bye bye!");
            }
        }
        
        public static void main(String[] args) throws Exception {
        		
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
        	
        	// Read in strings
        	String ircName		= prop.getProperty("ircName");
        	String ircLogin		= prop.getProperty("ircLogin");
        	String ircRealName	= prop.getProperty("ircRealName");
        	String ircServer	= prop.getProperty("ircServer");
        	String ircChannels	= prop.getProperty("ircChannels");
        	
        	// Read in integers
        	int ircPort = Integer.parseInt(prop.getProperty("ircPort"));
        	
            //Configure what we want our bot to do
            Configuration configuration = new Configuration.Builder()
                            .setName(ircName)
                            .setLogin(ircLogin)
                            .setRealName(ircRealName)
                            .addServer(ircServer, ircPort)
                            .addAutoJoinChannel(ircChannels)
                            .addListener(new jIRCBot())
                            .buildConfiguration();

            //Create our bot with the configuration
            PircBotX bot = new PircBotX(configuration);
            //Connect to the server
            bot.startBot();
        }
}