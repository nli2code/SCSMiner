package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import parser.meta.DirExplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Iterate over the classes and print their Javadoc.
 */
public class JavadocCommentExtractor {

    static class API {
        String name;
        String summarize;
        String javadoc;
    }

    public static String parseJavaDoc(String javadocComment) {
        javadocComment = javadocComment.replace("/", "");
        javadocComment = javadocComment.replaceAll("\\*", "");
        javadocComment = javadocComment.replaceAll("<[^<>]*>[^<>]*<[^<>]*>", "");
        String[] lines = javadocComment.split("\n");
        List<String> ans = new ArrayList<>();
        for (String line: lines) {
            if (line.contains("@since") || line.contains("@author")) continue;
            line = line.trim();
            if (line.equals("")) continue;
            ans.add(line);
        }
        if (ans.size() <= 2) return StringUtils.join(ans, "\n");
        else return ans.get(0).split("\\.")[0];
    }

    static int tot = 0;
    static String curPackage;
    static List<API> apis = new ArrayList<>();

    public static void main(String[] args) {
//        String s = "**\n **a\n**b\n**c";
//        System.out.println(s.replaceAll("\\*", ""));
        File projectDir = new File("D:\\data\\CodeGe\\src\\poi-3.16-beta2\\src\\java\\org\\apache\\poi\\ss\\usermodel");
        Map<String, String> deprecatedAPIs = new HashMap<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    public void visit(PackageDeclaration n, Object arg) {
                        super.visit(n, arg);
                        curPackage = n.getNameAsString();
                    }
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        tot++;
                        if (n.getComment().isPresent()) {
                            Comment comment = n.getComment().get();
                            if (comment instanceof JavadocComment) {
                                if (comment.getContent().contains("@deprecated")) {
                                    deprecatedAPIs.put(n.getNameAsString(), comment.getContent());
                                } else {
                                    API api = new API();
                                    api.name = curPackage + "." + n.getNameAsString();
                                    String c = comment.getContent();
                                    api.javadoc = c;
                                    api.summarize = parseJavaDoc(c);
                                    apis.add(api);
                                }
                            }
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        System.out.println(apis.size() + "/" + tot);
        for (API api: apis) {
            System.out.println(api.name + "\n" + api.summarize + "\n\n" + api.javadoc);
            System.out.println("*********************");
        }
    }

}