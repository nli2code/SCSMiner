package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import parser.entity.SCSFile;
import parser.entity.SCSUnit;
import parser.meta.DirExplorer;
import utils.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallSeqExtractor {

  static List<SCSFile> scsFiles = new ArrayList<>();
  static Set<String> focusApis;
  static Map<String, List<String>> mmap = new HashMap<>();
  static List<String> relatedFiles = new ArrayList<>();

  static class CallSeqVisitor implements DirExplorer.FileHandler {
    @Override
    public void handle(int level, String path, File file) {
      SCSFile scsFile = new SCSFile();
      scsFile.setPath(path);
      SCSUnit scsUnit = new SCSUnit();

      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(ImportDeclaration n, Object arg) {
            super.visit(n, arg);
            scsFile.addLibrary(n.getNameAsString());
          }
          public void visit(MethodDeclaration n, Object arg) {
            scsUnit.setMethodShortName(n.getNameAsString());
            scsUnit.clear();
            super.visit(n, arg);
          }
          public void visit(MethodCallExpr n, Object arg) {
            super.visit(n, arg);
            String mName = n.getNameAsString();
            if (mmap.containsKey(mName)) {
              List<String> qNames = mmap.get(mName);
              for (String lib : scsFile.getImportedLibraries()) {
                for (String qName: qNames) {
                  if (StringUtils.getCommonPrefix(qName, lib).equals(lib)) {
//                    System.out.println(qName + " " + lib + " " + mName);
                    scsFile.setLibRelated(true);
                  }
                }
              }
            }
          }
        }.visit(JavaParser.parse(file), null);
      } catch (ParseProblemException | IOException e) {
        System.out.println("Exception found in parsing " + path);
        new RuntimeException(e);
      }

      if (scsFile.getLibRelated()) {
//        System.out.println(scsFile.getPath());
        relatedFiles.add(scsFile.getPath());
      }
    }
  }

  public static List<String> extract(File projectDir, Set<String> apis) {
    for (String api: apis) {
      String[] tokens = api.split("\\.");
      if (tokens.length == 0) System.out.println(api);
      String shortName = tokens[tokens.length - 1];
      List<String> qNames = mmap.getOrDefault(shortName, new ArrayList<>());
      qNames.add(api);
      mmap.put(shortName, qNames);
    }
    new DirExplorer(((level, path, file) -> path.endsWith("java")), new CallSeqVisitor())
            .explore(projectDir);
    return relatedFiles;
  }

  public static void main(String[] args) throws IOException {
    System.out.println(StringUtils.getCommonPrefix("abs", "abd"));
  }
}
