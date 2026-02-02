package semanticSearch;

import aiService.AbstractAiService;
import analyze.FileCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher {

    AbstractAiService model;
    List<FileCode> fileCodeList;
    List<SearchResult> allResults;

    public Searcher(AbstractAiService model, List<FileCode> fileCodeList) {
        this.model = model;
        this.fileCodeList = fileCodeList;
    }


    public List<SearchResult> search(String query) {
        // make the query as a vector
        float[] queryVector = (float[]) model.askModel(query);
        allResults = new ArrayList<>();

        // Do semantic search in every file
        for (FileCode f : fileCodeList) {
            allResults.addAll(f.semanticSearch(queryVector));
        }

        List<SearchResult> retValue = allResults.stream().sorted(Comparator.comparingDouble(SearchResult::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return retValue;

    }

    public String allResultsToString(List<SearchResult> results) {
        String retValue = "";
        for (SearchResult sr : results) {
            retValue += sr.toString();
        }

        return retValue;
    }
}
