package jIRCBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Knowledge {
	
	/*
	 * Learn a new topic
	 */
	public static String learn(String message, String user) throws ConfigurationException {
		
		Pattern pattern = Pattern.compile("(\\b\\w+?\\b)\\s(.*)");
		Matcher m = pattern.matcher(message);
		
		if (m.matches()) {

			String topic = m.group(1).toLowerCase();
			String data = m.group(2);

			Knowledge kb = new Knowledge();
			BadWords bw = new BadWords();

			if (kb.botUser(user)) {
				if (!bw.badWords(topic) && !bw.badWords(data)) {
					if (kb.getKnowledge(topic)[0] != null) {
						String answer = String.format("I already know about %s.", topic);
						return answer;
					} else {
						kb.addKnowledge(topic, user, data);
						String answer = String.format("OK %s, I now known about %s.", user, topic);
						return answer;
					}
				} else {
					String answer = String.format("Sorry %s, but I'm now allowed to learn about that.", user);				
					return answer;
				}
			} else {
				String answer = "Sorry " + user + ", you're not allowed to do that.";
				return answer;
			}

		}

		return "Sorry, I don't understand!";

	}
	
	/*
	 * Forget a topic
	 */
	public static String forget(String topic, String user) throws ConfigurationException {
		
		Knowledge kb = new Knowledge();

		topic = topic.toLowerCase();
		
		String[] kbResult = kb.getKnowledge(topic);
		
		if (kb.botUser(user)) {
			if (kbResult[0] != null ) {
				switch (Integer.parseInt(kbResult[2])) {
				case 0:
					kb.deleteKnowledge(topic);
					return String.format("OK %s, I forgot about %s.", user, topic);
				case 1:
					return String.format("Sorry %s, the topic %s is locked.", user, topic);
				}
			} else {
				return String.format("Sorry, but I don't know about %s.", topic);
			}
		} else {
			return String.format("Sorry %s, you're not allowed to do that.", user);
		}
		return null;

	}
	
	/*
	 * Query a topic
	 */
	public static String query(String message) throws ConfigurationException {

		Pattern pattern = Pattern.compile("[\\?|!](\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);

		if (m.matches()) {

			String topic = m.group(1).toLowerCase();

			Knowledge kb = new Knowledge();
			String result[] = kb.getKnowledge(topic);

			if (result[0] != null) {
				String answer = String.format("[%s] %s", topic, result[1]);
				return answer;
			} else {
				String answer = String.format("Sorry, but I don't know about %s.", topic);
				return answer;
			}

		}

		return null;

	}

	public static String metadata(String topic) throws ConfigurationException {

		Knowledge kb = new Knowledge();
		String result[] = kb.getMetadata(topic.toLowerCase());
		
		if (result[0] != null) {
			String answer = String.format("[%s] Author: %s, Timestamp: %s, Locked: %s", topic, result[1], result[2], result[3]);
			return answer;
		} else {
			String answer = String.format("Sorry, but I don't know about %s.", topic);
			return answer;
		}

	}
	
	/*
	 * Database connection
	 */
	private Connection connect() throws ConfigurationException {

		Global global = Global.getInstance();

		String url = "jdbc:sqlite:" + global.knowledgeDB;

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;

	}
	
	/*
	 * Create knowledge database
	 */
    public void createKnowledgeDB() throws ConfigurationException {

    	Global global = Global.getInstance();

    	File f = new File(global.knowledgeDB);

    	if (!f.exists()) {
    		try (Connection conn = connect()) {
    			if (conn != null) {
    				System.out.println("A new knowledge database has been created: " + f);
    			}
    		} catch (SQLException e) {
    			System.out.println(e.getMessage());
    		}
    	}

	}
    
	/*
	 * Create knowledge table
	 */
	public void createKnowledgeTable() throws ConfigurationException {

		String sql = "CREATE TABLE IF NOT EXISTS knowledge (\n"
				+ " id integer PRIMARY KEY,\n"
				+ "	topic TEXT NOT NULL,\n"
				+ "	author TEXT NOT NULL,\n"
				+ " timestamp TEXT NOT NULL,\n"
				+ " data TEXT NOT NULL,\n"
				+ " lock INTEGER NOT NULL"
				+ ");";

		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {

			stmt.execute(sql);

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	}
    
	/*
	 * Add knowledge
	 */
	public void addKnowledge(String topic, String author, String data) throws ConfigurationException {

		if (this.getKnowledge(topic)[0] == null) {

			String sql = "INSERT INTO knowledge(topic,author,timestamp,data,lock) VALUES(?,?,datetime('now'),?,0);";

			try (Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {

				pstmt.setString(1, topic);
				pstmt.setString(2, author);
				pstmt.setString(3, data);
				pstmt.executeUpdate();

			} catch (SQLException e) {

				System.out.println(e.getMessage());

			}

		}

	}
    
	/*
	 * Delete knowledge
	 */
	public void deleteKnowledge(String topic) throws ConfigurationException {

		String sql = "DELETE FROM knowledge WHERE topic = ? AND lock = 0";

		if (this.getKnowledge(topic)[0] != null) {

			try (Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {

				pstmt.setString(1, topic);
				pstmt.executeUpdate();

			} catch (SQLException e) {

				System.out.print(e.getMessage());

			}

		}

	}

	/*
	 * Query knowledge
	 */
	public String[] getKnowledge(String topic) throws ConfigurationException {

		String sql = "SELECT topic, data, lock FROM knowledge WHERE topic = ? LIMIT 1;";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)){

			pstmt.setString(1,topic);

			ResultSet rs = pstmt.executeQuery();
                           
			while (rs.next()) {
				String[] result = {
						rs.getString("topic"),
						rs.getString("data"),
						rs.getString("lock"),
				};
				return result;
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		String[] result = {null,null,null};

		return result;

	}
	
	/*
	 * Knowledge metadata
	 */
	public String[] getMetadata(String topic) throws ConfigurationException {

		String sql = "SELECT topic, author, timestamp, lock FROM knowledge WHERE topic = ? LIMIT 1;";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)){

			pstmt.setString(1,topic);

			ResultSet rs = pstmt.executeQuery();
                           
			while (rs.next()) {
				String[] result = {
						rs.getString("topic"),
						rs.getString("author"),
						rs.getString("timestamp"),
						rs.getString("lock"),
				};
				return result;
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		String[] result = {null,null};

		return result;

	}
	
	public ArrayList<String> getIndex() throws ConfigurationException {
		
		String sql = "SELECT topic FROM knowledge order by TOPIC ASC;";
		
		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<String> index = new ArrayList<String>();
			
			while (rs.next()) {
				index.add(rs.getString("topic"));
			}

			return index;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public Boolean lockTopic(String topic) throws ConfigurationException {	
		String[] kbResult = this.getKnowledge(topic);		
		if ((kbResult[0] != null) && (Integer.parseInt(kbResult[2]) == 0)) {
			String sql = "UPDATE knowledge SET lock = '1' WHERE topic = ?;";
			try (Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1,topic);
				pstmt.execute();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		return false;
	}

	public Boolean unlockTopic(String topic) throws ConfigurationException {
		String[] kbResult = this.getKnowledge(topic);
		if ((kbResult[0] != null) && (Integer.parseInt(kbResult[2]) == 1)) {
			String sql = "UPDATE knowledge SET lock = '0' WHERE topic = ?;";
			try (Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1,topic);
				pstmt.execute();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*
	 * Compares the user to a know list of users and returns a boolean
	 */
	public boolean botUser(String user) {

		try {

			Global global = Global.getInstance();

			PropertiesConfiguration properties = new PropertiesConfiguration(global.config);
			properties.setListDelimiter('\u002C');
			properties.reload();

			if (Arrays.asList(properties.getStringArray("botUsers")).contains(user)) {
				return true;
			} else {
				return false;
			}

		} catch (ConfigurationException e) {

			System.out.print(e.getMessage());

		}

		return false;

	}

}
