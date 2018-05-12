package jIRCBot;

import java.util.Arrays;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class jIRCBot extends ListenerAdapter {

	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onMessage(org.pircbotx.hooks.events.MessageEvent)
	 * 
	 * Performs actions from channel
	 * 
	 * Usage: !command <args>
	 */
	@Override
	public void onMessage(MessageEvent event) throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();

		// Learn topic
		if (event.getMessage().startsWith("!learn")) {
			String message = event.getMessage().toString();
			String user = event.getUser().getNick().toString();
			event.respondChannel(Knowledge.learn(message, user));
		// Forget topic
		} else if (event.getMessage().startsWith("!forget")) {
			String message = event.getMessage().toString();
			String user = event.getUser().getNick().toString();
			event.respondChannel(Knowledge.forget(message, user));
		// Display URL title
		} else if (event.getMessage().contains("http")) {
			String message = event.getMessage().toString();
			String urlTitle = URLToolbox.getURLTitle(message);
			Boolean showTitles = Boolean.parseBoolean(properties.getString("botURLTitles"));
			if (showTitles && (urlTitle != null)) {
				event.respondChannel("^ " + urlTitle);
			}
		}

	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onPrivateMessage(org.pircbotx.hooks.events.PrivateMessageEvent)
	 * 
	 * Performs actions from PRIVMSG
	 * 
	 * Usage: !<command> <password> <args>
	 */
	@Override
	public void onPrivateMessage(PrivateMessageEvent event) throws ConfigurationException {

		Global global = Global.getInstance();

		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();

		String authFailure = "Sorry, but you're not allowed to do that!";

		// Set password
		if (event.getMessage().startsWith("!setpass")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			if (Owner.setPassword(user, password)) {
				event.respondPrivateMessage("Password set");
			} else {
				event.respondPrivateMessage(authFailure);
			}
		// Add user
		} else if (event.getMessage().startsWith("!adduser")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			String userToAdd = command[2];
			if (Owner.authenticateOwner(user, password)) {
				if (Owner.addUser(userToAdd)) {
					event.respondPrivateMessage("Added user " + userToAdd);
				} else {
					event.respondPrivateMessage("User '" + userToAdd + "' already exists");
				}
			} else {
					event.respondPrivateMessage(authFailure);
			}
		// Delete user
		} else if (event.getMessage().startsWith("!deluser")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			String userToDel = command[2];
			if (Owner.authenticateOwner(user, password)) {
				if (Owner.delUser(userToDel)) {
					event.respondPrivateMessage("Deleted user " + userToDel);
				} else {
					event.respondPrivateMessage("User '" + userToDel + "' does not exist");
				}
			} else {
				event.respondPrivateMessage(authFailure);
			}
		// List users
		} else if (event.getMessage().startsWith("!listusers")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			if (Owner.authenticateOwner(user, password)) {
				event.respondPrivateMessage("Users: " + Owner.listUsers());
			} else {
				event.respondPrivateMessage(authFailure);
			}
		// Add bad word
		} else if (event.getMessage().startsWith("!addword")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			String wordToAdd = command[2];
			if (Owner.authenticateOwner(user, password)) {
				if (BadWords.addWord(wordToAdd)) {
					event.respondPrivateMessage("Added bad word: " + wordToAdd);
				} else {
					event.respondPrivateMessage("Bad word '" + wordToAdd + "' already exists");
				}
			} else {
					event.respondPrivateMessage(authFailure);
			}
		// Delete bad word
		} else if (event.getMessage().startsWith("!delword")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			String wordToDel = command[2];
			if (Owner.authenticateOwner(user, password)) {
				if (BadWords.delWord(wordToDel)) {
					event.respondPrivateMessage("Deleted bad word: " + wordToDel);
				} else {
					event.respondPrivateMessage("Bad word '" + wordToDel + "' does not exist");
				}
			} else {
				event.respondPrivateMessage(authFailure);
			}
		// List bad words
		} else if (event.getMessage().startsWith("!listwords")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			if (Owner.authenticateOwner(user, password)) {
				event.respondPrivateMessage("Bad words: " + BadWords.listWords());
			} else {
				event.respondPrivateMessage(authFailure);
			}
		// Quit
		} else if (event.getMessage().startsWith("!quit")) {
			String[] command = event.getMessage().split(" ");
			String user = event.getUser().getNick().toString();
			String password = command[1];
			if (Owner.authenticateOwner(user, password)) {
				event.respondPrivateMessage("Quitting");
				event.getBot().stopBotReconnect();
				event.getBot().sendIRC().quitServer();
			} else {
				event.respondPrivateMessage(authFailure);
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onGenericMessage(org.pircbotx.hooks.types.GenericMessageEvent)
	 * 
	 * Perform actions from channel or PRIVMSG
	 * 
	 * Usage: ?<topic> | !<command> <args>
	 */
	@Override
	public void onGenericMessage(GenericMessageEvent event) throws ConfigurationException {

		Global global = Global.getInstance();

		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();

		// Query topic
		if (event.getMessage().startsWith("?")) {
			String message = event.getMessage().toString();
			event.respondWith(Knowledge.query(message));
		// 8 Ball
		}  else if (event.getMessage().startsWith("!8ball")) {
			event.respondWith(Toys.EightBall());
		// BOFH
		} else if (event.getMessage().startsWith("!bofh")) {
			event.respondWith(Toys.BOFH());
		// Flip a coin
		} else if (event.getMessage().startsWith("!flipcoin")) {
			event.respondWith(Toys.FlipCoin());
		}
		
	}

	/*
	* jIRCBot
	*/
	public static void main(String[] args) throws Exception {

		// Prepare the knowledge database
		Knowledge kb = new Knowledge();
		kb.createKnowledgeDB();
		kb.createKnowledgeTable();

		// Read configuration and start
		try {

			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
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
			
			// Close the bot
			bot.close();

		} catch (ConfigurationException e) {
			
			System.out.print(e.getMessage());
			
		}
		
	}
	
}