package miner;

import utils.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PatternParser {
  private List<Pattern> patterns;
  private Map<Integer, String> hashMap;

  public PatternParser() {
    ApiLoader.loadApis();
    if (Config.useShortName()) {
      hashMap = ApiLoader.getReverseLibraryShortApis();
    } else {
      hashMap = ApiLoader.getReverseLibraryApis();
    }
    patterns = new ArrayList<>();
  }

  public List<Pattern> getPatterns() {
    return patterns;
  }

  public void addPattern(Pattern p) {
    patterns.add(p);
  }

  public void solve() {
    File rawPatterns = new File(Config.getPatternsPath());
    try {
      BufferedReader reader = new BufferedReader(new FileReader(rawPatterns));
      String line;
      while ((line = reader.readLine()) != null) {
        Pattern p = new Pattern(line, hashMap);
        if (!p.filter())
          addPattern(p);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    PatternParser parser = new PatternParser();
    parser.solve();
  }
}
