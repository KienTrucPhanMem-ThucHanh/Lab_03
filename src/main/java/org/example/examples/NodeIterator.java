package org.example.examples;

import com.github.javaparser.ast.Node;

/**
 * Mô tả về chức năng của lớp.
 *
 * created-date: 26-01-2024
 * author: Hoài Hiệp
 */
public class NodeIterator {
    public static final String INVALID_NAME = "Invalid";
    public interface NodeHandler {
        int ID = 100;
        boolean handle(Node node);
    }

    private NodeHandler nodeHandler;

    public NodeIterator(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public void explore(Node node) {
        if (nodeHandler.handle(node)) {
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }
}
