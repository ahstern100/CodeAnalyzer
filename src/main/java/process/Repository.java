package process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static utils.Utils.prop;

public class Repository {

    private String repoUrl;
    private String authToken;
    private HttpClient client;

    public Repository(String url, String token) {
        repoUrl = url.replace("github.com/", "api.github.com/repos/") + "/contents";
        System.out.println("repoUrl = " + repoUrl);
        authToken = token;
        cleanDownloadsFolder();
    }

    /**
     * Cleaning download folder, for an instance that you want to reload from repository
     */
    public void cleanDownloadsFolder() {
        System.out.println("Cleaning 'downloads' folder");
        Path pathToBeDeleted = Paths.get("target/downloads");

        if (Files.exists(pathToBeDeleted)) {
            try {
                // Iterate all files and dirs
                Files.walk(pathToBeDeleted)
                        .sorted(Comparator.reverseOrder()) // starts with files deletion, then the dir itself
                        .filter(path -> !path.equals(pathToBeDeleted))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("File deletion failed: " + path + " - " + e.getMessage());
                            }
                        });
                System.out.println("folder 'download' has been cleaned");
            } catch (IOException e) {
                System.err.println("An error occurred while trying deleting folder: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @return A JsonNode object of repository files, to work on them later
     * @throws Exception
     */
    public JsonNode getRepoFiles() throws Exception {
        JsonNode rootNode = null;
        System.out.println("Connecting to github repository...");
        client = HttpClient.newHttpClient();

        // Build the request
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(repoUrl))
                .header("User-Agent", "Java-AI-Analyzer")
                .header("Accept", "application/vnd.github+json")
                .GET();

        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
        }

        HttpRequest request = requestBuilder.build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Response code: " + response.statusCode());
        }

        // Use the response's json
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.readTree(response.body());


        return rootNode;
    }

    /**
     * Download from github, root directory and sub directories. This method is recursive
     * @param rootNode JsonNode object that stores root of the directory
     * @param relativePath String that represents which directory we are downloading now
     * @throws Exception
     */
    public void downloadRecursive(JsonNode rootNode, String relativePath) throws Exception {
        Path targetDir = Paths.get(Constants.TEMP_DIR, relativePath);
        Files.createDirectories(targetDir);

        // Download files by a loop
        for (JsonNode node : rootNode) {
            String name = node.get("name").asText();
            String type = node.get("type").asText();

            String currentRelativePath = relativePath.isEmpty() ? name : relativePath + "/" + name;
            // get files
            if (type.equalsIgnoreCase("file")) {
                String downloadUrl = node.get("download_url").asText();
                Path filePath = targetDir.resolve(name);

                System.out.println("Downloading file: " + currentRelativePath + "...");

                // download the file
                HttpRequest.Builder downloadRequestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(downloadUrl))
                        .GET();

                if (authToken != null && !authToken.isEmpty()) {
                    downloadRequestBuilder.header("Authorization", "Bearer " + authToken);
                }

                HttpRequest downloadRequest = downloadRequestBuilder.build();

                client.send(downloadRequest, HttpResponse.BodyHandlers.ofFile(filePath));
                System.out.println("File downloaded successfully");
            }
            else if (type.equalsIgnoreCase("dir")) {
                System.out.println("Entering directory: " + currentRelativePath);

                String url = node.get("url").asText();
                HttpRequest dirRequest = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(dirRequest, HttpResponse.BodyHandlers.ofString());
                JsonNode subDirNode = new ObjectMapper().readTree(response.body());
                downloadRecursive(subDirNode, currentRelativePath);
                System.out.println("Finished directory: " + currentRelativePath);

            }

        }
        System.out.println("All files downloaded successfully");

    }
}
