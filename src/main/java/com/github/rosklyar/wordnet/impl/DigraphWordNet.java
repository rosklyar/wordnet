package com.github.rosklyar.wordnet.impl;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class DigraphWordNet implements WordNet {

    private final Map<String, List<Integer>> nounToSynonymSetId;
    private final ShortestCommonAncestor shortestCommonAncestor;


    public DigraphWordNet(Map<String, List<Integer>> nounToSynonymSetId,
                          ShortestCommonAncestor shortestCommonAncestor) {
        this.nounToSynonymSetId = nounToSynonymSetId;
        this.shortestCommonAncestor = shortestCommonAncestor;
    }

    @Override
    public boolean isNoun(String word) {
        return nounToSynonymSetId.containsKey(word);
    }

    @Override
    public int distance(String firstNoun, String secondNoun) {
        checkNoun(firstNoun);
        checkNoun(secondNoun);

        return shortestCommonAncestor.lengthSubset(
                nounToSynonymSetId.get(firstNoun),
                nounToSynonymSetId.get(secondNoun)
        );
    }

    private void checkNoun(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException(format("Word %s is not a WordNet noun!", noun));
        }
    }
}
