package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordNetOutcastSimpleBFSTest {

    private static final Pair<Digraph, Map<String, List<Integer>>> digraph = Helper.buildDigraph("synsets.txt", "hypernyms.txt", Helper::buildHypernymsDigraph);
    private static final WordNet wordNet = new DigraphWordNet(digraph.getValue1(), new SimpleBFSShortestCommonAncestor(digraph.getValue0()));
    private static final Outcast outcast = new WordNetOutcast(wordNet);

    @Test
    void checkOutcast1() {
        assertEquals("heart", outcast.outcast(of("earth", "fire", "air", "water", "heart").toArray(String[]::new)));
    }

    @Test
    void checkOutcast2() {
        assertEquals("bed", outcast.outcast(of("water", "soda", "bed", "milk", "orange_juice", "apple_juice", "tea", "coffee").toArray(String[]::new)));
    }

    @Test
    void checkOutcast3() {
        assertEquals("mongoose", outcast.outcast(of("competition", "cup", "event", "fielding", "football", "level", "practice", "prestige", "team", "tournament", "world", "mongoose").toArray(String[]::new)));
    }
}