package aiService;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface AIServiceInterface {
    public ChatLanguageModel getModel();
    public void setModel(String apiKey);
    public String askModel(String prompt);
    public float[] createEmbedding(String modelResponse);
}
