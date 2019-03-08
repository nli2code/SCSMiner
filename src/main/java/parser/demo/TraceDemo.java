package parser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import parser.meta.DirExplorer;

import java.io.File;
import java.io.IOException;

public class TraceDemo {

  static String focusInvocation = "setCellStyle";
  static String varName = "";

  static void setVarName(Expression str) {
    varName = str.toString();
  }

  public static void listVarName(File projectDir) {
    new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(MethodCallExpr n, Object arg) {
            super.visit(n, arg);
            if (n.getNameAsString().equals(focusInvocation)) {
              setVarName(n.getScope().get());
            }
          }
        }.visit(JavaParser.parse(file), null);
      } catch (IOException e) {
        new RuntimeException(e);
      }
    }).explore(projectDir);
  }

  public static void printTrace(File projectDir) {
    new DirExplorer(((level, path, file) -> path.endsWith("java")), ((level, path, file) -> {
      try {
        new VoidVisitorAdapter<Object>() {
          public void visit(VariableDeclarationExpr n, Object arg) {
            super.visit(n, arg);
//            System.out.println(n);
//            System.out.println(n.getVariables());
//            System.out.println(n.getVariable(0).getName());
//            System.out.println("------");
            if (n.getVariable(0).getNameAsString().equals(varName)) {
              System.out.println(n.getVariable(0).getInitializer().get());
            }
          }
        }.visit(JavaParser.parse(file), null);
      } catch (IOException e) {
        new RuntimeException(e);
      }
    })).explore(projectDir);
  }

  // with a variable name, it could be created by: declaration? assignment? parameter?
  // return the creation statements and new variables to be created

  public static void main(String[] args) {
    File projectDir = new File("data/test");
    listVarName(projectDir);
    printTrace(projectDir);
  }
}
