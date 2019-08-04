package com.github.rosklyar.wordnet.impl;

public interface ShortestCommonAncestor {

    int length(int x, int y);

    int ancestor(int x, int y);

    int lengthSubset(Iterable<Integer> x, Iterable<Integer> y);

    int ancestorSubset(Iterable<Integer> x, Iterable<Integer> y);
}
