package miner;

import utils.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiLoader {

  private static String libraryApiPath = Config.getLibraryApiPath();
  private static Map<String, Integer> libraryApis = new HashMap<>();
  private static Map<Integer, String> reverseLibraryApis = new HashMap<>();
  private static Map<String, Integer> libraryShortApis = new HashMap<>();
  private static Map<Integer, String> reverseLibraryShortApis = new HashMap<>();

  public static Map<String,Integer> getLibraryApis() {
    return libraryApis;
  }

  public static Map<String, Integer> getLibraryShortApis() {
    return libraryShortApis;
  }

  public static Map<Integer, String> getReverseLibraryApis() {
    return reverseLibraryApis;
  }

  public static Map<Integer, String> getReverseLibraryShortApis() {
    return reverseLibraryShortApis;
  }

  public static void loadApis() {
    try {
      File apiFile = new File(libraryApiPath);
      BufferedReader reader = new BufferedReader(new FileReader(apiFile));
      String line = null;
      int hashCode = 0;
      int shortHashCode = 0;
      while ((line = reader.readLine()) != null) {
        libraryApis.put(line, hashCode);
        reverseLibraryApis.put(hashCode, line);
        hashCode++;
        String[] tokens = line.split("\\.");
        String lastToken = tokens[tokens.length - 1];
        libraryShortApis.put(lastToken, shortHashCode);
        reverseLibraryShortApis.put(shortHashCode, lastToken);
        shortHashCode++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}