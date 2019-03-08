package miner;

import parser.entity.SCSFile;
import parser.entity.SCSUnit;
import utils.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HashSolver {

  private List<List<String>> items;

  public HashSolver() {
    items = new ArrayList<>();
    ApiLoader.loadApis();
  }

  public List<List<String>> getItems() {
    return items;
  }


  public String locateApiFullName(List<String> libs, String shortName) {
    for (String lib: libs) {
      String candidate = lib + ".*." + shortName;
      String suffix = "." + shortName;
      for (String api: ApiLoader.getLibraryApis().keySet()) {
        if (api.endsWith(suffix) && Pattern.matches(candidate, api)) return api;
      }
    }
    return "";
  }

  public void solve(List<SCSFile> scsFiles) {
    for (SCSFile file: scsFiles) {
      List<String> importedLibraries = file.getImportedLibraries();
      for (SCSUnit unit: file.getUnits()) {
        List<String> item = new ArrayList<>();
        for (String shortName: unit.getInvocationShortNames()) {
          String fullName = locateApiFullName(importedLibraries, shortName);
          if (fullName.length() == 0) continue;
          if (Config.useShortName()) {
            item.add(shortName);
          } else {
            item.add(fullName);
          }
        }
        items.add(item);
      }
    }
    Map<String, Integer> map = new HashMap<>();
    if (Config.useShortName()) {
      ApiLoader.getLibraryShortApis();
    } else {
      ApiLoader.getLibraryApis();
    }
    List<String> seq = new ArrayList<>();
    for (List<String> item: items) {
      if (item.size() == 0) continue;
      List<String> numbers = new ArrayList<>();
      for (String str: item) numbers.add(map.get(str).toString());
      seq.add(String.join(" ", numbers));
    }
    try {
      FileWriter fw = new FileWriter(Config.getSeqPath());
      fw.write(String.join("\n", seq));
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
