package process;

import aiService.*;
import analyze.FileAnalysisResponse;
import analyze.FileCode;
import parsing.CodeParser;
import parsing.JavaCodeParser;
import utils.CacheManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.Constants.TEMP_DIR;

public class Analyzer {
    private List<FileCode> fileCodeList;
    private AbstractAiService embedModel;
    private AbstractAiService languageModel;

    public Analyzer(String languageModelName, String embedModelName) {
        fileCodeList = new ArrayList<>();
        languageModel = createAiService(languageModelName);
        embedModel = createAiService(embedModelName);
    }

    public AbstractAiService getLanguageModel() {
        return languageModel;
    }

    public AbstractAiService getEmbedModel() {
        return embedModel;
    }

    public void parseFiles() throws IOException {
        CodeParser parser = new JavaCodeParser();
        Path startPath = Paths.get(TEMP_DIR);

        // Files.walk iterates all subdirectories
        try (Stream<Path> stream = Files.walk(startPath)) {
            List<Path> javaFiles = stream
                    .filter(Files::isRegularFile) // not a folder
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            System.out.println("Java files to parse: " + javaFiles.size());

            for (Path path : javaFiles) {
                File file = path.toFile();
                System.out.println("Parsing: " + file.getName() + "...");

                FileCode data = parser.parse(file);

                // Count lines for proper output later
                long lineCount = Files.lines(file.toPath()).count();
                data.setLineEnd((int) lineCount);

                fileCodeList.add(data);
                System.out.println("Parsed: " + data.getFilename() + " with " + data.getMethodList().size() + " methods");
            }
        }
        catch (IOException e) {
            System.err.println("Error walking through directory: " + e.getMessage());
        }
    }

    public List<FileCode> getFileCodeList() {
        return fileCodeList;
    }





    public void summarizeRepo() throws InterruptedException {
        for (FileCode f : fileCodeList) {
            f.setAiSummary(f.summarizeFile(languageModel));
        }
    }

    public void embedRepo() {
        for (FileCode f : fileCodeList) {
            f.embedFile2(embedModel);
        }
    }

    public AbstractAiService createAiService(String AiServiceType) {
        if (AiServiceType.equalsIgnoreCase("GROQ")) {
            return new Groq();
        }
        else if (AiServiceType.equalsIgnoreCase("MIXEDBREAD")) {
            return new MixedBread();
        }
        else if (AiServiceType.equalsIgnoreCase("ONNX")) {
            return new Onnx();
        }
        else if (AiServiceType.equalsIgnoreCase("OLLAMA")) {
            return new Ollama();
        }

        return null;
    }
}
