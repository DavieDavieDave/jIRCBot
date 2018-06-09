package com.invaliddomain.jircbot.tools;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringEscapeUtils;

import com.invaliddomain.jircbot.Global;

public class URL {
	
	/*
	 * Return the <TITLE> of the URL provided
	 */
	public static String getURLTitle(String url) throws ConfigurationException {

		URL toolbox = new URL();

		toolbox.createUrlDB();
		toolbox.createUrlTable();

		String rxUrl = ".*((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]).*";
		Pattern pattern = Pattern.compile(rxUrl);
		Matcher m = pattern.matcher(url);

		if (m.matches()) {
			
			String parsedURL = m.group(1);;
			toolbox.removeCachedUrl(parsedURL);
			String[] cachedUrl = toolbox.getUrl(parsedURL);

			if (cachedUrl[0] == null) {
				try {
					String urlTitle = TitleExtractor.getPageTitle(parsedURL);
					String title = StringEscapeUtils.unescapeHtml(urlTitle.trim());
					BadWords bw = new BadWords();
					if (!bw.badWords(title)) {
						toolbox.addUrl(parsedURL, title);
						return title;
					} else {
						return null;
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} 
			} else {
				return cachedUrl[1];
			}
			
		}

		return null;

	}

	/* 
	 * Database connection
	 */
	private Connection connect() throws ConfigurationException {

		Global global = Global.getInstance();
		
		String url = "jdbc:sqlite:" + global.urlDB;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
    
	/*
	 * Create database
	 */
	public void createUrlDB() throws ConfigurationException {

		Global global = Global.getInstance();

		File f = new File(global.urlDB);

		if (!f.exists()) {
			try (Connection conn = connect()) {
				if (conn != null) {
					System.err.println("A new URL database has been created: " + f);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}
    
	/*
	 * Create table
	 */
	public void createUrlTable() throws ConfigurationException {

		String sql = "CREATE TABLE IF NOT EXISTS url (\n"
				+ " id integer PRIMARY KEY,\n"
				+ " url TEXT NOT NULL,\n"
				+ " title TEXT NOT NULL,\n"
				+ " timestamp TEXT NOT NULL"
				+ ");";

		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			
			stmt.execute(sql);
			
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			
		}
		
	}
    
	/*
	 * Add a URL
	 */
	public void addUrl(String url, String title) throws ConfigurationException {

		if (this.getUrl(url)[0] == null) {

			String sql = "INSERT INTO url(url,title,timestamp) VALUES(?,?,datetime('now'));";
			
			try (Connection conn = this.connect();
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setString(1, url);
				pstmt.setString(2, title);
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				
				System.out.println(e.getMessage());
				
			}
			
		}
		
	}
    
	/*
	 * Remove a URL
	 */
	public String[] getUrl(String url) throws ConfigurationException {

		String sql = "SELECT url, title, timestamp FROM url WHERE url = ? AND timestamp >= datetime('now', '-6 hours') AND timestamp < datetime('now') LIMIT 1;";

		try (Connection conn = this.connect();
				PreparedStatement pstmt  = conn.prepareStatement(sql)){
			pstmt.setString(1,url);

			ResultSet rs  = pstmt.executeQuery();

			while (rs.next()) {    

				String[] result = {
						rs.getString("url"),
						rs.getString("title"),
				};

				return result;
				
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		String[] result = {null,null};

		return result;

	}
    
	/*
	 * Remove old data
	 */
	public void removeCachedUrl(String url) throws ConfigurationException {

		String sql = "DELETE FROM url WHERE url = ? AND timestamp < datetime('now', '-6 hours')";

		try (Connection conn = this.connect();
				PreparedStatement pstmt  = conn.prepareStatement(sql)){

			pstmt.setString(1,url);
			pstmt.executeUpdate();               

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	}
	
}