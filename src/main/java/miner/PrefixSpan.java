package miner;

import utils.Config;

import java.io.*;

public class PrefixSpan {

  public static void mine(String seqList, int minLen, int topK) {

    String ret = "";
    try {
      Process p = Runtime.getRuntime().exec(
              "prefixspan-cli top-k " + topK + " --minlen=" + minLen + " --closed " + seqList);

      BufferedReader stdInput = new BufferedReader(new
              InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new
              InputStreamReader(p.getErrorStream()));

      String s, errors = "";
      while ((s = stdInput.readLine()) != null) {
        ret += s + "\n";
      }
      while ((s = stdError.readLine()) != null) {
        errors += s + "\n";
      }
      if (errors.length() > 0) {
        System.out.println("Here is the standard error of PrefixSpan (if any):\n" + errors);
      }

      FileWriter fw = new FileWriter(Config.getPatternsPath());
      fw.write(ret);
      fw.close();
    }
    catch (IOException e) {
        System.out.println("exception happened in PrefixSpan - here's what I know: ");
        e.printStackTrace();
        System.exit(-1);
    }
  }

}