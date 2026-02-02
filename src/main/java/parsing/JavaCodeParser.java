package parsing;

import analyze.FileCode;
import analyze.MethodCode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaCodeParser implements CodeParser {

    @Override
    public FileCode parse(File file) throws IOException {
        FileCode fileCode = new FileCode();
        fileCode.setFilename(file.getName());
        fileCode.setName(file.getName());

        String rawContent = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        fileCode.setCodeSnippet(rawContent);

        // Read the file and make it a structural tree
        CompilationUnit cu = StaticJavaParser.parse(file);

        // Get file's package
        cu.getPackageDeclaration().ifPresent(pd -> fileCode.setFilePackage(pd.getNameAsString()));

        // Scan file's methods
        List<MethodDeclaration> allMethods = cu.findAll(MethodDeclaration.class);
        allMethods.forEach(method -> {
            MethodCode m = new MethodCode();
            m.setName(method.getNameAsString());
            m.setFilename(file.getName());
            m.setMethodSignature(method.getDeclarationAsString());
            m.setMethodBody(method.getBody().map(Object::toString).orElse(""));
            method.getRange().ifPresent(range -> {
                m.setStartLine(range.begin.line);
                m.setEndLine(range.end.line);
            });

            // javadoc
            method.getJavadoc().ifPresent(jd -> m.setJavadoc(jd.toText()));

            // Add the method to the methods list of the file
            fileCode.getMethodList().add(m);
        });

        return fileCode;
    }
}
