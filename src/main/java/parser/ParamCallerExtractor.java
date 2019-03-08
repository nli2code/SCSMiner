package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import parser.meta.DirExplorer;
import utils.Config;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ParamCallerExtractor {
  static Map<String, Boolean> focusApis = new HashMap<>();
  static Map<String, Map<String, Integer>> paramCounters = new HashMap<>();
  static Map<String, Map<String, Integer>> callerCounters = new HashMap<>();

  public static void addApis(List<String> apis) {
    for (String api: apis) {
      focusApis.put(api, false);
    }
  }

  public static void updateCounter(String api, String stmt, Map<String, Map<String, Integer>> map) {
    Map<String, Integer> counter = map.getOrDefault(api, new HashMap<>());
    if (counter.containsKey(stmt)) {
      counter.put(stmt, counter.get(stmt) + 1);
    } else {
      counter.put(stmt, 1);
    }
    map.put(api, counter);
  }

  public static void print(Map<String, Map<String, Integer>> map) {
    for (String api: map.keySet()) {
      System.out.println(api);
      System.out.println(Strings.repeat("=", api.length()));
      Map<String, Integer> counter = map.get(api);
      int tot = counter.entrySet().stream().mapToInt(item -> item.getValue()).sum();
      counter.entrySet().stream()
              .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
              .limit(5).forEach(System.out::println);
      System.out.println("tot: " + tot + "\n");
    }
  }

  static class ParameterVisitor implements DirExplorer.FileHandler {
    @Override
    public void handle(int level, String path, File file) {

      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(ImportDeclaration n, Object arg) {
            super.visit(n, arg);
          }
          public void visit(MethodCallExpr n, Object arg) {
            super.visit(n, arg);
            if (focusApis.containsKey(n.getNameAsString())) {
              updateCounter(n.getNameAsString(), n.getArguments().toString(), paramCounters);
              updateCounter(n.getNameAsString(), n.getScope().toString(), callerCounters);
            }
          }
        }.visit(JavaParser.parse(file), null);
      } catch (ParseProblemException | IOException e) {
        System.out.println("Exception found in parsing " + path);
        new RuntimeException(e);
      }
    }
  }

  public static void extract(File projectDir) {
    new DirExplorer(((level, path, file) -> path.endsWith("java")), new ParamCallerExtractor.ParameterVisitor())
            .explore(projectDir);
    }

  public static void main(String[] args) {
//    File projectDir = new File("data/test");
    File projectDir = new File(Config.getRepoCorpusPath());
    List<String> apis = new ArrayList<String>(){{
      add("createCellStyle");
      add("setFillForegroundColor");
      add("setFillPattern");
      add("setCellStyle");
    }};
    addApis(apis);
    extract(projectDir);
    print(paramCounters);
    print(callerCounters);
  }
}
