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
		
		String user = event.getUser().getNick().toString();
		String command = event.getMessage();

		// Learn topic
		if (command.startsWith("!learn")) {
			event.respondChannel(Knowledge.learn(command, user));
		// Forget topic
		} else if (command.startsWith("!forget")) {
			event.respondChannel(Knowledge.forget(command, user));
		// Display URL title
		} else if (command.contains("http")) {
			String url = URLToolbox.getURLTitle(command);
			if (Boolean.parseBoolean(properties.getString("botURLTitles")) && (url != null) ) {
				event.respondChannel("^ " + url);
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

		String user = event.getUser().getNick().toString();
		String mask = event.getUser().getHostmask().toString();
		
		String command = event.getMessage();
		
		// Authenticate
		if (command.startsWith("auth")) {
			String[] args = event.getMessage().split(" ");
			String password = args[1];
			if(Owner.authenticateOwner(user, password, mask)) {
				event.respondPrivateMessage("Authenticated");
			}
		// Set password
		} else if (command.startsWith("setpass")) {
			String[] args = event.getMessage().split(" ");
			String password = args[1];
			if(Owner.setPassword(user, password, mask)) {
				event.respondPrivateMessage("Password set");
			}
		// Add user
		} else if (command.startsWith("adduser")) {
			String[] args = event.getMessage().split(" ");
			String userToAdd = args[1];
			if(Owner.isOwner(user, mask) && Owner.addUser(userToAdd)) {
				event.respondPrivateMessage("User added");
			}
		// Del user
		} else if (command.startsWith("deluser")) {
			String[] args = event.getMessage().split(" ");
			String userToDel = args[1];
			if(Owner.isOwner(user, mask) && Owner.delUser(userToDel)) {
				event.respondPrivateMessage("User deleted");
			}
		// List users
		} else if (command.startsWith("listusers")) {
			if(Owner.isOwner(user, mask)) {
				event.respondPrivateMessage(Owner.listUsers());
			}
		// Add bad word
		} else if (command.startsWith("addword")) {
			String[] args = event.getMessage().split(" ");
			String wordToAdd = args[1];
			if(Owner.isOwner(user, mask) && BadWords.addWord(wordToAdd)) {
				event.respondPrivateMessage("Word added");
			}
		// Delete bad word
		} else if (command.startsWith("delword")) {
			String[] args = event.getMessage().split(" ");
			String wordToDel = args[1];
			if(Owner.isOwner(user, mask) && BadWords.delWord(wordToDel)) {
				event.respondPrivateMessage("Word deleted");
			}
		// List bad words
		} else if (command.startsWith("listwords")) {
			if(Owner.isOwner(user, mask)) {
				event.respondPrivateMessage(BadWords.listWords());
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

		String command = event.getMessage();

		// Query topic
		if (command.startsWith("?")) {
			event.respondWith(Knowledge.query(command));
		// Version
		} else if (command.startsWith("!version")) {
			event.respondWith("jIRCBot Version " + global.version);
		// 8 Ball
		} else if (command.startsWith("!8ball")) {
			event.respondWith(Toys.EightBall());
		// BOFH
		} else if (command.startsWith("!bofh")) {
			event.respondWith(Toys.BOFH());
		// Flip a coin
		} else if (command.startsWith("!flipcoin")) {
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

		Global global = Global.getInstance();

		Iterable<String> channelList = Arrays.asList(global.ircChannels);

		// Configure what we want our bot to do
		Configuration configuration = new Configuration.Builder()
				.setName(global.ircName)
				.setLogin(global.ircLogin)
				.setRealName(global.ircRealName)
				.addServer(global.ircServer, global.ircPort)
				.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
				.setSocketFactory(new UtilSSLSocketFactory().disableDiffieHellman())
				.addCapHandler(new SASLCapHandler(global.ircName, global.ircSASLPassword))
				.addAutoJoinChannels(channelList)
				.addListener(new jIRCBot())
				.buildConfiguration();

		// Create our bot with the configuration
		PircBotX bot = new PircBotX(configuration);

		// Connect to the server
		bot.startBot();
		
		// Close the bot
		bot.close();
		
	}
	
}