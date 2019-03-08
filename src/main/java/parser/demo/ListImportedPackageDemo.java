package parser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import parser.meta.DirExplorer;

import java.io.File;
import java.io.IOException;

public class ListImportedPackageDemo {

  static class MyVisitor implements DirExplorer.FileHandler {
    public void handle(int level, String path, File file) {
      System.out.println(path);
      System.out.println(Strings.repeat("=", path.length()));
      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(ImportDeclaration n, Object arg) {
            super.visit(n, arg);
            System.out.println(n.getName());
          }
        }.visit(JavaParser.parse(file), null);
        System.out.println();
      } catch (IOException e) {
        new RuntimeException(e);
      }
    }
  }

  public static void listPackages(File projectDir) {
    new DirExplorer((level, path, file) -> path.endsWith(".java"), new MyVisitor()).explore(projectDir);
  }

  public static void main(String[] args) {
    File projectDir = new File("data/library_conquer");
    listPackages(projectDir);
  }
}