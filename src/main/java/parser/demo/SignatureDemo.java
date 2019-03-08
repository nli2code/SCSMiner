package parser.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import parser.meta.DirExplorer;
import synthesizer.entity.Signature;
import utils.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SignatureDemo {

  static Set<Signature> signatureSet = new HashSet<>();

  public static void listAPIs(File projectDir) {

    new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
      // System.out.println(path);
      // System.out.println(Strings.repeat("=", path.length()));
      try {
        new VoidVisitorAdapter<Object>() {
          public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            super.visit(n, n.getNameAsString());
          }
          @Override
          public void visit(MethodDeclaration n, Object arg) {
            String comment = n.getComment().toString();
            super.visit(n, arg);
            if (!n.getComment().toString().contains("@deprecated")) {
              try {
                Signature item = new Signature(n.getNameAsString(), arg.toString(),
                        n.getType().toString().replace("\n", ""), n.getParameters());
                signatureSet.add(item);
              } catch (NullPointerException e) {}
            }
          }
        }.visit(JavaParser.parse(file), null);
      } catch (IOException e) {
        new RuntimeException(e);
      }
    }).explore(projectDir);
  }

  public static void main(String[] args) throws IOException {
     File projectDir = new File(Config.getLibSrcPath());
//    File projectDir = new File("data/test");
    listAPIs(projectDir);
    for (Signature signature: signatureSet) {
      System.out.println(signature);
    }
  }
}
