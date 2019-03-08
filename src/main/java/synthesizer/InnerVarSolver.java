package synthesizer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import synthesizer.entity.Context;
import synthesizer.entity.Invocation;
import synthesizer.entity.Signature;
import synthesizer.entity.SkeletonSnippet;

import java.util.*;
import java.util.stream.Collectors;

public class InnerVarSolver {

  private List<String> signatures = new ArrayList<>();
  private Map<String, List<Signature>> signatureMap;
  private List<ImmutablePair<SkeletonSnippet, Integer>> snippets;
  private int maxScore = 0;

  public InnerVarSolver(List<String> methodNames) {
    signatures.addAll(methodNames);
    signatureMap = SignatureLoader.loadSignatures();
    snippets = new ArrayList<>();
  }

  void dfs(int depth, Context ctx, SkeletonSnippet snippet, int score) {
    if (depth == signatures.size()) {
      snippets.add(new ImmutablePair<>(snippet.copy(), score));
      maxScore = Math.max(score, maxScore);
      return;
    }
    for (Signature sig: signatureMap.get(signatures.get(depth))) {
      Context newCtx = ctx.copy();
      Invocation invocation = new Invocation(sig, newCtx);
      snippet.addInvocation(invocation);
      int deltaScore = invocation.counterFilledVariables();
      dfs(depth + 1, newCtx, snippet, score + deltaScore);
      snippet.removeLastInvocation();
    }
  }

  public List<SkeletonSnippet> solve() {
    dfs(0, new Context(), new SkeletonSnippet(), 0);
//    snippets.stream().sorted((o1, o2) -> o2.right - o1.right)
//            .limit(5).forEach(item -> System.out.println(item.left.toString() + "--------------\n"));
    return snippets.stream().filter(item -> item.right == maxScore)
            .map(item -> item.left).collect(Collectors.toList());
  }

  public static void main(String[] args) {
    List<String> invocations = new ArrayList<String>(){{
      add("createCellStyle");
      add("setFillForegroundColor");
      add("setFillPattern");
      add("setCellStyle");}};
    List<SkeletonSnippet> skeletonSnippets = new InnerVarSolver(invocations).solve();
    for (SkeletonSnippet skeletonSnippet: skeletonSnippets) {
      System.out.println(skeletonSnippet.toString() + "---------------\n");
    }
  }
}
