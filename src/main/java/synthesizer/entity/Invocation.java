package synthesizer.entity;

import java.util.ArrayList;
import java.util.List;

public class Invocation {
  private String name;
  private String callerType, callerName;
  private String returnType, returnName;
  private List<String> paramTypes;
  private List<String> paramNames;

  public Invocation(Signature sig, Context ctx) {
    name = sig.name;
    callerType = sig.caller;
    callerName = ctx.getVariableNameByType(callerType);
    paramTypes = new ArrayList<>();
    paramNames = new ArrayList<>();
    for (String type: sig.params) {
      paramTypes.add(type);
      paramNames.add(ctx.getVariableNameByType(type));
    }
    returnType = sig.returnType;
    if (!returnType.equals("void")) {
      ctx.addVariable(returnType);
      returnName = ctx.getVariableNameByType(returnType);
    }
  }

  public int counterFilledVariables() {
    int ret = 0;
    if (callerName.startsWith("v")) ret++;
    for (String paramName: paramNames) {
      if (paramName.startsWith("v")) ret++;
    }
    return ret;
  }

  public String toString() {
    String ret = callerName + "." + name + "(" + String.join(", ", paramNames) + ")\n";
    if (!returnType.equals("void")) {
      ret = returnType + " " + returnName + " = "+ ret;
    }
    return ret;
  }

}
