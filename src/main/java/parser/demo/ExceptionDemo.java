package parser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import parser.meta.DirExplorer;

import java.io.File;
import java.io.IOException;

public class ExceptionDemo {
  public static void listTryCatchStmts(File projectDir) {
    new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
      System.out.println(path);
      System.out.println(Strings.repeat("=", path.length()));
      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(TryStmt n, Object arg) {
            super.visit(n, arg);
            System.out.println(n.getTryBlock());
            System.out.println(n.getCatchClauses());
          }
        }.visit(JavaParser.parse(file), null);
        System.out.println(); // empty line
      } catch (IOException e) {
        new RuntimeException(e);
      }
    }).explore(projectDir);
  }

  public static void main(String[] args) {
    File projectDir = new File("data/test");
    listTryCatchStmts(projectDir);
  }
}
