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

import java.io.File;

public class Knowledge {
	
	String database = "knowledge.db";
	
	// Learn a new topic
	public static String learn(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)\\s(.*)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			
			String topic = m.group(2);
			String data = m.group(3);
			
	    	Knowledge kb = new Knowledge();
	    	
	    	if (kb.getKnowledge(topic)[0] != null) {
	    		String answer = "I already known about " + topic;
	    		return answer;
	    	} else {
	    		kb.addKnowledge(topic, user, data);
	    		String answer = "OK " + user + ", I now know about " + topic;
	    		return answer;
	    	}
		}
		return "Sorry, I don't understand!";
	}
	
	// Forget a topic
	public static String forget(String message, String user) {
		Pattern pattern = Pattern.compile("!(\\b\\w+?\\b)\\s(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			String topic = m.group(2);
			Knowledge kb = new Knowledge();
			if (kb.getKnowledge(topic)[0] != null) {
				kb.deleteKnowledge(topic);
				String answer = "OK " + user + ", I forgot about " + topic;
				return answer;
			} else {
				String answer = "Sorry, I don't know about " + topic;
				return answer;
			}			
		} 
		return "Sorry, I don't understand!";
	}
	
	// Query a topic
	public static String query(String message) {
		Pattern pattern = Pattern.compile("\\?(\\b\\w+?\\b)");
		Matcher m = pattern.matcher(message);
		if (m.matches()) {
			
			String topic = m.group(1);
			
			Knowledge kb = new Knowledge();
			String result[] = kb.getKnowledge(topic);
			
			if (result[0] != null) {
				String answer = "Here's what I know about " + topic + ": " + result[1];
				return answer;
			} else {
				String answer = "Sorry, but I don't know about " + topic;
				return answer;
			}
		}
		return "Sorry, I don't understand!";
	}
	
	// Tell somebody a topic
	public static String tell(String message, String sender, String receiver) {
		// TODO allow the bot to 'tell' somebody about a topic
		return null;
	}
	
	// SQLite DB connection
    private Connection connect() {
    	   	
        // SQLite connection string
        String url = "jdbc:sqlite:" + database;
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
    	
    	File f = new File(database);
    	
    	if (f.exists()) {
    		System.out.println("Database already exists.");
    	} else {
	        try (Connection conn = connect()) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("A new database has been created.");
	            }
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
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
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Add knowledge
    public void addKnowledge(String topic, String author, String data) {
    	
	   	if (this.getKnowledge(topic)[0] == null) {
		    	
	    	String sql = "INSERT INTO knowledge(topic,author,timestamp,data) VALUES(?,?,datetime('now'),?);";
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
    
    public void deleteKnowledge(String topic) {
    	String sql = "DELETE FROM knowledge WHERE topic = ?";
    	
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
        
    public String[] getKnowledge(String topic) {
    	String sql = "SELECT topic, data FROM knowledge WHERE topic = ? LIMIT 1;"; 	
        try (Connection conn = this.connect();
                PreparedStatement pstmt  = conn.prepareStatement(sql)){
               pstmt.setString(1,topic);

               ResultSet rs  = pstmt.executeQuery();
                           
               while (rs.next()) {                 
                   String[] result = {
                		   rs.getString("topic"),
                		   rs.getString("data"),
                   };
                   return result;
               }
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	
        String[] result = {null,null};
        return result;
        
    }

}
