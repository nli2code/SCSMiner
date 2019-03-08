package utils;

public class Config {
  public static String getGithubAccessToken() {
    return "your_github_access_token";
  }

  public static String getFileCorpusPath() {
    return "data/test";
  }

  public static String getRepoCorpusPath() {
    return "/Users/maxkibble/Documents/Data/DomainNLI/org.apache.poi/api/setFillForegroundColor";
  }

  public static String getFilterPackage() {
    return "org.apache.poi";
  }

  public static String getSearchQuery() {
    return "apache+poi";
  }

  public static String getLibraryApiPath() {
    return "data/library_api.txt";
  }

  public static String getLibrartSigPath() {
    return "data/library_sig.txt";
  }

  public static String getSeqPath() {
    return "data/pattern/" + (useShortName()? "short/": "full/")
            + getFilterPackage() + "/" + getCurrentTask() + "/scs_seq.txt";
  }

  public static String getPatternsPath() {
    return "data/pattern/" + (useShortName()? "short/": "full/")
            + getFilterPackage() + "/" + getCurrentTask() + "/pattern.txt";
  }

  public static String getLibSrcPath() {
    return "/Users/maxkibble/Documents/Data/DomainNLI/" + getFilterPackage() + "/src";
  }

  public static String getCurrentTask() { return "fill_color"; }

  public static boolean useShortName() {
    return true;
  }

  public static String getHoleWithType(String type) {
    return "<HOLE: " + type + ">";
  }
}
