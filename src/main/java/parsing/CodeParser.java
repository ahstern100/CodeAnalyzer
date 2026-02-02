package parsing;

import analyze.FileCode;

import java.io.File;
import java.io.IOException;

public interface CodeParser {
    FileCode parse(File file) throws IOException;
}
