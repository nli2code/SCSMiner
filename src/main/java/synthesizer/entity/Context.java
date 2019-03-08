package synthesizer.entity;

import utils.Config;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Context {

  private int varCounter;
  private Map<String, Deque<String>> content;

  public Context() {
    varCounter = 0;
    content = new HashMap<>();
  }

  public boolean containsType(String type) {
    return content.containsKey(type);
  }

  public String getVariableNameByType(String type) {
    if (!containsType(type)) return Config.getHoleWithType(type);
    else return content.get(type).getLast();
  }

  public void addVariable(String type) {
    Deque<String> names = content.getOrDefault(type, new ArrayDeque<>());
    String newName = "v" + varCounter;
    names.addLast(newName);
    content.put(type, names);
  }

  public Context copy() {
    Context ret = new Context();
    for (String key: content.keySet()) {
      Deque<String> valueCopy = new ArrayDeque<String>(){{addAll(content.get(key));}};
      ret.content.put(key, valueCopy);
    }
    return ret;
  }
}
