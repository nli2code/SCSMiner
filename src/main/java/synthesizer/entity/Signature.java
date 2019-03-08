package synthesizer.entity;

import com.github.javaparser.ast.body.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Signature {
  public String name, caller, returnType;
  public List<String> params = new ArrayList<>();

  public static String separator = "|";

  public Signature() {}

  public Signature(String name, String caller, String returnType, List<Parameter> params) {
    this.name = name;
    this.caller = caller;
    this.returnType = returnType;
    for (Parameter param: params) {
      this.params.add(param.getType().toString());
    }
  }

  @Override
  public String toString() {
    return name + separator + caller + separator + returnType
            + separator + String.join(",", params);
  }

  @Deprecated
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (!(obj instanceof Signature)) return false;
    else {
      return name.equals(((Signature) obj).name) &&
              caller.equals(((Signature) obj).caller) &&
              returnType.equals(((Signature) obj).returnType) &&
              params.toString().equals(((Signature) obj).params.toString());
    }
  }

  public int hashCode() {
    return 0;
  }
}
