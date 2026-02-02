package process;

import com.fasterxml.jackson.databind.JsonNode;
import semanticSearch.SearchResult;
import semanticSearch.Searcher;
import ui.SimpleUI;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Utils.loadProperties;

public class EntryPoint {

    public static String runProcessOnRepo(String url, String token, Boolean isRepositoryAlreadyExist, String languageModelName, String embedModelName, String query) throws Exception {
        // Repository
        if (!isRepositoryAlreadyExist) {
            Repository repo = new Repository(url, token);
            JsonNode rootNode = repo.getRepoFiles();
            repo.downloadRecursive(rootNode, "");
        }

        // Analyze the downloaded files
        Analyzer analyzer = new Analyzer(languageModelName, embedModelName);
        analyzer.parseFiles();
        analyzer.summarizeRepo();
        analyzer.embedRepo();

        Searcher searcher = new Searcher(analyzer.getEmbedModel(), analyzer.getFileCodeList());
        List<SearchResult> results = searcher.search(query);
        return searcher.allResultsToString(results);

    }

    public static void main(String[] args) throws Exception {
        // Properties
        System.setProperty("jdk.httpclient.HttpClient.log", "off");
        Logger.getLogger("jdk.internal.httpclient.debug").setLevel(Level.OFF);
        Logger.getLogger("jdk.httpclient.HttpClient").setLevel(Level.OFF);
        loadProperties();

        // Show UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new SimpleUI().setVisible(true);
        });



    }
}
