package analyze;

import aiService.AbstractAiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import semanticSearch.SearchResult;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FileCode extends AnyCode {
    private List<MethodCode> methodList;
    private String fileSummary;
    private String aiSummary;
    private String filePackage;
    private float[] vector;

    public FileCode() {
        setLineBegin(1);
        methodList = new ArrayList<>();
        this.setType("File");
    }

    private static float[] convertToFloatArray(JSONArray embeddingArray) {
        float[] retValue = new float[embeddingArray.length()];
        for (int i = 0; i < embeddingArray.length(); i++) {
            retValue[i] = embeddingArray.getFloat(i);
        }

        return retValue;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        String cleanedJson = aiSummary.replace("```json", "").replace("```", "").trim();

        try {
            // Convert into JSON
            ObjectMapper mapper = new ObjectMapper();
            FileAnalysisResponse data = mapper.readValue(cleanedJson, FileAnalysisResponse.class);

            // Store file summary here
            this.aiSummary = data.file_summary;
            System.out.println("Summary of file " + this.getFilename() + ": " + this.aiSummary);


            // Store methods summary in each MethodCode object
            if (data.methods != null) {
                for (FileAnalysisResponse.MethodSummaryDTO dto : data.methods) {
                    this.getMethodList().stream().filter(m -> m.getName().equals(dto.method_name)).findFirst().ifPresent(m -> {
                        m.setAiSummary(dto.method_summary);
                        System.out.println("    Method " + m.getName() + " summary: " + dto.method_summary);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse AI JSON response: " + e.getMessage());
            System.err.println("Original response: " + aiSummary);
        }
    }

    public String getFilePackage() {
        return filePackage;
    }

    public void setFilePackage(String filePackage) {
        this.filePackage = filePackage;
    }

    public String getFileSummary() {
        return fileSummary;
    }

    public void setFileSummary(String fileSummary) {
        this.fileSummary = fileSummary;
    }

    public List<MethodCode> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<MethodCode> methodList) {


        this.methodList = methodList;
    }

    public float[] getVector() {

        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public String summarizeFile(AbstractAiService model) {
        String summaryJson = (String) model.askModel(this.getCodeSnippet());
        return summaryJson;
    }

//    public void embedFile(AbstractAiService model) {
//        System.out.println("embedding (making vector) file " + this.getName());
//
//        // Get file summary and all methods summary in one array object, then send it to embed model
//        JSONArray allSummaries = new JSONArray();
//        allSummaries.put(this.getAiSummary());
//        for (MethodCode m : this.getMethodList()) {
////            m.embedMethod(model);
//            allSummaries.put(m.getAiSummary());
//        }
//
//        JSONArray allVectors = (JSONArray) model.askModel(allSummaries.toString());
//
//        // The first item is the file's vector itself
//        JSONArray fileEmbeddingArray = allVectors.getJSONObject(0).getJSONArray("embedding");
//        this.setVector(convertToFloatArray(fileEmbeddingArray));
//
//        // Now iterate all methods
//        for (int i = 0; i < this.getMethodList().size(); i++) {
//            JSONArray methodEmbeddingArray = allVectors.getJSONObject(i + 1).getJSONArray("embedding");
//            this.getMethodList().get(i).setVector(convertToFloatArray(methodEmbeddingArray));
//        }
//
//        System.out.println("Finished file " + this.getName());
//    }

    public void embedFile2(AbstractAiService model) {
        System.out.println("Embedding file: " + this.getName());
        this.setVector((float[]) model.askModel(this.getAiSummary()));

        // Iterate all methods
        for (MethodCode m : this.getMethodList()) {
            System.out.println("    Embedding method: " + m.getName());
            m.setVector((float[]) model.askModel(m.getAiSummary()));
            System.out.println("    Finished embedding method " + m.getName() + "\n");
        }

        System.out.println("Finished embedding file " + this.getName() + "\n\n");
    }

    public List<SearchResult> semanticSearch(float[] queryVector) {
        List<SearchResult> allResults = new ArrayList<>();

        // find result for this file itself
        SearchResult fileResult = new SearchResult();
        fileResult.setRelevantCode(this);
        fileResult.setScore(Utils.calculateCosineSimiliarity(queryVector, this.getVector()));
        if (!Double.isNaN(fileResult.getScore())) {
            allResults.add(fileResult);
        }

        // find result for all methods
        for (MethodCode m : getMethodList()) {
            allResults.addAll(m.semanticSearch(queryVector));
        }

        return allResults;
    }

}
