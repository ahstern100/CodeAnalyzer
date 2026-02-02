package aiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public abstract class AbstractAiService {
    protected String apiKey;
    protected String url;

    public abstract Object askModel(String prompt);
    public abstract List<Object> askModel(List<Object> prompt);

    public JSONObject getResponse(JSONObject requestJson) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Status Code: " + response.statusCode() + "\n" + response.body());
        }

        return new JSONObject((String) response.body());
    }
}
