package parser.entity;

import java.util.ArrayList;
import java.util.List;

public class SCSUnit {
  private String methodShortName;
  private List<String> invocationShortNames;

  public SCSUnit() {
    methodShortName = "";
    invocationShortNames = new ArrayList<>();
  }

  public void setMethodShortName(String name) {
    methodShortName = name;
  }

  public List<String> getInvocationShortNames() {
    return invocationShortNames;
  }

  public SCSUnit copy() {
    SCSUnit unitCopy = new SCSUnit();
    unitCopy.setMethodShortName(methodShortName);
    for (String item: invocationShortNames) {
      unitCopy.add(item);
    }
    return unitCopy;
  }

  public void add(String methodShortName) {
    invocationShortNames.add(methodShortName);
  }

  public void clear() {
    invocationShortNames.clear();
  }

  public String toString() {
    String ret = "Invocations in " + methodShortName + ":\n";
    for (String name: invocationShortNames) {
      ret += "  " + name + "\n";
    }
    return ret;
  }
}
