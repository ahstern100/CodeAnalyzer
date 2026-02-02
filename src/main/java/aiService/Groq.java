package aiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Groq extends AbstractAiService {

    public Groq() {
        this.apiKey = System.getenv("GROQ_API_KEY");
        this.url = "https://api.groq.com/openai/v1/chat/completions";
    }

    @Override
    public String askModel(String prompt) {
        try {
            JSONObject root = new JSONObject();
            root.put("model", "llama-3.3-70b-versatile");

            JSONArray messages = new JSONArray();

            // System message to define format
            messages.put(new JSONObject()
                    .put("role", "system")
                    .put("content", "You are a Java expert. Return ONLY JSON.\n" +
                            "You MUST include EVERY method found in the code, including short getters, setters, and one-liners.\n" +
                            "Even if a method is simple, provide a brief summary for it.\n" +
                            "Structure: {\"file_name\": \"\", \"file_summary\": \"\", \"methods\": [{\"method_name\": \"\", \"method_summary\": \"\"}]}"));

            // user message
            messages.put(new JSONObject()
                    .put("role", "user")
                    .put("content", "Analyze this code: " + prompt));

            root.put("messages", messages);
            JSONObject responseFormatType = new JSONObject();
            responseFormatType.put("type", "json_object");
            root.put("response_format", responseFormatType);

            JSONObject resJson = this.getResponse(root);

            return resJson.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
        catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    public List<Object> askModel(List<Object> prompt) {
        return null;
    }
}
