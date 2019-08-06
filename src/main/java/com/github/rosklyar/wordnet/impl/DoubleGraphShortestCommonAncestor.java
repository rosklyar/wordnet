package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Integer.MAX_VALUE;

public class DoubleGraphShortestCommonAncestor implements ShortestCommonAncestor {

    private final Digraph digraph;

    public DoubleGraphShortestCommonAncestor(Digraph digraph) {
        this.digraph = digraph;
    }

    @Override
    public int length(int x, int y) {
        return 0;
    }

    @Override
    public int ancestor(int x, int y) {
        return 0;
    }

    @Override
    public int lengthSubset(Iterable<Integer> x, Iterable<Integer> y) {
        var destination = new HashSet<>();
        y.forEach(d -> destination.add(d + digraph.V() / 2));
        int vertexNumber = digraph.V();
        var marked = new boolean[vertexNumber];
        var distTo = new int[vertexNumber];
        Queue<Integer> queue = new LinkedList<>();
        for (int s : x) {
            distTo[s] = 0;
            marked[s] = true;
            queue.add(s);
        }
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : digraph.adj(v)) {
                if (!marked[w]) {
                    if (destination.contains(w)) {
                        return distTo[v];
                    }
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    queue.add(w);
                }
            }
        }
        return MAX_VALUE;
    }

    @Override
    public int ancestorSubset(Iterable<Integer> x, Iterable<Integer> y) {
        return 0;
    }
}
