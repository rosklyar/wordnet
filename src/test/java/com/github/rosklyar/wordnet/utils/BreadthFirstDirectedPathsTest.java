package com.github.rosklyar.wordnet.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BreadthFirstDirectedPathsTest {

    @Test
    void checkMinPath() {
        Digraph digraph = new Digraph(4);
        digraph.addEdge(1, 0);
        digraph.addEdge(1, 2);
        digraph.addEdge(2, 3);
        digraph.addEdge(3, 0);

        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, 1);
        assertEquals(1, bfs.distTo(0));
    }

}