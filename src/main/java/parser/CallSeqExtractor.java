package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import parser.entity.SCSFile;
import parser.entity.SCSUnit;
import parser.meta.DirExplorer;
import utils.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallSeqExtractor {

  static List<SCSFile> scsFiles = new ArrayList<>();
  static Map<String, Boolean> focusApis = new HashMap<>();

  public static void addApis(List<String> apis) {
    for (String api: apis) {
      focusApis.put(api, false);
    }
  }

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
            for (String api: focusApis.keySet()) {
              focusApis.replace(api, false);
            }
            super.visit(n, arg);
            boolean containsAllFocusApis = true;
            for (String api: focusApis.keySet()) {
              if (!focusApis.get(api)) {
                containsAllFocusApis = false;
                break;
              }
            }
            if (containsAllFocusApis) {
              scsFile.addUnit(scsUnit.copy());
            }
          }
          public void visit(MethodCallExpr n, Object arg) {
            super.visit(n, arg);
            scsUnit.add(n.getNameAsString());
            if (focusApis.containsKey(n.getNameAsString())) {
              focusApis.replace(n.getNameAsString(), true);
            }
          }
//          public void visit(TryStmt n, Object arg) {
//            System.out.println("Start try");
//            super.visit(n.getTryBlock(), arg);
//            System.out.println("End try");
//          }
        }.visit(JavaParser.parse(file), null);
        // System.out.println();
      } catch (ParseProblemException | IOException e) {
        System.out.println("Exception found in parsing " + path);
        new RuntimeException(e);
      }

      scsFiles.add(scsFile);
    }
  }

  public static List<SCSFile> extract(File projectDir) {
    new DirExplorer(((level, path, file) -> path.endsWith("java")), new CallSeqVisitor())
            .explore(projectDir);
    return scsFiles;
  }

  public static void main(String[] args) {
    File projectDir = new File("data/test");
    List<String> apis = new ArrayList<String>(){{add("setFillForegroundColor");}};
    addApis(apis);
    for (SCSFile scsFile: extract(projectDir)) {
      System.out.println(scsFile.toString());
    }
  }
}
