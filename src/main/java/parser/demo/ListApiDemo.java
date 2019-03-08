package parser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import parser.meta.DirExplorer;
import utils.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListApiDemo {

  static String curPackage;
  static List<String> classOrInterfaces = new ArrayList<>();
  static List<String> methods = new ArrayList<>();

  public static void listAPIs(File projectDir) {
    new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
      // System.out.println(path);
      // System.out.println(Strings.repeat("=", path.length()));
      try {
        new VoidVisitorAdapter<Object>() {
          public void visit(PackageDeclaration n, Object arg) {
            super.visit(n, arg);
            curPackage = n.getNameAsString();
          }
          @Override
          public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            super.visit(n, arg);
            String classOrInterfaceFullName = curPackage + "." + n.getNameAsString();
            classOrInterfaces.add(classOrInterfaceFullName);
            System.out.println(classOrInterfaceFullName);
            System.out.println(StringUtils.repeat("=", classOrInterfaceFullName.length()));
            for (MethodDeclaration method: n.getMethods()) {
              if (method.getNameAsString().equals("main")) continue;
              String methodFullName = classOrInterfaceFullName + "." + method.getNameAsString();
              methods.add(methodFullName);
              System.out.println(methodFullName);
            }
            System.out.println();
          }
        }.visit(JavaParser.parse(file), null);
      } catch (IOException e) {
        new RuntimeException(e);
      }
    }).explore(projectDir);
  }

  public static void main(String[] args) throws IOException {
    File projectDir = new File(Config.getLibSrcPath());
    listAPIs(projectDir);
//    FileWriter fw = new FileWriter(Config.getLibraryApiPath());
//    fw.write(String.join("\n", methods));
//    fw.close();
    System.out.println("tot " + methods.size() + " methods");
  }
}