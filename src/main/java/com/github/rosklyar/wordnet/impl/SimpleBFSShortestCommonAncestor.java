package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.BreadthFirstDirectedPaths;
import com.github.rosklyar.wordnet.utils.Digraph;
import org.javatuples.Pair;

import static java.lang.Integer.MAX_VALUE;
import static java.util.List.of;
import static org.javatuples.Pair.with;


public class SimpleBFSShortestCommonAncestor implements ShortestCommonAncestor {

    private final Digraph digraph;

    public SimpleBFSShortestCommonAncestor(Digraph digraph) {
        this.digraph = digraph;
    }

    @Override
    public int length(int x, int y) {
        return lengthSubset(of(x), of(y));
    }

    @Override
    public int ancestor(int x, int y) {
        return ancestorSubset(of(x), of(y));
    }

    @Override
    public int lengthSubset(Iterable<Integer> x, Iterable<Integer> y) {
        var pathsFromX = new BreadthFirstDirectedPaths(digraph, x);
        var pathsFromY = new BreadthFirstDirectedPaths(digraph, y);
        return length(pathsFromX, pathsFromY).getValue0();
    }

    @Override
    public int ancestorSubset(Iterable<Integer> x, Iterable<Integer> y) {
        var pathsFromX = new BreadthFirstDirectedPaths(digraph, x);
        var pathsFromY = new BreadthFirstDirectedPaths(digraph, y);
        return length(pathsFromX, pathsFromY).getValue1();
    }

    private Pair<Integer, Integer> length(BreadthFirstDirectedPaths pathsFromV, BreadthFirstDirectedPaths pathsFromW) {
        int minDistanceViaAncestor = MAX_VALUE;
        int shortestCommonAncestor = -1;
        int currentVertex = 0;
        while (currentVertex < digraph.V()) {
            if (isCommonAncestor(pathsFromV, pathsFromW, currentVertex)) {
                int currentDistance = pathsFromV.distTo(currentVertex) + pathsFromW.distTo(currentVertex);
                if (currentDistance < minDistanceViaAncestor) {
                    shortestCommonAncestor = currentVertex;
                    minDistanceViaAncestor = currentDistance;
                }
            }
            currentVertex++;
        }
        return with(minDistanceViaAncestor, shortestCommonAncestor);
    }

    private boolean isCommonAncestor(BreadthFirstDirectedPaths pathsFromV,
                                     BreadthFirstDirectedPaths pathsFromW,
                                     int currentVertex) {
        return pathsFromV.hasPathTo(currentVertex) && pathsFromW.hasPathTo(currentVertex);
    }
}
