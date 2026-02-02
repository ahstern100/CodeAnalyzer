package semanticSearch;

import analyze.AnyCode;

public class SearchResult {
    public AnyCode getRelevantCode() {
        return code;
    }

    public void setRelevantCode(AnyCode relevantCode) {
        this.code = relevantCode;
    }

    private AnyCode code;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    private double score;

    public String toString() {
        String inFile = "";
        if (code.getType().equalsIgnoreCase("method")) {
            inFile = "in file " + code.getFilename();
        }
        return String.format("%s %s %s:\n" +
                        "Code snippet:\n" +
                        "%s\n" +
                        "Lines:%d-%d\n" +
                        "Score: %f\n\n",
                code.getType(), inFile, code.getName(),
                code.getCodeSnippet(),
                code.getLineBegin(), code.getLineEnd(),
                this.getScore());

    }
}
