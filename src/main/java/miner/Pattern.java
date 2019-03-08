package miner;

import java.util.*;

public class Pattern {

  public int freq;
  public List<String> content = new ArrayList<>();

  public Pattern(String s, Map<Integer, String> hashMap) {
    String[] parts = s.split(":");
    freq = Integer.parseInt(parts[1].trim());
    for (String hashCode: parts[0].split(" ")) {
      int invocationId = Integer.parseInt(hashCode.trim());
      content.add(hashMap.get(invocationId));
    }
  }

  public boolean filter() {
    // filter those patterns with duplicated invocations
    Set<String> stringSet = new HashSet<>();
    for (String invo: content) {
      if (stringSet.contains(invo)) return true;
      else stringSet.add(invo);
    }
    return false;
  }

  public String toString() {
    return content.toString() + " : " + freq;
  }
}
