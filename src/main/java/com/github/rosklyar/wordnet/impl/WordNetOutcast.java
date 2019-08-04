package com.github.rosklyar.wordnet.impl;


import org.javatuples.Pair;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Stream.of;
import static org.javatuples.Pair.with;

public class WordNetOutcast implements Outcast {

    private final WordNet wordNet;

    public WordNetOutcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    @Override
    public String outcast(String[] nouns) {
        return of(nouns)
                .map(noun -> with(
                        of(nouns)
                                .filter(n -> !n.equals(noun))
                                .mapToInt(other -> wordNet.distance(noun, other))
                                .sum(),
                        noun))
                .max(comparingInt(Pair::getValue0))
                .orElseThrow(() -> new RuntimeException("No outcast found!"))
                .getValue1();
    }
}
