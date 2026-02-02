package aiService;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.output.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Onnx extends AbstractAiService {

    private final EmbeddingModel embeddingModel;
    public Onnx() {
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    }

    @Override
    public float[] askModel(String prompt) {
        if ((prompt != null) && (!prompt.isEmpty()) && (!prompt.isBlank())) {
            float[] retValue = embeddingModel.embed(prompt).content().vector();
            return retValue;
        }

        float[] retValue = new float[384];
        System.out.println("No prompt - returns array of zero values");
        return retValue;
    }

    public List<Object> askModel(List<Object> summariesObj) {
        List<TextSegment> summariesTS = new ArrayList<>();
        for (Object summaryObj : summariesObj) {
            TextSegment summaryTS = (TextSegment) summaryObj;
            summariesTS.add(summaryTS);
        }

        List<Embedding> retValueEmb = embeddingModel.embedAll(summariesTS).content();
        List<Object> retValueObj = Arrays.asList(retValueEmb.toArray());

        return retValueObj;
    }
}
