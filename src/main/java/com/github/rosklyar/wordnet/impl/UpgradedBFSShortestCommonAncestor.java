package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;
import static java.util.List.of;
import static org.javatuples.Triplet.with;

public class UpgradedBFSShortestCommonAncestor implements ShortestCommonAncestor {

    private final Digraph digraph;

    public UpgradedBFSShortestCommonAncestor(Digraph digraph) {
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
        return minAncestorSearch(x, y).getValue0();
    }

    @Override
    public int ancestorSubset(Iterable<Integer> x, Iterable<Integer> y) {
        return minAncestorSearch(x, y).getValue1();
    }

    private Pair<Integer, Integer> minAncestorSearch(Iterable<Integer> x, Iterable<Integer> y) {
        int vertexNumber = digraph.V();
        var markedX = new boolean[vertexNumber];
        var distToX = new int[vertexNumber];
        var currentWaveX = initWave(x, markedX);

        var markedY = new boolean[vertexNumber];
        var distToY = new int[vertexNumber];
        var currentWaveY = initWave(y, markedY);

        int lengthUpperBound = MAX_VALUE;
        int minAncestor = -1;
        int currentBFSDistance = 0;
        while (lengthUpperBound > currentBFSDistance && (!currentWaveX.isEmpty() || !currentWaveY.isEmpty())) {
            var xWaveProcessed = proceedWave(currentWaveX, distToX, distToY, markedX, markedY);
            currentWaveX = xWaveProcessed.getValue0();

            var yWaveProcessed = proceedWave(currentWaveY, distToY, distToX, markedY, markedX);
            currentWaveY = yWaveProcessed.getValue0();

            int wavesAncestor;
            int wavesMinLength;
            if (xWaveProcessed.getValue1() < yWaveProcessed.getValue1()) {
                wavesAncestor = xWaveProcessed.getValue2();
                wavesMinLength = xWaveProcessed.getValue1();
            } else {
                wavesAncestor = yWaveProcessed.getValue2();
                wavesMinLength = yWaveProcessed.getValue1();
            }
            if (wavesMinLength < lengthUpperBound) {
                lengthUpperBound = wavesMinLength;
                minAncestor = wavesAncestor;
            }

            lengthUpperBound = min(lengthUpperBound, wavesMinLength);
            currentBFSDistance++;
        }

        return Pair.with(lengthUpperBound, minAncestor);
    }

    private Triplet<List<Integer>, Integer, Integer> proceedWave(List<Integer> wave,
                                                                 int[] distToFirst,
                                                                 int[] distToSecond,
                                                                 boolean[] markedFirst,
                                                                 boolean[] markedSecond) {
        List<Integer> nextWave = new LinkedList<>();
        int upperBound = MAX_VALUE;
        int currentAncestor = -1;
        for (Integer v : wave) {
            for (Integer w : digraph.adj(v)) {
                if (!markedFirst[w]) {
                    distToFirst[w] = distToFirst[v] + 1;
                    markedFirst[w] = true;
                    nextWave.add(w);
                    if (markedSecond[w] && distToSecond[w] + distToFirst[w] < upperBound) {
                        upperBound = distToSecond[w] + distToFirst[w];
                        currentAncestor = w;
                    }
                }
            }
        }
        return with(nextWave, upperBound, currentAncestor);
    }

    private List<Integer> initWave(Iterable<Integer> source, boolean[] marked) {
        List<Integer> wave = new LinkedList<>();
        for (int s : source) {
            marked[s] = true;
            wave.add(s);
        }
        return wave;
    }
}
