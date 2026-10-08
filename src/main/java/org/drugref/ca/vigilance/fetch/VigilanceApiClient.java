package org.drugref.ca.vigilance.fetch;

import org.apache.logging.log4j.Logger;
import org.drugref.util.DrugrefProperties;
import org.drugref.util.MiscUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal HTTP client for the Vigilance data files API.
 * <p>
 * Provides the three calls needed by {@link VigilanceUpdateDBWorker}:
 * <ul>
 *   <li>{@link #authenticate()} — POST {@code {baseUrl}/oauth/v2/token} (OAuth2 resource owner
 *       password grant with client credentials in the form body).</li>
 *   <li>{@link #getFileList(String)} — GET {@code {baseUrl}/files}.</li>
 *   <li>{@link #downloadFile(String, String, File)} — GET {@code {baseUrl}/files/{base64(fileName)}}.</li>
 * </ul>
 * All credentials and the base URL are read once from the {@code vigilance.*} properties of the
 * external {@code drugref2.properties} via {@link DrugrefProperties}; the client itself is
 * stateless and not thread-safe, so one instance is created per update run.
 */
public class VigilanceApiClient {
    private static final Logger logger = MiscUtils.getLogger();
    private static final String USER_AGENT = "OpenOSP-Drugref/1.0";

    private final String baseUrl;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private final String userId;

    /**
     * Reads the {@code vigilance.*} configuration (base URL, client id/secret, username,
     * password, user id) from {@link DrugrefProperties#getInstance()}.
     * <p>
     * Missing values are tolerated here and only cause {@link #authenticate()} to fail.
     */
    public VigilanceApiClient() {
        DrugrefProperties props = DrugrefProperties.getInstance();
        this.baseUrl = props.getVigilanceBaseUrl();
        this.clientId = props.getVigilanceClientId();
        this.clientSecret = props.getVigilanceClientSecret();
        this.username = props.getVigilanceUsername();
        this.password = props.getVigilancePassword();
        this.userId = props.getVigilanceUserId();
    }

    /**
     * Authenticates against the Vigilance data files API using the
     * resource owner password credentials grant with client credentials
     * sent in the form body.
     *
     * POST {baseUrl}/oauth/v2/token
     * Content-Type: application/x-www-form-urlencoded
     * Cookie: _locale=fr
     * Body: grant_type=password&client_id=...&client_secret=...&username=...&password=...
     *
     * @return access token
     */
    public String authenticate() throws IOException {
        if (isBlank(baseUrl) || isBlank(clientId) || isBlank(clientSecret)
                || isBlank(username) || isBlank(password)) {
            throw new IOException("Vigilance API credentials not configured. "
                    + "Set vigilance.* properties in the external drugref2.properties.");
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl + "/oauth/v2/token").openConnection();
        try {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Cookie", "_locale=fr");
            connection.setDoOutput(true);

            String body = "grant_type=password"
                    + "&client_id=" + encode(clientId)
                    + "&client_secret=" + encode(clientSecret)
                    + "&username=" + encode(username)
                    + "&password=" + encode(password);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            String response = readBody(
                    responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream());
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Vigilance authentication failed with HTTP " + responseCode + ": " + response);
            }

            JSONObject json = new JSONObject(response);
            String token = json.optString("access_token", null);
            if (isBlank(token)) {
                throw new IOException("Vigilance authentication response missing access_token");
            }

            logger.info("Authenticated with Vigilance API");
            return token;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Fetches the list of available data files.
     *
     * GET {baseUrl}/files
     *
     * @param accessToken OAuth2 access token
     * @return raw response body
     */
    public String getFileList(String accessToken) throws IOException {
        return get(accessToken, baseUrl + "/files");
    }

    /**
     * Downloads a data file by name.
     *
     * GET {baseUrl}/files/{base64(fileName)}
     *
     * @param accessToken OAuth2 access token
     * @param fileName    file name, e.g. fgenPlus.dat
     * @param dest        destination file
     */
    public void downloadFile(String accessToken, String fileName, File dest) throws IOException {
        String encodedName = Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));
        HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl + "/files/" + encodedName).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to download " + fileName + ": HTTP " + responseCode);
            }

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(dest)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            logger.info("Downloaded {} ({} bytes)", fileName, dest.length());
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Extracts .dat file names from the file list response.
     * Tries JSON first (array of strings or array of objects with name-like keys,
     * or an object wrapping a "files" array), then falls back to text extraction
     * for non-JSON payloads.
     *
     * @param listResponse raw response body of GET /files
     * @return list of .dat file names
     */
    public List<String> findDatFiles(String listResponse) {
        List<String> files = new ArrayList<String>();
        if (listResponse == null || listResponse.trim().isEmpty()) {
            return files;
        }

        String trimmed = listResponse.trim();
        if (trimmed.startsWith("[") || trimmed.startsWith("{")) {
            try {
                if (trimmed.startsWith("[")) {
                    JSONArray array = new JSONArray(trimmed);
                    for (int i = 0; i < array.length(); i++) {
                        Object element = array.get(i);
                        if (element instanceof String) {
                            addDatFile(files, (String) element);
                        } else if (element instanceof JSONObject) {
                            JSONObject obj = (JSONObject) element;
                            for (String key : new String[]{"name", "filename", "fileName", "file", "path"}) {
                                if (obj.has(key)) {
                                    addDatFile(files, obj.getString(key));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    JSONObject obj = new JSONObject(trimmed);
                    if (obj.has("files")) {
                        return findDatFiles(obj.getJSONArray("files").toString());
                    }
                }
                if (!files.isEmpty()) {
                    return files;
                }
            } catch (Exception e) {
                logger.debug("File list is not JSON, falling back to text extraction", e);
            }
        }

        Matcher matcher = Pattern.compile("[\"']?([\\w.\\-]+\\.dat)[\"']?").matcher(listResponse);
        while (matcher.find()) {
            addDatFile(files, matcher.group(1));
        }
        return files;
    }

    /**
     * Appends a file name to the collected list when it ends with {@code .dat} (case-insensitive)
     * and is not already present, preserving list order and deduplicating.
     *
     * @param files collected file names, modified in place
     * @param name  candidate file name, may be {@code null}
     */
    private void addDatFile(List<String> files, String name) {
        if (name != null && name.toLowerCase().endsWith(".dat") && !files.contains(name)) {
            files.add(name);
        }
    }

    /**
     * Performs an authenticated GET request and returns the response body.
     *
     * @param accessToken OAuth2 access token sent as {@code Authorization: Bearer ...}
     * @param url         absolute request URL
     * @return response body (UTF-8), or the error body when the request failed
     * @throws IOException on any HTTP status other than 200
     */
    private String get(String accessToken, String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            String response = readBody(
                    responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream());
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Vigilance API request failed for " + url + " with HTTP " + responseCode);
            }
            return response;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Reads an input stream fully into a UTF-8 string.
     *
     * @param in stream to read, may be {@code null}
     * @return the stream contents, or an empty string when {@code null}
     */
    private String readBody(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        try (InputStream is = in;
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    /**
     * URL-encodes a value as UTF-8; {@code null} is encoded as an empty string.
     *
     * @param value value to encode, may be {@code null}
     * @return percent-encoded value
     */
    private static String encode(String value) throws IOException {
        return URLEncoder.encode(value == null ? "" : value, "UTF-8");
    }

    /**
     * Tests whether a string is {@code null}, empty or whitespace-only.
     *
     * @param value string to test
     * @return {@code true} when the value is missing/blank
     */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
