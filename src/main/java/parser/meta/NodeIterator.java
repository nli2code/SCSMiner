package parser.meta;

import com.github.javaparser.ast.Node;

public class NodeIterator {
  public interface NodeHandler {
    boolean handle(Node node);
  }

  private NodeHandler nodeHandler;

  public NodeIterator(NodeHandler handler) {
    this.nodeHandler = handler;
  }

  public void explore(Node node) {
    if (nodeHandler.handle(node)) {
      for (Node child: node.getChildNodes()) {
        explore(child);
      }
    }
  }

}
