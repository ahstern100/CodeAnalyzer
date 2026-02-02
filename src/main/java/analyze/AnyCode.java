package analyze;

public class AnyCode {
    protected String type;
    protected String name;
    protected String codeSnippet;
    protected int lineBegin;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    protected String filename;

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCodeSnippet() {

        return codeSnippet;
    }

    public void setCodeSnippet(String codeSnippet) {

        this.codeSnippet = codeSnippet;
    }

    public int getLineBegin() {

        return lineBegin;
    }

    public void setLineBegin(int lineBegin) {

        this.lineBegin = lineBegin;
    }

    public int getLineEnd() {

        return lineEnd;
    }

    public void setLineEnd(int lineEnd) {

        this.lineEnd = lineEnd;
    }

    protected int lineEnd;
}
