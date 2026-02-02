package aiService;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MixedBread extends AbstractAiService {

    public MixedBread() {
        this.url = "https://api.mixedbread.ai/v1/embeddings";
        this.apiKey = System.getenv("MIXEDBREAD_API_KEY");
    }

    @Override
    public JSONArray askModel(String modelResponse) {
        try {
            JSONObject root = new JSONObject();
            root.put("model", "mixedbread-ai/mxbai-embed-large-v1");
            root.put("input", modelResponse);

            JSONObject resJson = this.getResponse(root);

            JSONArray embeddingArray = resJson.getJSONArray("data");

            return embeddingArray;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> askModel(List<Object> prompt) {
        return null;
    }
}
