package aiService;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import static utils.Utils.prop;

public class OpenAI implements AIServiceInterface {

    private OpenAiChatModel model;

    public OpenAI(String apiKey) {
        setModel(apiKey);
    }

    @Override
    public ChatLanguageModel getModel() {
        return model;
    }

    @Override
    public void setModel(String apiKey) {
        model = OpenAiChatModel.withApiKey(apiKey);
    }

    @Override
    public String askModel(String prompt) {
        return model.generate(prompt);
    }

    @Override
    public float[] createEmbedding(String modelResponse) {
        System.out.println("Embedding not implemented yet");
        return null;
    }
}
