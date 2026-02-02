package utils;

import analyze.FileAnalysisResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheManager {
    private static final String CACHE_PATH = "repo_cache.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String, FileAnalysisResponse> cache = new HashMap<>();

    private static CacheManager instance;
    public static CacheManager get() {
        if (instance == null) {
            instance = new CacheManager();
        }

        return instance;
    }

    private CacheManager() {
        loadCache();
    }

    private void loadCache() {
        File file = new File(CACHE_PATH);
        if (file.exists()) {
            try {
                List<FileAnalysisResponse> list = mapper.readValue(file, new TypeReference<List<FileAnalysisResponse>>() {});
                for (FileAnalysisResponse res : list) {
                    cache.put(res.file_name, res);
                }
                System.out.println("Loaded " + cache.size() + " files from cache");
            }
            catch (Exception e) {
                System.err.println("Could not read cache: " + e.getMessage());
            }
        }
    }

    public FileAnalysisResponse getCachedFile(String filename) {
        return cache.get(filename);
    }

    public void updateCache(FileAnalysisResponse response) {
        cache.put(response.file_name, response);
        saveCache();
    }

    public void saveCache() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CACHE_PATH), cache.values());
        }
        catch (Exception e) {
            System.err.println("Failed to save cache: " + e.getMessage());
        }
    }
}
