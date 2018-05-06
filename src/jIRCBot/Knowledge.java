package jIRCBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Knowledge {
	
	// Learn a new topic
	public static String learn(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)\\s(.*)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "OK " + user + ", I now know about " + m.group(2);
			return answer;
		}
		return "Sorry, I don't understand!";
	}
	
	// Forget a topic
	public static String forget(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "OK " + user + ", I forgot about " + m.group(2);
			return answer;
		} 
		return "Sorry, I don't understand!";
	}
	
	// Query a topic
	public static String query(String message) {
		Pattern pattern = Pattern.compile("\\?(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String answer = "Here's what I know about " + m.group(1);
			return answer;
		}
		return "Sorry, but I don't know about" + m.group(1);
	}
	
	// Tell somebody a topic
	public static String tell(String message, String sender, String receiver) {
		// TODO allow the bot to 'tell' somebody about a topic
		return null;
	}
	
	// SQLite DB connection
    private Connection connect() {
    	
    	String fileName = "test.db";
    	
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
	// Create the DB
    public void createKnowledgeDB() {
    	 
        try (Connection conn = connect()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Create knowledge table
    public void createKnowledgeTable() {
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS knowledge (\n"
        		+ " id integer PRIMARY KEY,\n"
                + "	topic TEXT NOT NULL,\n"
                + "	author TEXT NOT NULL,\n"
                + " timestamp TEXT NOT NULL,\n"
                + " data TEXT NOT NULL"
                + ");";
        
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Add knowledge
    public void addKnowledge(String topic, String author, String timestamp, String data) {
    	String sql = "INSERT INTO knowledge(topic,author,timestamp,data) VALUES(?,?,?,?)";
    	  	
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topic);
            pstmt.setString(2, author);
            pstmt.setString(3, timestamp);
            pstmt.setString(4, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
    }
    
    // Get knowledge
    public void getKnowledge(String topic) {
    	String sql = "SELECT topic,data FROM knowledge WHERE topic = " + topic;
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement();
    			ResultSet rs = stmt.executeQuery(sql)) {
    		while (rs.next()) {
    			System.out.println(rs.getString("topic"));
    		}
    	} catch (SQLException e) {
    		System.out.print(e.getMessage());
    	}
    }
    
    public static void main(String[] args) {

    	String topic = "test";
    	String author = "m0nkey_";
    	String timestamp = "now";
    	String data = "This is a test entry";
    	
    	Knowledge kb = new Knowledge();
    	//kb.createKnowledgeDB();
    	//kb.createKnowledgeTable();
    	kb.addKnowledge(topic, author, timestamp, data);
        kb.getKnowledge(topic);
    }


}
