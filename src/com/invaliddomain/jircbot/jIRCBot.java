package com.invaliddomain.jircbot;

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

import com.invaliddomain.jircbot.knowledge.Knowledge;
import com.invaliddomain.jircbot.tools.BadWords;
import com.invaliddomain.jircbot.tools.Owner;
import com.invaliddomain.jircbot.tools.URL;
import com.invaliddomain.jircbot.tools.User;
import com.invaliddomain.jircbot.tools.ZFSCalc;
import com.invaliddomain.jircbot.toys.BOFH;
import com.invaliddomain.jircbot.toys.EightBall;
import com.invaliddomain.jircbot.toys.FlipCoin;

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
		
		if(User.isBadUser(user))
			return;
		
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
		String owner = properties.getString("botOwner");
		String eventText = event.getMessage();
		String command = null;
		String args = null;
		
		if(User.isBadUser(user))
			return;
		
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
				if (User.addUser(args))
					event.respondPrivateMessage("User added");
				break;
			case "deluser":
				if (User.delUser(args))
					event.respondPrivateMessage("User deleted");
				break;
			case "listusers":
				event.respondPrivateMessage(User.listUsers());
				break;
			case "addword":
				if (BadWords.addWord(args))
					event.respondPrivateMessage("Word added");
				break;
			case "delword":
				if (BadWords.delWord(args))
					event.respondPrivateMessage("Word deleted");
				break;
			case "addbaduser":
				if (User.addBadUser(args))
					event.respondPrivateMessage("Bad user added");
				break;
			case "delbaduser":
				if (User.delBadUser(args))
					event.respondPrivateMessage("Bad user deleted");
				break;
			case "listbadusers":
				event.respondPrivateMessage(User.listBadUsers());
				break;
			case "listwords":
				event.respondPrivateMessage(BadWords.listWords());
				break;
			case "join":
				event.respondPrivateMessage(String.format("Joining channel %s", args));
				event.getBot().sendIRC().joinChannel(args);
				break;
			case "part":
				event.respondPrivateMessage(String.format("Leaving channel %s", args));
				event.getBot().sendRaw().rawLine("PART " + args);
				break;
			case "listchannels":
				Iterable<Channel> channelsIterable = event.getBot().getUserBot().getChannels();
				ArrayList<String> channelArrayList = new ArrayList<String>();
				for (Channel channel : channelsIterable) {
					String name = channel.getName();
					String mode = channel.getMode();
					channelArrayList.add(String.format("%s [%s]", name, mode));
				}
				String channels = StringUtils.join(channelArrayList, ", ");
				event.respondPrivateMessage(String.format("Channels: %s", channels));
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
				if (Owner.authenticateOwner(user, args, mask)) {
					event.respondPrivateMessage("Authenticated");
				} else {
					if (!user.equals(owner))
						event.getBot().sendIRC().notice(owner, String.format("Warning! The user %s attempted to authenticate as the bot owner!", user));
				}
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

		String user = event.getUser().getNick().toString();
		String eventText = event.getMessage();
		String command = null;
		String args = null;
		
		if(User.isBadUser(user))
			return;
		
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
			event.respondWith(String.format("jIRCBot Version %s", global.version));
			break;
		case "!8ball":
			event.respondWith(EightBall.main());
			break;
		case "!bofh":
			event.respondWith(BOFH.main());
			break;
		case "!flipcoin":
			event.respondWith(FlipCoin.main());
			break;
		case "!index":
			String index = StringUtils.join(kb.getIndex(), ", ");
			event.respondPrivateMessage(String.format("Index: %s", index));
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
		event.respond(String.format("VERSION jIRCBot Version %s (Powered by PircBotX) - %s", global.version, global.source));
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