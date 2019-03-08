package synthesizer.entity;

import java.util.ArrayList;
import java.util.List;

public class SkeletonSnippet {

  private List<Invocation> invocations;

  public SkeletonSnippet() {
    invocations = new ArrayList<>();
  }

  public void addInvocation(Invocation invocation) {
    invocations.add(invocation);
  }

  public void removeLastInvocation() {
    invocations.remove(invocations.size() - 1);
  }

  public String toString() {
    String ret = "";
    for (Invocation invocation: invocations) {
      ret += invocation.toString() + "\n";
    }
    return ret;
  }

  public SkeletonSnippet copy() {
    SkeletonSnippet ret = new SkeletonSnippet();
    ret.invocations.addAll(invocations);
    return ret;
  }
}
