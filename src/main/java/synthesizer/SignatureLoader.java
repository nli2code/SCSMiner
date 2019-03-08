package synthesizer;

import synthesizer.entity.Signature;
import utils.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SignatureLoader {
  private static String librarySigPath = Config.getLibrartSigPath();
  private static Map<String, List<Signature>> signatureMap = new HashMap();

  public static Map<String, List<Signature>> loadSignatures() {
    try {
      File sigFile = new File(librarySigPath);
      BufferedReader reader = new BufferedReader(new FileReader(sigFile));
      String line;
      int cnt = 1;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length != 3 && parts.length != 4) {
          System.out.println(line + " " + cnt);
          break;
        }
        Signature signature = new Signature();
        signature.name = parts[0];
        signature.caller = parts[1];
        signature.returnType = parts[2];
        if (parts.length > 3) {
          signature.params.addAll(Arrays.asList(parts[3].split(",")));
        }
        cnt++;
        List<Signature> val = signatureMap.getOrDefault(signature.name, new ArrayList<>());
        val.add(signature);
        signatureMap.put(signature.name, val);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return signatureMap;
  }

  public static void main(String[] args) {
    loadSignatures();
    for (Signature signature: signatureMap.get("setCellStyle")) {
      System.out.println(signature);
    }
  }
}
