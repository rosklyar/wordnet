package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DigraphWordNetUpgradedBFSTest {

    private static final Pair<Digraph, Map<String, List<Integer>>> digraph = Helper.buildDigraph("synsets.txt", "hypernyms.txt");
    private static final WordNet wordNet = new DigraphWordNet(digraph.getValue1(), new UpgradedBFSShortestCommonAncestor(digraph.getValue0()));

    @Test
    void checkNouns() {
        assertTrue(wordNet.isNoun("byte"));
        assertFalse(wordNet.isNoun("notaword"));
    }

    @Test
    void checkDistance() {
        assertEquals(23, wordNet.distance("white_marlin", "mileage"));
        assertEquals(33, wordNet.distance("Black_Plague", "black_marlin"));
        assertEquals(27, wordNet.distance("American_water_spaniel", "histology"));
        assertEquals(29, wordNet.distance("Brown_Swiss", "barrel_roll"));
    }

}