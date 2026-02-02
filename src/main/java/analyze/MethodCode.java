package analyze;

import aiService.AIServiceInterface;
import aiService.AbstractAiService;
import semanticSearch.SearchResult;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MethodCode extends AnyCode {
    private List<ChunksCode> chunksList;
    private String methodName;
    private String methodBody;
    private String methodSignature;
    private String javadoc;
    private int startLine;
    private int endLine;
    private String aiSummary;
    private float[] vector;

    public MethodCode() {
        this.setType("Method");
    }

    public List<ChunksCode> getChunksList() {
        return chunksList;
    }

    public void setChunksList(List<ChunksCode> chunksList) {
        this.chunksList = chunksList;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public void setName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodBody() {
        return methodBody.isEmpty() ? "None\n" : methodBody;
    }

    public void setMethodBody(String methodBody) {
        this.methodBody = methodBody;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public String getJavadoc() {
        return javadoc;
    }

    public void setJavadoc(String javadoc) {
        this.javadoc = javadoc;
    }

    public int getStartLine() {

        return startLine;
    }

    public void setStartLine(int startLine) {

        this.startLine = startLine;
    }

    public int getEndLine() {

        return endLine;
    }

    public void setEndLine(int endLine) {

        this.endLine = endLine;
    }

    public String getAiSummary() {

        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {

        this.aiSummary = aiSummary;
    }

    public float[] getVector() {

        return vector;
    }

    public void setVector(float[] vector) {

        this.vector = vector;
    }

    public List<SearchResult> semanticSearch(float[] queryVector) {
        List<SearchResult> allResults = new ArrayList<>();

        // find score of this method itself
        SearchResult methodResult = new SearchResult();
        methodResult.setRelevantCode(this);
        methodResult.setScore(Utils.calculateCosineSimiliarity(this.getVector(), queryVector));
        if (!Double.isNaN(methodResult.getScore())) {
            allResults.add(methodResult);
        }

        // Iterate inner chunks

        return allResults;
    }

}
