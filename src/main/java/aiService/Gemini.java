package aiService;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.output.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static utils.Utils.prop;

public class Gemini implements AIServiceInterface {
    private GoogleAiGeminiChatModel model;
    private GoogleAiEmbeddingModel embeddingModel;
    private String apiKey;

    public Gemini(String apiKey) {
        this.apiKey = apiKey;
        setModel(apiKey);
    }

    @Override
    public ChatLanguageModel getModel() {
        return (ChatLanguageModel) model;
    }

    @Override
    public void setModel(String apiKey) {
        System.out.println("Setting AI language model");
        model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-flash-latest")
//                .logRequestsAndResponses(true)
//                .responseFormat(ResponseFormat.JSON)
                .build();

        System.out.println("Done.\nSetting AI embedding model");
        embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("text-embedding-004")
                .build();

        System.out.println("Done");

    }

    @Override
    public String askModel(String prompt) {
        try {
            // שימוש ב-endpoint הכי יציב (v1) ובמודל שעבד לך ב-Studio
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent?key=" + apiKey;

            // בניית הג'ייסון של הבקשה בצורה פשוטה
            String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\"}]}]}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // כאן צריך לחלץ את הטקסט מתוך ה-JSON שחוזר (באמצעות Jackson או Regex פשוט)
                return response.body();
            } else {
                return "Error: " + response.statusCode() + " - " + response.body();
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
//        return model.generate(prompt);
    }

    @Override
    public float[] createEmbedding(String modelResponse) {
        Response<Embedding> response = embeddingModel.embed(modelResponse);
        return response.content().vector();
    }
}
