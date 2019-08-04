package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;
import com.github.rosklyar.wordnet.utils.In;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.javatuples.Pair.with;

public class Helper {

    public static Pair<Digraph, Map<String, List<Integer>>> buildDigraph(String synonymsFile, String hypernymsFile) {
        var synonymsInput = new In(synonymsFile);
        int verticesInSynonymsSetGraph = 0;
        var nounToSynonymSetId = new HashMap<String, List<Integer>>();
        while (synonymsInput.hasNextLine()) {
            var line = synonymsInput.readLine().split(",");
            String synonym = line[1];
            stream(synonym.split(" "))
                    .forEach(noun -> nounToSynonymSetId.computeIfAbsent(noun, k -> new ArrayList<>()).add(parseInt(line[0])));
            verticesInSynonymsSetGraph++;
        }
        return with(buildHypernymsDigraph(hypernymsFile, verticesInSynonymsSetGraph), nounToSynonymSetId);
    }

    public static List<String> nouns(String synonymsFile) {
        var synonymsInput = new In(synonymsFile);
        var nouns = new HashSet<String>();
        while (synonymsInput.hasNextLine()) {
            var line = synonymsInput.readLine().split(",");
            String synonym = line[1];
            nouns.addAll(asList(synonym.split(" ")));
        }
        return new ArrayList<>(nouns);
    }

    private static Digraph buildHypernymsDigraph(String hypernymsFile, int synonymsCount) {
        var digraph = new Digraph(synonymsCount);
        var hypernymsInput = new In(hypernymsFile);
        while (hypernymsInput.hasNextLine()) {
            var line = hypernymsInput.readLine().split(",");
            int id = parseInt(line[0]);
            stream(line)
                    .skip(1)
                    .forEach(hypernymsId -> digraph.addEdge(id, parseInt(hypernymsId)));
        }
        return digraph;
    }
}
