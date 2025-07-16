package org.drugref.ca.vigilance.fetch;

import org.apache.logging.log4j.Logger;
import org.drugref.util.MiscUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VigilanceImport implements Serializable {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String LOGIN_URL = "https://manager.vigilance.ca/login";
	private static final String DOWNLOAD_URL = "https://api-manager.vigilance.ca/files/";
	// https://api-manager.vigilance.ca/files/{id}
	private static final String USER_AGENT = "Mozilla/5.0";

	private String username;
	private String password;
	private String sessionCookie;

	public VigilanceImport(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public URL getLoginUrl() throws MalformedURLException {
		return new URL(LOGIN_URL);
	}


	public synchronized void login() throws IOException {
		URL url = new URL(LOGIN_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setInstanceFollowRedirects(true);
		// Set request properties
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setDoOutput(true);

		// Send login data
		String loginData = "_username=" + username + "&_password=" + password;
		try (OutputStream os = connection.getOutputStream()) {
			os.write(loginData.getBytes());
			os.flush();
		}

		// Handle response & extract session cookie
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String cookies = connection.getHeaderField("Set-Cookie");
			sessionCookie = cookies.split(";")[0];
		} else {
			throw new IOException("Failed to log in. Response Code: " + responseCode);
		}

		connection.disconnect();
	}

	public synchronized void downloadAllDatFiles(String outputFolder) throws IOException {
		if (sessionCookie == null) {
			throw new IllegalStateException("You must log in before attempting to download files.");
		}

		URL url = new URL(DOWNLOAD_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Set request with session cookie
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Cookie", sessionCookie);
		String htmlContent = "";
		// Read the HTML content to find .dat files
		try(InputStream inputStream = connection.getInputStream();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}

			htmlContent = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
		}

		if(!htmlContent.isEmpty() && !htmlContent.contains("No files found")) {
			// Extract .dat file links using regex
			Pattern pattern = Pattern.compile("href=\"(.*?\\.dat)\"");
			Matcher matcher = pattern.matcher(htmlContent);

			while (matcher.find()) {
				String fileName = matcher.group(1);
				downloadFile(DOWNLOAD_URL + fileName, outputFolder, fileName);
			}
		}

		connection.disconnect();
	}

	private void downloadFile(String fileUrl, String outputFolder, String fileName) throws IOException {
		URL url = new URL(fileUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Set request with session cookie
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Cookie", sessionCookie);

		// Download file
		try (InputStream inputStream = connection.getInputStream();
		     FileOutputStream outputStream = new FileOutputStream(new File(outputFolder, fileName))) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}

		connection.disconnect();
	}

	public static void main(String[] args) {
		String username = "dwarren@openosp.ca";  // Replace with actual username
		String password = "u^T82Pkw;pHM2(F";  // Replace with actual password
		String outputFolder = "/downloads";

		VigilanceImport downloader = new VigilanceImport(username, password);

		try {
			downloader.login();
			downloader.downloadAllDatFiles(outputFolder);
			System.out.println("All .dat files have been downloaded to " + outputFolder);
		} catch (Exception e) {
			logger.error("Error downloading files", e);
		}
	}

	public String getSessionCookie() {
		return sessionCookie;
	}

}