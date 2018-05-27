package jIRCBot;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.VersionEvent;
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
		String eventText = event.getMessage();
		String command = null;
		String args = null;
		
		if(eventText.contains(" ")) {
			String[] tmpArr = eventText.split(" ", 2);
			command = tmpArr[0];
			args = tmpArr[1];
		} else {
			command = event.getMessage();
			args = null;
		}
		
		switch (command.toLowerCase()) {
		case "!learn":
			event.respondChannel(Knowledge.learn(args, user));
			break;
		case "!forget":
			event.respondChannel(Knowledge.forget(args, user));
			break;
		default:
			if (eventText.contains("http")) {
				String url = URL.getURLTitle(eventText);
				if (Boolean.parseBoolean(properties.getString("botURLTitles")) && (url != null) ) {
					event.respondChannel("^ " + url);
				}
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
		Knowledge kb = new Knowledge();

		PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
		properties.reload();

		String user = event.getUser().getNick().toString();
		String mask = event.getUser().getHostmask().toString();
		String eventText = event.getMessage();
		String command = null;
		String args = null;
		
		if(eventText.contains(" ")) {
			String[] tmpArr = eventText.split(" ", 2);
			command = tmpArr[0];
			args = tmpArr[1];
		} else {
			command = event.getMessage();
			args = null;
		}
	
		if (Owner.isOwner(user, mask)) {			
			switch (command.toLowerCase()) {
			case "setpass":
				if (Owner.setPassword(user, args, mask))
					event.respondPrivateMessage("Password set");
				break;
			case "adduser":
				if (Owner.addUser(args))
					event.respondPrivateMessage("User added");
				break;
			case "deluser":
				if (Owner.delUser(args))
					event.respondPrivateMessage("User deleted");
				break;
			case "listusers":
				event.respondPrivateMessage(Owner.listUsers());
				break;
			case "addword":
				if (BadWords.addWord(args))
					event.respondPrivateMessage("Word added");
				break;
			case "delword":
				if (BadWords.delWord(args))
					event.respondPrivateMessage("Word deleted");
				break;
			case "listwords":
				event.respondPrivateMessage(BadWords.listWords());
				break;
			case "join":
				event.respondPrivateMessage("Joining channel " + args);
				event.getBot().sendIRC().joinChannel(args);
				break;
			case "part":
				event.respondPrivateMessage("Leaving channel " + args);
				event.getBot().sendRaw().rawLine("PART " + args);
				break;
			case "listchannels":
				Iterable<Channel> channelsIterable = event.getBot().getUserBot().getChannels();
				ArrayList<String> channelArrayList = new ArrayList<String>();
				for (Channel channel : channelsIterable) {
					String name = channel.getName();
					String mode = channel.getMode();
					channelArrayList.add(name + " [" + mode + "]");
				}
				String channels = StringUtils.join(channelArrayList, ", ");
				event.respondPrivateMessage("Channels: " + channels);
				break;
			case "learn":
				event.respondPrivateMessage(Knowledge.learn(args, user));
				break;
			case "forget":
				event.respondPrivateMessage(Knowledge.forget(args, user));
				break;
			case "locktopic":
				if (kb.lockTopic(args))
					event.respondPrivateMessage("Topic locked");
				break;
			case "unlocktopic":
				if (kb.unlockTopic(args))
					event.respondPrivateMessage("Topic unlocked");
				break;
			case "quit":
				event.respondPrivateMessage("Quitting");
				event.getBot().stopBotReconnect();
				event.getBot().sendIRC().quitServer();
				break;
			}
		} 
			
		switch(command.toLowerCase()) {
			case "auth":
				if (Owner.authenticateOwner(user, args, mask))
					event.respondPrivateMessage("Authenticated");
				break;
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
		Knowledge kb = new Knowledge();

		String eventText = event.getMessage();
		String command = null;
		String args = null;
		
		if(eventText.contains(" ")) {
			String[] tmpArr = eventText.split(" ", 2);
			command = tmpArr[0];
			args = tmpArr[1];
		} else {
			command = event.getMessage();
			args = null;
		}

		switch (command.toLowerCase()) {
		case "!version":
			event.respondWith("jIRCBot Version " + global.version);
			break;
		case "!8ball":
			event.respondWith(Toys.EightBall());
			break;
		case "!bofh":
			event.respondWith(Toys.BOFH());
			break;
		case "!flipcoin":
			event.respondWith(Toys.FlipCoin());
			break;
		case "!index":
			String index = StringUtils.join(kb.getIndex(), ", ");
			event.respondPrivateMessage("Index: " + index);
			break;
		case "!zfscalc":
			ZFSCalc zfscalc = ZFSCalc.getInstance();
			String[] values = args.split(" ");
			String raidz = values[0];
			int drives = Integer.parseInt(values[1]);
			int size = Integer.parseInt(values[2]);
			String zfsResult = zfscalc.RIADZCalculator(raidz, drives, size);
			if (zfsResult != null)
				event.respondWith(zfsResult);
			break;
		case "!meta":
			event.respondWith(Knowledge.metadata(args));
			break;
		default:
			if (command.startsWith("?")) {
				String kbAnswer = Knowledge.query(command);
				if (kbAnswer != null) {
					event.respondWith(kbAnswer);
				}
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onInvite(org.pircbotx.hooks.events.InviteEvent)
	 * 
	 * Join known channel on invite
	 */
	@Override
	public void onInvite(InviteEvent event) throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		if (Arrays.asList(global.ircChannels).contains(event.getChannel())) {
			event.getBot().sendIRC().joinChannel(event.getChannel());
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onKick(org.pircbotx.hooks.events.KickEvent)
	 * 
	 * Join known channel when kicked 
	 */
	@Override
	public void onKick(KickEvent event) throws ConfigurationException {
		
		Global global = Global.getInstance();
		
		if (Arrays.asList(global.ircChannels).contains(event.getChannel().getName()) && global.ircJoinOnKick) {
			event.getBot().sendIRC().joinChannel(event.getChannel().getName());
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.pircbotx.hooks.ListenerAdapter#onVersion(org.pircbotx.hooks.events.VersionEvent)
	 * 
	 * Returns version if requested
	 */
	@Override
	public void onVersion(VersionEvent event) throws ConfigurationException {
		Global global = Global.getInstance();
		event.respond(String.format("VERSION jIRCBot Version %s - %s", global.version, global.source));
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