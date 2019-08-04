package com.github.rosklyar.wordnet.impl;

public interface WordNet {

    boolean isNoun(String word);

    int distance(String firstNoun, String secondNoun);
}
