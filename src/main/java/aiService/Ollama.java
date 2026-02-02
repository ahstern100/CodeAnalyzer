package aiService;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.time.Duration;
import java.util.List;

public class Ollama extends AbstractAiService {

    private static final String ONE_MODEL = "llama3.2:3b";
    private static final String  SECOND_MODEL = "tinyllama";

    private static final String CHOSEN_MODEL = SECOND_MODEL;
    private final ChatLanguageModel model;
    public Ollama() {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(CHOSEN_MODEL)
                .format("json")
                .timeout(Duration.ofMinutes(2))
                .temperature(0.3)
                .build();
    }
    @Override
    public String askModel(String prompt) {
        String systemInstruction = "You are a specialized code analyzer. " +
                "Return ONLY a JSON object. No conversational text. No preamble. " +
                "Follow this exact structure: " +
                "{\"file_name\": \"\", \"file_summary\": \"\", \"methods\": [{\"method_name\": \"\", \"method_summary\": \"\"}]}";

        String userPrompt = "Analyze this Java code and provide the summary in the requested JSON format:\n\n" + prompt;

        String retValue = model.generate(userPrompt + "\n\nInstruction: " + systemInstruction);
        return retValue;
    }

    @Override
    public List<Object> askModel(List<Object> prompt) {
        return null;
    }
}
