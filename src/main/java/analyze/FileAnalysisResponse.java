package analyze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileAnalysisResponse {
    public String file_name;
    public String file_summary;
    public List<MethodSummaryDTO> methods;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MethodSummaryDTO {
        public String method_name;
        public String method_summary;
    }
}